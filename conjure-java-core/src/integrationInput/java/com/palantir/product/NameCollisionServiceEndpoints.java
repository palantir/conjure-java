package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class NameCollisionServiceEndpoints implements UndertowService {
    private final UndertowNameCollisionService delegate;

    private NameCollisionServiceEndpoints(UndertowNameCollisionService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowNameCollisionService delegate) {
        return new NameCollisionServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(Arrays.asList(new IntEndpoint(runtime, delegate)));
    }

    private static final class IntEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowNameCollisionService delegate;

        private final Serializer<String> serializer;

        IntEndpoint(UndertowRuntime runtime, UndertowNameCollisionService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            String authHeader_ =
                    runtime.plainSerDe().deserializeString(queryParams.get("authHeader"));
            String long_ = runtime.plainSerDe().deserializeString(queryParams.get("long"));
            String runtime_ = runtime.plainSerDe().deserializeString(queryParams.get("runtime"));
            String serializer_ =
                    runtime.plainSerDe().deserializeString(queryParams.get("serializer"));
            String deserializer_ =
                    runtime.plainSerDe().deserializeString(queryParams.get("deserializer"));
            String delegate_ = runtime.plainSerDe().deserializeString(queryParams.get("delegate"));
            String result_ = runtime.plainSerDe().deserializeString(queryParams.get("result"));
            String result =
                    delegate.int_(
                            authHeader,
                            authHeader_,
                            long_,
                            runtime_,
                            serializer_,
                            deserializer_,
                            delegate_,
                            result_);
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
            return "UndertowNameCollisionService";
        }

        @Override
        public String name() {
            return "int";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
