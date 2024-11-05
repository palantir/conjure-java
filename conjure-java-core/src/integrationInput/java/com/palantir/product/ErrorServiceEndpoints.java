package com.palantir.product;

import com.google.common.collect.ImmutableList;
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
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class ErrorServiceEndpoints implements UndertowService {
    private final UndertowErrorService delegate;

    private ErrorServiceEndpoints(UndertowErrorService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowErrorService delegate) {
        return new ErrorServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(
                new TestBasicErrorEndpoint(runtime, delegate),
                new TestImportedErrorEndpoint(runtime, delegate),
                new TestMultipleErrorsAndPackagesEndpoint(runtime, delegate));
    }

    private static final class TestBasicErrorEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowErrorService delegate;

        private final Serializer<String> serializer;

        TestBasicErrorEndpoint(UndertowRuntime runtime, UndertowErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException, ServerTestErrors.InvalidArgument {
            AuthHeader authHeader = runtime.auth().header(exchange);
            String result = delegate.testBasicError(authHeader);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/basic";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testBasicError";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestImportedErrorEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowErrorService delegate;

        private final Serializer<String> serializer;

        TestImportedErrorEndpoint(UndertowRuntime runtime, UndertowErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange)
                throws IOException, ServerEndpointSpecificErrors.EndpointError {
            AuthHeader authHeader = runtime.auth().header(exchange);
            String result = delegate.testImportedError(authHeader);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/imported";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testImportedError";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestMultipleErrorsAndPackagesEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowErrorService delegate;

        private final Serializer<String> serializer;

        TestMultipleErrorsAndPackagesEndpoint(UndertowRuntime runtime, UndertowErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange)
                throws IOException, ServerTestErrors.InvalidArgument, ServerTestErrors.NotFound,
                        ServerEndpointSpecificTwoErrors.DifferentNamespace,
                        com.palantir.another.ServerEndpointSpecificErrors.DifferentPackage {
            AuthHeader authHeader = runtime.auth().header(exchange);
            String result = delegate.testMultipleErrorsAndPackages(authHeader);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/multiple";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testMultipleErrorsAndPackages";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
