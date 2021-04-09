package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import com.palantir.tokens.auth.AuthHeader;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
@ConjureService(name = "AliasTestService", package_ = "com.palantir.product")
public interface UndertowAliasTestService {
    /**
     * @apiNote {@code GET /alias/test}
     */
    @ConjureEndpoint(path = "/alias/test", method = "GET")
    void testOptionalAliasQueryParams(
            AuthHeader authHeader,
            Optional<AliasedInteger> optionalAliasInt,
            Optional<AliasedDouble> optionalAliasDouble,
            Optional<AliasedSafeLong> optionalAliasSafeLong,
            Optional<AliasedBoolean> optionalAliasBoolean);
}
