package dialogueendpointresulttypes.com.palantir.product;

import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.EndpointErrorGenerator")
public final class ConjureServerErrors {
    private ConjureServerErrors() {}

    public static ConflictingCauseSafeArg conflictingCauseSafeArg(@Safe String cause_, @Safe boolean shouldThrow_) {
        return new ConflictingCauseSafeArg(cause_, shouldThrow_, null);
    }

    public static ConflictingCauseSafeArg conflictingCauseSafeArg(
            @Safe String cause_, @Safe boolean shouldThrow_, @Nullable Throwable cause) {
        return new ConflictingCauseSafeArg(cause_, shouldThrow_, cause);
    }

    /**
     * Throws a {@link ConflictingCauseSafeArg} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param cause_
     * @param shouldThrow_
     */
    public static void throwIfConflictingCauseSafeArg(
            boolean shouldThrow, @Safe String cause_, @Safe boolean shouldThrow_) {
        if (shouldThrow) {
            throw conflictingCauseSafeArg(cause_, shouldThrow_);
        }
    }

    public static final class ConflictingCauseSafeArg extends EndpointServiceException {
        private ConflictingCauseSafeArg(@Safe String cause_, @Safe boolean shouldThrow_, @Nullable Throwable cause) {
            super(
                    ConjureErrors.CONFLICTING_CAUSE_SAFE_ARG,
                    cause,
                    SafeArg.of("cause", cause_),
                    SafeArg.of("shouldThrow", shouldThrow_));
        }
    }
}
