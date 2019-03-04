package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
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

        private final ServiceContext context;

        private EteServiceRegistrable(ServiceContext context, UndertowEteService delegate) {
            this.context = context;
            this.delegate = context.instrument(delegate, UndertowEteService.class);
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
                AuthHeader authHeader = context.authHeader(exchange);
                String result = delegate.string(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class IntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                int result = delegate.integer(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class Double_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                double result = delegate.double_(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class Boolean_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                boolean result = delegate.boolean_(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class SafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                SafeLong result = delegate.safelong(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class RidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                ResourceIdentifier result = delegate.rid(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class BearertokenHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                BearerToken result = delegate.bearertoken(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class OptionalStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Optional<String> result = delegate.optionalString(authHeader);
                if (result.isPresent()) {
                    context.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class OptionalEmptyHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Optional<String> result = delegate.optionalEmpty(authHeader);
                if (result.isPresent()) {
                    context.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class DatetimeHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                OffsetDateTime result = delegate.datetime(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class BinaryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                BinaryResponseBody result = delegate.binary(authHeader);
                context.serialize(result, exchange);
            }
        }

        private class PathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                String param = context.deserializeString(pathParams.get("param"));
                String result = delegate.path(authHeader, param);
                context.serialize(result, exchange);
            }
        }

        private class NotNullBodyHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                StringAliasExample notNullBody = context.deserialize(notNullBodyType, exchange);
                StringAliasExample result = delegate.notNullBody(authHeader, notNullBody);
                context.serialize(result, exchange);
            }
        }

        private class AliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String queryParamNameRaw =
                        context.deserializeString(queryParams.get("queryParamName"));
                StringAliasExample queryParamName = StringAliasExample.of(queryParamNameRaw);
                StringAliasExample result = delegate.aliasOne(authHeader, queryParamName);
                context.serialize(result, exchange);
            }
        }

        private class OptionalAliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<String> queryParamNameRaw =
                        context.deserializeOptionalString(queryParams.get("queryParamName"));
                Optional<StringAliasExample> queryParamName =
                        Optional.ofNullable(
                                queryParamNameRaw.isPresent()
                                        ? StringAliasExample.of(queryParamNameRaw.get())
                                        : null);
                StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
                context.serialize(result, exchange);
            }
        }

        private class AliasTwoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String queryParamNameRaw =
                        context.deserializeString(queryParams.get("queryParamName"));
                NestedStringAliasExample queryParamName =
                        NestedStringAliasExample.of(StringAliasExample.of(queryParamNameRaw));
                NestedStringAliasExample result = delegate.aliasTwo(authHeader, queryParamName);
                context.serialize(result, exchange);
            }
        }

        private class NotNullBodyExternalImportHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                StringAliasExample notNullBody = context.deserialize(notNullBodyType, exchange);
                StringAliasExample result =
                        delegate.notNullBodyExternalImport(authHeader, notNullBody);
                context.serialize(result, exchange);
            }
        }

        private class OptionalBodyExternalImportHandler implements HttpHandler {
            private final TypeToken<Optional<StringAliasExample>> bodyType =
                    new TypeToken<Optional<StringAliasExample>>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Optional<StringAliasExample> body = context.deserialize(bodyType, exchange);
                Optional<StringAliasExample> result =
                        delegate.optionalBodyExternalImport(authHeader, body);
                if (result.isPresent()) {
                    context.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class OptionalQueryExternalImportHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<StringAliasExample> query =
                        context.deserializeOptionalComplex(
                                queryParams.get("query"), StringAliasExample::valueOf);
                Optional<StringAliasExample> result =
                        delegate.optionalQueryExternalImport(authHeader, query);
                if (result.isPresent()) {
                    context.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class NoReturnHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                delegate.noReturn(authHeader);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class EnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                SimpleEnum queryParamName =
                        context.deserializeComplex(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                SimpleEnum result = delegate.enumQuery(authHeader, queryParamName);
                context.serialize(result, exchange);
            }
        }

        private class EnumListQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                List<SimpleEnum> queryParamName =
                        context.deserializeComplexList(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                List<SimpleEnum> result = delegate.enumListQuery(authHeader, queryParamName);
                context.serialize(result, exchange);
            }
        }

        private class OptionalEnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<SimpleEnum> queryParamName =
                        context.deserializeOptionalComplex(
                                queryParams.get("queryParamName"), SimpleEnum::valueOf);
                Optional<SimpleEnum> result =
                        delegate.optionalEnumQuery(authHeader, queryParamName);
                if (result.isPresent()) {
                    context.serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class EnumHeaderHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = context.authHeader(exchange);
                HeaderMap headerParams = exchange.getRequestHeaders();
                SimpleEnum headerParameter =
                        context.deserializeComplex(
                                headerParams.get("Custom-Header"), SimpleEnum::valueOf);
                SimpleEnum result = delegate.enumHeader(authHeader, headerParameter);
                context.serialize(result, exchange);
            }
        }
    }
}
