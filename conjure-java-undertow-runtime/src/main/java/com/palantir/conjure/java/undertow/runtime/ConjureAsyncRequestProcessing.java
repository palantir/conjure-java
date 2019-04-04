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

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.undertow.lib.AsyncContext;
import com.palantir.conjure.java.undertow.lib.AsyncRequestProcessing;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.XnioExecutor;

final class ConjureAsyncRequestProcessing implements AsyncRequestProcessing {

    private static final Logger log = LoggerFactory.getLogger(ConjureAsyncRequestProcessing.class);

    private final Serializer<SerializableError> exceptionSerializer = ConjureExceptions.serializer();

    private final Duration timeout;

    ConjureAsyncRequestProcessing(Duration timeout) {
        this.timeout = Preconditions.checkNotNull(timeout, "Timeout is required");
    }

    @Override
    public <T> AsyncContext<T> context(ReturnValueWriter<T> returnValueWriter, HttpServerExchange exchange) {
        DefaultAsyncContext<T> context = new DefaultAsyncContext<>(returnValueWriter, exchange);
        context.register();
        return context;
    }

    final class DefaultAsyncContext<T> implements AsyncContext<T>, FutureCallback<T> {

        private final ReturnValueWriter<T> returnValueWriter;
        private final HttpServerExchange exchange;
        private final SettableFuture<T> future = SettableFuture.create();

        private final List<Runnable> onTimeout = Lists.newCopyOnWriteArrayList();
        private volatile XnioExecutor.Key timeoutKey;
        private volatile boolean registered;

        DefaultAsyncContext(ReturnValueWriter<T> returnValueWriter, HttpServerExchange exchange) {
            this.returnValueWriter = returnValueWriter;
            this.exchange = exchange;
        }

        @Override
        public boolean complete(T value) {
            return future.set(value);
        }

        @Override
        public boolean completeExceptionally(Throwable throwable) {
            return future.setException(throwable);
        }

        @Override
        public void onTimeout(Runnable onTimeoutCallback) {
            Preconditions.checkState(!registered,
                    "Timeout callbacks must me registered on the initial request thread");
            onTimeout.add(onTimeoutCallback);
        }

        void timeout() {
            // Only execute cancellation if the request has not already been completed normally
            // TODO(ckozak): Consider 'if (completeExceptionally(<specific exception>))' for clarity
            if (future.cancel(false)
                    // Bypass if there is no timeout listener
                    && !onTimeout.isEmpty()) {
                // Always execute timeouts from the task pool
                exchange.dispatch(serverExchange -> onTimeout.forEach(runnable -> {
                    try {
                        runnable.run();
                    } catch (RuntimeException e) {
                        log.error("Timeout callback failed", e);
                    }
                }));
            }
        }

        void register() {
            // Dispatch the registration task. We're executing inside of root handler, so the dispatched task will
            // execute on this thread after the current handler completes execution. This allows us to avoid racing
            // registration with completion.
            exchange.dispatch(() -> {
                // If an exception has was thrown in the initial request handler (bytes have already been written to
                // the response) do not register callbacks.
                if (exchange.isResponseStarted() || exchange.getResponseBytesSent() > 0) {
                    exchange.endExchange();
                    return;
                }
                registered = true;
                // TODO(ckozak): potential optimization: avoid scheduling a timeout if the future is already complete.
                // Timeout scheduling MUST occur prior to adding the future callback, otherwise quick completion
                // will fail to remove scheduled tasks.
                this.timeoutKey = exchange.getIoThread()
                        .executeAfter(this::timeout, timeout.toMillis(), TimeUnit.MILLISECONDS);
                Futures.addCallback(future, this, MoreExecutors.directExecutor());
            });
        }

        // Guava FutureCallback

        @Override
        public void onSuccess(T result) {
            completeCallback(serverExchange -> returnValueWriter.write(result, exchange));
        }

        @Override
        public void onFailure(Throwable throwable) {
            completeCallback(serverExchange ->
                    ConjureExceptions.handle(serverExchange, exceptionSerializer, throwable));
        }

        private void completeCallback(HttpHandler completionHandler) {
            removeTimeout();
            exchange.dispatch(new ConjureExceptionHandler(completionHandler, exceptionSerializer));
        }

        private void removeTimeout() {
            XnioExecutor.Key key = this.timeoutKey;
            if (key != null) {
                key.remove();
            }
        }
    }
}
