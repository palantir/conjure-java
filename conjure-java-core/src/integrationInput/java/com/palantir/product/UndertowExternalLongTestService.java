package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowExternalLongTestService {
    /**
     * @apiNote {@code POST /external-long/testDangerousLong}
     */
    void testDangerousLong(AuthHeader authHeader, long dangerousLong);

    /**
     * @apiNote {@code POST /external-long/testSafeExternalLong}
     */
    void testSafeExternalLong(AuthHeader authHeader, long safeExternalLong);

    /**
     * @apiNote {@code POST /external-long/testLong}
     */
    void testLong(AuthHeader authHeader, long long_);

    /**
     * @apiNote {@code POST /external-long/testDangerousLongAlias}
     */
    void testDangerousLongAlias(AuthHeader authHeader, DangerousLongAlias dangerousLongAlias);

    /**
     * @apiNote {@code POST /external-long/testSafeExternalLongAlias}
     */
    void testSafeExternalLongAlias(AuthHeader authHeader, SafeLongAlias safeExternalLongAlias);

    /**
     * @apiNote {@code POST /external-long/testLongAlias}
     */
    void testLongAlias(AuthHeader authHeader, ExternalLongAlias longAlias);
}
