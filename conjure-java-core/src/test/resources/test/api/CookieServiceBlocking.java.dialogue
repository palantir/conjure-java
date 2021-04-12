package test.api;

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
import com.palantir.tokens.auth.BearerToken;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(CookieServiceBlocking.Factory.class)
public interface CookieServiceBlocking {
    /**
     * @apiNote {@code GET /cookies}
     */
    @ClientEndpoint(method = "GET", path = "/cookies")
    void eatCookies(BearerToken token);

    /**
     * Creates a synchronous/blocking client for a CookieService service.
     */
    static CookieServiceBlocking of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new CookieServiceBlocking() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel eatCookiesChannel =
                    _endpointChannelFactory.endpoint(DialogueCookieEndpoints.eatCookies);

            private final Deserializer<Void> eatCookiesDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            @Override
            public void eatCookies(BearerToken token) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Cookie", "PALANTIR_TOKEN=" + _plainSerDe.serializeBearerToken(token));
                _runtime.clients().callBlocking(eatCookiesChannel, _request.build(), eatCookiesDeserializer);
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
