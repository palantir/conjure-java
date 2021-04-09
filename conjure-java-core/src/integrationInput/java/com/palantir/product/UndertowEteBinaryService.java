package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
@ConjureService(name = "EteBinaryService", package_ = "com.palantir.product")
public interface UndertowEteBinaryService {
    /**
     * @apiNote {@code POST /binary}
     */
    @ConjureEndpoint(path = "/binary", method = "POST")
    BinaryResponseBody postBinary(AuthHeader authHeader, InputStream body);

    /**
     * @apiNote {@code POST /binary/throws}
     */
    @ConjureEndpoint(path = "/binary/throws", method = "POST")
    BinaryResponseBody postBinaryThrows(AuthHeader authHeader, int bytesToRead, InputStream body);

    /**
     * @apiNote {@code GET /binary/optional/present}
     */
    @ConjureEndpoint(path = "/binary/optional/present", method = "GET")
    Optional<BinaryResponseBody> getOptionalBinaryPresent(AuthHeader authHeader);

    /**
     * @apiNote {@code GET /binary/optional/empty}
     */
    @ConjureEndpoint(path = "/binary/optional/empty", method = "GET")
    Optional<BinaryResponseBody> getOptionalBinaryEmpty(AuthHeader authHeader);

    /**
     * Throws an exception after partially writing a binary response.
     * @apiNote {@code GET /binary/failure}
     */
    @ConjureEndpoint(path = "/binary/failure", method = "GET")
    BinaryResponseBody getBinaryFailure(AuthHeader authHeader, int numBytes);

    /**
     * @apiNote {@code GET /binary/aliased}
     */
    @ConjureEndpoint(path = "/binary/aliased", method = "GET")
    Optional<BinaryResponseBody> getAliased(AuthHeader authHeader);
}
