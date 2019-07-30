package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAsyncRequestProcessingTestService {
    ListenableFuture<String> delay(OptionalInt delayMillis);

    ListenableFuture<Void> throwsInHandler();

    ListenableFuture<Void> failedFuture(OptionalInt delayMillis);

    ListenableFuture<Optional<BinaryResponseBody>> binary(Optional<String> stringValue);

    ListenableFuture<Object> futureTraceId(OptionalInt delayMillis);
}
