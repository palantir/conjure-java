package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAsyncRequestProcessingTestService {
    /**
     * GET /async/delay
     */
    ListenableFuture<String> delay(OptionalInt delayMillis);

    /**
     * GET /async/throws
     */
    ListenableFuture<Void> throwsInHandler();

    /**
     * GET /async/failed-future
     */
    ListenableFuture<Void> failedFuture(OptionalInt delayMillis);

    /**
     * GET /async/binary
     */
    ListenableFuture<Optional<BinaryResponseBody>> binary(Optional<String> stringValue);

    /**
     * GET /async/future-trace
     */
    ListenableFuture<Object> futureTraceId(OptionalInt delayMillis);
}
