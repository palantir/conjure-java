package com.palantir.product;

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
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(EmptyPathServiceBlocking.Factory.class)
public interface EmptyPathServiceBlocking {
    /**
     * @apiNote {@code GET /}
     */
    boolean emptyPath();

    /**
     * Creates a synchronous/blocking client for a EmptyPathService service.
     */
    static EmptyPathServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new EmptyPathServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel emptyPathChannel =
                    _endpointChannelFactory.endpoint(DialogueEmptyPathEndpoints.emptyPath);

            private final Deserializer<Boolean> emptyPathDeserializer =
                    _runtime.bodySerDe().deserializer(new TypeMarker<Boolean>() {});

            @Override
            public boolean emptyPath() {
                Request.Builder _request = Request.builder();
                return _runtime.clients().callBlocking(emptyPathChannel, _request.build(), emptyPathDeserializer);
            }

            @Override
            public String toString() {
                return "EmptyPathServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /**
     * Creates an asynchronous/non-blocking client for a EmptyPathService service.
     */
    static EmptyPathServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<EmptyPathServiceBlocking> {
        @Override
        public EmptyPathServiceBlocking create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return EmptyPathServiceBlocking.of(endpointChannelFactory, runtime);
        }
    }
}
