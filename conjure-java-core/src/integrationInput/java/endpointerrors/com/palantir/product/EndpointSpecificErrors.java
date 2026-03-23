package endpointerrors.com.palantir.product;

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
import com.palantir.logsafe.Unsafe;
import java.util.Objects;
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
            @JsonProperty("typeName") @Safe String typeName,
            @JsonProperty("typeDef") @Unsafe Object typeDef) {}

    public static final class EndpointErrorSerializableError
            extends AbstractSerializableError<EndpointErrorParameters> {
        public static final TypeMarker<EndpointErrorSerializableError> TYPE_MARKER =
                new TypeMarker<EndpointErrorSerializableError>() {};

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        EndpointErrorSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") EndpointErrorParameters parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            return SerializableError.builder()
                    .putParameters("typeName", Objects.toString(parameters().typeName()))
                    .putParameters("typeDef", Objects.toString(parameters().typeDef()))
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId())
                    .build();
        }
    }

    @SuppressWarnings("serial")
    public static final class EndpointErrorException extends RemoteException
            implements SerializableErrorProvider<EndpointErrorParameters> {
        public static final TypeMarker<EndpointErrorException> TYPE_MARKER =
                new TypeMarker<EndpointErrorException>() {};

        private EndpointErrorSerializableError error;

        public EndpointErrorException(EndpointErrorSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public EndpointErrorSerializableError error() {
            return error;
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class EndpointErrorTestUtility {
        public static EndpointErrorSerializableError createSerializableError(
                String errorInstanceId, EndpointErrorParameters parameters) {
            return new EndpointErrorSerializableError(
                    ENDPOINT_ERROR.code().name(), ENDPOINT_ERROR.name(), errorInstanceId, parameters);
        }

        public static EndpointErrorException createException(
                String errorInstanceId, EndpointErrorParameters parameters) {
            return new EndpointErrorException(
                    createSerializableError(errorInstanceId, parameters), ENDPOINT_ERROR.httpErrorCode());
        }
    }
}
