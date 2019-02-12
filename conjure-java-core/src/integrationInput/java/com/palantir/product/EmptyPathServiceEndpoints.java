package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.ServiceContext;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EmptyPathServiceEndpoints implements Service {
    private final UndertowEmptyPathService delegate;

    private EmptyPathServiceEndpoints(UndertowEmptyPathService delegate) {
        this.delegate = delegate;
    }

    public static Service of(UndertowEmptyPathService delegate) {
        return new EmptyPathServiceEndpoints(delegate);
    }

    @Override
    public Registrable create(ServiceContext context) {
        return new EmptyPathServiceRegistrable(context, delegate);
    }

    private static final class EmptyPathServiceRegistrable implements Registrable {
        private final UndertowEmptyPathService delegate;

        private final SerializerRegistry serializers;

        private EmptyPathServiceRegistrable(
                ServiceContext context, UndertowEmptyPathService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate =
                    context.serviceInstrumenter()
                            .instrument(delegate, UndertowEmptyPathService.class);
        }

        @Override
        public void register(EndpointRegistry endpointRegistry) {
            endpointRegistry.add(
                    Endpoint.get("/", "EmptyPathService", "emptyPath"), new EmptyPathHandler());
        }

        private class EmptyPathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                boolean result = delegate.emptyPath();
                serializers.serialize(result, exchange);
            }
        }
    }
}
