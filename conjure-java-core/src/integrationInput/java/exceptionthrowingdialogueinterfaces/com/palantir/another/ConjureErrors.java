package exceptionthrowingdialogueinterfaces.com.palantir.another;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.SerializableErrorProvider;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.dialogue.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
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

    public static record DifferentPackageErrorParameters() {}

    public static final class DifferentPackageErrorSerializableError
            extends AbstractSerializableError<DifferentPackageErrorParameters> {
        public static final TypeMarker<DifferentPackageErrorSerializableError> TYPE_MARKER =
                new TypeMarker<DifferentPackageErrorSerializableError>() {};

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        DifferentPackageErrorSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") DifferentPackageErrorParameters parameters) {
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
    public static final class DifferentPackageErrorException extends RemoteException
            implements SerializableErrorProvider<DifferentPackageErrorParameters> {
        public static final TypeMarker<DifferentPackageErrorException> TYPE_MARKER =
                new TypeMarker<DifferentPackageErrorException>() {};

        private DifferentPackageErrorSerializableError error;

        public DifferentPackageErrorException(DifferentPackageErrorSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public DifferentPackageErrorSerializableError error() {
            return error;
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class DifferentPackageErrorTestUtility {
        public static DifferentPackageErrorSerializableError createSerializableError(
                String errorInstanceId, DifferentPackageErrorParameters parameters) {
            return new DifferentPackageErrorSerializableError(
                    DIFFERENT_PACKAGE_ERROR.code().name(), DIFFERENT_PACKAGE_ERROR.name(), errorInstanceId, parameters);
        }

        public static DifferentPackageErrorException createException(
                String errorInstanceId, DifferentPackageErrorParameters parameters) {
            return new DifferentPackageErrorException(
                    createSerializableError(errorInstanceId, parameters), DIFFERENT_PACKAGE_ERROR.httpErrorCode());
        }
    }
}
