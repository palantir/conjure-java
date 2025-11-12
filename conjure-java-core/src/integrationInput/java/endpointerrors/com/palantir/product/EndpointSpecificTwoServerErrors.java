package endpointerrors.com.palantir.product;

import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Contract;

@ConjureGenerated("com.palantir.conjure.java.types.EndpointErrorGenerator")
public final class EndpointSpecificTwoServerErrors {
    private EndpointSpecificTwoServerErrors() {}

    public static DifferentNamespace differentNamespace() {
        return new DifferentNamespace(null);
    }

    public static DifferentNamespace differentNamespace(@Nullable Throwable cause) {
        return new DifferentNamespace(cause);
    }

    /**
     * Throws a {@link DifferentNamespace} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     */
    @Contract("true -> fail")
    public static void throwIfDifferentNamespace(boolean shouldThrow) {
        if (shouldThrow) {
            throw differentNamespace();
        }
    }

    public static final class DifferentNamespace extends EndpointServiceException {
        private DifferentNamespace(@Nullable Throwable cause) {
            super(EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE, cause);
        }
    }
}
