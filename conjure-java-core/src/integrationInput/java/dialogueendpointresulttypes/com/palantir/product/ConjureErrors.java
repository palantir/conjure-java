package dialogueendpointresulttypes.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Objects;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Cause argument conflicts with reserved Throwable cause parameter. */
    public static final ErrorType CONFLICTING_CAUSE_SAFE_ARG =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:ConflictingCauseSafeArg");

    private ConjureErrors() {}

    /** Returns true if the {@link RemoteException} is named Conjure:ConflictingCauseSafeArg */
    public static boolean isConflictingCauseSafeArg(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return CONFLICTING_CAUSE_SAFE_ARG
                .name()
                .equals(remoteException.getError().errorName());
    }

    public static record ConflictingCauseSafeArgParameters(
            @JsonProperty("cause") @Safe String cause_, @JsonProperty("shouldThrow") @Safe boolean shouldThrow_) {}

    public static final class ConflictingCauseSafeArgSerializableError
            extends AbstractSerializableError<ConflictingCauseSafeArgParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ConflictingCauseSafeArgSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") ConflictingCauseSafeArgParameters parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder()
                    .putParameters("cause", Objects.toString(parameters().cause_()))
                    .putParameters("shouldThrow", Objects.toString(parameters().shouldThrow_()))
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class ConflictingCauseSafeArgException extends RemoteException {
        private ConflictingCauseSafeArgSerializableError error;

        private int status;

        public ConflictingCauseSafeArgException(ConflictingCauseSafeArgSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
            this.status = status;
        }

        public ConflictingCauseSafeArgSerializableError error() {
            return error;
        }
    }
}
