package test.api;

import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.dialogue.Deserializer;
import com.palantir.dialogue.EndpointChannel;
import com.palantir.dialogue.PlainSerDe;
import com.palantir.dialogue.Request;
import com.palantir.tokens.auth.BearerToken;
import java.lang.Override;
import java.lang.String;
import java.lang.Void;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.dialogue.DialogueInterfaceGenerator")
public interface CookieServiceAsync {
    /**
     * @apiNote {@code GET /cookies}
     */
    ListenableFuture<Void> eatCookies(BearerToken token);

    /**
     * Creates an asynchronous/non-blocking client for a CookieService service.
     */
    static CookieServiceAsync of(Channel _channel, ConjureRuntime _runtime) {
        return new CookieServiceAsync() {
            private final PlainSerDe _plainSerDe = _runtime.plainSerDe();

            private final EndpointChannel eatCookiesChannel =
                    _runtime.clients().bind(_channel, DialogueCookieEndpoints.eatCookies);

            private final Deserializer<Void> eatCookiesDeserializer =
                    _runtime.bodySerDe().emptyBodyDeserializer();

            @Override
            public ListenableFuture<Void> eatCookies(BearerToken token) {
                Request.Builder _request = Request.builder();
                _request.putHeaderParams("Cookie", "PALANTIR_TOKEN=" + _plainSerDe.serializeBearerToken(token));
                return _runtime.clients().call(eatCookiesChannel, _request.build(), eatCookiesDeserializer);
            }

            @Override
            public String toString() {
                return "CookieServiceBlocking{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
