package com.palantir.conjure.verification.server;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import com.palantir.conjure.java.undertow.lib.internal.StringDeserializers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class AutoDeserializeConfirmServiceEndpoints implements Service {
    private final UndertowAutoDeserializeConfirmService delegate;

    private AutoDeserializeConfirmServiceEndpoints(UndertowAutoDeserializeConfirmService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowAutoDeserializeConfirmService delegate) {
        return new AutoDeserializeConfirmServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new AutoDeserializeConfirmServiceRegistrable(context, delegate);
    }

    private static final class AutoDeserializeConfirmServiceRegistrable implements Registrable {
        private final UndertowAutoDeserializeConfirmService delegate;

        private final SerializerRegistry serializers;

        private AutoDeserializeConfirmServiceRegistrable(
                ServiceContext context, UndertowAutoDeserializeConfirmService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry.add(
                    Endpoint.post(
                            "/confirm/{endpoint}/{index}",
                            "AutoDeserializeConfirmService",
                            "confirm"),
                    new ConfirmHandler());
        }

        private class ConfirmHandler implements HttpHandler {
            private final TypeToken<Object> bodyType = new TypeToken<Object>() {};

            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                Object body = serializers.deserialize(bodyType, exchange);
                Map<String, String> pathParams =
                        exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
                String endpointRaw =
                        StringDeserializers.deserializeString(pathParams.get("endpoint"));
                EndpointName endpoint = EndpointName.of(endpointRaw);
                int index = StringDeserializers.deserializeInteger(pathParams.get("index"));
                delegate.confirm(endpoint, index, body);
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }
    }
}
