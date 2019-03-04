package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
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
    public void register(UndertowRuntime runtime, EndpointRegistry registry) {
        new EteServiceRegistrable(runtime, delegate).register(registry);
    }

    private static final class EteServiceRegistrable {
        private final UndertowEteService delegate;

        private final UndertowRuntime runtime;

        private EteServiceRegistrable(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        void register(EndpointRegistry registry) {
            registry.add(Endpoint.get("/base/string", "EteService", "string"), new StringHandler());
            registry.add(
                    Endpoint.get("/base/integer", "EteService", "integer"), new IntegerHandler());
            registry.add(
                    Endpoint.get("/base/double", "EteService", "double_"), new Double_Handler());
            registry.add(
                    Endpoint.get("/base/boolean", "EteService", "boolean_"), new Boolean_Handler());
            registry.add(
                    Endpoint.get("/base/safelong", "EteService", "safelong"),
                    new SafelongHandler());
            registry.add(Endpoint.get("/base/rid", "EteService", "rid"), new RidHandler());
            registry.add(
                    Endpoint.get("/base/bearertoken", "EteService", "bearertoken"),
                    new BearertokenHandler());
            registry.add(
                    Endpoint.get("/base/optionalString", "EteService", "optionalString"),
                    new OptionalStringHandler());
            registry.add(
                    Endpoint.get("/base/optionalEmpty", "EteService", "optionalEmpty"),
                    new OptionalEmptyHandler());
            registry.add(
                    Endpoint.get("/base/datetime", "EteService", "datetime"),
                    new DatetimeHandler());
            registry.add(Endpoint.get("/base/binary", "EteService", "binary"), new BinaryHandler());
            registry.add(
                    Endpoint.get("/base/path/{param}", "EteService", "path"), new PathHandler());
            registry.add(
                    Endpoint.post("/base/notNullBody", "EteService", "notNullBody"),
                    new NotNullBodyHandler());
            registry.add(
                    Endpoint.get("/base/aliasOne", "EteService", "aliasOne"),
                    new AliasOneHandler());
            registry.add(
                    Endpoint.get("/base/optionalAliasOne", "EteService", "optionalAliasOne"),
                    new OptionalAliasOneHandler());
            registry.add(
                    Endpoint.get("/base/aliasTwo", "EteService", "aliasTwo"),
                    new AliasTwoHandler());
            registry.add(
                    Endpoint.post(
                            "/base/external/notNullBody",
                            "EteService",
                            "notNullBodyExternalImport"),
                    new NotNullBodyExternalImportHandler());
            registry.add(
                    Endpoint.post(
                            "/base/external/optional-body",
                            "EteService",
                            "optionalBodyExternalImport"),
                    new OptionalBodyExternalImportHandler());
            registry.add(
                    Endpoint.post(
                            "/base/external/optional-query",
                            "EteService",
                            "optionalQueryExternalImport"),
                    new OptionalQueryExternalImportHandler());
            registry.add(
                    Endpoint.post("/base/no-return", "EteService", "noReturn"),
                    new NoReturnHandler());
            registry.add(
                    Endpoint.get("/base/enum/query", "EteService", "enumQuery"),
                    new EnumQueryHandler());
            registry.add(
                    Endpoint.get("/base/enum/list/query", "EteService", "enumListQuery"),
                    new EnumListQueryHandler());
            registry.add(
                    Endpoint.get("/base/enum/optional/query", "EteService", "optionalEnumQuery"),
                    new OptionalEnumQueryHandler());
            registry.add(
                    Endpoint.get("/base/enum/header", "EteService", "enumHeader"),
                    new EnumHeaderHandler());
        }

        private class StringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                String result = delegate.string(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class IntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                int result = delegate.integer(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class Double_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                double result = delegate.double_(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class Boolean_Handler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                boolean result = delegate.boolean_(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class SafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                SafeLong result = delegate.safelong(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class RidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                ResourceIdentifier result = delegate.rid(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class BearertokenHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                BearerToken result = delegate.bearertoken(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class OptionalStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Optional<String> result = delegate.optionalString(authHeader);
                if (result.isPresent()) {
                    runtime.serde().serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class OptionalEmptyHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Optional<String> result = delegate.optionalEmpty(authHeader);
                if (result.isPresent()) {
                    runtime.serde().serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class DatetimeHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                OffsetDateTime result = delegate.datetime(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class BinaryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                BinaryResponseBody result = delegate.binary(authHeader);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class PathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                String param = runtime.serde().deserializeString(pathParams.get("param"));
                String result = delegate.path(authHeader, param);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class NotNullBodyHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                StringAliasExample notNullBody =
                        runtime.serde().deserialize(notNullBodyType, exchange);
                StringAliasExample result = delegate.notNullBody(authHeader, notNullBody);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class AliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String queryParamNameRaw =
                        runtime.serde().deserializeString(queryParams.get("queryParamName"));
                StringAliasExample queryParamName = StringAliasExample.of(queryParamNameRaw);
                StringAliasExample result = delegate.aliasOne(authHeader, queryParamName);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class OptionalAliasOneHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<String> queryParamNameRaw =
                        runtime.serde()
                                .deserializeOptionalString(queryParams.get("queryParamName"));
                Optional<StringAliasExample> queryParamName =
                        Optional.ofNullable(
                                queryParamNameRaw.isPresent()
                                        ? StringAliasExample.of(queryParamNameRaw.get())
                                        : null);
                StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class AliasTwoHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String queryParamNameRaw =
                        runtime.serde().deserializeString(queryParams.get("queryParamName"));
                NestedStringAliasExample queryParamName =
                        NestedStringAliasExample.of(StringAliasExample.of(queryParamNameRaw));
                NestedStringAliasExample result = delegate.aliasTwo(authHeader, queryParamName);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class NotNullBodyExternalImportHandler implements HttpHandler {
            private final TypeToken<StringAliasExample> notNullBodyType =
                    new TypeToken<StringAliasExample>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                StringAliasExample notNullBody =
                        runtime.serde().deserialize(notNullBodyType, exchange);
                StringAliasExample result =
                        delegate.notNullBodyExternalImport(authHeader, notNullBody);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class OptionalBodyExternalImportHandler implements HttpHandler {
            private final TypeToken<Optional<StringAliasExample>> bodyType =
                    new TypeToken<Optional<StringAliasExample>>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Optional<StringAliasExample> body = runtime.serde().deserialize(bodyType, exchange);
                Optional<StringAliasExample> result =
                        delegate.optionalBodyExternalImport(authHeader, body);
                if (result.isPresent()) {
                    runtime.serde().serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class OptionalQueryExternalImportHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<StringAliasExample> query =
                        runtime.serde()
                                .deserializeOptionalComplex(
                                        queryParams.get("query"), StringAliasExample::valueOf);
                Optional<StringAliasExample> result =
                        delegate.optionalQueryExternalImport(authHeader, query);
                if (result.isPresent()) {
                    runtime.serde().serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class NoReturnHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                delegate.noReturn(authHeader);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class EnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                SimpleEnum queryParamName =
                        runtime.serde()
                                .deserializeComplex(
                                        queryParams.get("queryParamName"), SimpleEnum::valueOf);
                SimpleEnum result = delegate.enumQuery(authHeader, queryParamName);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class EnumListQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                List<SimpleEnum> queryParamName =
                        runtime.serde()
                                .deserializeComplexList(
                                        queryParams.get("queryParamName"), SimpleEnum::valueOf);
                List<SimpleEnum> result = delegate.enumListQuery(authHeader, queryParamName);
                runtime.serde().serialize(result, exchange);
            }
        }

        private class OptionalEnumQueryHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<SimpleEnum> queryParamName =
                        runtime.serde()
                                .deserializeOptionalComplex(
                                        queryParams.get("queryParamName"), SimpleEnum::valueOf);
                Optional<SimpleEnum> result =
                        delegate.optionalEnumQuery(authHeader, queryParamName);
                if (result.isPresent()) {
                    runtime.serde().serialize(result, exchange);
                } else {
                    exchange.setStatusCode(StatusCodes.NO_CONTENT);
                }
            }
        }

        private class EnumHeaderHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                HeaderMap headerParams = exchange.getRequestHeaders();
                SimpleEnum headerParameter =
                        runtime.serde()
                                .deserializeComplex(
                                        headerParams.get("Custom-Header"), SimpleEnum::valueOf);
                SimpleEnum result = delegate.enumHeader(authHeader, headerParameter);
                runtime.serde().serialize(result, exchange);
            }
        }
    }
}
