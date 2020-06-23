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

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEmptyPathService extends UndertowService {
    boolean emptyPath();

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(Arrays.asList(new EmptyPathEndpoint(runtime, this)));
    }

    final class EmptyPathEndpoint implements HttpHandler, Endpoint {
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
            return "EmptyPathService";
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
