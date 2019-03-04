package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EteBinaryServiceEndpoints implements Service {
    private final UndertowEteBinaryService delegate;

    private EteBinaryServiceEndpoints(UndertowEteBinaryService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowEteBinaryService delegate) {
        return new EteBinaryServiceEndpoints(delegate);
    }

    @Override
    public void register(UndertowRuntime runtime, EndpointRegistry registry) {
        new EteBinaryServiceRegistrable(runtime, delegate).register(registry);
    }

    private static final class EteBinaryServiceRegistrable {
        private final UndertowEteBinaryService delegate;

        private final UndertowRuntime runtime;

        private EteBinaryServiceRegistrable(
                UndertowRuntime runtime, UndertowEteBinaryService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        void register(EndpointRegistry registry) {
            registry.add(
                    Endpoint.post("/binary", "EteBinaryService", "postBinary"),
                    new PostBinaryHandler());
            registry.add(
                    Endpoint.get(
                            "/binary/optional/present",
                            "EteBinaryService",
                            "getOptionalBinaryPresent"),
                    new GetOptionalBinaryPresentHandler());
            registry.add(
                    Endpoint.get(
                            "/binary/optional/empty", "EteBinaryService", "getOptionalBinaryEmpty"),
                    new GetOptionalBinaryEmptyHandler());
            registry.add(
                    Endpoint.get("/binary/failure", "EteBinaryService", "getBinaryFailure"),
                    new GetBinaryFailureHandler());
        }

        private class PostBinaryHandler implements HttpHandler {
            private final TypeToken<InputStream> bodyType = new TypeToken<InputStream>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                InputStream body = runtime.serde().deserializeInputStream(exchange);
                BinaryResponseBody result = delegate.postBinary(authHeader, body);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class GetOptionalBinaryPresentHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Optional<BinaryResponseBody> result = delegate.getOptionalBinaryPresent(authHeader);
                if (result.isPresent()) {
                    runtime.serde().serialize(result.get(), exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class GetOptionalBinaryEmptyHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Optional<BinaryResponseBody> result = delegate.getOptionalBinaryEmpty(authHeader);
                if (result.isPresent()) {
                    runtime.serde().serialize(result.get(), exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class GetBinaryFailureHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                int numBytes = runtime.serde().deserializeInteger(queryParams.get("numBytes"));
                BinaryResponseBody result = delegate.getBinaryFailure(authHeader, numBytes);
                runtime.serde().serialize(result, exchange);
            }
        }
    }
}
