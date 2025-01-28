package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DeserializerArgs;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.EndpointError;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.tokens.auth.AuthHeader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(ErrorServiceAsync.Factory.class)
public interface ErrorServiceAsync {
    /** @apiNote {@code POST /errors/basic} */
    @ClientEndpoint(method = "POST", path = "/errors/basic")
    ListenableFuture<TestBasicErrorResponse> testBasicError(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/imported} */
    @ClientEndpoint(method = "POST", path = "/errors/imported")
    ListenableFuture<TestImportedErrorResponse> testImportedError(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/multiple} */
    @ClientEndpoint(method = "POST", path = "/errors/multiple")
    ListenableFuture<TestMultipleErrorsAndPackagesResponse> testMultipleErrorsAndPackages(
            AuthHeader authHeader, Optional<String> errorToThrow);

    /** @apiNote {@code POST /errors/empty} */
    @ClientEndpoint(method = "POST", path = "/errors/empty")
    ListenableFuture<TestEmptyBodyResponse> testEmptyBody(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/binary} */
    @ClientEndpoint(method = "POST", path = "/errors/binary")
    ListenableFuture<TestBinaryResponse> testBinary(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/optional-binary} */
    @ClientEndpoint(method = "POST", path = "/errors/optional-binary")
    ListenableFuture<TestOptionalBinaryResponse> testOptionalBinary(
            AuthHeader authHeader, OptionalBinaryResponseMode mode);

