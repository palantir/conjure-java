package com.palantir.product;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
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
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Methods;
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
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/string")
                                    .serviceName("EteService")
                                    .name("string")
                                    .build(),
                            new StringHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/integer")
                                    .serviceName("EteService")
                                    .name("integer")
                                    .build(),
                            new IntegerHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/double")
                                    .serviceName("EteService")
                                    .name("double_")
                                    .build(),
                            new Double_Handler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/boolean")
                                    .serviceName("EteService")
                                    .name("boolean_")
                                    .build(),
                            new Boolean_Handler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/safelong")
                                    .serviceName("EteService")
                                    .name("safelong")
                                    .build(),
                            new SafelongHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/rid")
                                    .serviceName("EteService")
                                    .name("rid")
                                    .build(),
                            new RidHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/bearertoken")
                                    .serviceName("EteService")
                                    .name("bearertoken")
                                    .build(),
                            new BearertokenHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/optionalString")
                                    .serviceName("EteService")
                                    .name("optionalString")
                                    .build(),
                            new OptionalStringHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/optionalEmpty")
                                    .serviceName("EteService")
                                    .name("optionalEmpty")
                                    .build(),
                            new OptionalEmptyHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/datetime")
                                    .serviceName("EteService")
                                    .name("datetime")
                                    .build(),
                            new DatetimeHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/binary")
                                    .serviceName("EteService")
                                    .name("binary")
                                    .build(),
                            new BinaryHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/path/{param}")
                                    .serviceName("EteService")
                                    .name("path")
                                    .build(),
                            new PathHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/base/notNullBody")
                                    .serviceName("EteService")
                                    .name("notNullBody")
                                    .build(),
                            new NotNullBodyHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/aliasOne")
                                    .serviceName("EteService")
                                    .name("aliasOne")
                                    .build(),
                            new AliasOneHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/optionalAliasOne")
                                    .serviceName("EteService")
                                    .name("optionalAliasOne")
                                    .build(),
                            new OptionalAliasOneHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/aliasTwo")
                                    .serviceName("EteService")
                                    .name("aliasTwo")
                                    .build(),
                            new AliasTwoHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/base/external/notNullBody")
                                    .serviceName("EteService")
                                    .name("notNullBodyExternalImport")
                                    .build(),
                            new NotNullBodyExternalImportHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/base/external/optional-body")
                                    .serviceName("EteService")
                                    .name("optionalBodyExternalImport")
                                    .build(),
                            new OptionalBodyExternalImportHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/base/external/optional-query")
                                    .serviceName("EteService")
                                    .name("optionalQueryExternalImport")
                                    .build(),
                            new OptionalQueryExternalImportHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/base/no-return")
                                    .serviceName("EteService")
                                    .name("noReturn")
                                    .build(),
                            new NoReturnHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/enum/query")
                                    .serviceName("EteService")
                                    .name("enumQuery")
                                    .build(),
                            new EnumQueryHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/enum/list/query")
                                    .serviceName("EteService")
                                    .name("enumListQuery")
                                    .build(),
                            new EnumListQueryHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/enum/optional/query")
                                    .serviceName("EteService")
                                    .name("optionalEnumQuery")
                                    .build(),
                            new OptionalEnumQueryHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.GET)
                                    .template("/base/enum/header")
                                    .serviceName("EteService")
                                    .name("enumHeader")
                                    .build(),
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
                        StringDeserializers.deserializeOptionalComplex(
                                queryParams.get("query"), StringAliasExample::valueOf);
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
                SimpleEnum result = delegate.enumHeader(authHeader, headerParameter);
                serializers.serialize(result, exchange);
            }
        }
    }
}
