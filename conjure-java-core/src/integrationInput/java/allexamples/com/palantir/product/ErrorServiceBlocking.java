package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureErrors;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DeserializerArgs;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Override;
import java.lang.String;
import java.util.Optional;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(ErrorServiceBlocking.Factory.class)
public interface ErrorServiceBlocking {
    /** @apiNote {@code GET /base/basic} */
    @ClientEndpoint(method = "GET", path = "/base/basic")
    TestBasicErrorResponse testBasicError(AuthHeader authHeader);

    /** @apiNote {@code GET /base/imported} */
    @ClientEndpoint(method = "GET", path = "/base/imported")
    TestImportedErrorResponse testImportedError(AuthHeader authHeader);

    /** @apiNote {@code GET /base/multiple} */
    @ClientEndpoint(method = "GET", path = "/base/multiple")
    TestMultipleErrorsAndPackagesResponse testMultipleErrorsAndPackages(AuthHeader authHeader);

    /** @apiNote {@code GET /base/empty} */
    @ClientEndpoint(method = "GET", path = "/base/empty")
    TestEmptyBodyResponse testEmptyBody(AuthHeader authHeader);

    /** @apiNote {@code GET /base/binary} */
    @ClientEndpoint(method = "GET", path = "/base/binary")
    TestBinaryResponse testBinary(AuthHeader authHeader);

    /** @apiNote {@code GET /base/optional-binary} */
    @ClientEndpoint(method = "GET", path = "/base/optional-binary")
    TestOptionalBinaryResponse testOptionalBinary(AuthHeader authHeader);

    /** Creates a synchronous/blocking client for a ErrorService service. */
    static ErrorServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new ErrorServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

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
            public TestBasicErrorResponse testBasicError(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(testBasicErrorChannel, _request.build(), testBasicErrorDeserializer);
            }

            @Override
            public TestImportedErrorResponse testImportedError(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(testImportedErrorChannel, _request.build(), testImportedErrorDeserializer);
            }

            @Override
            public TestMultipleErrorsAndPackagesResponse testMultipleErrorsAndPackages(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(
                                testMultipleErrorsAndPackagesChannel,
                                _request.build(),
                                testMultipleErrorsAndPackagesDeserializer);
            }

            @Override
            public TestEmptyBodyResponse testEmptyBody(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(testEmptyBodyChannel, _request.build(), testEmptyBodyDeserializer);
            }

            @Override
            public TestBinaryResponse testBinary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients().callBlocking(testBinaryChannel, _request.build(), testBinaryDeserializer);
            }

            @Override
            public TestOptionalBinaryResponse testOptionalBinary(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(testOptionalBinaryChannel, _request.build(), testOptionalBinaryDeserializer);
            }

            @Override
            public String toString() {
                return "ErrorServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a ErrorService service. */
    static ErrorServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<ErrorServiceBlocking> {
        @Override
        public ErrorServiceBlocking create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return ErrorServiceBlocking.of(endpointChannelFactory, runtime);
        }
    }

    sealed interface TestBasicErrorResponse
            permits TestBasicErrorResponse.Success, TestBasicErrorResponse.InvalidArgument {
        record Success(String value) implements TestBasicErrorResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class InvalidArgument extends ConjureErrors.BaseEndpointError<TestErrors.InvalidArgumentParameters>
                implements TestBasicErrorResponse {
            @JsonCreator
            InvalidArgument(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestImportedErrorResponse
            permits TestImportedErrorResponse.Success, TestImportedErrorResponse.EndpointError {
        record Success(String value) implements TestImportedErrorResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class EndpointError
                extends ConjureErrors.BaseEndpointError<EndpointSpecificErrors.EndpointErrorParameters>
                implements TestImportedErrorResponse {
            @JsonCreator
            EndpointError(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
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
        record Success(String value) implements TestMultipleErrorsAndPackagesResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class InvalidArgument extends ConjureErrors.BaseEndpointError<TestErrors.InvalidArgumentParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator
            InvalidArgument(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }

        final class NotFound extends ConjureErrors.BaseEndpointError<TestErrors.NotFoundParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator
            NotFound(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.NotFoundParameters parameters) {
                super(errorCode, TestErrors.NOT_FOUND.name(), errorInstanceId, parameters);
            }
        }

        final class DifferentNamespace extends ConjureErrors.BaseEndpointError<ConjureErrors.EmptyErrorParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator
            DifferentNamespace(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId) {
                super(
                        errorCode,
                        EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE.name(),
                        errorInstanceId,
                        ConjureErrors.EMPTY_ERROR_PARAMETERS_INSTANCE);
            }
        }

        final class DifferentPackage extends ConjureErrors.BaseEndpointError<ConjureErrors.EmptyErrorParameters>
                implements TestMultipleErrorsAndPackagesResponse {
            @JsonCreator
            DifferentPackage(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId) {
                super(
                        errorCode,
                        com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE.name(),
                        errorInstanceId,
                        ConjureErrors.EMPTY_ERROR_PARAMETERS_INSTANCE);
            }
        }
    }

    sealed interface TestEmptyBodyResponse
            permits TestEmptyBodyResponse.Success, TestEmptyBodyResponse.InvalidArgument {
        record Success() implements TestEmptyBodyResponse {
            @JsonCreator
            public static Success create() {
                return new Success();
            }
        }

        final class InvalidArgument extends ConjureErrors.BaseEndpointError<TestErrors.InvalidArgumentParameters>
                implements TestEmptyBodyResponse {
            @JsonCreator
            InvalidArgument(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestBinaryResponse permits TestBinaryResponse.Success, TestBinaryResponse.InvalidArgument {
        record Success(@MustBeClosed InputStream value) implements TestBinaryResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class InvalidArgument extends ConjureErrors.BaseEndpointError<TestErrors.InvalidArgumentParameters>
                implements TestBinaryResponse {
            @JsonCreator
            InvalidArgument(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }

    sealed interface TestOptionalBinaryResponse
            permits TestOptionalBinaryResponse.Success, TestOptionalBinaryResponse.InvalidArgument {
        record Success(Optional<InputStream> value) implements TestOptionalBinaryResponse {
            public Success {
                Preconditions.checkArgumentNotNull(value, "value cannot be null");
            }
        }

        final class InvalidArgument extends ConjureErrors.BaseEndpointError<TestErrors.InvalidArgumentParameters>
                implements TestOptionalBinaryResponse {
            @JsonCreator
            InvalidArgument(
                    @JsonProperty("errorCode") String errorCode,
                    @JsonProperty("errorInstanceId") String errorInstanceId,
                    @JsonProperty("parameters") TestErrors.InvalidArgumentParameters parameters) {
                super(errorCode, TestErrors.INVALID_ARGUMENT.name(), errorInstanceId, parameters);
            }
        }
    }
}
