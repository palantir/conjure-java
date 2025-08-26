package dialogue.com.palantir.another;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Different package. */
    public static final ErrorType DIFFERENT_PACKAGE_ERROR =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:DifferentPackageError");

    private ConjureErrors() {}

    public static ServiceException differentPackageError() {
        return new ServiceException(DIFFERENT_PACKAGE_ERROR);
    }

    public static ServiceException differentPackageError(@Nullable Throwable cause) {
        return new ServiceException(DIFFERENT_PACKAGE_ERROR, cause);
    }

    /**
     * Throws a {@link ServiceException} of type DifferentPackageError when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     */
    public static void throwIfDifferentPackageError(boolean shouldThrow) {
        if (shouldThrow) {
            throw differentPackageError();
        }
    }

    /** Returns true if the {@link RemoteException} is named Conjure:DifferentPackageError */
    public static boolean isDifferentPackageError(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return DIFFERENT_PACKAGE_ERROR.name().equals(remoteException.getError().errorName());
    }
}
