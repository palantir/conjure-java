package com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import io.undertow.httpcore.StatusCodes;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class AsyncRequestProcessingTestServiceEndpoints implements UndertowService {
    private final UndertowAsyncRequestProcessingTestService delegate;

    private AsyncRequestProcessingTestServiceEndpoints(UndertowAsyncRequestProcessingTestService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowAsyncRequestProcessingTestService delegate) {
        return new AsyncRequestProcessingTestServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(
                new DelayEndpoint(runtime, delegate),
                new ThrowsInHandlerEndpoint(runtime, delegate),
                new FailedFutureEndpoint(runtime, delegate),
                new BinaryEndpoint(runtime, delegate),
                new FutureTraceIdEndpoint(runtime, delegate));
    }

    private static final class DelayEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<String> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncRequestProcessingTestService delegate;

        private final Serializer<String> serializer;

        DelayEndpoint(UndertowRuntime runtime, UndertowAsyncRequestProcessingTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            OptionalInt delayMillis = runtime.plainSerDe().deserializeOptionalInteger(queryParams.get("delayMillis"));
            ListenableFuture<String> result = delegate.delay(delayMillis);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(String result, HttpServerExchange exchange) throws IOException {
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/async/delay";
        }

        @Override
        public String serviceName() {
            return "AsyncRequestProcessingTestService";
        }

        @Override
        public String name() {
            return "delay";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class ThrowsInHandlerEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<Void> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncRequestProcessingTestService delegate;

        ThrowsInHandlerEndpoint(UndertowRuntime runtime, UndertowAsyncRequestProcessingTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            ListenableFuture<Void> result = delegate.throwsInHandler();
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Void result, HttpServerExchange exchange) throws IOException {
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/async/throws";
        }

        @Override
        public String serviceName() {
            return "AsyncRequestProcessingTestService";
        }

        @Override
        public String name() {
            return "throwsInHandler";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class FailedFutureEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<Void> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncRequestProcessingTestService delegate;

        FailedFutureEndpoint(UndertowRuntime runtime, UndertowAsyncRequestProcessingTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            OptionalInt delayMillis = runtime.plainSerDe().deserializeOptionalInteger(queryParams.get("delayMillis"));
            ListenableFuture<Void> result = delegate.failedFuture(delayMillis);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Void result, HttpServerExchange exchange) throws IOException {
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/async/failed-future";
        }

        @Override
        public String serviceName() {
            return "AsyncRequestProcessingTestService";
        }

        @Override
        public String name() {
            return "failedFuture";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class BinaryEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<Optional<BinaryResponseBody>> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncRequestProcessingTestService delegate;

        BinaryEndpoint(UndertowRuntime runtime, UndertowAsyncRequestProcessingTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<String> stringValue =
                    runtime.plainSerDe().deserializeOptionalString(queryParams.get("stringValue"));
            ListenableFuture<Optional<BinaryResponseBody>> result = delegate.binary(stringValue);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Optional<BinaryResponseBody> result, HttpServerExchange exchange) throws IOException {
            if (result.isPresent()) {
                runtime.bodySerDe().serialize(result.get(), exchange);
            } else {
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/async/binary";
        }

        @Override
        public String serviceName() {
            return "AsyncRequestProcessingTestService";
        }

        @Override
        public String name() {
            return "binary";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class FutureTraceIdEndpoint implements HttpHandler, Endpoint, ReturnValueWriter<Object> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncRequestProcessingTestService delegate;

        private final Serializer<Object> serializer;

        FutureTraceIdEndpoint(UndertowRuntime runtime, UndertowAsyncRequestProcessingTestService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Object>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            OptionalInt delayMillis = runtime.plainSerDe().deserializeOptionalInteger(queryParams.get("delayMillis"));
            ListenableFuture<Object> result = delegate.futureTraceId(delayMillis);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Object result, HttpServerExchange exchange) throws IOException {
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/async/future-trace";
        }

        @Override
        public String serviceName() {
            return "AsyncRequestProcessingTestService";
        }

        @Override
        public String name() {
            return "futureTraceId";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
