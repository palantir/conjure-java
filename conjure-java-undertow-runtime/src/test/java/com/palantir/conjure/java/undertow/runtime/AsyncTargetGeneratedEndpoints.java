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

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// TODO(ckozak): Delete and replace with generated code when the generator is ready
@SuppressWarnings("unused") // Example Code
public final class AsyncTargetGeneratedEndpoints implements UndertowService {
    private final AsyncTargetGeneratedService delegate;

    private AsyncTargetGeneratedEndpoints(AsyncTargetGeneratedService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(AsyncTargetGeneratedService delegate) {
        return new AsyncTargetGeneratedEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(
                Arrays.asList(
                        new StringEndpoint(runtime, delegate),
                        new IntegerEndpoint(runtime, delegate),
                        new BinaryEndpoint(runtime, delegate)));
    }

    private static final class StringEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<String> {
        private final UndertowRuntime runtime;

        private final AsyncTargetGeneratedService delegate;

        private final Serializer<String> serializer;

        StringEndpoint(UndertowRuntime runtime, AsyncTargetGeneratedService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<String> result = delegate.string(authHeader);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(String returnValue, HttpServerExchange exchange) throws IOException {
            serializer.serialize(returnValue, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/string";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "string";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class IntegerEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<Integer> {
        private final UndertowRuntime runtime;

        private final AsyncTargetGeneratedService delegate;

        private final Serializer<Integer> serializer;

        IntegerEndpoint(UndertowRuntime runtime, AsyncTargetGeneratedService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Integer>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<Integer> result = delegate.integer(authHeader);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Integer returnValue, HttpServerExchange exchange) throws IOException {
            serializer.serialize(returnValue, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/integer";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "integer";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class BinaryEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<BinaryResponseBody> {
        private final UndertowRuntime runtime;

        private final AsyncTargetGeneratedService delegate;

        BinaryEndpoint(UndertowRuntime runtime, AsyncTargetGeneratedService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<BinaryResponseBody> result = delegate.binary(authHeader);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(BinaryResponseBody returnValue, HttpServerExchange exchange) throws IOException {
            runtime.bodySerDe().serialize(returnValue, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/binary";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "binary";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
