package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Parameters;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
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
public final class EteServiceEndpoints implements Service {
    private final UndertowEteService delegate;

    private EteServiceEndpoints(UndertowEteService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowEteService delegate) {
        return new EteServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new EteServiceRegistrable(context, delegate);
    }

    private static final class EteServiceRegistrable implements Registrable {
        private final UndertowEteService delegate;

        private final SerializerRegistry serializers;

        private EteServiceRegistrable(ServiceContext context, UndertowEteService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate =
                    context.serviceInstrumenter().instrument(delegate, UndertowEteService.class);
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(Endpoint.get("/base/string", "EteService", "string"), new StringHandler())
                    .add(
                            Endpoint.get("/base/integer", "EteService", "integer"),
                            new IntegerHandler())
                    .add(
                            Endpoint.get("/base/double", "EteService", "double_"),
                            new Double_Handler())
                    .add(
                            Endpoint.get("/base/boolean", "EteService", "boolean_"),
                            new Boolean_Handler())
                    .add(
                            Endpoint.get("/base/safelong", "EteService", "safelong"),
                            new SafelongHandler())
                    .add(Endpoint.get("/base/rid", "EteService", "rid"), new RidHandler())
                    .add(
                            Endpoint.get("/base/bearertoken", "EteService", "bearertoken"),
                            new BearertokenHandler())
                    .add(
                            Endpoint.get("/base/optionalString", "EteService", "optionalString"),
                            new OptionalStringHandler())
                    .add(
                            Endpoint.get("/base/optionalEmpty", "EteService", "optionalEmpty"),
                            new OptionalEmptyHandler())
                    .add(
                            Endpoint.get("/base/datetime", "EteService", "datetime"),
                            new DatetimeHandler())
                    .add(Endpoint.get("/base/binary", "EteService", "binary"), new BinaryHandler())
                    .add(
                            Endpoint.get("/base/path/{param}", "EteService", "path"),
                            new PathHandler())
                    .add(
                            Endpoint.post("/base/notNullBody", "EteService", "notNullBody"),
                            new NotNullBodyHandler())
                    .add(
                            Endpoint.get("/base/aliasOne", "EteService", "aliasOne"),
                            new AliasOneHandler())
                    .add(
                            Endpoint.get(
                                    "/base/optionalAliasOne", "EteService", "optionalAliasOne"),
                            new OptionalAliasOneHandler())
                    .add(
                            Endpoint.get("/base/aliasTwo", "EteService", "aliasTwo"),
                            new AliasTwoHandler())
                    .add(
                            Endpoint.post(
                                    "/base/external/notNullBody",
                                    "EteService",
                                    "notNullBodyExternalImport"),
                            new NotNullBodyExternalImportHandler())
                    .add(
                            Endpoint.post(
                                    "/base/external/optional-body",
                                    "EteService",
                                    "optionalBodyExternalImport"),
                            new OptionalBodyExternalImportHandler())
                    .add(
                            Endpoint.post(
                                    "/base/external/optional-query",
                                    "EteService",
                                    "optionalQueryExternalImport"),
                            new OptionalQueryExternalImportHandler())
                    .add(
                            Endpoint.post("/base/no-return", "EteService", "noReturn"),
                            new NoReturnHandler())
                    .add(
                            Endpoint.get("/base/enum/query", "EteService", "enumQuery"),
                            new EnumQueryHandler())
                    .add(
                            Endpoint.get("/base/enum/list/query", "EteService", "enumListQuery"),
                            new EnumListQueryHandler())
                    .add(
                            Endpoint.get(
                                    "/base/enum/optional/query", "EteService", "optionalEnumQuery"),
                            new OptionalEnumQueryHandler())
                    .add(
                            Endpoint.get("/base/enum/header", "EteService", "enumHeader"),
                            new EnumHeaderHandler());
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

        private class PathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                String param = StringDeserializers.deserializeString(pathParams.get("param"));
                Parameters.putUnsafePathParam(exchange, "param", param);
                String result = delegate.path(authHeader, param);
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
                Parameters.putUnsafeQueryParam(exchange, "queryParamName", queryParamName);
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
                Parameters.putUnsafeQueryParam(exchange, "queryParamName", queryParamName);
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
                Parameters.putUnsafeQueryParam(exchange, "queryParamName", queryParamName);
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
                        StringDeserializers.deserializeOptionalComplex(
                                queryParams.get("query"), StringAliasExample::valueOf);
                Parameters.putUnsafeQueryParam(exchange, "query", query);
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

        private class EnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = Auth.header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                SimpleEnum queryParamName =
                        StringDeserializers.deserializeComplex(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                Parameters.putUnsafeQueryParam(exchange, "queryParamName", queryParamName);
                SimpleEnum result = delegate.enumQuery(authHeader, queryParamName);
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
                Parameters.putUnsafeQueryParam(exchange, "queryParamName", queryParamName);
                List<SimpleEnum> result = delegate.enumListQuery(authHeader, queryParamName);
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
                Parameters.putUnsafeQueryParam(exchange, "queryParamName", queryParamName);
                Optional<SimpleEnum> result =
                        delegate.optionalEnumQuery(authHeader, queryParamName);
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
                Parameters.putUnsafeHeaderParam(exchange, "Custom-Header", headerParameter);
                SimpleEnum result = delegate.enumHeader(authHeader, headerParameter);
                serializers.serialize(result, exchange);
            }
        }
    }
}
