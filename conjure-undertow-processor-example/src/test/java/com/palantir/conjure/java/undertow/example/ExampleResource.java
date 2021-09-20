/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.example;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

final class ExampleResource implements ExampleService {
    @Override
    public void simple() {}

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public ListenableFuture<String> pingAsync() {
        return Futures.immediateFuture(ping());
    }

    @Override
    public ListenableFuture<Void> voidAsync() {
        return Futures.immediateVoidFuture();
    }

    @Override
    public int returnPrimitive() {
        return 1;
    }

    @Override
    public BinaryResponseBody binary() {
        return Binary.INSTANCE;
    }

    @Override
    public CustomBinaryResponseBody namedBinary() {
        return Binary.INSTANCE;
    }

    @Override
    public Optional<BinaryResponseBody> optionalBinary() {
        return Optional.of(binary());
    }

    @Override
    public Optional<CustomBinaryResponseBody> optionalNamedBinary() {
        return Optional.of(namedBinary());
    }

    @Override
    public String post(String body) {
        return Preconditions.checkNotNull(body, "Body is required");
    }

    @Override
    public String queryParam(String queryParameter) {
        return Preconditions.checkNotNull(queryParameter, "Query parameter is required");
    }

    @Override
    public String pathParam(String param) {
        return Preconditions.checkNotNull(param, "Path parameter is required");
    }

    @Override
    public String headerParam(String headerValue) {
        return Preconditions.checkNotNull(headerValue, "Header parameter is required");
    }

    @Override
    public String authenticated(AuthHeader auth) {
        return Preconditions.checkNotNull(auth, "AuthHeader is required")
                .getBearerToken()
                .toString();
    }

    @Override
    public void exchange(HttpServerExchange exchange) {
        Preconditions.checkNotNull(exchange, "HttpServerExchange is required");
    }

    @Override
    public void context(RequestContext context) {
        Preconditions.checkNotNull(context, "RequestContext is required");
    }

    private enum Binary implements CustomBinaryResponseBody {
        INSTANCE;

        @Override
        public void write(OutputStream responseBody) throws IOException {
            responseBody.write("binary".getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void close() {
            // nop
        }
    }
}
