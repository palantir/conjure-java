package com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.Serializer;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class AsyncServiceEndpoints implements UndertowService {
    private final UndertowAsyncService delegate;

    private AsyncServiceEndpoints(UndertowAsyncService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(UndertowAsyncService delegate) {
        return new AsyncServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(
                Arrays.asList(
                        new BinaryAsyncEndpoint(runtime, delegate),
                        new BinaryOptionalAsyncEndpoint(runtime, delegate),
                        new NoReturnAsyncEndpoint(runtime, delegate),
                        new NoReturnThrowingAsyncEndpoint(runtime, delegate),
                        new NoReturnFailedFutureAsyncEndpoint(runtime, delegate),
                        new OptionalEnumQueryAsyncEndpoint(runtime, delegate)));
    }

    private static final class BinaryAsyncEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<BinaryResponseBody> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncService delegate;

        BinaryAsyncEndpoint(UndertowRuntime runtime, UndertowAsyncService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<BinaryResponseBody> result = delegate.binaryAsync(authHeader);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(BinaryResponseBody result, HttpServerExchange exchange)
                throws IOException {
            runtime.bodySerDe().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/base/async/async/binary";
        }

        @Override
        public String serviceName() {
            return "AsyncService";
        }

        @Override
        public String name() {
            return "binaryAsync";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class BinaryOptionalAsyncEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<Optional<BinaryResponseBody>> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncService delegate;

        BinaryOptionalAsyncEndpoint(UndertowRuntime runtime, UndertowAsyncService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<Optional<BinaryResponseBody>> result =
                    delegate.binaryOptionalAsync(authHeader);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Optional<BinaryResponseBody> result, HttpServerExchange exchange)
                throws IOException {
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
            return "/base/async/async/optional/binary";
        }

        @Override
        public String serviceName() {
            return "AsyncService";
        }

        @Override
        public String name() {
            return "binaryOptionalAsync";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NoReturnAsyncEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<Void> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncService delegate;

        NoReturnAsyncEndpoint(UndertowRuntime runtime, UndertowAsyncService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<Void> result = delegate.noReturnAsync(authHeader);
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
            return "/base/async/async/no-return";
        }

        @Override
        public String serviceName() {
            return "AsyncService";
        }

        @Override
        public String name() {
            return "noReturnAsync";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NoReturnThrowingAsyncEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<Void> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncService delegate;

        NoReturnThrowingAsyncEndpoint(UndertowRuntime runtime, UndertowAsyncService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<Void> result = delegate.noReturnThrowingAsync(authHeader);
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
            return "/base/async/async/no-return/throwing";
        }

        @Override
        public String serviceName() {
            return "AsyncService";
        }

        @Override
        public String name() {
            return "noReturnThrowingAsync";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class NoReturnFailedFutureAsyncEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<Void> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncService delegate;

        NoReturnFailedFutureAsyncEndpoint(UndertowRuntime runtime, UndertowAsyncService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            ListenableFuture<Void> result = delegate.noReturnFailedFutureAsync(authHeader);
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
            return "/base/async/async/no-return/failed";
        }

        @Override
        public String serviceName() {
            return "AsyncService";
        }

        @Override
        public String name() {
            return "noReturnFailedFutureAsync";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class OptionalEnumQueryAsyncEndpoint
            implements HttpHandler, Endpoint, ReturnValueWriter<Optional<String>> {
        private final UndertowRuntime runtime;

        private final UndertowAsyncService delegate;

        private final Serializer<Optional<String>> serializer;

        OptionalEnumQueryAsyncEndpoint(UndertowRuntime runtime, UndertowAsyncService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<Optional<String>>() {});
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            Optional<String> queryParamName =
                    runtime.plainSerDe()
                            .deserializeOptionalString(queryParams.get("queryParamName"));
            ListenableFuture<Optional<String>> result =
                    delegate.optionalEnumQueryAsync(authHeader, queryParamName);
            runtime.async().register(result, this, exchange);
        }

        @Override
        public void write(Optional<String> result, HttpServerExchange exchange) throws IOException {
            if (result.isPresent()) {
                serializer.serialize(result, exchange);
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
            return "/base/async/enum/optional/query";
        }

        @Override
        public String serviceName() {
            return "AsyncService";
        }

        @Override
        public String name() {
            return "optionalEnumQueryAsync";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
