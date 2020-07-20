package com.palantir.product;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface EmptyPathServiceBlocking {
    /**
     * @apiNote {@code GET /}
     */
    boolean emptyPath();

    /**
     * Creates a synchronous/blocking client for a EmptyPathService service.
     */
    static EmptyPathServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        EmptyPathServiceAsync delegate = EmptyPathServiceAsync.of(_endpointChannelFactory, _runtime);
        return new EmptyPathServiceBlocking() {
            @Override
            public boolean emptyPath() {
                return _runtime.clients().block(delegate.emptyPath());
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
}
