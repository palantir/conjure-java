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
        return ImmutableList.of(
                new StringEndpoint(runtime, delegate),
                new IntegerEndpoint(runtime, delegate),
                new Double_Endpoint(runtime, delegate),
                new Boolean_Endpoint(runtime, delegate),
                new SafelongEndpoint(runtime, delegate),
                new RidEndpoint(runtime, delegate),
                new BearertokenEndpoint(runtime, delegate),
                new OptionalStringEndpoint(runtime, delegate),
                new OptionalEmptyEndpoint(runtime, delegate),
                new DatetimeEndpoint(runtime, delegate),
                new BinaryEndpoint(runtime, delegate),
                new PathEndpoint(runtime, delegate),
                new NotNullBodyEndpoint(runtime, delegate),
                new AliasOneEndpoint(runtime, delegate),
                new OptionalAliasOneEndpoint(runtime, delegate),
                new AliasTwoEndpoint(runtime, delegate),
                new NotNullBodyExternalImportEndpoint(runtime, delegate),
                new OptionalBodyExternalImportEndpoint(runtime, delegate),
                new OptionalQueryExternalImportEndpoint(runtime, delegate),
                new NoReturnEndpoint(runtime, delegate),
                new EnumQueryEndpoint(runtime, delegate),
                new EnumListQueryEndpoint(runtime, delegate),
                new OptionalEnumQueryEndpoint(runtime, delegate),
                new EnumHeaderEndpoint(runtime, delegate));
    }

    private static final class StringEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        StringEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            String result = delegate.string(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/string";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("string");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class IntegerEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        IntegerEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            int result = delegate.integer(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/integer";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("integer");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class Double_Endpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        Double_Endpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            double result = delegate.double_(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/double";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("double_");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class Boolean_Endpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        Boolean_Endpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            boolean result = delegate.boolean_(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/boolean";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("boolean_");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class SafelongEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        SafelongEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            SafeLong result = delegate.safelong(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/safelong";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("safelong");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class RidEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        RidEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ResourceIdentifier result = delegate.rid(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/rid";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("rid");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class BearertokenEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        BearertokenEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            BearerToken result = delegate.bearertoken(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/bearertoken";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("bearertoken");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalStringEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        OptionalStringEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/optionalString";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("optionalString");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalEmptyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        OptionalEmptyEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/optionalEmpty";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("optionalEmpty");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class DatetimeEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        DatetimeEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            OffsetDateTime result = delegate.datetime(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/datetime";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("datetime");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class BinaryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        BinaryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            BinaryResponseBody result = delegate.binary(authHeader);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/binary";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("binary");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class PathEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        PathEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/path/{param}";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("path");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NotNullBodyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final TypeToken<StringAliasExample> notNullBodyType =
                new TypeToken<StringAliasExample>() {};

        NotNullBodyEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            StringAliasExample notNullBody = runtime.serde().deserialize(notNullBodyType, exchange);
            StringAliasExample result = delegate.notNullBody(authHeader, notNullBody);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/base/notNullBody";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("notNullBody");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class AliasOneEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        AliasOneEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/aliasOne";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("aliasOne");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalAliasOneEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        OptionalAliasOneEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<String> queryParamNameRaw =
                    runtime.serde().deserializeOptionalString(queryParams.get("queryParamName"));
            Optional<StringAliasExample> queryParamName =
                    Optional.ofNullable(
                            queryParamNameRaw.isPresent()
                                    ? StringAliasExample.of(queryParamNameRaw.get())
                                    : null);
            StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/optionalAliasOne";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("optionalAliasOne");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class AliasTwoEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        AliasTwoEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/aliasTwo";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("aliasTwo");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NotNullBodyExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final TypeToken<StringAliasExample> notNullBodyType =
                new TypeToken<StringAliasExample>() {};

        NotNullBodyExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            StringAliasExample notNullBody = runtime.serde().deserialize(notNullBodyType, exchange);
            StringAliasExample result = delegate.notNullBodyExternalImport(authHeader, notNullBody);
            runtime.serde().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/base/external/notNullBody";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("notNullBodyExternalImport");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalBodyExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final TypeToken<Optional<StringAliasExample>> bodyType =
                new TypeToken<Optional<StringAliasExample>>() {};

        OptionalBodyExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/base/external/optional-body";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("optionalBodyExternalImport");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalQueryExternalImportEndpoint
            implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        OptionalQueryExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/base/external/optional-query";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("optionalQueryExternalImport");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NoReturnEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        NoReturnEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            delegate.noReturn(authHeader);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/base/no-return";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("noReturn");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class EnumQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        EnumQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/enum/query";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("enumQuery");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class EnumListQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        EnumListQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/enum/list/query";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("enumListQuery");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalEnumQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        OptionalEnumQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<SimpleEnum> queryParamName =
                    runtime.serde()
                            .deserializeOptionalComplex(
                                    queryParams.get("queryParamName"), SimpleEnum::valueOf);
            Optional<SimpleEnum> result = delegate.optionalEnumQuery(authHeader, queryParamName);
            if (result.isPresent()) {
                runtime.serde().serialize(result, exchange);
            } else {
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/enum/optional/query";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("optionalEnumQuery");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class EnumHeaderEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        EnumHeaderEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

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
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/enum/header";
        }

        @Override
        public Optional<String> serviceName() {
            return Optional.of("UndertowEteService");
        }

        @Override
        public Optional<String> name() {
            return Optional.of("enumHeader");
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
