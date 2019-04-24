/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.undertow.lib.AsyncRequestProcessing;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.xnio.XnioExecutor;

final class ConjureAsyncRequestProcessing implements AsyncRequestProcessing {

    private static final Executor DIRECT_EXECUTOR = MoreExecutors.directExecutor();

    private final Serializer<SerializableError> exceptionSerializer = ConjureExceptions.serializer();

    private final Duration timeout;

    ConjureAsyncRequestProcessing(Duration timeout) {
        this.timeout = Preconditions.checkNotNull(timeout, "Timeout is required");
    }

    @Override
    public <T> void register(
            ListenableFuture<T> future,
            ReturnValueWriter<T> returnValueWriter,
            HttpServerExchange exchange) throws IOException {
        Preconditions.checkNotNull(future, "future");
        Preconditions.checkNotNull(returnValueWriter, "returnValueWriter");
        Preconditions.checkNotNull(exchange, "exchange");

        // TODO(ckozak): potential optimization: avoid both dispatching and scheduling a timeout if the future is
        // already complete.

        // Dispatch the registration task. We're executing inside of root handler, so the dispatched task will
        // execute on this thread after the current handler completes execution. This allows us to avoid racing
        // registration with completion.
        exchange.dispatch(() -> {
            XnioExecutor.Key timeoutKey = exchange.getIoThread()
                    .executeAfter(() -> future.cancel(false), timeout.toMillis(), TimeUnit.MILLISECONDS);
            future.addListener(timeoutKey::remove, DIRECT_EXECUTOR);
            Futures.addCallback(future, new FutureCallback<T>() {
                @Override
                public void onSuccess(@Nullable T result) {
                    exchange.dispatch(new ConjureExceptionHandler(serverExchange ->
                            returnValueWriter.write(result, exchange), exceptionSerializer));
                }

                @Override
                public void onFailure(Throwable throwable) {
                    exchange.dispatch(new ConjureExceptionHandler(serverExchange -> ConjureExceptions.handle(
                            serverExchange, exceptionSerializer, throwable), exceptionSerializer));
                }
            }, DIRECT_EXECUTOR);
        });
    }
}
