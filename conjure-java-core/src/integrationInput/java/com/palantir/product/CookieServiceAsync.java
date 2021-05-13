package test.api;

import com.google.common.util.concurrent.ListenableFuture;
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
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
@DialogueService(CookieServiceAsync.Factory.class)
public interface CookieServiceAsync {
    /**
     * @apiNote {@code GET /cookies}
     */
    @ClientEndpoint(method = "GET", path = "/cookies")
    ListenableFuture<Void> eatCookies(BearerToken token);

    /**
     * Creates a client instance that provides fair request execution.
     */
    CookieServiceAsync createSession();

    /**
     * Creates an asynchronous/non-blocking client for a CookieService service.
     */
    static CookieServiceAsync of(EndpointChannelFactory _endpointChannelFactory, ConjureRuntime _runtime) {
        return new CookieServiceAsyncImpl(
                _endpointChannelFactory, _endpointChannelFactory.scoped(UUID::randomUUID), _runtime);
    }

    /**
     * Creates an asynchronous/non-blocking client for a CookieService service.
     */
    static CookieServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
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

    final class Factory implements DialogueServiceFactory<CookieServiceAsync> {
        @Override
        public CookieServiceAsync create(EndpointChannelFactory endpointChannelFactory, ConjureRuntime runtime) {
            return CookieServiceAsync.of(endpointChannelFactory, runtime);
        }
    }

    final class CookieServiceAsyncImpl implements CookieServiceAsync {

        private final EndpointChannelFactory _endpointChannelFactory;

        private final ConjureRuntime _runtime;

        private final PlainSerDe _plainSerDe;

        private final EndpointChannel eatCookiesChannel;

        private final Deserializer<Void> eatCookiesDeserializer;

        public CookieServiceAsyncImpl(
                EndpointChannelFactory _endpointChannelFactory,
                EndpointChannelFactory _scopedEndpointChannelFactory,
                ConjureRuntime _runtime) {
            this._endpointChannelFactory = _endpointChannelFactory;
            this._runtime = _runtime;
            this._plainSerDe = _runtime.plainSerDe();
            this.eatCookiesChannel =
                    _scopedEndpointChannelFactory.endpoint(test.api.DialogueCookieEndpoints.eatCookies);
            this.eatCookiesDeserializer = _runtime.bodySerDe().emptyBodyDeserializer();
        }

        @Override
        public ListenableFuture<Void> eatCookies(BearerToken token) {
            Request.Builder _request = Request.builder();
            _request.putHeaderParams("Cookie", "PALANTIR_TOKEN=" + _plainSerDe.serializeBearerToken(token));
            return _runtime.clients().call(eatCookiesChannel, _request.build(), eatCookiesDeserializer);
        }

        @Override
        public CookieServiceAsync createSession() {
            UUID session = UUID.randomUUID();
            return new CookieServiceAsyncImpl(
                    _endpointChannelFactory, _endpointChannelFactory.scoped(UUID::randomUUID), _runtime);
        }

        @Override
        public String toString() {
            return "CookieServiceAsync{_endpointChannelFactory=" + _endpointChannelFactory + ", runtime=" + _runtime
                    + '}';
        }
    }
}
