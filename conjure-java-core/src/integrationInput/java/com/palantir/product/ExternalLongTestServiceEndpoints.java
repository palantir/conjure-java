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
        return ImmutableList.of(
                new TestDangerousLongEndpoint(runtime, delegate),
                new TestSafeExternalLongEndpoint(runtime, delegate),
                new TestLongEndpoint(runtime, delegate),
                new TestDangerousLongAliasEndpoint(runtime, delegate),
                new TestSafeExternalLongAliasEndpoint(runtime, delegate),
                new TestLongAliasEndpoint(runtime, delegate));
    }

    private static final class TestDangerousLongEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<Long> deserializer;

        TestDangerousLongEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Long>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Long dangerousLong = deserializer.deserialize(exchange);
            delegate.testDangerousLong(authHeader, dangerousLong);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/testDangerousLong";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testDangerousLong";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestSafeExternalLongEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<Long> deserializer;

        TestSafeExternalLongEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Long>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Long safeExternalLong = deserializer.deserialize(exchange);
            delegate.testSafeExternalLong(authHeader, safeExternalLong);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/testSafeExternalLong";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testSafeExternalLong";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestLongEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<Long> deserializer;

        TestLongEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Long>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Long long_ = deserializer.deserialize(exchange);
            delegate.testLong(authHeader, long_);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/testLong";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testLong";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestDangerousLongAliasEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<DangerousLongAlias> deserializer;

        TestDangerousLongAliasEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<DangerousLongAlias>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            DangerousLongAlias dangerousLongAlias = deserializer.deserialize(exchange);
            delegate.testDangerousLongAlias(authHeader, dangerousLongAlias);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/testDangerousLongAlias";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testDangerousLongAlias";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestSafeExternalLongAliasEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<SafeLongAlias> deserializer;

        TestSafeExternalLongAliasEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<SafeLongAlias>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            SafeLongAlias safeExternalLongAlias = deserializer.deserialize(exchange);
            delegate.testSafeExternalLongAlias(authHeader, safeExternalLongAlias);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/testSafeExternalLongAlias";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testSafeExternalLongAlias";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestLongAliasEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowExternalLongTestService delegate;

        private final Deserializer<ExternalLongAlias> deserializer;

        TestLongAliasEndpoint(UndertowRuntime runtime, UndertowExternalLongTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<ExternalLongAlias>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ExternalLongAlias longAlias = deserializer.deserialize(exchange);
            delegate.testLongAlias(authHeader, longAlias);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/external-long/testLongAlias";
        }

        @Override
        public String serviceName() {
            return "ExternalLongTestService";
        }

        @Override
        public String name() {
            return "testLongAlias";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
