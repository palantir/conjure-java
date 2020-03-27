package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteBinaryService {
    BinaryResponseBody postBinary(AuthHeader authHeader, InputStream body);

    Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader authHeader);

    Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     */
    BinaryResponseBody getBinaryFailure(AuthHeader authHeader, int numBytes);
}
