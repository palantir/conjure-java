package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
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
    public void register(ServiceContext context, EndpointRegistry endpointRegistry) {
        new EteBinaryServiceRegistrable(context, delegate).register(endpointRegistry);
    }

    private static final class EteBinaryServiceRegistrable {
        private final UndertowEteBinaryService delegate;

        private final ServiceContext context;

        private EteBinaryServiceRegistrable(
                ServiceContext context, UndertowEteBinaryService delegate) {
            this.context = context;
            this.delegate = context.instrument(delegate, UndertowEteBinaryService.class);
        }

        void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.post("/binary", "EteBinaryService", "postBinary"),
                            new PostBinaryHandler())
                    .add(
                            Endpoint.get(
                                    "/binary/optional/present",
                                    "EteBinaryService",
                                    "getOptionalBinaryPresent"),
                            new GetOptionalBinaryPresentHandler())
                    .add(
                            Endpoint.get(
                                    "/binary/optional/empty",
                                    "EteBinaryService",
                                    "getOptionalBinaryEmpty"),
                            new GetOptionalBinaryEmptyHandler())
                    .add(
                            Endpoint.get("/binary/failure", "EteBinaryService", "getBinaryFailure"),
                            new GetBinaryFailureHandler());
        }

        private class PostBinaryHandler implements HttpHandler {
            private final TypeToken<InputStream> bodyType = new TypeToken<InputStream>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                InputStream body = context.deserializeInputStream(exchange);
                BinaryResponseBody result = delegate.postBinary(authHeader, body);
                context.serialize(result, exchange);
            }
        }

        private class GetOptionalBinaryPresentHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Optional<BinaryResponseBody> result = delegate.getOptionalBinaryPresent(authHeader);
                if (result.isPresent()) {
                    context.serialize(result.get(), exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class GetOptionalBinaryEmptyHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Optional<BinaryResponseBody> result = delegate.getOptionalBinaryEmpty(authHeader);
                if (result.isPresent()) {
                    context.serialize(result.get(), exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class GetBinaryFailureHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                int numBytes = context.deserializeInteger(queryParams.get("numBytes"));
                BinaryResponseBody result = delegate.getBinaryFailure(authHeader, numBytes);
                context.serialize(result, exchange);
            }
        }
    }
}
