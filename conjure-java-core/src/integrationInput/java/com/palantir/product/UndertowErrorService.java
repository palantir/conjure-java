package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowErrorService {
    /**
     * @apiNote {@code GET /base/basic}
     * @throws InvalidArgument
     */
    String testBasicError(AuthHeader authHeader) throws ServerTestErrors.InvalidArgument;

    /**
     * @apiNote {@code GET /base/imported}
     * @throws EndpointError
     */
    String testImportedError(AuthHeader authHeader) throws ServerEndpointSpecificErrors.EndpointError;

    /**
     * @apiNote {@code GET /base/multiple}
     * @throws InvalidArgument
     * @throws NotFound Something was not found.
     * @throws DifferentNamespace
     * @throws DifferentPackage
     */
    String testMultipleErrorsAndPackages(AuthHeader authHeader)
            throws ServerTestErrors.InvalidArgument, ServerTestErrors.NotFound,
                    ServerEndpointSpecificTwoErrors.DifferentNamespace,
                    com.palantir.another.ServerEndpointSpecificErrors.DifferentPackage;
}
