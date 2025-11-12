package undertow.com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.tokens.auth.AuthHeader;
import java.util.Optional;

@ConjureGenerated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowErrorService {
    /**
     * @apiNote {@code POST /errors/basic}
     * @throws TestServerErrors.InvalidArgument
     * @throws ConjureServerErrors.ConflictingCauseSafeArgErr
     */
    String testBasicError(AuthHeader authHeader, boolean shouldThrowError)
            throws TestServerErrors.InvalidArgument, ConjureServerErrors.ConflictingCauseSafeArgErr;

    /**
     * @apiNote {@code POST /errors/imported}
     * @throws EndpointSpecificServerErrors.EndpointError
     */
    String testImportedError(AuthHeader authHeader, boolean shouldThrowError)
            throws EndpointSpecificServerErrors.EndpointError;

    /**
     * @apiNote {@code POST /errors/multiple}
     * @throws TestServerErrors.InvalidArgument
     * @throws TestServerErrors.NotFound Something was not found.
     * @throws EndpointSpecificTwoServerErrors.DifferentNamespace
     * @throws undertow.com.palantir.another.EndpointSpecificServerErrors.DifferentPackage
     * @throws TestServerErrors.ComplicatedParameters
     */
    String testMultipleErrorsAndPackages(AuthHeader authHeader, Optional<String> errorToThrow)
            throws TestServerErrors.InvalidArgument, TestServerErrors.NotFound,
                    EndpointSpecificTwoServerErrors.DifferentNamespace,
                    undertow.com.palantir.another.EndpointSpecificServerErrors.DifferentPackage,
                    TestServerErrors.ComplicatedParameters;

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
