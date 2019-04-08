package com.palantir.product;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
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
import java.util.Arrays;
import java.util.Collections;
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
        return Collections.unmodifiableList(
                Arrays.asList(
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
                        new EnumHeaderEndpoint(runtime, delegate)));
    }

    private static final class StringEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<String> serializer;

        StringEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            String result = delegate.string(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "string";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class IntegerEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Integer> serializer;

        IntegerEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Integer>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            int result = delegate.integer(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "integer";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class Double_Endpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Double> serializer;

        Double_Endpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Double>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            double result = delegate.double_(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "double_";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class Boolean_Endpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Boolean> serializer;

        Boolean_Endpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            boolean result = delegate.boolean_(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "boolean_";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class SafelongEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<SafeLong> serializer;

        SafelongEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<SafeLong>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            SafeLong result = delegate.safelong(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "safelong";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class RidEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<ResourceIdentifier> serializer;

        RidEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<ResourceIdentifier>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ResourceIdentifier result = delegate.rid(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "rid";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class BearertokenEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<BearerToken> serializer;

        BearertokenEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<BearerToken>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            BearerToken result = delegate.bearertoken(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "bearertoken";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalStringEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<String>> serializer;

        OptionalStringEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<String>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<String> result = delegate.optionalString(authHeader);
            if (result.isPresent()) {
                serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "optionalString";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalEmptyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<String>> serializer;

        OptionalEmptyEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<String>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<String> result = delegate.optionalEmpty(authHeader);
            if (result.isPresent()) {
                serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "optionalEmpty";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class DatetimeEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<OffsetDateTime> serializer;

        DatetimeEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<OffsetDateTime>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            OffsetDateTime result = delegate.datetime(authHeader);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "datetime";
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
            runtime.bodySerDe().serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "binary";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class PathEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<String> serializer;

        PathEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            String param = runtime.plainSerDe().deserializeString(pathParams.get("mark"));
            String result = delegate.path(authHeader, param);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/path/{mark}";
        }

        @Override
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "path";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NotNullBodyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Deserializer<StringAliasExample> deserializer;

        private final Serializer<StringAliasExample> serializer;

        NotNullBodyEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            StringAliasExample notNullBody = deserializer.deserialize(exchange);
            StringAliasExample result = delegate.notNullBody(authHeader, notNullBody);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "notNullBody";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class AliasOneEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<StringAliasExample> serializer;

        AliasOneEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String queryParamNameRaw =
                    runtime.plainSerDe().deserializeString(queryParams.get("queryParamName"));
            StringAliasExample queryParamName = StringAliasExample.of(queryParamNameRaw);
            StringAliasExample result = delegate.aliasOne(authHeader, queryParamName);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "aliasOne";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalAliasOneEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<StringAliasExample> serializer;

        OptionalAliasOneEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<String> queryParamNameRaw =
                    runtime.plainSerDe()
                            .deserializeOptionalString(queryParams.get("queryParamName"));
            Optional<StringAliasExample> queryParamName =
                    Optional.ofNullable(
                            queryParamNameRaw.isPresent()
                                    ? StringAliasExample.of(queryParamNameRaw.get())
                                    : null);
            StringAliasExample result = delegate.optionalAliasOne(authHeader, queryParamName);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "optionalAliasOne";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class AliasTwoEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<NestedStringAliasExample> serializer;

        AliasTwoEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<NestedStringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String queryParamNameRaw =
                    runtime.plainSerDe().deserializeString(queryParams.get("queryParamName"));
            NestedStringAliasExample queryParamName =
                    NestedStringAliasExample.of(StringAliasExample.of(queryParamNameRaw));
            NestedStringAliasExample result = delegate.aliasTwo(authHeader, queryParamName);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "aliasTwo";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NotNullBodyExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Deserializer<StringAliasExample> deserializer;

        private final Serializer<StringAliasExample> serializer;

        NotNullBodyExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer =
                    runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            StringAliasExample notNullBody = deserializer.deserialize(exchange);
            StringAliasExample result = delegate.notNullBodyExternalImport(authHeader, notNullBody);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "notNullBodyExternalImport";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalBodyExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Deserializer<Optional<StringAliasExample>> deserializer;

        private final Serializer<Optional<StringAliasExample>> serializer;

        OptionalBodyExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer =
                    runtime.bodySerDe()
                            .deserializer(new TypeMarker<Optional<StringAliasExample>>() {});
            this.serializer =
                    runtime.bodySerDe()
                            .serializer(new TypeMarker<Optional<StringAliasExample>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<StringAliasExample> body = deserializer.deserialize(exchange);
            Optional<StringAliasExample> result =
                    delegate.optionalBodyExternalImport(authHeader, body);
            if (result.isPresent()) {
                serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "optionalBodyExternalImport";
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

        private final Serializer<Optional<StringAliasExample>> serializer;

        OptionalQueryExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer =
                    runtime.bodySerDe()
                            .serializer(new TypeMarker<Optional<StringAliasExample>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<StringAliasExample> query =
                    runtime.plainSerDe()
                            .deserializeOptionalComplex(
                                    queryParams.get("query"), StringAliasExample::valueOf);
            Optional<StringAliasExample> result =
                    delegate.optionalQueryExternalImport(authHeader, query);
            if (result.isPresent()) {
                serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "optionalQueryExternalImport";
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "noReturn";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class EnumQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<SimpleEnum> serializer;

        EnumQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<SimpleEnum>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            SimpleEnum queryParamName =
                    runtime.plainSerDe()
                            .deserializeComplex(
                                    queryParams.get("queryParamName"), SimpleEnum::valueOf);
            SimpleEnum result = delegate.enumQuery(authHeader, queryParamName);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "enumQuery";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class EnumListQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<List<SimpleEnum>> serializer;

        EnumListQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<List<SimpleEnum>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            List<SimpleEnum> queryParamName =
                    runtime.plainSerDe()
                            .deserializeComplexList(
                                    queryParams.get("queryParamName"), SimpleEnum::valueOf);
            List<SimpleEnum> result = delegate.enumListQuery(authHeader, queryParamName);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "enumListQuery";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalEnumQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<SimpleEnum>> serializer;

        OptionalEnumQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer =
                    runtime.bodySerDe().serializer(new TypeMarker<Optional<SimpleEnum>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<SimpleEnum> queryParamName =
                    runtime.plainSerDe()
                            .deserializeOptionalComplex(
                                    queryParams.get("queryParamName"), SimpleEnum::valueOf);
            Optional<SimpleEnum> result = delegate.optionalEnumQuery(authHeader, queryParamName);
            if (result.isPresent()) {
                serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "optionalEnumQuery";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class EnumHeaderEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<SimpleEnum> serializer;

        EnumHeaderEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<SimpleEnum>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            HeaderMap headerParams = exchange.getRequestHeaders();
            SimpleEnum headerParameter =
                    runtime.plainSerDe()
                            .deserializeComplex(
                                    headerParams.get("Custom-Header"), SimpleEnum::valueOf);
            SimpleEnum result = delegate.enumHeader(authHeader, headerParameter);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "UndertowEteService";
        }

        @Override
        public String name() {
            return "enumHeader";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
