package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
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
    public void register(ServiceContext context, EndpointRegistry endpointRegistry) {
        new EmptyPathServiceRegistrable(context, delegate).register(endpointRegistry);
    }

    private static final class EmptyPathServiceRegistrable {
        private final UndertowEmptyPathService delegate;

        private final ServiceContext context;

        private EmptyPathServiceRegistrable(
                ServiceContext context, UndertowEmptyPathService delegate) {
            this.context = context;
            this.delegate = context.instrument(delegate, UndertowEmptyPathService.class);
        }

        void register(EndpointRegistry endpointRegistry) {
            endpointRegistry.add(
                    Endpoint.get("/", "EmptyPathService", "emptyPath"), new EmptyPathHandler());
        }

        private class EmptyPathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                boolean result = delegate.emptyPath();
                context.serialize(result, exchange);
            }
        }
    }
}
