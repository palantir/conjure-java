package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowErrorService {
    /**
     * @apiNote {@code GET /base/basic}
     * @throws ServerTestErrors.InvalidArgument
     */
    String testBasicError(AuthHeader authHeader) throws ServerTestErrors.InvalidArgument;

    /**
     * @apiNote {@code GET /base/imported}
     * @throws ServerEndpointSpecificErrors.EndpointError
     */
    String testImportedError(AuthHeader authHeader) throws ServerEndpointSpecificErrors.EndpointError;

    /**
     * @apiNote {@code GET /base/multiple}
     * @throws ServerTestErrors.InvalidArgument
     * @throws ServerTestErrors.NotFound Something was not found.
     * @throws ServerEndpointSpecificTwoErrors.DifferentNamespace
     * @throws com.palantir.another.ServerEndpointSpecificErrors.DifferentPackage
     */
    String testMultipleErrorsAndPackages(AuthHeader authHeader)
            throws ServerTestErrors.InvalidArgument, ServerTestErrors.NotFound,
                    ServerEndpointSpecificTwoErrors.DifferentNamespace,
                    com.palantir.another.ServerEndpointSpecificErrors.DifferentPackage;
}
