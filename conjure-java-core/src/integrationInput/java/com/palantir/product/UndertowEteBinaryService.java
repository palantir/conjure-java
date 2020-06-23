package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteBinaryService extends UndertowService {
    BinaryResponseBody postBinary(AuthHeader authHeader, InputStream body);

    Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader authHeader);

    Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     */
    BinaryResponseBody getBinaryFailure(AuthHeader authHeader, int numBytes);

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return Collections.unmodifiableList(Arrays.asList(
                new PostBinaryEndpoint(runtime, this),
                new GetOptionalBinaryPresentEndpoint(runtime, this),
                new GetOptionalBinaryEmptyEndpoint(runtime, this),
                new GetBinaryFailureEndpoint(runtime, this)));
    }

    final class PostBinaryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteBinaryService delegate;

        PostBinaryEndpoint(UndertowRuntime runtime, UndertowEteBinaryService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            InputStream body = runtime.bodySerDe().deserializeInputStream(exchange);
            BinaryResponseBody result = delegate.postBinary(authHeader, body);
            runtime.bodySerDe().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/binary";
        }

        @Override
        public String serviceName() {
            return "EteBinaryService";
        }

        @Override
        public String name() {
            return "postBinary";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    final class GetOptionalBinaryPresentEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteBinaryService delegate;

        GetOptionalBinaryPresentEndpoint(UndertowRuntime runtime, UndertowEteBinaryService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<BinaryResponseBody> result = delegate.getOptionalBinaryPresent(authHeader);
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
            return "/binary/optional/present";
        }

        @Override
        public String serviceName() {
            return "EteBinaryService";
        }

        @Override
        public String name() {
            return "getOptionalBinaryPresent";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    final class GetOptionalBinaryEmptyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteBinaryService delegate;

        GetOptionalBinaryEmptyEndpoint(UndertowRuntime runtime, UndertowEteBinaryService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<BinaryResponseBody> result = delegate.getOptionalBinaryEmpty(authHeader);
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
            return "/binary/optional/empty";
        }

        @Override
        public String serviceName() {
            return "EteBinaryService";
        }

        @Override
        public String name() {
            return "getOptionalBinaryEmpty";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    final class GetBinaryFailureEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final UndertowEteBinaryService delegate;

        GetBinaryFailureEndpoint(UndertowRuntime runtime, UndertowEteBinaryService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Map<String, Deque<String>> queryParams = exchange.getQueryParameters();
            int numBytes = runtime.plainSerDe().deserializeInteger(queryParams.get("numBytes"));
            BinaryResponseBody result = delegate.getBinaryFailure(authHeader, numBytes);
            runtime.bodySerDe().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.GET;
        }

        @Override
        public String template() {
            return "/binary/failure";
        }

        @Override
        public String serviceName() {
            return "EteBinaryService";
        }

        @Override
        public String name() {
            return "getBinaryFailure";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
