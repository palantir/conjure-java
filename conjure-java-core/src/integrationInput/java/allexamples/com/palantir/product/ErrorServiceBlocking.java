package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ClientEndpoint;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.DialogueService;
import com.palantir.dialogue.DialogueServiceFactory;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.dialogue.TypeMarker;
import com.palantir.tokens.auth.AuthHeader;
import java.lang.Override;
import java.lang.String;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(ErrorServiceBlocking.Factory.class)
public interface ErrorServiceBlocking {
    /** @apiNote {@code GET /base/basic} */
    @ClientEndpoint(method = "GET", path = "/base/basic")
    String testBasicError(AuthHeader authHeader);

    /** @apiNote {@code GET /base/imported} */
    @ClientEndpoint(method = "GET", path = "/base/imported")
    String testImportedError(AuthHeader authHeader);

    /** @apiNote {@code GET /base/multiple} */
    @ClientEndpoint(method = "GET", path = "/base/multiple")
    String testMultipleErrorsAndPackages(AuthHeader authHeader);

    /** Creates a synchronous/blocking client for a ErrorService service. */
    static ErrorServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new ErrorServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel testBasicErrorChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testBasicError);

            private final Deserializer<String> testBasicErrorDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final EndpointChannel testImportedErrorChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testImportedError);

            private final Deserializer<String> testImportedErrorDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            private final EndpointChannel testMultipleErrorsAndPackagesChannel =
                    _endpointChannelFactory.endpoint(DialogueErrorEndpoints.testMultipleErrorsAndPackages);

            private final Deserializer<String> testMultipleErrorsAndPackagesDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<String>() {});

            @Override
            public String testBasicError(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(testBasicErrorChannel, _request.build(), testBasicErrorDeserializer);
            }

            @Override
            public String testImportedError(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(testImportedErrorChannel, _request.build(), testImportedErrorDeserializer);
            }

            @Override
            public String testMultipleErrorsAndPackages(AuthHeader authHeader) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Authorization", authHeader.toString());
                return _runtime.clients()
                        .callBlocking(
                                testMultipleErrorsAndPackagesChannel,
                                _request.build(),
                                testMultipleErrorsAndPackagesDeserializer);
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
}
