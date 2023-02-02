package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class ExternalLongTestServiceEndpoints implements UndertowService {
    private final UndertowExternalLongTestService delegate;

    private ExternalLongTestServiceEndpoints(UndertowExternalLongTestService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowExternalLongTestService delegate) {
        return new ExternalLongTestServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(new TestExternalLongArgEndpoint(runtime, delegate));
    }

    private static final class TestExternalLongArgEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<Long> deserializer;

        TestExternalLongArgEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Long>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Long externalLong = deserializer.deserialize(exchange);
            delegate.testExternalLongArg(authHeader, externalLong);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/test";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testExternalLongArg";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
