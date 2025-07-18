package dialogueendpointresulttypes.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Cause argument conflicts with reserved Throwable cause parameter. */
    public static final ErrorType CONFLICTING_CAUSE_SAFE_ARG =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:ConflictingCauseSafeArg");

    private ConjureErrors() {}

    /** Returns true if the {@link RemoteException} is named Conjure:ConflictingCauseSafeArg */
    public static boolean isConflictingCauseSafeArg(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return CONFLICTING_CAUSE_SAFE_ARG
                .name()
                .equals(remoteException.getError().errorName());
    }

    public static record ConflictingCauseSafeArgParameters(
            @JsonProperty("cause") @Safe String cause_, @JsonProperty("shouldThrow") @Safe boolean shouldThrow_) {}
}
