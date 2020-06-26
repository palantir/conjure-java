package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteBinaryService {
    /**
     * POST /binary
     */
    BinaryResponseBody postBinary(AuthHeader authHeader, InputStream body);

    /**
     * GET /binary/optional/present
     */
    Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * GET /binary/optional/empty
     */
    Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * GET /binary/failure
     */
    BinaryResponseBody getBinaryFailure(AuthHeader authHeader, int numBytes);
}
