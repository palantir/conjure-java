package test.api;

import com.palantir.dialogue.Channel;
import com.palantir.dialogue.ConjureRuntime;
import com.palantir.tokens.auth.BearerToken;
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
    static CookieServiceBlocking of(Channel _channel, ConjureRuntime _runtime) {
        test.api.CookieServiceAsync delegate = test.api.CookieServiceAsync.of(_channel, _runtime);
        return new CookieServiceBlocking() {
            @Override
            public void eatCookies(BearerToken token) {
                _runtime.clients().block(delegate.eatCookies(token));
            }

            @Override
            public String toString() {
                return "CookieServiceBlocking{channel=" + _channel + ", runtime=" + _runtime + '}';
            }
        };
    }
}
