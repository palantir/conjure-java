package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EteBinaryServiceEndpoints implements UndertowService {
    private final UndertowEteBinaryService delegate;

    private EteBinaryServiceEndpoints(UndertowEteBinaryService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowEteBinaryService delegate) {
        return new EteBinaryServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return new EteBinaryServiceFactory(runtime, delegate).create();
    }

    private static final class EteBinaryServiceFactory {
        private final UndertowEteBinaryService delegate;

        private final UndertowRuntime runtime;

        private EteBinaryServiceFactory(
                UndertowRuntime runtime, UndertowEteBinaryService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        List<Endpoint> create() {
            return ImmutableList.of(
                    new PostBinaryEndpoint(),
                    new GetOptionalBinaryPresentEndpoint(),
                    new GetOptionalBinaryEmptyEndpoint(),
                    new GetBinaryFailureEndpoint());
        }

        private class PostBinaryEndpoint implements HttpHandler, Endpoint {
            private final TypeToken<InputStream> bodyType = new TypeToken<InputStream>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                InputStream body = runtime.serde().deserializeInputStream(exchange);
                BinaryResponseBody result = delegate.postBinary(authHeader, body);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.POST;
            }

            @Override
            public final String template() {
                return "/binary";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteBinaryService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("postBinary");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class GetOptionalBinaryPresentEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/binary/optional/present";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteBinaryService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("getOptionalBinaryPresent");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class GetOptionalBinaryEmptyEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/binary/optional/empty";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteBinaryService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("getOptionalBinaryEmpty");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class GetBinaryFailureEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                int numBytes = runtime.serde().deserializeInteger(queryParams.get("numBytes"));
                BinaryResponseBody result = delegate.getBinaryFailure(authHeader, numBytes);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/binary/failure";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteBinaryService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("getBinaryFailure");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }
    }
}
