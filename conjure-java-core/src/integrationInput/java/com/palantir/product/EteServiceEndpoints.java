package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
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
public final class EteServiceEndpoints implements UndertowService {
    private final UndertowEteService delegate;

    private EteServiceEndpoints(UndertowEteService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowEteService delegate) {
        return new EteServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return new EteServiceFactory(runtime, delegate).create();
    }

    private static final class EteServiceFactory {
        private final UndertowEteService delegate;

        private final UndertowRuntime runtime;

        private EteServiceFactory(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        List<Endpoint> create() {
            return ImmutableList.of(
                    new StringEndpoint(),
                    new IntegerEndpoint(),
                    new Double_Endpoint(),
                    new Boolean_Endpoint(),
                    new SafelongEndpoint(),
                    new RidEndpoint(),
                    new BearertokenEndpoint(),
                    new OptionalStringEndpoint(),
                    new OptionalEmptyEndpoint(),
                    new DatetimeEndpoint(),
                    new BinaryEndpoint(),
                    new PathEndpoint(),
                    new NotNullBodyEndpoint(),
                    new AliasOneEndpoint(),
                    new OptionalAliasOneEndpoint(),
                    new AliasTwoEndpoint(),
                    new NotNullBodyExternalImportEndpoint(),
                    new OptionalBodyExternalImportEndpoint(),
                    new OptionalQueryExternalImportEndpoint(),
                    new NoReturnEndpoint(),
                    new EnumQueryEndpoint(),
                    new EnumListQueryEndpoint(),
                    new OptionalEnumQueryEndpoint(),
                    new EnumHeaderEndpoint());
        }

        private class StringEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                String result = delegate.string(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/string";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("string");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class IntegerEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                int result = delegate.integer(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/integer";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("integer");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class Double_Endpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                double result = delegate.double_(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/double";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("double_");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class Boolean_Endpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                boolean result = delegate.boolean_(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/boolean";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("boolean_");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class SafelongEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                SafeLong result = delegate.safelong(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/safelong";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("safelong");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class RidEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                ResourceIdentifier result = delegate.rid(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/rid";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("rid");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class BearertokenEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                BearerToken result = delegate.bearertoken(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/bearertoken";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("bearertoken");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class OptionalStringEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/optionalString";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("optionalString");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class OptionalEmptyEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/optionalEmpty";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("optionalEmpty");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class DatetimeEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                OffsetDateTime result = delegate.datetime(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/datetime";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("datetime");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class BinaryEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                BinaryResponseBody result = delegate.binary(authHeader);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/binary";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("binary");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class PathEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                String param = runtime.serde().deserializeString(pathParams.get("param"));
                String result = delegate.path(authHeader, param);
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/path/{param}";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("path");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class NotNullBodyEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.POST;
            }

            @Override
            public final String template() {
                return "/base/notNullBody";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("notNullBody");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class AliasOneEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/aliasOne";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("aliasOne");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class OptionalAliasOneEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/optionalAliasOne";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("optionalAliasOne");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class AliasTwoEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/aliasTwo";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("aliasTwo");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class NotNullBodyExternalImportEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.POST;
            }

            @Override
            public final String template() {
                return "/base/external/notNullBody";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("notNullBodyExternalImport");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class OptionalBodyExternalImportEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.POST;
            }

            @Override
            public final String template() {
                return "/base/external/optional-body";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("optionalBodyExternalImport");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class OptionalQueryExternalImportEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.POST;
            }

            @Override
            public final String template() {
                return "/base/external/optional-query";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("optionalQueryExternalImport");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class NoReturnEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                AuthHeader authHeader = runtime.auth().header(exchange);
                delegate.noReturn(authHeader);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }

            @Override
            public final HttpString method() {
                return Methods.POST;
            }

            @Override
            public final String template() {
                return "/base/no-return";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("noReturn");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class EnumQueryEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/enum/query";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("enumQuery");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class EnumListQueryEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/enum/list/query";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("enumListQuery");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class OptionalEnumQueryEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/enum/optional/query";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("optionalEnumQuery");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }

        private class EnumHeaderEndpoint implements HttpHandler, Endpoint {
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

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/base/enum/header";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EteService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("enumHeader");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }
    }
}
