package undertow.com.palantir.another;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Map;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Different package. */
    public static final ErrorType DIFFERENT_PACKAGE_ERR =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:DifferentPackageErr");

    private ConjureErrors() {}

    public static ServiceException differentPackageErr() {
        return new ServiceException(DIFFERENT_PACKAGE_ERR);
    }

    public static ServiceException differentPackageErr(@Nullable Throwable cause) {
        return new ServiceException(DIFFERENT_PACKAGE_ERR, cause);
    }

    /**
     * Throws a {@link ServiceException} of type DifferentPackageErr when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     */
    @Contract("true -> fail")
    public static void throwIfDifferentPackageErr(boolean shouldThrow) {
        if (shouldThrow) {
            throw differentPackageErr();
        }
    }

    /** Returns true if the {@link RemoteException} is named Conjure:DifferentPackageErr */
    public static boolean isDifferentPackageErr(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return DIFFERENT_PACKAGE_ERR.name().equals(remoteException.getError().errorName());
    }

    public static record DifferentPackageErrParameters() {}

    public static final class DifferentPackageErrSerializableError
            extends AbstractSerializableError<DifferentPackageErrParameters> {
        @org.jspecify.annotations.Nullable
        private final Map<String, String> legacyParameters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        DifferentPackageErrSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") DifferentPackageErrParameters parameters,
                @JsonProperty("legacyParameters") @org.jspecify.annotations.Nullable
                        Map<String, String> legacyParameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
            this.legacyParameters = legacyParameters;
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder();
            if (legacyParameters != null) {
                builder.putAllParameters(legacyParameters);
            } else {
            }
            builder.errorCode(errorCode()).errorName(errorName()).errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class DifferentPackageErrException extends RemoteException {
        private DifferentPackageErrSerializableError error;

        public DifferentPackageErrException(DifferentPackageErrSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public DifferentPackageErrSerializableError error() {
            return error;
        }
    }
}
