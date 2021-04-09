package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
@ConjureService(name = "AsyncRequestProcessingTestService", package_ = "com.palantir.product")
public interface UndertowAsyncRequestProcessingTestService {
    /**
     * @apiNote {@code GET /async/delay}
     */
    @ConjureEndpoint(path = "/async/delay", method = "GET")
    ListenableFuture<String> delay(OptionalInt delayMillis);

    /**
     * @apiNote {@code GET /async/throws}
     */
    @ConjureEndpoint(path = "/async/throws", method = "GET")
    ListenableFuture<Void> throwsInHandler();

    /**
     * @apiNote {@code GET /async/failed-future}
     */
    @ConjureEndpoint(path = "/async/failed-future", method = "GET")
    ListenableFuture<Void> failedFuture(OptionalInt delayMillis);

    /**
     * @apiNote {@code GET /async/binary}
     */
    @ConjureEndpoint(path = "/async/binary", method = "GET")
    ListenableFuture<Optional<BinaryResponseBody>> binary(Optional<String> stringValue);

    /**
     * @apiNote {@code GET /async/future-trace}
     */
    @ConjureEndpoint(path = "/async/future-trace", method = "GET")
    ListenableFuture<Object> futureTraceId(OptionalInt delayMillis);
}
