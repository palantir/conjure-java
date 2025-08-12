package dialogueendpointresulttypes.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import java.util.Map;
import java.util.Objects;
import javax.annotation.processing.Generated;
import org.jspecify.annotations.Nullable;

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

    public static final class EndpointErrorSerializableError
            extends AbstractSerializableError<EndpointErrorParameters> {
        @Nullable
        private final Map<String, String> legacyParameters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        EndpointErrorSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") EndpointErrorParameters parameters,
                @JsonProperty("legacyParameters") @Nullable Map<String, String> legacyParameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
            this.legacyParameters = legacyParameters;
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder();
            if (legacyParameters != null) {
                builder.putAllParameters(legacyParameters);
            } else {
                builder.putParameters("typeName", Objects.toString(parameters().typeName()))
                        .putParameters("typeDef", Objects.toString(parameters().typeDef()));
            }
            builder.errorCode(errorCode()).errorName(errorName()).errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class EndpointErrorException extends RemoteException {
        private EndpointErrorSerializableError error;

        public EndpointErrorException(EndpointErrorSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public EndpointErrorSerializableError error() {
            return error;
        }
    }
}
