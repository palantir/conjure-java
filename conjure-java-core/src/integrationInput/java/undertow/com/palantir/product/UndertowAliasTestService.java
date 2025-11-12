package undertow.com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.tokens.auth.AuthHeader;
import java.util.Optional;

@ConjureGenerated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAliasTestService {
    /** @apiNote {@code GET /alias/test} */
    void testOptionalAliasQueryParams(
            AuthHeader authHeader,
            Optional<AliasedInteger> optionalAliasInt,
            Optional<AliasedDouble> optionalAliasDouble,
            Optional<AliasedSafeLong> optionalAliasSafeLong,
            Optional<AliasedBoolean> optionalAliasBoolean);
}
