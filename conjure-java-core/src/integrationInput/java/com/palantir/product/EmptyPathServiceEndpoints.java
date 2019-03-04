package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EmptyPathServiceEndpoints implements UndertowService {
    private final UndertowEmptyPathService delegate;

    private EmptyPathServiceEndpoints(UndertowEmptyPathService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowEmptyPathService delegate) {
        return new EmptyPathServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return new EmptyPathServiceFactory(runtime, delegate).create();
    }

    private static final class EmptyPathServiceFactory {
        private final UndertowEmptyPathService delegate;

        private final UndertowRuntime runtime;

        private EmptyPathServiceFactory(
                UndertowRuntime runtime, UndertowEmptyPathService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        List<Endpoint> create() {
            return ImmutableList.of(new EmptyPathEndpoint());
        }

        private class EmptyPathEndpoint implements HttpHandler, Endpoint {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws IOException {
                boolean result = delegate.emptyPath();
                runtime.serde().serialize(result, exchange);
            }

            @Override
            public final HttpString method() {
                return Methods.GET;
            }

            @Override
            public final String template() {
                return "/";
            }

            @Override
            public final Optional<String> serviceName() {
                return Optional.of("EmptyPathService");
            }

            @Override
            public final Optional<String> name() {
                return Optional.of("emptyPath");
            }

            @Override
            public final HttpHandler handler() {
                return this;
            }
        }
    }
}