    /** Creates an asynchronous/non-blocking client for a ErrorService service. */
    static ErrorServiceAsync of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new ErrorServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final Serializer<Boolean> testBasicErrorSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testBasicErrorChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testBasicError);

            private final Deserializer<TestBasicErrorResponse> testBasicErrorDeserializer = _runtime.bodySerDe()
                    .deserializer(DeserializerArgs.<TestBasicErrorResponse>builder()
                            .baseType(new TypeMarker<>() {})
                            .success(new TypeMarker<TestBasicErrorResponse.Success>() {})
                            .error(
                                    TestErrors.INVALID_ARGUMENT.name(),
                                    new TypeMarker<TestBasicErrorResponse.InvalidArgument>() {})
                            .build());

            private final Serializer<Boolean> testImportedErrorSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testImportedErrorChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testImportedError);

            private final Deserializer<TestImportedErrorResponse> testImportedErrorDeserializer = _runtime.bodySerDe()
                    .deserializer(DeserializerArgs.<TestImportedErrorResponse>builder()
                            .baseType(new TypeMarker<>() {})
                            .success(new TypeMarker<TestImportedErrorResponse.Success>() {})
                            .error(
                                    EndpointSpecificErrors.ENDPOINT_ERROR.name(),
                                    new TypeMarker<TestImportedErrorResponse.EndpointError>() {})
                            .build());

            private final Serializer<Optional<String>> testMultipleErrorsAndPackagesSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Optional<String>>() {});

            private final EndpointChannel testMultipleErrorsAndPackagesChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testMultipleErrorsAndPackages);

            private final Deserializer<TestMultipleErrorsAndPackagesResponse>
                    testMultipleErrorsAndPackagesDeserializer = _runtime.bodySerDe()
                            .deserializer(DeserializerArgs.<TestMultipleErrorsAndPackagesResponse>builder()
                                    .baseType(new TypeMarker<>() {})
                                    .success(new TypeMarker<TestMultipleErrorsAndPackagesResponse.Success>() {})
                                    .error(
                                            TestErrors.INVALID_ARGUMENT.name(),
                                            new TypeMarker<TestMultipleErrorsAndPackagesResponse.InvalidArgument>() {})
                                    .error(
                                            TestErrors.NOT_FOUND.name(),
                                            new TypeMarker<TestMultipleErrorsAndPackagesResponse.NotFound>() {})
                                    .error(
                                            EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE.name(),
                                            new TypeMarker<
                                                    TestMultipleErrorsAndPackagesResponse.DifferentNamespace>() {})
                                    .error(
                                            com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE.name(),
                                            new TypeMarker<TestMultipleErrorsAndPackagesResponse.DifferentPackage>() {})
                                    .build());

            private final Serializer<Boolean> testEmptyBodySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testEmptyBodyChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testEmptyBody);

            private final Deserializer<TestEmptyBodyResponse> testEmptyBodyDeserializer = _runtime.bodySerDe()
                    .deserializer(DeserializerArgs.<TestEmptyBodyResponse>builder()
                            .baseType(new TypeMarker<>() {})
                            .success(new TypeMarker<TestEmptyBodyResponse.Success>() {})
                            .error(
                                    TestErrors.INVALID_ARGUMENT.name(),
                                    new TypeMarker<TestEmptyBodyResponse.InvalidArgument>() {})
                            .build());

            private final Serializer<Boolean> testBinarySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testBinaryChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testBinary);

            private final Deserializer<TestBinaryResponse> testBinaryDeserializer = _runtime.bodySerDe()
                    .inputStreamDeserializer(DeserializerArgs.<TestBinaryResponse>builder()
                            .baseType(new TypeMarker<>() {})
                            .success(new TypeMarker<TestBinaryResponse.Success>() {})
                            .error(
                                    TestErrors.INVALID_ARGUMENT.name(),
                                    new TypeMarker<TestBinaryResponse.InvalidArgument>() {})
                            .build());

            private final Serializer<OptionalBinaryResponseMode> testOptionalBinarySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<OptionalBinaryResponseMode>() {});

            private final EndpointChannel testOptionalBinaryChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testOptionalBinary);

            private final Deserializer<TestOptionalBinaryResponse> testOptionalBinaryDeserializer = _runtime.bodySerDe()
                    .optionalInputStreamDeserializer(DeserializerArgs.<TestOptionalBinaryResponse>builder()
                            .baseType(new TypeMarker<>() {})
                            .success(new TypeMarker<TestOptionalBinaryResponse.Success>() {})
                            .error(
                                    TestErrors.INVALID_ARGUMENT.name(),
                                    new TypeMarker<TestOptionalBinaryResponse.InvalidArgument>() {})
                            .build());

            @Override
            public ListenableFuture<TestBasicErrorResponse> testBasicError(
                    AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testBasicErrorSerializer.serialize(shouldThrowError));
                return _runtime.clients().call(testBasicErrorChannel, _request.build(), testBasicErrorDeserializer);
            }

            @Override
            public ListenableFuture<TestImportedErrorResponse> testImportedError(
                    AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testImportedErrorSerializer.serialize(shouldThrowError));
                return _runtime.clients()
                        .call(testImportedErrorChannel, _request.build(), testImportedErrorDeserializer);
            }

            @Override
            public ListenableFuture<TestMultipleErrorsAndPackagesResponse> testMultipleErrorsAndPackages(
                    AuthHeader authHeader, Optional<String> errorToThrow) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testMultipleErrorsAndPackagesSerializer.serialize(errorToThrow));
                return _runtime.clients()
                        .call(
                                testMultipleErrorsAndPackagesChannel,
                                _request.build(),
                                testMultipleErrorsAndPackagesDeserializer);
            }

            @Override
            public ListenableFuture<TestEmptyBodyResponse> testEmptyBody(
                    AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testEmptyBodySerializer.serialize(shouldThrowError));
                return _runtime.clients().call(testEmptyBodyChannel, _request.build(), testEmptyBodyDeserializer);
            }

            @Override
            public ListenableFuture<TestBinaryResponse> testBinary(AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testBinarySerializer.serialize(shouldThrowError));
                return _runtime.clients().call(testBinaryChannel, _request.build(), testBinaryDeserializer);
            }

            @Override
            public ListenableFuture<TestOptionalBinaryResponse> testOptionalBinary(
                    AuthHeader authHeader, OptionalBinaryResponseMode mode) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testOptionalBinarySerializer.serialize(mode));
                return _runtime.clients()
                        .call(testOptionalBinaryChannel, _request.build(), testOptionalBinaryDeserializer);
            }

            @Override
            public String toString() {
                return "ErrorServiceAsync{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime=" + _runtime
                        + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a ErrorService service. */
    static ErrorServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
        if (_channel instanceof EndpointChannelFactory) {
            return of((EndpointChannelFactory) _channel, _runtime);
        }
        return of(
                new EndpointChannelFactory() {
                    @Override
                    public EndpointChannel endpoint(Endpoint endpoint) {
                        return _runtime.clients().bind(_channel, endpoint);
                    }
                },
                _runtime);
    }

    final class Factory implements DialogueServiceFactory<ErrorServiceAsync> {
        @Override
        public ErrorServiceAsync create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return ErrorServiceAsync.of(endpointChannelFactory, runtime);
        }
    }

    sealed interface TestBasicErrorResponse
            permits TestBasicErrorResponse.Success, TestBasicErrorResponse.InvalidArgument {
        record Success(@JsonValue String value) implements TestBasicErrorResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class InvalidArgument extends EndpointError<TestErrors.InvalidArgumentParameters>
                implements TestBasicErrorResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            InvalidArgument(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestImportedErrorResponse
            permits TestImportedErrorResponse.Success, TestImportedErrorResponse.EndpointError {
        record Success(@JsonValue String value) implements TestImportedErrorResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class EndpointError
                extends com.palantir.dialogue.EndpointError<EndpointSpecificErrors.EndpointErrorParameters>
                implements TestImportedErrorResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            EndpointError(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") EndpointSpecificErrors.EndpointErrorParameters parameters) {
                super(errorCode, EndpointSpecificErrors.ENDPOINT_ERROR.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestMultipleErrorsAndPackagesResponse
            permits TestMultipleErrorsAndPackagesResponse.Success,
                    TestMultipleErrorsAndPackagesResponse.InvalidArgument,
                    TestMultipleErrorsAndPackagesResponse.NotFound,
                    TestMultipleErrorsAndPackagesResponse.DifferentNamespace,
                    TestMultipleErrorsAndPackagesResponse.DifferentPackage {
        record Success(@JsonValue String value) implements TestMultipleErrorsAndPackagesResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class InvalidArgument extends EndpointError<TestErrors.InvalidArgumentParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            InvalidArgument(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }

        final class NotFound extends EndpointError<TestErrors.NotFoundParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            NotFound(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.NotFoundParameters parameters) {
                super(errorCode, TestErrors.NOT_FOUND.name(), errorInstanceId, parameters);
            }
        }

        final class DifferentNamespace extends EndpointError<EndpointSpecificTwoErrors.DifferentNamespaceParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            DifferentNamespace(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId) {
                super(
                        errorCode,
                        EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE.name(),
                        errorInstanceId,
                        new EndpointSpecificTwoErrors.DifferentNamespaceParameters());
            }
        }

        final class DifferentPackage
                extends EndpointError<com.palantir.another.EndpointSpecificErrors.DifferentPackageParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            DifferentPackage(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId) {
                super(
                        errorCode,
                        com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE.name(),
                        errorInstanceId,
                        new com.palantir.another.EndpointSpecificErrors.DifferentPackageParameters());
            }
        }
    }

    sealed interface TestEmptyBodyResponse
            permits TestEmptyBodyResponse.Success, TestEmptyBodyResponse.InvalidArgument {
        record Success() implements TestEmptyBodyResponse {}

        final class InvalidArgument extends EndpointError<TestErrors.InvalidArgumentParameters>
                implements TestEmptyBodyResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            InvalidArgument(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestBinaryResponse extends Closeable
            permits TestBinaryResponse.Success, TestBinaryResponse.InvalidArgument {
        @Override
        default void close() throws IOException {}

        record Success(@MustBeClosed @JsonValue InputStream value) implements TestBinaryResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }

            @Override
            public void close() throws IOException {
                value.close();
            }
        }

        final class InvalidArgument extends EndpointError<TestErrors.InvalidArgumentParameters>
                implements TestBinaryResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            InvalidArgument(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestOptionalBinaryResponse extends Closeable
            permits TestOptionalBinaryResponse.Success, TestOptionalBinaryResponse.InvalidArgument {
        @Override
        default void close() throws IOException {}

        record Success(@JsonValue Optional<InputStream> value) implements TestOptionalBinaryResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }

            @Override
            public void close() throws IOException {
                if (value.isPresent()) {
                    value.get().close();
                }
            }
        }

        final class InvalidArgument extends EndpointError<TestErrors.InvalidArgumentParameters>
                implements TestOptionalBinaryResponse {
            @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
            InvalidArgument(
                    @JsonProperty("errorCode") @Safe String errorCode,
                    @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }
}
