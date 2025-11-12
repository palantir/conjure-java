package undertow.com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;

@ConjureGenerated("com.palantir.conjure.java.types.ErrorGenerator")
public final class EndpointSpecificErrors {
    /** Docs for an endpoint error. */
    public static final ErrorType ENDPOINT_ERROR =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "EndpointSpecific:EndpointError");

    private EndpointSpecificErrors() {}

    /** Returns true if the {@link RemoteException} is named EndpointSpecific:EndpointError */
    public static boolean isEndpointError(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return ENDPOINT_ERROR.name().equals(remoteException.getError().errorName());
    }
}
