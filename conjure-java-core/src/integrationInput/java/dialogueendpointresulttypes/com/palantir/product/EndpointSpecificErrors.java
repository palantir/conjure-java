package dialogueendpointresulttypes.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
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

    public static record EndpointErrorParameters(
            @JsonProperty("typeName") @Safe String typeName, @JsonProperty("typeDef") @Unsafe Object typeDef) {}
}
