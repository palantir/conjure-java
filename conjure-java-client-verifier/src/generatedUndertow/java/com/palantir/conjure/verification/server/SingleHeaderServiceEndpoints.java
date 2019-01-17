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
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class SingleHeaderServiceEndpoints implements Service {
    private final UndertowSingleHeaderService delegate;

    private SingleHeaderServiceEndpoints(UndertowSingleHeaderService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowSingleHeaderService delegate) {
        return new SingleHeaderServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new SingleHeaderServiceRegistrable(context, delegate);
    }

    private static final class SingleHeaderServiceRegistrable implements Registrable {
        private final UndertowSingleHeaderService delegate;

        private final SerializerRegistry serializers;

        private SingleHeaderServiceRegistrable(
                ServiceContext context, UndertowSingleHeaderService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerBearertoken/{index}",
                                    "SingleHeaderService",
                                    "headerBearertoken"),
                            new HeaderBearertokenHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerBoolean/{index}",
                                    "SingleHeaderService",
                                    "headerBoolean"),
                            new HeaderBooleanHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerDatetime/{index}",
                                    "SingleHeaderService",
                                    "headerDatetime"),
                            new HeaderDatetimeHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerDouble/{index}",
                                    "SingleHeaderService",
                                    "headerDouble"),
                            new HeaderDoubleHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerInteger/{index}",
                                    "SingleHeaderService",
                                    "headerInteger"),
                            new HeaderIntegerHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerRid/{index}",
                                    "SingleHeaderService",
                                    "headerRid"),
                            new HeaderRidHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerSafelong/{index}",
                                    "SingleHeaderService",
                                    "headerSafelong"),
                            new HeaderSafelongHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerString/{index}",
                                    "SingleHeaderService",
                                    "headerString"),
                            new HeaderStringHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerUuid/{index}",
                                    "SingleHeaderService",
                                    "headerUuid"),
                            new HeaderUuidHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerOptionalOfString/{index}",
                                    "SingleHeaderService",
                                    "headerOptionalOfString"),
                            new HeaderOptionalOfStringHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerAliasString/{index}",
                                    "SingleHeaderService",
                                    "headerAliasString"),
                            new HeaderAliasStringHandler())
                    .add(
                            Endpoint.post(
                                    "/single-header-param/headerEnumExample/{index}",
                                    "SingleHeaderService",
                                    "headerEnumExample"),
                            new HeaderEnumExampleHandler());
        }

        private class HeaderBearertokenHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                BearerToken header =
                        StringDeserializers.deserializeBearerToken(headerParams.get("Some-Header"));
                delegate.headerBearertoken(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderBooleanHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                boolean header =
                        StringDeserializers.deserializeBoolean(headerParams.get("Some-Header"));
                delegate.headerBoolean(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderDatetimeHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                OffsetDateTime header =
                        StringDeserializers.deserializeDateTime(headerParams.get("Some-Header"));
                delegate.headerDatetime(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderDoubleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                double header =
                        StringDeserializers.deserializeDouble(headerParams.get("Some-Header"));
                delegate.headerDouble(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderIntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                int header =
                        StringDeserializers.deserializeInteger(headerParams.get("Some-Header"));
                delegate.headerInteger(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderRidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                ResourceIdentifier header =
                        StringDeserializers.deserializeRid(headerParams.get("Some-Header"));
                delegate.headerRid(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderSafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                SafeLong header =
                        StringDeserializers.deserializeSafeLong(headerParams.get("Some-Header"));
                delegate.headerSafelong(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                String header =
                        StringDeserializers.deserializeString(headerParams.get("Some-Header"));
                delegate.headerString(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderUuidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                UUID header = StringDeserializers.deserializeUuid(headerParams.get("Some-Header"));
                delegate.headerUuid(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderOptionalOfStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                Optional<String> header =
                        StringDeserializers.deserializeOptionalString(
                                headerParams.get("Some-Header"));
                delegate.headerOptionalOfString(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderAliasStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                String headerRaw =
                        StringDeserializers.deserializeString(headerParams.get("Some-Header"));
                AliasString header = AliasString.of(headerRaw);
                delegate.headerAliasString(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class HeaderEnumExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                HeaderMap headerParams = exchange.getRequestHeaders();
                EnumExample header =
                        StringDeserializers.deserializeComplex(
                                headerParams.get("Some-Header"), EnumExample::valueOf);
                delegate.headerEnumExample(header, index);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }
}
