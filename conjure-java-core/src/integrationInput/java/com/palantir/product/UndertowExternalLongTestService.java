package com.palantir.product;

import com.palantir.logsafe.DoNotLog;
import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowExternalLongTestService {
    /**
     * @apiNote {@code POST /external-long/test}
     */
    void testExternalLongArg(AuthHeader authHeader, @DoNotLog long externalLong);
}
