package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAsyncService {
    ListenableFuture<BinaryResponseBody> binaryAsync(AuthHeader authHeader);

    ListenableFuture<Optional<BinaryResponseBody>> binaryOptionalAsync(AuthHeader authHeader);

    ListenableFuture<Void> noReturnAsync(AuthHeader authHeader);

    ListenableFuture<Void> noReturnThrowingAsync(AuthHeader authHeader);

    ListenableFuture<Void> noReturnFailedFutureAsync(AuthHeader authHeader);

    ListenableFuture<Optional<String>> optionalEnumQueryAsync(
            AuthHeader authHeader, Optional<String> queryParamName);
}
