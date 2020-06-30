package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEteBinaryService extends UndertowService {
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

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return EteBinaryServiceEndpoints.of(this).endpoints(runtime);
    }
}
