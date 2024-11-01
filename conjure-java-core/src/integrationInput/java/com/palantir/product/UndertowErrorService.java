package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowErrorService {
    /** @apiNote {@code GET /base/basic} */
    String testBasicError(AuthHeader authHeader) throws ServerTestErrors.InvalidArgument;
}
