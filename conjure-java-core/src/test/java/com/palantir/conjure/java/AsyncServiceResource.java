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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.logsafe.SafeArg;
import com.palantir.product.UndertowAsyncService;
import com.palantir.tokens.auth.AuthHeader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/** Simple synchronous implementation using asynchronous APIs. */
final class AsyncServiceResource implements UndertowAsyncService {
    @Override
    public ListenableFuture<BinaryResponseBody> binaryAsync(AuthHeader authHeader) {
        return Futures.immediateFuture(responseBody ->
                responseBody.write("Hello, World".getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public ListenableFuture<Optional<BinaryResponseBody>> binaryOptionalAsync(AuthHeader authHeader) {
        return Futures.immediateFuture(Optional.of(responseBody ->
                responseBody.write("Hello, World".getBytes(StandardCharsets.UTF_8))));
    }

    @Override
    public ListenableFuture<Void> noReturnAsync(AuthHeader authHeader) {
        return Futures.immediateFuture(null);
    }

    @Override
    public ListenableFuture<Void> noReturnThrowingAsync(AuthHeader authHeader) {
        throw new ServiceException(ErrorType.INTERNAL, SafeArg.of("type", "throwing"));
    }

    @Override
    public ListenableFuture<Void> noReturnFailedFutureAsync(AuthHeader authHeader) {
        return Futures.immediateFailedFuture(new ServiceException(ErrorType.INTERNAL,
                SafeArg.of("type", "failedFuture")));
    }

    @Override
    public ListenableFuture<Optional<String>> optionalEnumQueryAsync(
            AuthHeader authHeader, Optional<String> queryParamName) {
        return Futures.immediateFuture(queryParamName);
    }
}
