package endpointerrors.com.palantir.product;

import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.ExceptionDeserializerArgs;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.Serializer;
import com.palantir.dialogue.TypeMarker;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import java.util.Optional;

@ConjureGenerated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(ErrorServiceBlocking.Factory.class)
public interface ErrorServiceBlocking {
    /** @apiNote {@code POST /errors/basic} */
    @ClientEndpoint(method = "POST", path = "/errors/basic")
    String testBasicError(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/imported} */
    @ClientEndpoint(method = "POST", path = "/errors/imported")
    String testImportedError(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/multiple} */
    @ClientEndpoint(method = "POST", path = "/errors/multiple")
    String testMultipleErrorsAndPackages(AuthHeader authHeader, Optional<String> errorToThrow);

    /** @apiNote {@code POST /errors/empty} */
    @ClientEndpoint(method = "POST", path = "/errors/empty")
    void testEmptyBody(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/binary} */
    @ClientEndpoint(method = "POST", path = "/errors/binary")
    @MustBeClosed
    InputStream testBinary(AuthHeader authHeader, boolean shouldThrowError);

    /** @apiNote {@code POST /errors/optional-binary} */
    @ClientEndpoint(method = "POST", path = "/errors/optional-binary")
    Optional<InputStream> testOptionalBinary(AuthHeader authHeader, OptionalBinaryResponseMode mode);

