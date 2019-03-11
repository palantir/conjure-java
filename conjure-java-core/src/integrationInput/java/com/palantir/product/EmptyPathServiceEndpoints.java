package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        return Collections.unmodifiableList(
                Arrays.asList(new EmptyPathEndpoint(runtime, delegate)));
    }

    private static final class EmptyPathEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEmptyPathService delegate;

        private final Serializer<Boolean> serializer;

        EmptyPathEndpoint(UndertowRuntime runtime, UndertowEmptyPathService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            boolean result = delegate.emptyPath();
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/";
        }

        @Override
        public String serviceName() {
            return "UndertowEmptyPathService";
        }

        @Override
        public String name() {
            return "emptyPath";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
