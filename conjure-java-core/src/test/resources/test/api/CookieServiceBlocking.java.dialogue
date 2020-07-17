package test.api;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Endpoint;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.EndpointChannelFactory;
import com.palantir.tokens.auth.BearerToken;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface CookieServiceBlocking {
    /**
     * @apiNote {@code GET /cookies}
     */
    void eatCookies(BearerToken token);

    /**
     * Creates a synchronous/blocking client for a CookieService service.
     */
    static CookieServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        CookieServiceAsync delegate = CookieServiceAsync.of(_endpointChannelFactory, _runtime);
        return new CookieServiceBlocking() {
            @Override
            public void eatCookies(BearerToken token) {
                _runtime.clients().block(delegate.eatCookies(token));
            }

            @Override
            public String toString() {
                return "CookieServiceBlocking{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime="
                        + _runtime + '}';
            }
        };
    }

    /**
     * Creates an asynchronous/non-blocking client for a CookieService service.
     */
    static CookieServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        if (_channel instanceof EndpointChannelFactory) {
            return of((EndpointChannelFactory) _channel, _runtime);
        }
        return of(
                new EndpointChannelFactory() {
                    @Override
                    public EndpointChannel endpoint(Endpoint endpoint) {
                        return _runtime.clients().bind(_endpointChannelFactory, endpoint);
                    }
                },
                _runtime);
    }
}
