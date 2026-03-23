package exceptionthrowingdialogueinterfaces.test.api;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
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
import com.palantir.dialogue.TypeMarker;
import com.palantir.tokens.auth.BearerToken;
import exceptionthrowingdialogueinterfaces.com.palantir.another.ConjureErrors;
import exceptionthrowingdialogueinterfaces.com.palantir.another.ConjureErrorsTypeMarkers;
import exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureJavaErrors;
import exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureJavaErrorsTypeMarkers;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(CookieServiceBlocking.Factory.class)
public interface CookieServiceBlocking {
    /** @apiNote {@code GET /cookies} */
    @ClientEndpoint(method = "GET", path = "/cookies")
    void eatCookies(BearerToken token);

    /** Creates a synchronous/blocking client for a CookieService service. */
    static CookieServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new CookieServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel eatCookiesChannel =
                    _endpointChannelFactory.endpoint(DialogueCookieEndpoints.eatCookies);

            private final Deserializer<Void> eatCookiesDeserializer = _runtime.bodySerDe()
                    .emptyBodyDeserializer(createExceptionDeserializerArgs(new TypeMarker<Void>() {}));

            private <T> ExceptionDeserializerArgs<T> createExceptionDeserializerArgs(TypeMarker<T> returnType) {
                return ExceptionDeserializerArgs.<T>builder()
                        .returnType(returnType)
                        .exception(
                                ConjureErrors.DIFFERENT_PACKAGE_ERROR.name(),
                                ConjureErrorsTypeMarkers.DifferentPackageErrorSerializableError,
                                ConjureErrorsTypeMarkers.DifferentPackageErrorException)
                        .exception(
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrors
                                        .CONFLICTING_CAUSE_SAFE_ARG
                                        .name(),
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .ConflictingCauseSafeArgSerializableError,
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .ConflictingCauseSafeArgException)
                        .exception(
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrors
                                        .CONFLICTING_CAUSE_UNSAFE_ARG
                                        .name(),
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .ConflictingCauseUnsafeArgSerializableError,
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .ConflictingCauseUnsafeArgException)
                        .exception(
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrors
                                        .ERROR_WITH_COMPLEX_ARGS
                                        .name(),
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .ErrorWithComplexArgsSerializableError,
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .ErrorWithComplexArgsException)
                        .exception(
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrors
                                        .INVALID_SERVICE_DEFINITION
                                        .name(),
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .InvalidServiceDefinitionSerializableError,
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .InvalidServiceDefinitionException)
                        .exception(
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrors
                                        .INVALID_TYPE_DEFINITION
                                        .name(),
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .InvalidTypeDefinitionSerializableError,
                                exceptionthrowingdialogueinterfaces.com.palantir.product.ConjureErrorsTypeMarkers
                                        .InvalidTypeDefinitionException)
                        .exception(
                                ConjureJavaErrors.JAVA_COMPILATION_FAILED.name(),
                                ConjureJavaErrorsTypeMarkers.JavaCompilationFailedSerializableError,
                                ConjureJavaErrorsTypeMarkers.JavaCompilationFailedException)
                        .build();
            }

            @Override
            public void eatCookies(BearerToken token) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Cookie", "PALANTIR_TOKEN=" + _plainSerDe.serializeBearerToken(token));
                if (_runtime.bodySerDe().errorParameterFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe().errorParameterFormat().get().toString());
                }
                _runtime.clients().callBlocking(eatCookiesChannel, _request.build(), eatCookiesDeserializer);
            }

            @Override
            public String toString() {
                return "CookieServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a CookieService service. */
    static CookieServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<CookieServiceBlocking> {
        @Override
        public CookieServiceBlocking create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return CookieServiceBlocking.of(endpointChannelFactory, runtime);
        }
    }
}
