package endpointerrors.com.palantir.another;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.SerializableErrorProvider;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;

@ConjureGenerated("com.palantir.conjure.java.types.ErrorGenerator")
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

    public static final class DifferentPackageSerializableError
            extends AbstractSerializableError<DifferentPackageParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        DifferentPackageSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") DifferentPackageParameters parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            return SerializableError.builder()
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId())
                    .build();
        }
    }

    public static final class DifferentPackageException extends RemoteException
            implements SerializableErrorProvider<DifferentPackageParameters> {
        private DifferentPackageSerializableError error;

        public DifferentPackageException(DifferentPackageSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public DifferentPackageSerializableError error() {
            return error;
        }
    }
}
