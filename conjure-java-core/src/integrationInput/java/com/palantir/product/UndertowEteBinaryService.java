package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteBinaryService {
    /**
     * @apiNote {@code POST /binary}
     */
    BinaryResponseBody postBinary(AuthHeader authHeader, InputStream body);

    /**
     * @apiNote {@code GET /binary/optional/present}
     */
    Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /binary/optional/empty}
     */
    Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * @apiNote {@code GET /binary/failure}
     */
    BinaryResponseBody getBinaryFailure(AuthHeader authHeader, int numBytes);
}
