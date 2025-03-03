package dialogueendpointresulttypes.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import java.util.Map;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class TestErrors {
    public static final ErrorType COMPLICATED_PARAMETERS =
            ErrorType.create(ErrorType.Code.INTERNAL, "Test:ComplicatedParameters");

    public static final ErrorType INVALID_ARGUMENT =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Test:InvalidArgument");

    public static final ErrorType NOT_FOUND = ErrorType.create(ErrorType.Code.NOT_FOUND, "Test:NotFound");

    private TestErrors() {}

    /** Returns true if the {@link RemoteException} is named Test:ComplicatedParameters */
    public static boolean isComplicatedParameters(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return COMPLICATED_PARAMETERS.name().equals(remoteException.getError().errorName());
    }

    /** Returns true if the {@link RemoteException} is named Test:InvalidArgument */
    public static boolean isInvalidArgument(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return INVALID_ARGUMENT.name().equals(remoteException.getError().errorName());
    }

    /** Returns true if the {@link RemoteException} is named Test:NotFound */
    public static boolean isNotFound(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return NOT_FOUND.name().equals(remoteException.getError().errorName());
    }

    public static record ComplicatedParametersParameters(
            @JsonProperty("complicatedObjectMap") @Safe Map<Integer, ComplicatedObject> complicatedObjectMap) {}

    public static record InvalidArgumentParameters(
            @JsonProperty("field") @Safe String field, @JsonProperty("value") @Unsafe String value) {}

    public static record NotFoundParameters(@JsonProperty("resource") @Safe String resource) {}
}
