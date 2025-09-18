package undertow.com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Preconditions;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Cause argument conflicts with reserved Throwable cause parameter. */
    public static final ErrorType CONFLICTING_CAUSE_SAFE_ARG_ERR =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:ConflictingCauseSafeArgErr");

    private ConjureErrors() {}

    /** Returns true if the {@link RemoteException} is named Conjure:ConflictingCauseSafeArgErr */
    public static boolean isConflictingCauseSafeArgErr(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return CONFLICTING_CAUSE_SAFE_ARG_ERR
                .name()
                .equals(remoteException.getError().errorName());
    }
}
