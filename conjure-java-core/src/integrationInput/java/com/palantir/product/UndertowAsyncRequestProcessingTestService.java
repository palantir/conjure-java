package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAsyncRequestProcessingTestService extends UndertowService {
    /**
     * @apiNote {@code GET /async/delay}
     */
    ListenableFuture<String> delay(OptionalInt delayMillis);

    /**
     * @apiNote {@code GET /async/throws}
     */
    ListenableFuture<Void> throwsInHandler();

    /**
     * @apiNote {@code GET /async/failed-future}
     */
    ListenableFuture<Void> failedFuture(OptionalInt delayMillis);

    /**
     * @apiNote {@code GET /async/binary}
     */
    ListenableFuture<Optional<BinaryResponseBody>> binary(Optional<String> stringValue);

    /**
     * @apiNote {@code GET /async/future-trace}
     */
    ListenableFuture<Object> futureTraceId(OptionalInt delayMillis);

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return AsyncRequestProcessingTestServiceEndpoints.of(this).endpoints(runtime);
    }
}
