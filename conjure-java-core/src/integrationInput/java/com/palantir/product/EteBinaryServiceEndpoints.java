package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import com.palantir.conjure.java.undertow.lib.internal.Auth;
import com.palantir.conjure.java.undertow.lib.internal.BinarySerializers;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Methods;
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
    public Registrable create(ServiceContext context) {
        return new EteBinaryServiceRegistrable(context, delegate);
    }

    private static final class EteBinaryServiceRegistrable implements Registrable {
        private final UndertowEteBinaryService delegate;

        private final SerializerRegistry serializers;

        private EteBinaryServiceRegistrable(
                ServiceContext context, UndertowEteBinaryService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/binary")
                                    .serviceName("EteBinaryService")
                                    .name("postBinary")
                                    .build(),
                            new PostBinaryHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/binary/optional/present")
                                    .serviceName("EteBinaryService")
                                    .name("getOptionalBinaryPresent")
                                    .build(),
                            new GetOptionalBinaryPresentHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/binary/optional/empty")
                                    .serviceName("EteBinaryService")
                                    .name("getOptionalBinaryEmpty")
                                    .build(),
                            new GetOptionalBinaryEmptyHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/binary/failure")
                                    .serviceName("EteBinaryService")
                                    .name("getBinaryFailure")
                                    .build(),
                            new GetBinaryFailureHandler());
        }

        private class PostBinaryHandler implements HttpHandler {
            private final TypeToken<InputStream> bodyType = new TypeToken<InputStream>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                InputStream body = BinarySerializers.deserializeInputStream(exchange);
                BinaryResponseBody result = delegate.postBinary(authHeader, body);
                BinarySerializers.serialize(result, exchange);
            }
        }

        private class GetOptionalBinaryPresentHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Optional<BinaryResponseBody> result = delegate.getOptionalBinaryPresent(authHeader);
                if (result.isPresent()) {
                    BinarySerializers.serialize(result.get(), exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class GetOptionalBinaryEmptyHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Optional<BinaryResponseBody> result = delegate.getOptionalBinaryEmpty(authHeader);
                if (result.isPresent()) {
                    BinarySerializers.serialize(result.get(), exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class GetBinaryFailureHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                int numBytes = StringDeserializers.deserializeInteger(queryParams.get("numBytes"));
                BinaryResponseBody result = delegate.getBinaryFailure(authHeader, numBytes);
                BinarySerializers.serialize(result, exchange);
            }
        }
    }
}
