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
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.xnio.XnioExecutor;

/**
 * <h3>Thread Model</h3>
 *
 * <ul>
 *     <li>
 *         Any {@link ListenableFuture} may be registered for asynchronous request processing regardless of type,
 *         where it's executed, and what thread completes it.
 *     </li>
 *     <li>
 *         All serialization and I/O occurs on the server task pool, matching synchronous conjure services.
 *     </li>
 * </ul>
 *
 * This requires us to move execution away from {@link ListenableFuture} callbacks as quickly as
 * possible, because they're controlled by a future created in the service implementation. This way
 * service authors do not need to be aware of the time it takes to serialize results and write them to
 * clients, which can be time consuming depending on the network. For example, an endpoint which
 * schedules results on a single-threaded {@link java.util.concurrent.ScheduledExecutorService} would
 * congest the executor and fail to execute other scheduled work at the expected time.
 * We use {@link HttpServerExchange#dispatch(HttpHandler)} to put work onto the server task pool where
 * it is executed as similarly as possible to synchronous conjure endpoints to avoid behavior
 * differences between the two.
 */
final class ConjureAsyncRequestProcessing implements AsyncRequestProcessing {

    private static final Executor DIRECT_EXECUTOR = MoreExecutors.directExecutor();
    // Thread interruption can result in unexpected behavior. Most uses of this feature are not based on
    // running tasks, so the value passed to Future.cancel makes no difference.
    private static final boolean INTERRUPT_ON_CANCEL = false;
    private static final AttachmentKey<ListenableFuture<?>> FUTURE = AttachmentKey.create(ListenableFuture.class);
    // If the request is ended before the future has completed, cancel the future to signal that work
    // should be stopped. This occurs when clients cancel requests or connections are closed.
    private static final ExchangeCompletionListener COMPLETION_LISTENER =
            SafeExchangeCompletionListener.of(exchange -> {
                ListenableFuture<?> future = exchange.getAttachment(FUTURE);
                if (future != null) {
                    future.cancel(INTERRUPT_ON_CANCEL);
                }
            });

    private final Serializer<SerializableError> exceptionSerializer = ConjureExceptions.serializer();

    private final Duration timeout;

    ConjureAsyncRequestProcessing(Duration timeout) {
        this.timeout = timeout;
    }

    @Override
    public <T> void register(
            ListenableFuture<T> future,
            ReturnValueWriter<T> returnValueWriter,
            HttpServerExchange exchange) throws IOException {
        Preconditions.checkNotNull(future, "future");
        Preconditions.checkNotNull(returnValueWriter, "returnValueWriter");
        Preconditions.checkNotNull(exchange, "exchange");

        if (future.isDone()) {
            // Optimization: write the completed result immediately without dispatching across threads.
            writeCompleteFuture(future, returnValueWriter, exchange);
        } else {
            registerCallback(future, returnValueWriter, exchange);
        }
    }

    private <T> void writeCompleteFuture(
            ListenableFuture<T> future,
            ReturnValueWriter<T> returnValueWriter,
            HttpServerExchange exchange) throws IOException {
        try {
            T result = Futures.getDone(future);
            returnValueWriter.write(result, exchange);
        } catch (ExecutionException e) {
            ConjureExceptions.handle(exchange, exceptionSerializer, e.getCause());
        } catch (RuntimeException e) {
            ConjureExceptions.handle(exchange, exceptionSerializer, e);
        }
    }

    private <T> void registerCallback(
            ListenableFuture<T> future, ReturnValueWriter<T> returnValueWriter, HttpServerExchange exchange) {
        // Attach data to the exchange in order to reuse a stateless listener
        exchange.putAttachment(FUTURE, future);
        // Cancel the future if the exchange is completed before the future is done
        exchange.addExchangeCompleteListener(COMPLETION_LISTENER);

        XnioExecutor.Key timeoutKey = exchange.getIoThread()
                .executeAfter(() -> future.cancel(INTERRUPT_ON_CANCEL), timeout.toMillis(), TimeUnit.MILLISECONDS);
        future.addListener(timeoutKey::remove, DIRECT_EXECUTOR);
        // Dispatch the registration task, this accomplishes two things:
        // 1. Puts the exchange into a 'dispatched' state, otherwise the request will be terminated when
        //    the endpoint HttpHandler returns. See Connectors.executeRootHandler for more information.
        // 2. Avoids racing completion with the current handler chain. The dispatched task will be executed
        //    after this Endpoints HttpHandler returns. Otherwise an unlucky future could race
        //    HttpServerExchange.isInCall transitioning from true -> false, causing hte dispatch task not
        //    to execute.
        exchange.dispatch(() -> Futures.addCallback(future, new FutureCallback<T>() {
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
        }, DIRECT_EXECUTOR));
    }
}
