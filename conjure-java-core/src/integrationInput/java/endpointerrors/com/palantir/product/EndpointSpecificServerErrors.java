package endpointerrors.com.palantir.product;

import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Contract;

@ConjureGenerated("com.palantir.conjure.java.types.EndpointErrorGenerator")
public final class EndpointSpecificServerErrors {
    private EndpointSpecificServerErrors() {}

    public static EndpointError endpointError(@Safe String typeName, @Unsafe Object typeDef) {
        return new EndpointError(typeName, typeDef, null);
    }

    public static EndpointError endpointError(
            @Safe String typeName, @Unsafe Object typeDef, @Nullable Throwable cause) {
        return new EndpointError(typeName, typeDef, cause);
    }

    /**
     * Throws a {@link EndpointError} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param typeName
     * @param typeDef
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfEndpointError(boolean shouldThrow, @Safe String typeName, @Unsafe Object typeDef) {
        if (shouldThrow) {
            throw endpointError(typeName, typeDef);
        }
    }

    public static final class EndpointError extends EndpointServiceException {
        private EndpointError(@Safe String typeName, @Unsafe Object typeDef, @Nullable Throwable cause) {
            super(
                    EndpointSpecificErrors.ENDPOINT_ERROR,
                    cause,
                    SafeArg.of("typeName", typeName),
                    UnsafeArg.of("typeDef", typeDef));
        }
    }
}
