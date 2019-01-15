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
import io.undertow.util.Methods;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class SinglePathParamServiceEndpoints implements Service {
    private final UndertowSinglePathParamService delegate;

    private SinglePathParamServiceEndpoints(UndertowSinglePathParamService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowSinglePathParamService delegate) {
        return new SinglePathParamServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new SinglePathParamServiceRegistrable(context, delegate);
    }

    private static final class SinglePathParamServiceRegistrable implements Registrable {
        private final UndertowSinglePathParamService delegate;

        private final SerializerRegistry serializers;

        private SinglePathParamServiceRegistrable(
                ServiceContext context, UndertowSinglePathParamService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/single-path-param/pathParamBoolean/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamBoolean")
                                    .build(),
                            new PathParamBooleanHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template(
                                            "/single-path-param/pathParamDatetime/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamDatetime")
                                    .build(),
                            new PathParamDatetimeHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/single-path-param/pathParamDouble/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamDouble")
                                    .build(),
                            new PathParamDoubleHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/single-path-param/pathParamInteger/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamInteger")
                                    .build(),
                            new PathParamIntegerHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/single-path-param/pathParamRid/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamRid")
                                    .build(),
                            new PathParamRidHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template(
                                            "/single-path-param/pathParamSafelong/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamSafelong")
                                    .build(),
                            new PathParamSafelongHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/single-path-param/pathParamString/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamString")
                                    .build(),
                            new PathParamStringHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template("/single-path-param/pathParamUuid/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamUuid")
                                    .build(),
                            new PathParamUuidHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template(
                                            "/single-path-param/pathParamAliasString/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamAliasString")
                                    .build(),
                            new PathParamAliasStringHandler())
                    .add(
                            Endpoint.builder()
                                    .method(Methods.POST)
                                    .template(
                                            "/single-path-param/pathParamEnumExample/{index}/{param}")
                                    .serviceName("SinglePathParamService")
                                    .name("pathParamEnumExample")
                                    .build(),
                            new PathParamEnumExampleHandler());
        }

        private class PathParamBooleanHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                boolean param = StringDeserializers.deserializeBoolean(pathParams.get("param"));
                delegate.pathParamBoolean(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamDatetimeHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                OffsetDateTime param =
                        StringDeserializers.deserializeDateTime(pathParams.get("param"));
                delegate.pathParamDatetime(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamDoubleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                double param = StringDeserializers.deserializeDouble(pathParams.get("param"));
                delegate.pathParamDouble(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamIntegerHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                int param = StringDeserializers.deserializeInteger(pathParams.get("param"));
                delegate.pathParamInteger(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamRidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                ResourceIdentifier param =
                        StringDeserializers.deserializeRid(pathParams.get("param"));
                delegate.pathParamRid(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamSafelongHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                SafeLong param = StringDeserializers.deserializeSafeLong(pathParams.get("param"));
                delegate.pathParamSafelong(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                String param = StringDeserializers.deserializeString(pathParams.get("param"));
                delegate.pathParamString(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamUuidHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                UUID param = StringDeserializers.deserializeUuid(pathParams.get("param"));
                delegate.pathParamUuid(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamAliasStringHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                String paramRaw = StringDeserializers.deserializeString(pathParams.get("param"));
                AliasString param = AliasString.of(paramRaw);
                delegate.pathParamAliasString(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        private class PathParamEnumExampleHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                EnumExample param =
                        StringDeserializers.deserializeComplex(
                                pathParams.get("param"), EnumExample::valueOf);
                delegate.pathParamEnumExample(index, param);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }
}
