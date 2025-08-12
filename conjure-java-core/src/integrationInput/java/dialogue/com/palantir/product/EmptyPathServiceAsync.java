package dialogue.com.palantir.product;

import com.google.common.util.concurrent.ListenableFuture;
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
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(EmptyPathServiceAsync.Factory.class)
public interface EmptyPathServiceAsync {
    /** @apiNote {@code GET /} */
    @ClientEndpoint(method = "GET", path = "/")
    ListenableFuture<Boolean> emptyPath();

    /** Creates an asynchronous/non-blocking client for a EmptyPathService service. */
    static EmptyPathServiceAsync of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EmptyPathServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel emptyPathChannel =
                    _endpointChannelFactory.endpoint(DialogueEmptyPathEndpoints.emptyPath);

            private final Deserializer<Boolean> emptyPathDeserializer = _runtime.bodySerDe()
                    .deserializer(DeserializerArgs.<Boolean>builder()
                            .baseType(new TypeMarker<>() {})
                            .success(new TypeMarker<Boolean>() {})
                            .exception(
                                    ConjureErrors.CONFLICTING_CAUSE_SAFE_ARG.name(),
                                    new TypeMarker<ConjureErrors.ConflictingCauseSafeArgSerializableError>() {},
                                    new TypeMarker<ConjureErrors.ConflictingCauseSafeArgException>() {})
                            .exception(
                                    ConjureErrors.CONFLICTING_CAUSE_UNSAFE_ARG.name(),
                                    new TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgSerializableError>() {},
                                    new TypeMarker<ConjureErrors.ConflictingCauseUnsafeArgException>() {})
                            .exception(
                                    ConjureErrors.ERROR_WITH_COMPLEX_ARGS.name(),
                                    new TypeMarker<ConjureErrors.ErrorWithComplexArgsSerializableError>() {},
                                    new TypeMarker<ConjureErrors.ErrorWithComplexArgsException>() {})
                            .exception(
                                    ConjureErrors.INVALID_SERVICE_DEFINITION.name(),
                                    new TypeMarker<ConjureErrors.InvalidServiceDefinitionSerializableError>() {},
                                    new TypeMarker<ConjureErrors.InvalidServiceDefinitionException>() {})
                            .exception(
                                    ConjureErrors.INVALID_TYPE_DEFINITION.name(),
                                    new TypeMarker<ConjureErrors.InvalidTypeDefinitionSerializableError>() {},
                                    new TypeMarker<ConjureErrors.InvalidTypeDefinitionException>() {})
                            .exception(
                                    ConjureJavaErrors.JAVA_COMPILATION_FAILED.name(),
                                    new TypeMarker<ConjureJavaErrors.JavaCompilationFailedSerializableError>() {},
                                    new TypeMarker<ConjureJavaErrors.JavaCompilationFailedException>() {})
                            .build());

            @Override
            public ListenableFuture<Boolean> emptyPath() {
                Request.Builder _request = Request.builder();
                if (_runtime.bodySerDe().errorParameterDeserializationFormat().isPresent()) {
                    _request.putHeaderParams(
                            "Accept-Conjure-Error-Parameter-Format",
                            _runtime.bodySerDe()
                                    .errorParameterDeserializationFormat()
                                    .get()
                                    .toString());
                }
                return _runtime.clients().call(emptyPathChannel, _request.build(), emptyPathDeserializer);
            }

            @Override
            public String toString() {
                return "EmptyPathServiceAsync{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /** Creates an asynchronous/non-blocking client for a EmptyPathService service. */
    static EmptyPathServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<EmptyPathServiceAsync> {
        @Override
        public EmptyPathServiceAsync create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return EmptyPathServiceAsync.of(endpointChannelFactory, runtime);
        }
    }
}
