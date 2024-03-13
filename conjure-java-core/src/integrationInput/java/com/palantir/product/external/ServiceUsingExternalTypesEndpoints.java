package com.palantir.product.external;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.logsafe.SafeArg;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.PathTemplateMatch;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class ServiceUsingExternalTypesEndpoints implements UndertowService {
    private final ServiceUsingExternalTypes delegate;

    private ServiceUsingExternalTypesEndpoints(ServiceUsingExternalTypes delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(ServiceUsingExternalTypes delegate) {
        return new ServiceUsingExternalTypesEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(new ExternalEndpoint(runtime, delegate));
    }

    private static final class ExternalEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final ServiceUsingExternalTypes delegate;

        private final Deserializer<ImmutableList<String>> deserializer;

        private final Serializer<Map<String, String>> serializer;

        ExternalEndpoint(UndertowRuntime runtime, ServiceUsingExternalTypes delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<ImmutableList<String>>() {}, this);
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Map<String, String>>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            RequestContext requestContext = runtime.contexts().createContext(exchange, this);
            List<String> body = deserializer.deserialize(exchange);
            Map<String, String> pathParams =
                    exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
            String path = runtime.plainSerDe().deserializeString(pathParams.get("path"));
            requestContext.requestArg(SafeArg.of("path", path));
            Map<String, String> result = delegate.external(path, body);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.PUT;
        }

        @Override
        public String template() {
            return "/external/{path}";
        }

        @Override
        public String serviceName() {
            return "ServiceUsingExternalTypes";
        }

        @Override
        public String name() {
            return "external";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
