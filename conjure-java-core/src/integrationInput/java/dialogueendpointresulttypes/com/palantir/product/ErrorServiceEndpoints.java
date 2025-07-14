package dialogueendpointresulttypes.com.palantir.product;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Deserializer;
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
import io.undertow.util.StatusCodes;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class ErrorServiceEndpoints implements UndertowService {
    private final ErrorService delegate;

    private ErrorServiceEndpoints(ErrorService delegate) {
        this.delegate = delegate;
    }

    public static UndertowService of(ErrorService delegate) {
        return new ErrorServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return ImmutableList.of(
                new TestNonEndpointAssociatedErrorEndpoint(runtime, delegate),
                new TestBasicErrorEndpoint(runtime, delegate),
                new TestImportedErrorEndpoint(runtime, delegate),
                new TestMultipleErrorsAndPackagesEndpoint(runtime, delegate),
                new TestEmptyBodyEndpoint(runtime, delegate),
                new TestBinaryEndpoint(runtime, delegate),
                new TestOptionalBinaryEndpoint(runtime, delegate));
    }

    private static final class TestNonEndpointAssociatedErrorEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final ErrorService delegate;

        private final Deserializer<Boolean> deserializer;

        private final Serializer<String> serializer;

        TestNonEndpointAssociatedErrorEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {}, this);
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Boolean shouldThrowError = deserializer.deserialize(exchange);
            String result = delegate.testNonEndpointAssociatedError(authHeader, shouldThrowError);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/nonEndpointError";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testNonEndpointAssociatedError";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestBasicErrorEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final ErrorService delegate;

        private final Deserializer<Boolean> deserializer;

        private final Serializer<String> serializer;

        TestBasicErrorEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {}, this);
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException, TestServerErrors.InvalidArgument {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Boolean shouldThrowError = deserializer.deserialize(exchange);
            String result = delegate.testBasicError(authHeader, shouldThrowError);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/basic";
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

        private final ErrorService delegate;

        private final Deserializer<Boolean> deserializer;

        private final Serializer<String> serializer;

        TestImportedErrorEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {}, this);
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange)
                throws IOException, EndpointSpecificServerErrors.EndpointError {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Boolean shouldThrowError = deserializer.deserialize(exchange);
            String result = delegate.testImportedError(authHeader, shouldThrowError);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/imported";
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

        private final ErrorService delegate;

        private final Deserializer<Optional<String>> deserializer;

        private final Serializer<String> serializer;

        TestMultipleErrorsAndPackagesEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Optional<String>>() {}, this);
            this.serializer = runtime.bodySerDe().serializer(new TypeMarker<String>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange)
                throws IOException, TestServerErrors.InvalidArgument, TestServerErrors.NotFound,
                        EndpointSpecificTwoServerErrors.DifferentNamespace,
                        dialogueendpointresulttypes.com.palantir.another.EndpointSpecificServerErrors.DifferentPackage,
                        TestServerErrors.ComplicatedParameters {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Optional<String> errorToThrow = deserializer.deserialize(exchange);
            String result = delegate.testMultipleErrorsAndPackages(authHeader, errorToThrow);
            serializer.serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/multiple";
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

    private static final class TestEmptyBodyEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final ErrorService delegate;

        private final Deserializer<Boolean> deserializer;

        TestEmptyBodyEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException, TestServerErrors.InvalidArgument {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Boolean shouldThrowError = deserializer.deserialize(exchange);
            delegate.testEmptyBody(authHeader, shouldThrowError);
            exchange.setStatusCode(StatusCodes.NO_CONTENT);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/empty";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testEmptyBody";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestBinaryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final ErrorService delegate;

        private final Deserializer<Boolean> deserializer;

        TestBinaryEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException, TestServerErrors.InvalidArgument {
            AuthHeader authHeader = runtime.auth().header(exchange);
            Boolean shouldThrowError = deserializer.deserialize(exchange);
            BinaryResponseBody result = delegate.testBinary(authHeader, shouldThrowError);
            runtime.bodySerDe().serialize(result, exchange);
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/binary";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testBinary";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }

    private static final class TestOptionalBinaryEndpoint implements HttpHandler, Endpoint {
        private final UndertowRuntime runtime;

        private final ErrorService delegate;

        private final Deserializer<OptionalBinaryResponseMode> deserializer;

        TestOptionalBinaryEndpoint(UndertowRuntime runtime, ErrorService delegate) {
            this.runtime = runtime;
            this.delegate = delegate;
            this.deserializer = runtime.bodySerDe().deserializer(new TypeMarker<OptionalBinaryResponseMode>() {}, this);
        }

        @Override
        public void handleRequest(HttpServerExchange exchange) throws IOException, TestServerErrors.InvalidArgument {
            AuthHeader authHeader = runtime.auth().header(exchange);
            OptionalBinaryResponseMode mode = deserializer.deserialize(exchange);
            Optional<BinaryResponseBody> result = delegate.testOptionalBinary(authHeader, mode);
            if (result.isPresent()) {
                runtime.bodySerDe().serialize(result.get(), exchange);
            } else {
                exchange.setStatusCode(StatusCodes.NO_CONTENT);
            }
        }

        @Override
        public HttpString method() {
            return Methods.POST;
        }

        @Override
        public String template() {
            return "/errors/optional-binary";
        }

        @Override
        public String serviceName() {
            return "ErrorService";
        }

        @Override
        public String name() {
            return "testOptionalBinary";
        }

        @Override
        public HttpHandler handler() {
            return this;
        }
    }
}
