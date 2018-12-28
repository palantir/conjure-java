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
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
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
                    .get("/base/aliasTwo", new AliasTwoHandler());
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
                    exchange.setStatusCode(204);
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
                    exchange.setStatusCode(204);
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
                StringAliasExample queryParamName =
                        StringAliasExample.valueOf(
                                StringDeserializers.deserializeString(
                                        queryParams.get("queryParamName")));
                StringAliasExample result = delegate.aliasOne(authHeader, queryParamName);
                serializers.serialize(result, exchange);
            }
        }

        private class OptionalAliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<StringAliasExample> queryParamName =
                        StringDeserializers.deserializeOptionalString(
                                        queryParams.get("queryParamName"))
                                .map(StringAliasExample::valueOf);
                StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
                serializers.serialize(result, exchange);
            }
        }

        private class AliasTwoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                NestedStringAliasExample queryParamName =
                        NestedStringAliasExample.valueOf(
                                StringDeserializers.deserializeString(
                                        queryParams.get("queryParamName")));
                NestedStringAliasExample result = delegate.aliasTwo(authHeader, queryParamName);
                serializers.serialize(result, exchange);
            }
        }
    }
}