    /** Creates a synchronous/blocking client for a ErrorService service. */
    static ErrorServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new ErrorServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final Serializer<Boolean> testBasicErrorSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testBasicErrorChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testBasicError);

            private final Deserializer<String> testBasicErrorDeserializer =
                    _runtime.bodySerDe().deserializer(createExceptionDeserializerArgs(new TypeMarker<String>() {}));

            private final Serializer<Boolean> testImportedErrorSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testImportedErrorChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testImportedError);

            private final Deserializer<String> testImportedErrorDeserializer =
                    _runtime.bodySerDe().deserializer(createExceptionDeserializerArgs(new TypeMarker<String>() {}));

            private final Serializer<Optional<String>> testMultipleErrorsAndPackagesSerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Optional<String>>() {});

            private final EndpointChannel testMultipleErrorsAndPackagesChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testMultipleErrorsAndPackages);

            private final Deserializer<String> testMultipleErrorsAndPackagesDeserializer =
                    _runtime.bodySerDe().deserializer(createExceptionDeserializerArgs(new TypeMarker<String>() {}));

            private final Serializer<Boolean> testEmptyBodySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testEmptyBodyChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testEmptyBody);

            private final Deserializer<Void> testEmptyBodyDeserializer = _runtime.bodySerDe()
                    .emptyBodyDeserializer(createExceptionDeserializerArgs(new TypeMarker<Void>() {}));

            private final Serializer<Boolean> testBinarySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<Boolean>() {});

            private final EndpointChannel testBinaryChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testBinary);

            private final Deserializer<InputStream> testBinaryDeserializer = _runtime.bodySerDe()
                    .inputStreamDeserializer(createExceptionDeserializerArgs(new TypeMarker<InputStream>() {}));

            private final Serializer<OptionalBinaryResponseMode> testOptionalBinarySerializer =
                    _runtime.bodySerDe().serializer(new TypeMarker<OptionalBinaryResponseMode>() {});

            private final EndpointChannel testOptionalBinaryChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testOptionalBinary);

            private final Deserializer<Optional<InputStream>> testOptionalBinaryDeserializer = _runtime.bodySerDe()
                    .optionalInputStreamDeserializer(
                            createExceptionDeserializerArgs(new TypeMarker<Optional<InputStream>>() {}));

            private <T> ExceptionDeserializerArgs<T> createExceptionDeserializerArgs(TypeMarker<T> returnType) {
                return ExceptionDeserializerArgs.<T>builder()
                        .returnType(returnType)
                        .exception(
                                endpointerrors.com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE.name(),
                                new TypeMarker<
                                        endpointerrors.com.palantir.another.EndpointSpecificErrors
                                                .DifferentPackageSerializableError>() {},
                                new TypeMarker<
                                        endpointerrors.com.palantir.another.EndpointSpecificErrors
                                                .DifferentPackageException>() {})
                        .exception(
                                TestErrors.COMPLICATED_PARAMETERS.name(),
                                new TypeMarker<TestErrors.ComplicatedParametersSerializableError>() {},
                                new TypeMarker<TestErrors.ComplicatedParametersException>() {})
                        .exception(
                                ConjureErrors.CONFLICTING_CAUSE_SAFE_ARG_ERR.name(),
                                new TypeMarker<ConjureErrors.ConflictingCauseSafeArgErrSerializableError>() {},
                                new TypeMarker<ConjureErrors.ConflictingCauseSafeArgErrException>() {})
                        .exception(
                                EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE.name(),
                                new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceSerializableError>() {},
                                new TypeMarker<EndpointSpecificTwoErrors.DifferentNamespaceException>() {})
                        .exception(
                                EndpointSpecificErrors.ENDPOINT_ERROR.name(),
                                new TypeMarker<EndpointSpecificErrors.EndpointErrorSerializableError>() {},
                                new TypeMarker<EndpointSpecificErrors.EndpointErrorException>() {})
                        .exception(
                                TestErrors.INVALID_ARGUMENT.name(),
                                new TypeMarker<TestErrors.InvalidArgumentSerializableError>() {},
                                new TypeMarker<TestErrors.InvalidArgumentException>() {})
                        .exception(
                                TestErrors.NOT_FOUND.name(),
                                new TypeMarker<TestErrors.NotFoundSerializableError>() {},
                                new TypeMarker<TestErrors.NotFoundException>() {})
                        .build();
            }

            @Override
            public String testBasicError(AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testBasicErrorSerializer.serialize(shouldThrowError));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .callBlocking(testBasicErrorChannel, _request.build(), testBasicErrorDeserializer);
            }

            @Override
            public String testImportedError(AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testImportedErrorSerializer.serialize(shouldThrowError));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .callBlocking(testImportedErrorChannel, _request.build(), testImportedErrorDeserializer);
            }

            @Override
            public String testMultipleErrorsAndPackages(AuthHeader authHeader, Optional<String> errorToThrow) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testMultipleErrorsAndPackagesSerializer.serialize(errorToThrow));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients()
                        .callBlocking(
                                testMultipleErrorsAndPackagesChannel,
                                _request.build(),
                                testMultipleErrorsAndPackagesDeserializer);
            }

            @Override
            public void testEmptyBody(AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testEmptyBodySerializer.serialize(shouldThrowError));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                _runtime.clients().callBlocking(testEmptyBodyChannel, _request.build(), testEmptyBodyDeserializer);
            }

            @Override
            public InputStream testBinary(AuthHeader authHeader, boolean shouldThrowError) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testBinarySerializer.serialize(shouldThrowError));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                return _runtime.clients().callBlocking(testBinaryChannel, _request.build(), testBinaryDeserializer);
            }

            @Override
            public Optional<InputStream> testOptionalBinary(AuthHeader authHeader, OptionalBinaryResponseMode mode) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                _request.body(testOptionalBinarySerializer.serialize(mode));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
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

    sealed interface TestBasicErrorError
            permits TestBasicErrorError.InvalidArgument,
                    TestBasicErrorError.ConflictingCauseSafeArgErr,
                    TestBasicErrorError.Unknown {
        static TestBasicErrorError from(RemoteException e) {
            if (TestErrors.isInvalidArgument(e)) {
                return new InvalidArgument((TestErrors.InvalidArgumentException) e);
            } else if (ConjureErrors.isConflictingCauseSafeArgErr(e)) {
                return new ConflictingCauseSafeArgErr((ConjureErrors.ConflictingCauseSafeArgErrException) e);
            } else {
                return new Unknown(e);
            }
        }

        record InvalidArgument(TestErrors.InvalidArgumentException exception) implements TestBasicErrorError {}

        record ConflictingCauseSafeArgErr(ConjureErrors.ConflictingCauseSafeArgErrException exception)
                implements TestBasicErrorError {}

        record Unknown(RemoteException exception) implements TestBasicErrorError {}
    }

    sealed interface TestImportedErrorError
            permits TestImportedErrorError.EndpointError, TestImportedErrorError.Unknown {
        static TestImportedErrorError from(RemoteException e) {
            if (EndpointSpecificErrors.isEndpointError(e)) {
                return new EndpointError((EndpointSpecificErrors.EndpointErrorException) e);
            } else {
                return new Unknown(e);
            }
        }

        record EndpointError(EndpointSpecificErrors.EndpointErrorException exception)
                implements TestImportedErrorError {}

        record Unknown(RemoteException exception) implements TestImportedErrorError {}
    }

    sealed interface TestMultipleErrorsAndPackagesError
            permits TestMultipleErrorsAndPackagesError.InvalidArgument,
                    TestMultipleErrorsAndPackagesError.NotFound,
                    TestMultipleErrorsAndPackagesError.DifferentNamespace,
                    TestMultipleErrorsAndPackagesError.DifferentPackage,
                    TestMultipleErrorsAndPackagesError.ComplicatedParameters,
                    TestMultipleErrorsAndPackagesError.Unknown {
        static TestMultipleErrorsAndPackagesError from(RemoteException e) {
            if (TestErrors.isInvalidArgument(e)) {
                return new InvalidArgument((TestErrors.InvalidArgumentException) e);
            } else if (TestErrors.isNotFound(e)) {
                return new NotFound((TestErrors.NotFoundException) e);
            } else if (EndpointSpecificTwoErrors.isDifferentNamespace(e)) {
                return new DifferentNamespace((EndpointSpecificTwoErrors.DifferentNamespaceException) e);
            } else if (endpointerrors.com.palantir.another.EndpointSpecificErrors.isDifferentPackage(e)) {
                return new DifferentPackage(
                        (endpointerrors.com.palantir.another.EndpointSpecificErrors.DifferentPackageException) e);
            } else if (TestErrors.isComplicatedParameters(e)) {
                return new ComplicatedParameters((TestErrors.ComplicatedParametersException) e);
            } else {
                return new Unknown(e);
            }
        }

        record InvalidArgument(TestErrors.InvalidArgumentException exception)
                implements TestMultipleErrorsAndPackagesError {}

        record NotFound(TestErrors.NotFoundException exception) implements TestMultipleErrorsAndPackagesError {}

        record DifferentNamespace(EndpointSpecificTwoErrors.DifferentNamespaceException exception)
                implements TestMultipleErrorsAndPackagesError {}

        record DifferentPackage(
                endpointerrors.com.palantir.another.EndpointSpecificErrors.DifferentPackageException exception)
                implements TestMultipleErrorsAndPackagesError {}

        record ComplicatedParameters(TestErrors.ComplicatedParametersException exception)
                implements TestMultipleErrorsAndPackagesError {}

        record Unknown(RemoteException exception) implements TestMultipleErrorsAndPackagesError {}
    }

    sealed interface TestEmptyBodyError permits TestEmptyBodyError.InvalidArgument, TestEmptyBodyError.Unknown {
        static TestEmptyBodyError from(RemoteException e) {
            if (TestErrors.isInvalidArgument(e)) {
                return new InvalidArgument((TestErrors.InvalidArgumentException) e);
            } else {
                return new Unknown(e);
            }
        }

        record InvalidArgument(TestErrors.InvalidArgumentException exception) implements TestEmptyBodyError {}

        record Unknown(RemoteException exception) implements TestEmptyBodyError {}
    }

    sealed interface TestBinaryError permits TestBinaryError.InvalidArgument, TestBinaryError.Unknown {
        static TestBinaryError from(RemoteException e) {
            if (TestErrors.isInvalidArgument(e)) {
                return new InvalidArgument((TestErrors.InvalidArgumentException) e);
            } else {
                return new Unknown(e);
            }
        }

        record InvalidArgument(TestErrors.InvalidArgumentException exception) implements TestBinaryError {}

        record Unknown(RemoteException exception) implements TestBinaryError {}
    }

    sealed interface TestOptionalBinaryError
            permits TestOptionalBinaryError.InvalidArgument, TestOptionalBinaryError.Unknown {
        static TestOptionalBinaryError from(RemoteException e) {
            if (TestErrors.isInvalidArgument(e)) {
                return new InvalidArgument((TestErrors.InvalidArgumentException) e);
            } else {
                return new Unknown(e);
            }
        }

        record InvalidArgument(TestErrors.InvalidArgumentException exception) implements TestOptionalBinaryError {}

        record Unknown(RemoteException exception) implements TestOptionalBinaryError {}
    }
}
