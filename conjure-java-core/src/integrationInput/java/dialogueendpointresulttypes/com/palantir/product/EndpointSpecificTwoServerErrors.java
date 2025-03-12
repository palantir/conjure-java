package dialogueendpointresulttypes.com.palantir.product;

import com.palantir.conjure.java.api.errors.CheckedServiceException;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
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
    public static void throwIfDifferentNamespace(boolean shouldThrow) throws DifferentNamespace {
        if (shouldThrow) {
            throw differentNamespace();
        }
    }

    public static final class DifferentNamespace extends CheckedServiceException {
        private DifferentNamespace(@Nullable Throwable cause) {
            super(EndpointSpecificTwoErrors.DIFFERENT_NAMESPACE, cause);
        }
    }
}
