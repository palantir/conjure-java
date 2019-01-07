package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.Routable;
import com.palantir.conjure.java.undertow.lib.RoutingRegistry;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.internal.Auth;
import com.palantir.conjure.java.undertow.lib.internal.BinarySerializers;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EteServiceEndpoint implements Endpoint {
    private final UndertowEteService delegate;

    private EteServiceEndpoint(UndertowEteService delegate) {
        this.delegate = delegate;
    }

    public static Endpoint of(UndertowEteService delegate) {
        return new EteServiceEndpoint(delegate);
    }

    @Override
    public Routable create(HandlerContext context) {
        return new EteServiceRoutable(context, delegate);
    }

    private static final class EteServiceRoutable implements Routable {
        private final UndertowEteService delegate;

        private final SerializerRegistry serializers;

        private EteServiceRoutable(HandlerContext context, UndertowEteService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(RoutingRegistry routingRegistry) {
            routingRegistry
                    .get("/base/string", new StringHandler())
                    .get("/base/integer", new IntegerHandler())
                    .get("/base/double", new Double_Handler())
                    .get("/base/boolean", new Boolean_Handler())
                    .get("/base/safelong", new SafelongHandler())
                    .get("/base/rid", new RidHandler())
                    .get("/base/bearertoken", new BearertokenHandler())
                    .get("/base/optionalString", new OptionalStringHandler())
                    .get("/base/optionalEmpty", new OptionalEmptyHandler())
                    .get("/base/datetime", new DatetimeHandler())
                    .get("/base/binary", new BinaryHandler())
                    .post("/base/notNullBody", new NotNullBodyHandler())
                    .get("/base/aliasOne", new AliasOneHandler())
                    .get("/base/optionalAliasOne", new OptionalAliasOneHandler())
                    .get("/base/aliasTwo", new AliasTwoHandler())
                    .post("/base/external/notNullBody", new NotNullBodyExternalImportHandler())
                    .post("/base/external/optional-body", new OptionalBodyExternalImportHandler())
                    .post("/base/external/optional-query", new OptionalQueryExternalImportHandler())
                    .post("/base/no-return", new NoReturnHandler());
        }

        private class StringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                String result = delegate.string(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class IntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                int result = delegate.integer(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class Double_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                double result = delegate.double_(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class Boolean_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                boolean result = delegate.boolean_(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class SafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                SafeLong result = delegate.safelong(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class RidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                ResourceIdentifier result = delegate.rid(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class BearertokenHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                BearerToken result = delegate.bearertoken(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class OptionalStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Optional<String> result = delegate.optionalString(authHeader);
                if (result.isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class OptionalEmptyHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Optional<String> result = delegate.optionalEmpty(authHeader);
                if (result.isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class DatetimeHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                OffsetDateTime result = delegate.datetime(authHeader);
                serializers.serialize(result, exchange);
            }
        }

        private class BinaryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                BinaryResponseBody result = delegate.binary(authHeader);
                BinarySerializers.serialize(result, exchange);
            }
        }

        private class NotNullBodyHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                StringAliasExample notNullBody = serializers.deserialize(notNullBodyType, exchange);
                StringAliasExample result = delegate.notNullBody(authHeader, notNullBody);
                serializers.serialize(result, exchange);
            }
        }

        private class AliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String queryParamNameRaw =
                        StringDeserializers.deserializeString(queryParams.get("queryParamName"));
                StringAliasExample queryParamName = StringAliasExample.of(queryParamNameRaw);
                StringAliasExample result = delegate.aliasOne(authHeader, queryParamName);
                serializers.serialize(result, exchange);
            }
        }

        private class OptionalAliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<String> queryParamNameRaw =
                        StringDeserializers.deserializeOptionalString(
                                queryParams.get("queryParamName"));
                Optional<StringAliasExample> queryParamName =
                        Optional.ofNullable(
                                queryParamNameRaw.isPresent()
                                        ? StringAliasExample.of(queryParamNameRaw.get())
                                        : null);
                StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
                serializers.serialize(result, exchange);
            }
        }

        private class AliasTwoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String queryParamNameRaw =
                        StringDeserializers.deserializeString(queryParams.get("queryParamName"));
                NestedStringAliasExample queryParamName =
                        NestedStringAliasExample.of(StringAliasExample.of(queryParamNameRaw));
                NestedStringAliasExample result = delegate.aliasTwo(authHeader, queryParamName);
                serializers.serialize(result, exchange);
            }
        }

        private class NotNullBodyExternalImportHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                StringAliasExample notNullBody = serializers.deserialize(notNullBodyType, exchange);
                StringAliasExample result =
                        delegate.notNullBodyExternalImport(authHeader, notNullBody);
                serializers.serialize(result, exchange);
            }
        }

        private class OptionalBodyExternalImportHandler implements HttpHandler {
            private final TypeToken<Optional<StringAliasExample>> bodyType =
                    new TypeToken<Optional<StringAliasExample>>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Optional<StringAliasExample> body = serializers.deserialize(bodyType, exchange);
                Optional<StringAliasExample> result =
                        delegate.optionalBodyExternalImport(authHeader, body);
                if (result.isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class OptionalQueryExternalImportHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<StringAliasExample> query =
                        StringDeserializers.deserializeOptionalString(queryParams.get("query"))
                                .map(StringAliasExample::valueOf);
                Optional<StringAliasExample> result =
                        delegate.optionalQueryExternalImport(authHeader, query);
                if (result.isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class NoReturnHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                delegate.noReturn(authHeader);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }
}
