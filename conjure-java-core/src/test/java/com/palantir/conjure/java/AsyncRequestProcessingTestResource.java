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

package com.palantir.conjure.java;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.product.UndertowAsyncRequestProcessingTestService;
import com.palantir.tracing.Tracer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.TimeUnit;

final class AsyncRequestProcessingTestResource implements UndertowAsyncRequestProcessingTestService {

    private final ListeningScheduledExecutorService executor;

    AsyncRequestProcessingTestResource(ListeningScheduledExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public ListenableFuture<String> delay(OptionalInt delayMillis) {
        if (delayMillis.isPresent()) {
            int delay = delayMillis.getAsInt();
            return Futures.transform(
                    executor.schedule(() -> {}, delay, TimeUnit.MILLISECONDS),
                    ignored -> "Completed after " + delay + "ms",
                    MoreExecutors.directExecutor());
        } else {
            return Futures.immediateFuture("Completed immediately");
        }
    }

    @Override
    public ListenableFuture<Void> throwsInHandler() {
        throw new ServiceException(ErrorType.CONFLICT);
    }

    @Override
    public ListenableFuture<Void> failedFuture(OptionalInt delayMillis) {
        if (delayMillis.isPresent()) {
            return Futures.transformAsync(
                    executor.schedule(() -> {}, delayMillis.getAsInt(), TimeUnit.MILLISECONDS),
                    input -> Futures.immediateFailedFuture(new ServiceException(ErrorType.CONFLICT)),
                    MoreExecutors.directExecutor());
        } else {
            return Futures.immediateFailedFuture(new ServiceException(ErrorType.CONFLICT));
        }
    }

    @Override
    public ListenableFuture<Optional<BinaryResponseBody>> binary(Optional<String> stringValue) {
        return executor.submit(() ->
                stringValue.map(string -> responseBody -> responseBody.write(string.getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public ListenableFuture<Object> futureTraceId(OptionalInt delayMillis) {
        if (delayMillis.isPresent()) {
            int delay = delayMillis.getAsInt();
            return Futures.transform(
                    executor.schedule(() -> {}, delay, TimeUnit.MILLISECONDS),
                    ignored -> new LazyTraceValue(),
                    MoreExecutors.directExecutor());
        } else {
            return Futures.immediateFuture(new LazyTraceValue());
        }
    }

    private static final class LazyTraceValue {

        @JsonValue
        public String traceId() {
            return Tracer.getTraceId();
        }
    }
}
