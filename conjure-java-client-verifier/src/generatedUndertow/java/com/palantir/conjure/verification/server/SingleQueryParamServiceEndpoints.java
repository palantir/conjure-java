package com.palantir.conjure.verification.server;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import com.palantir.conjure.verification.types.AliasString;
import com.palantir.conjure.verification.types.EnumExample;
import com.palantir.ri.ResourceIdentifier;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class SingleQueryParamServiceEndpoints implements Service {
    private final UndertowSingleQueryParamService delegate;

    private SingleQueryParamServiceEndpoints(UndertowSingleQueryParamService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowSingleQueryParamService delegate) {
        return new SingleQueryParamServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new SingleQueryParamServiceRegistrable(context, delegate);
    }

    private static final class SingleQueryParamServiceRegistrable implements Registrable {
        private final UndertowSingleQueryParamService delegate;

        private final SerializerRegistry serializers;

        private SingleQueryParamServiceRegistrable(
                ServiceContext context, UndertowSingleQueryParamService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamBoolean/{index}",
                                    "SingleQueryParamService",
                                    "queryParamBoolean"),
                            new QueryParamBooleanHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamDouble/{index}",
                                    "SingleQueryParamService",
                                    "queryParamDouble"),
                            new QueryParamDoubleHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamInteger/{index}",
                                    "SingleQueryParamService",
                                    "queryParamInteger"),
                            new QueryParamIntegerHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamRid/{index}",
                                    "SingleQueryParamService",
                                    "queryParamRid"),
                            new QueryParamRidHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamSafelong/{index}",
                                    "SingleQueryParamService",
                                    "queryParamSafelong"),
                            new QueryParamSafelongHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamString/{index}",
                                    "SingleQueryParamService",
                                    "queryParamString"),
                            new QueryParamStringHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamUuid/{index}",
                                    "SingleQueryParamService",
                                    "queryParamUuid"),
                            new QueryParamUuidHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamOptionalOfString/{index}",
                                    "SingleQueryParamService",
                                    "queryParamOptionalOfString"),
                            new QueryParamOptionalOfStringHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamAliasString/{index}",
                                    "SingleQueryParamService",
                                    "queryParamAliasString"),
                            new QueryParamAliasStringHandler())
                    .add(
                            Endpoint.post(
                                    "/single-query-param/queryParamEnumExample/{index}",
                                    "SingleQueryParamService",
                                    "queryParamEnumExample"),
                            new QueryParamEnumExampleHandler());
        }

        private class QueryParamBooleanHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                boolean someQuery = StringDeserializers.deserializeBoolean(queryParams.get("foo"));
                delegate.queryParamBoolean(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamDoubleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                double someQuery = StringDeserializers.deserializeDouble(queryParams.get("foo"));
                delegate.queryParamDouble(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamIntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                int someQuery = StringDeserializers.deserializeInteger(queryParams.get("foo"));
                delegate.queryParamInteger(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamRidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                ResourceIdentifier someQuery =
                        StringDeserializers.deserializeRid(queryParams.get("foo"));
                delegate.queryParamRid(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamSafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                SafeLong someQuery =
                        StringDeserializers.deserializeSafeLong(queryParams.get("foo"));
                delegate.queryParamSafelong(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String someQuery = StringDeserializers.deserializeString(queryParams.get("foo"));
                delegate.queryParamString(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamUuidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                UUID someQuery = StringDeserializers.deserializeUuid(queryParams.get("foo"));
                delegate.queryParamUuid(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamOptionalOfStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                Optional<String> someQuery =
                        StringDeserializers.deserializeOptionalString(queryParams.get("foo"));
                delegate.queryParamOptionalOfString(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamAliasStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                String someQueryRaw = StringDeserializers.deserializeString(queryParams.get("foo"));
                AliasString someQuery = AliasString.of(someQueryRaw);
                delegate.queryParamAliasString(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class QueryParamEnumExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
                EnumExample someQuery =
                        StringDeserializers.deserializeComplex(
                                queryParams.get("foo"), EnumExample::valueOf);
                delegate.queryParamEnumExample(index, someQuery);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }
}
