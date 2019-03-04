package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.EndpointRegistry;
import com.palantir.conjure.java.undertow.lib.Service;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
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
    public void register(UndertowRuntime runtime, EndpointRegistry registry) {
        new EmptyPathServiceRegistrable(runtime, delegate).register(registry);
    }

    private static final class EmptyPathServiceRegistrable {
        private final UndertowEmptyPathService delegate;

        private final UndertowRuntime runtime;

        private EmptyPathServiceRegistrable(
                UndertowRuntime runtime, UndertowEmptyPathService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        void register(EndpointRegistry registry) {
            registry.add(
                    Endpoint.get("/", "EmptyPathService", "emptyPath"), new EmptyPathHandler());
        }

        private class EmptyPathHandler implements HttpHandler {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                boolean result = delegate.emptyPath();
                runtime.serde().serialize(result, exchange);
            }
        }
    }
}
