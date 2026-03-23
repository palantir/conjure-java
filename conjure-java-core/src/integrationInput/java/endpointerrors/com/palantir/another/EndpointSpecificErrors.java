package endpointerrors.com.palantir.another;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.SerializableErrorProvider;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
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

    public static final class DifferentPackageSerializableError
            extends AbstractSerializableError<DifferentPackageParameters> {
        public static final TypeMarker<DifferentPackageSerializableError> TYPE_MARKER =
                new TypeMarker<DifferentPackageSerializableError>() {};

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

    @SuppressWarnings("serial")
    public static final class DifferentPackageException extends RemoteException
            implements SerializableErrorProvider<DifferentPackageParameters> {
        public static final TypeMarker<DifferentPackageException> TYPE_MARKER =
                new TypeMarker<DifferentPackageException>() {};

        private DifferentPackageSerializableError error;

        public DifferentPackageException(DifferentPackageSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public DifferentPackageSerializableError error() {
            return error;
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class DifferentPackageTestUtility {
        public static DifferentPackageSerializableError createSerializableError(
                String errorInstanceId, DifferentPackageParameters parameters) {
            return new DifferentPackageSerializableError(
                    DIFFERENT_PACKAGE.code().name(), DIFFERENT_PACKAGE.name(), errorInstanceId, parameters);
        }

        public static DifferentPackageException createException(
                String errorInstanceId, DifferentPackageParameters parameters) {
            return new DifferentPackageException(
                    createSerializableError(errorInstanceId, parameters), DIFFERENT_PACKAGE.httpErrorCode());
        }
    }
}
