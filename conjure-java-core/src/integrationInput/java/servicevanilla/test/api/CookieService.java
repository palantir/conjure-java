package servicevanilla.test.api;

import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.tokens.auth.BearerToken;

@ConjureGenerated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface CookieService {
    /** @apiNote {@code GET /cookies} */
    void eatCookies(BearerToken token);
}
