package dialogueendpointresulttypes.com.palantir.product;

import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.EndpointErrorGenerator")
public final class ConjureServerErrors {
    private ConjureServerErrors() {}

    public static ConflictingCauseSafeArgErr conflictingCauseSafeArgErr(
            @Safe String cause_, @Safe boolean shouldThrow_) {
        return new ConflictingCauseSafeArgErr(cause_, shouldThrow_, null);
    }

    public static ConflictingCauseSafeArgErr conflictingCauseSafeArgErr(
            @Safe String cause_, @Safe boolean shouldThrow_, @Nullable Throwable cause) {
        return new ConflictingCauseSafeArgErr(cause_, shouldThrow_, cause);
    }

    /**
     * Throws a {@link ConflictingCauseSafeArgErr} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param cause_
     * @param shouldThrow_
     */
    public static void throwIfConflictingCauseSafeArgErr(
            boolean shouldThrow, @Safe String cause_, @Safe boolean shouldThrow_) {
        if (shouldThrow) {
            throw conflictingCauseSafeArgErr(cause_, shouldThrow_);
        }
    }

    public static final class ConflictingCauseSafeArgErr extends EndpointServiceException {
        private ConflictingCauseSafeArgErr(@Safe String cause_, @Safe boolean shouldThrow_, @Nullable Throwable cause) {
            super(
                    ConjureErrors.CONFLICTING_CAUSE_SAFE_ARG_ERR,
                    cause,
                    SafeArg.of("cause", cause_),
                    SafeArg.of("shouldThrow", shouldThrow_));
        }
    }
}
