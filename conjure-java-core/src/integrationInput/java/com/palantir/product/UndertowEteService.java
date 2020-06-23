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
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteService extends UndertowService {
    /**
     * foo bar baz.
     * <h2>Very Important Documentation</h2>
     * <p>This documentation provides a <em>list</em>:</p>
     * <ul>
     * <li>Docs rule</li>
     * <li>Lists are wonderful</li>
     * </ul>
     */
    String string(AuthHeader authHeader);

    /**
     * one <em>two</em> three.
     */
    int integer(AuthHeader authHeader);

    double double_(AuthHeader authHeader);

    boolean boolean_(AuthHeader authHeader);

    SafeLong safelong(AuthHeader authHeader);

    ResourceIdentifier rid(AuthHeader authHeader);

    BearerToken bearertoken(AuthHeader authHeader);

    Optional<String> optionalString(AuthHeader authHeader);

    Optional<String> optionalEmpty(AuthHeader authHeader);

    OffsetDateTime datetime(AuthHeader authHeader);

    BinaryResponseBody binary(AuthHeader authHeader);

    /**
     * Path endpoint.
     * @param param Documentation for <code>param</code>
     */
    String path(AuthHeader authHeader, String param);

    long externalLongPath(AuthHeader authHeader, long param);

    Optional<Long> optionalExternalLongQuery(AuthHeader authHeader, Optional<Long> param);

    StringAliasExample notNullBody(AuthHeader authHeader, StringAliasExample notNullBody);

    StringAliasExample aliasOne(AuthHeader authHeader, StringAliasExample queryParamName);

    StringAliasExample optionalAliasOne(AuthHeader authHeader, Optional<StringAliasExample> queryParamName);

    NestedStringAliasExample aliasTwo(AuthHeader authHeader, NestedStringAliasExample queryParamName);

    StringAliasExample notNullBodyExternalImport(AuthHeader authHeader, StringAliasExample notNullBody);

    Optional<StringAliasExample> optionalBodyExternalImport(AuthHeader authHeader, Optional<StringAliasExample> body);

    Optional<StringAliasExample> optionalQueryExternalImport(AuthHeader authHeader, Optional<StringAliasExample> query);

    void noReturn(AuthHeader authHeader);

    SimpleEnum enumQuery(AuthHeader authHeader, SimpleEnum queryParamName);

    List<SimpleEnum> enumListQuery(AuthHeader authHeader, List<SimpleEnum> queryParamName);

    Optional<SimpleEnum> optionalEnumQuery(AuthHeader authHeader, Optional<SimpleEnum> queryParamName);

    SimpleEnum enumHeader(AuthHeader authHeader, SimpleEnum headerParameter);

    Optional<LongAlias> aliasLongEndpoint(AuthHeader authHeader, Optional<LongAlias> input);

    void complexQueryParameters(
            AuthHeader authHeader,
            ResourceIdentifier datasetRid,
            Set<StringAliasExample> strings,
            Set<Long> longs,
            Set<Integer> ints);

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(Arrays.asList(
                new StringEndpoint(runtime, this),
                new IntegerEndpoint(runtime, this),
                new Double_Endpoint(runtime, this),
                new Boolean_Endpoint(runtime, this),
                new SafelongEndpoint(runtime, this),
                new RidEndpoint(runtime, this),
                new BearertokenEndpoint(runtime, this),
                new OptionalStringEndpoint(runtime, this),
                new OptionalEmptyEndpoint(runtime, this),
                new DatetimeEndpoint(runtime, this),
                new BinaryEndpoint(runtime, this),
                new PathEndpoint(runtime, this),
                new ExternalLongPathEndpoint(runtime, this),
                new OptionalExternalLongQueryEndpoint(runtime, this),
                new NotNullBodyEndpoint(runtime, this),
                new AliasOneEndpoint(runtime, this),
                new OptionalAliasOneEndpoint(runtime, this),
                new AliasTwoEndpoint(runtime, this),
                new NotNullBodyExternalImportEndpoint(runtime, this),
                new OptionalBodyExternalImportEndpoint(runtime, this),
                new OptionalQueryExternalImportEndpoint(runtime, this),
                new NoReturnEndpoint(runtime, this),
                new EnumQueryEndpoint(runtime, this),
                new EnumListQueryEndpoint(runtime, this),
                new OptionalEnumQueryEndpoint(runtime, this),
                new EnumHeaderEndpoint(runtime, this),
                new AliasLongEndpointEndpoint(runtime, this),
                new ComplexQueryParametersEndpoint(runtime, this)));
    }

    final class StringEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class IntegerEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class Double_Endpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class Boolean_Endpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class SafelongEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class RidEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<ResourceIdentifier> serializer;

        RidEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<ResourceIdentifier>() {});
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
            return "EteService";
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

    final class BearertokenEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class OptionalStringEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class OptionalEmptyEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class DatetimeEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class BinaryEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class PathEndpoint implements HttpHandler, Endpoint {
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
            String param = runtime.plainSerDe().deserializeString(pathParams.get("param"));
            String result = delegate.path(authHeader, param);
            serializer.serialize(result, exchange);
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
        public String serviceName() {
            return "EteService";
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

    final class ExternalLongPathEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Long> serializer;

        ExternalLongPathEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Long>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            long param = Long.valueOf(runtime.plainSerDe().deserializeString(pathParams.get("param")));
            long result = delegate.externalLongPath(authHeader, param);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/externalLong/{param}";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "externalLongPath";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    final class OptionalExternalLongQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<Long>> serializer;

        OptionalExternalLongQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<Long>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<Long> param =
                    runtime.plainSerDe().deserializeOptionalComplex(queryParams.get("param"), Long::valueOf);
            Optional<Long> result = delegate.optionalExternalLongQuery(authHeader, param);
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
            return "/base/optionalExternalLong";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "optionalExternalLongQuery";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    final class NotNullBodyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Deserializer<StringAliasExample> deserializer;

        private final Serializer<StringAliasExample> serializer;

        NotNullBodyEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
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
            return "EteService";
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

    final class AliasOneEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<StringAliasExample> serializer;

        AliasOneEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String queryParamNameRaw = runtime.plainSerDe().deserializeString(queryParams.get("queryParamName"));
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
            return "EteService";
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

    final class OptionalAliasOneEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<StringAliasExample> serializer;

        OptionalAliasOneEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<String> queryParamNameRaw =
                    runtime.plainSerDe().deserializeOptionalString(queryParams.get("queryParamName"));
            Optional<StringAliasExample> queryParamName = Optional.ofNullable(
                    queryParamNameRaw.isPresent() ? StringAliasExample.of(queryParamNameRaw.get()) : null);
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
            return "EteService";
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

    final class AliasTwoEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<NestedStringAliasExample> serializer;

        AliasTwoEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<NestedStringAliasExample>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String queryParamNameRaw = runtime.plainSerDe().deserializeString(queryParams.get("queryParamName"));
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
            return "EteService";
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

    final class NotNullBodyExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Deserializer<StringAliasExample> deserializer;

        private final Serializer<StringAliasExample> serializer;

        NotNullBodyExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<StringAliasExample>() {});
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<StringAliasExample>() {});
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
            return "EteService";
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

    final class OptionalBodyExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Deserializer<Optional<StringAliasExample>> deserializer;

        private final Serializer<Optional<StringAliasExample>> serializer;

        OptionalBodyExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Optional<StringAliasExample>>() {});
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<StringAliasExample>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<StringAliasExample> body = deserializer.deserialize(exchange);
            Optional<StringAliasExample> result = delegate.optionalBodyExternalImport(authHeader, body);
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
            return "EteService";
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

    final class OptionalQueryExternalImportEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<StringAliasExample>> serializer;

        OptionalQueryExternalImportEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<StringAliasExample>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<StringAliasExample> query = runtime.plainSerDe()
                    .deserializeOptionalComplex(queryParams.get("query"), StringAliasExample::valueOf);
            Optional<StringAliasExample> result = delegate.optionalQueryExternalImport(authHeader, query);
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
            return "EteService";
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

    final class NoReturnEndpoint implements HttpHandler, Endpoint {
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
            return "EteService";
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

    final class EnumQueryEndpoint implements HttpHandler, Endpoint {
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
                    runtime.plainSerDe().deserializeComplex(queryParams.get("queryParamName"), SimpleEnum::valueOf);
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
            return "EteService";
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

    final class EnumListQueryEndpoint implements HttpHandler, Endpoint {
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
                    runtime.plainSerDe().deserializeComplexList(queryParams.get("queryParamName"), SimpleEnum::valueOf);
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
            return "EteService";
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

    final class OptionalEnumQueryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<SimpleEnum>> serializer;

        OptionalEnumQueryEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<SimpleEnum>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<SimpleEnum> queryParamName = runtime.plainSerDe()
                    .deserializeOptionalComplex(queryParams.get("queryParamName"), SimpleEnum::valueOf);
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
            return "EteService";
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

    final class EnumHeaderEndpoint implements HttpHandler, Endpoint {
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
                    runtime.plainSerDe().deserializeComplex(headerParams.get("Custom-Header"), SimpleEnum::valueOf);
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
            return "EteService";
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

    final class AliasLongEndpointEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        private final Serializer<Optional<LongAlias>> serializer;

        AliasLongEndpointEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<LongAlias>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<Long> inputRaw =
                    runtime.plainSerDe().deserializeOptionalComplex(queryParams.get("input"), Long::valueOf);
            Optional<LongAlias> input = Optional.ofNullable(inputRaw.isPresent() ? LongAlias.of(inputRaw.get()) : null);
            Optional<LongAlias> result = delegate.aliasLongEndpoint(authHeader, input);
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
            return "/base/alias-long";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "aliasLongEndpoint";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    final class ComplexQueryParametersEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteService delegate;

        ComplexQueryParametersEndpoint(UndertowRuntime runtime, UndertowEteService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            ResourceIdentifier datasetRid = runtime.plainSerDe().deserializeRid(pathParams.get("datasetRid"));
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Set<StringAliasExample> strings =
                    runtime.plainSerDe().deserializeComplexSet(queryParams.get("strings"), StringAliasExample::valueOf);
            Set<Long> longs = runtime.plainSerDe().deserializeComplexSet(queryParams.get("longs"), Long::valueOf);
            Set<Integer> ints = runtime.plainSerDe().deserializeIntegerSet(queryParams.get("ints"));
            delegate.complexQueryParameters(authHeader, datasetRid, strings, longs, ints);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/datasets/{datasetRid}/strings";
        }

        @Override
        public String serviceName() {
            return "EteService";
        }

        @Override
        public String name() {
            return "complexQueryParameters";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
