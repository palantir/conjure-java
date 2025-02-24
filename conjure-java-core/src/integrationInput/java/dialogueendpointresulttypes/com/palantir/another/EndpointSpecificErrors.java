package dialogueendpointresulttypes.com.palantir.another;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Preconditions;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class EndpointSpecificErrors {
    /** An error in a different package. */
    public static final ErrorType DIFFERENT_PACKAGE =
            ErrorType.create(ErrorType.Code.INTERNAL, "EndpointSpecific:DifferentPackage");

    private EndpointSpecificErrors() {}

    /** Returns true if the {@link RemoteException} is named EndpointSpecific:DifferentPackage */
    public static boolean isDifferentPackage(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return DIFFERENT_PACKAGE.name().equals(remoteException.getError().errorName());
    }

    public static record DifferentPackageParameters() {}
}
