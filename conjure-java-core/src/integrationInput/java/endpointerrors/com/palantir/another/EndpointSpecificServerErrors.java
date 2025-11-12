package endpointerrors.com.palantir.another;

import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Contract;

@ConjureGenerated("com.palantir.conjure.java.types.EndpointErrorGenerator")
public final class EndpointSpecificServerErrors {
    private EndpointSpecificServerErrors() {}

    public static DifferentPackage differentPackage() {
        return new DifferentPackage(null);
    }

    public static DifferentPackage differentPackage(@Nullable Throwable cause) {
        return new DifferentPackage(cause);
    }

    /**
     * Throws a {@link DifferentPackage} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     */
    @Contract("true -> fail")
    public static void throwIfDifferentPackage(boolean shouldThrow) {
        if (shouldThrow) {
            throw differentPackage();
        }
    }

    public static final class DifferentPackage extends EndpointServiceException {
        private DifferentPackage(@Nullable Throwable cause) {
            super(EndpointSpecificErrors.DIFFERENT_PACKAGE, cause);
        }
    }
}
