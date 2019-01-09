package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.HandlerContext;
import com.palantir.conjure.java.undertow.lib.Routable;
import com.palantir.conjure.java.undertow.lib.RoutingRegistry;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EmptyPathServiceEndpoint implements Endpoint {
    private final UndertowEmptyPathService delegate;

    private EmptyPathServiceEndpoint(UndertowEmptyPathService delegate) {
        this.delegate = delegate;
    }

    public static Endpoint of(UndertowEmptyPathService delegate) {
        return new EmptyPathServiceEndpoint(delegate);
    }

    @Override
    public Routable create(HandlerContext context) {
        return new EmptyPathServiceRoutable(context, delegate);
    }

    private static final class EmptyPathServiceRoutable implements Routable {
        private final UndertowEmptyPathService delegate;

        private final SerializerRegistry serializers;

        private EmptyPathServiceRoutable(
                HandlerContext context, UndertowEmptyPathService delegate) {
            this.serializers = context.serializerRegistry();
            this.delegate = delegate;
        }

        @Override
        public void register(RoutingRegistry routingRegistry) {
            routingRegistry.get("/", new EmptyPathHandler());
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
