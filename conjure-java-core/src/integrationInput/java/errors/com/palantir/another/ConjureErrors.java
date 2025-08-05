package errors.com.palantir.another;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

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
    @Contract("true -> fail")
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
            SerializableError.Builder builder = SerializableError.builder()
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class DifferentPackageException extends RemoteException {
        private DifferentPackageSerializableError error;

        private int status;

        public DifferentPackageException(DifferentPackageSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
            this.status = status;
        }

        public DifferentPackageSerializableError error() {
            return error;
        }
    }
}
