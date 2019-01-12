package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.Metrics;
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
import io.undertow.util.HeaderMap;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Deque;
import java.util.List;
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
                    .get("/base/path/{param}", new PathHandler())
                    .post("/base/notNullBody", new NotNullBodyHandler())
                    .get("/base/aliasOne", new AliasOneHandler())
                    .get("/base/optionalAliasOne", new OptionalAliasOneHandler())
                    .get("/base/aliasTwo", new AliasTwoHandler())
                    .post("/base/external/notNullBody", new NotNullBodyExternalImportHandler())
                    .post("/base/external/optional-body", new OptionalBodyExternalImportHandler())
                    .post("/base/external/optional-query", new OptionalQueryExternalImportHandler())
                    .post("/base/no-return", new NoReturnHandler())
                    .get("/base/enum/query", new EnumQueryHandler())
                    .get("/base/enum/list/query", new EnumListQueryHandler())
                    .get("/base/enum/optional/query", new OptionalEnumQueryHandler())
                    .get("/base/enum/header", new EnumHeaderHandler());
        }

        private class StringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                String result = delegate.string(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class IntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                int result = delegate.integer(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class Double_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                double result = delegate.double_(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class Boolean_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                boolean result = delegate.boolean_(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class SafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                SafeLong result = delegate.safelong(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class RidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                ResourceIdentifier result = delegate.rid(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class BearertokenHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                BearerToken result = delegate.bearertoken(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class OptionalStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                Optional<String> result = delegate.optionalString(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                Optional<String> result = delegate.optionalEmpty(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                OffsetDateTime result = delegate.datetime(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class BinaryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Long start = System.currentTimeMillis();
                BinaryResponseBody result = delegate.binary(authHeader);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                BinarySerializers.serialize(result, exchange);
            }
        }

        private class PathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                String param = StringDeserializers.deserializeString(pathParams.get("param"));
                Long start = System.currentTimeMillis();
                String result = delegate.path(authHeader, param);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class NotNullBodyHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                StringAliasExample notNullBody = serializers.deserialize(notNullBodyType, exchange);
                Long start = System.currentTimeMillis();
                StringAliasExample result = delegate.notNullBody(authHeader, notNullBody);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                StringAliasExample result = delegate.aliasOne(authHeader, queryParamName);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                NestedStringAliasExample result = delegate.aliasTwo(authHeader, queryParamName);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                StringAliasExample result =
                        delegate.notNullBodyExternalImport(authHeader, notNullBody);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                Long start = System.currentTimeMillis();
                Optional<StringAliasExample> result =
                        delegate.optionalBodyExternalImport(authHeader, body);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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
                        StringDeserializers.deserializeOptionalComplex(
                                queryParams.get("query"), StringAliasExample::valueOf);
                Long start = System.currentTimeMillis();
                Optional<StringAliasExample> result =
                        delegate.optionalQueryExternalImport(authHeader, query);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
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

        private class EnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                SimpleEnum queryParamName =
                        StringDeserializers.deserializeComplex(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                Long start = System.currentTimeMillis();
                SimpleEnum result = delegate.enumQuery(authHeader, queryParamName);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class EnumListQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                List<SimpleEnum> queryParamName =
                        StringDeserializers.deserializeComplexList(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                Long start = System.currentTimeMillis();
                List<SimpleEnum> result = delegate.enumListQuery(authHeader, queryParamName);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }

        private class OptionalEnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<SimpleEnum> queryParamName =
                        StringDeserializers.deserializeOptionalComplex(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                Long start = System.currentTimeMillis();
                Optional<SimpleEnum> result =
                        delegate.optionalEnumQuery(authHeader, queryParamName);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                if (result.isPresent()) {
                    serializers.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class EnumHeaderHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                HeaderMap headerParams = exchange.getRequestHeaders();
                SimpleEnum headerParameter =
                        StringDeserializers.deserializeComplex(
                                headerParams.get("Custom-Header"), SimpleEnum::valueOf);
                Long start = System.currentTimeMillis();
                SimpleEnum result = delegate.enumHeader(authHeader, headerParameter);
                Long duration = System.currentTimeMillis() - start;
                exchange.putAttachment(Metrics.DELEGATE_DURATION_KEY, duration);
                serializers.serialize(result, exchange);
            }
        }
    }
}
