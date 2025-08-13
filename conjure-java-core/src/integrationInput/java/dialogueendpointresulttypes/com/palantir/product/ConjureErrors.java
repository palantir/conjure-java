package dialogueendpointresulttypes.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Cause argument conflicts with reserved Throwable cause parameter. */
    public static final ErrorType CONFLICTING_CAUSE_SAFE_ARG_ERR =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:ConflictingCauseSafeArgErr");

    private ConjureErrors() {}

    /** Returns true if the {@link RemoteException} is named Conjure:ConflictingCauseSafeArgErr */
    public static boolean isConflictingCauseSafeArgErr(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return CONFLICTING_CAUSE_SAFE_ARG_ERR
                .name()
                .equals(remoteException.getError().errorName());
    }

    public static record ConflictingCauseSafeArgErrParameters(
            @JsonProperty("cause") @Safe String cause_, @JsonProperty("shouldThrow") @Safe boolean shouldThrow_) {}

    public static final class ConflictingCauseSafeArgErrSerializableError
            extends AbstractSerializableError<ConflictingCauseSafeArgErrParameters> {
        @Nullable
        private final Map<String, String> legacyParameters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ConflictingCauseSafeArgErrSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") ConflictingCauseSafeArgErrParameters parameters,
                @JsonProperty("legacyParameters") @Nullable Map<String, String> legacyParameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
            this.legacyParameters = legacyParameters;
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder();
            if (legacyParameters != null) {
                builder.putAllParameters(legacyParameters);
            } else {
                builder.putParameters("cause", Objects.toString(parameters().cause_()))
                        .putParameters(
                                "shouldThrow", Objects.toString(parameters().shouldThrow_()));
            }
            builder.errorCode(errorCode()).errorName(errorName()).errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class ConflictingCauseSafeArgErrException extends RemoteException {
        private ConflictingCauseSafeArgErrSerializableError error;

        public ConflictingCauseSafeArgErrException(ConflictingCauseSafeArgErrSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public ConflictingCauseSafeArgErrSerializableError error() {
            return error;
        }
    }
}
