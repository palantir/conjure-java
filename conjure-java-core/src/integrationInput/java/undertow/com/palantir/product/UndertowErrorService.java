package undertow.com.palantir.product;

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import dialogueendpointresulttypes.com.palantir.product.OptionalBinaryResponseMode;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowErrorService {
    /**
     * @apiNote {@code GET /base/basic}
     * @throws TestServerErrors.InvalidArgument
     */
    String testBasicError(AuthHeader authHeader) throws TestServerErrors.InvalidArgument;

    /**
     * @apiNote {@code GET /base/imported}
     * @throws EndpointSpecificServerErrors.EndpointError
     */
    String testImportedError(AuthHeader authHeader) throws EndpointSpecificServerErrors.EndpointError;

    /**
     * @apiNote {@code GET /base/multiple}
     * @throws TestServerErrors.InvalidArgument
     * @throws TestServerErrors.NotFound Something was not found.
     * @throws EndpointSpecificTwoServerErrors.DifferentNamespace
     * @throws undertow.com.palantir.another.EndpointSpecificServerErrors.DifferentPackage
     */
    String testMultipleErrorsAndPackages(AuthHeader authHeader)
            throws TestServerErrors.InvalidArgument, TestServerErrors.NotFound,
                    EndpointSpecificTwoServerErrors.DifferentNamespace,
                    undertow.com.palantir.another.EndpointSpecificServerErrors.DifferentPackage;

    /**
     * @apiNote {@code POST /errors/empty}
     * @throws TestServerErrors.InvalidArgument
     */
    void testEmptyBody(AuthHeader authHeader, boolean shouldThrowError) throws TestServerErrors.InvalidArgument;

    /**
     * @apiNote {@code POST /errors/binary}
     * @throws TestServerErrors.InvalidArgument
     */
    BinaryResponseBody testBinary(AuthHeader authHeader, boolean shouldThrowError)
            throws TestServerErrors.InvalidArgument;

    /**
     * @apiNote {@code POST /errors/optional-binary}
     * @throws TestServerErrors.InvalidArgument
     */
    Optional<BinaryResponseBody> testOptionalBinary(AuthHeader authHeader, OptionalBinaryResponseMode mode)
            throws TestServerErrors.InvalidArgument;
}
