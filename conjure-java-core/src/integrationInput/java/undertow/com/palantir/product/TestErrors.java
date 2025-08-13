package undertow.com.palantir.product;

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
import javax.annotation.Nullable;
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

    public static final class ComplicatedParametersSerializableError
            extends AbstractSerializableError<ComplicatedParametersParameters> {
        @Nullable
        private final Map<String, String> legacyParameters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ComplicatedParametersSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") ComplicatedParametersParameters parameters,
                @JsonProperty("legacyParameters") @Nullable Map<String, String> legacyParameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
            this.legacyParameters = legacyParameters;
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder();
            if (legacyParameters != null) {
                builder.putAllParameters(legacyParameters);
            } else {
                builder.putParameters(
                        "complicatedObjectMap", Objects.toString(parameters().complicatedObjectMap()));
            }
            builder.errorCode(errorCode()).errorName(errorName()).errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class InvalidArgumentSerializableError
            extends AbstractSerializableError<InvalidArgumentParameters> {
        @Nullable
        private final Map<String, String> legacyParameters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        InvalidArgumentSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") InvalidArgumentParameters parameters,
                @JsonProperty("legacyParameters") @Nullable Map<String, String> legacyParameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
            this.legacyParameters = legacyParameters;
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder();
            if (legacyParameters != null) {
                builder.putAllParameters(legacyParameters);
            } else {
                builder.putParameters("field", Objects.toString(parameters().field()))
                        .putParameters("value", Objects.toString(parameters().value()));
            }
            builder.errorCode(errorCode()).errorName(errorName()).errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class NotFoundSerializableError extends AbstractSerializableError<NotFoundParameters> {
        @Nullable
        private final Map<String, String> legacyParameters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        NotFoundSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") NotFoundParameters parameters,
                @JsonProperty("legacyParameters") @Nullable Map<String, String> legacyParameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
            this.legacyParameters = legacyParameters;
        }

        public SerializableError toSerializableError() {
            SerializableError.Builder builder = SerializableError.builder();
            if (legacyParameters != null) {
                builder.putAllParameters(legacyParameters);
            } else {
                builder.putParameters("resource", Objects.toString(parameters().resource()));
            }
            builder.errorCode(errorCode()).errorName(errorName()).errorInstanceId(errorInstanceId());
            return builder.build();
        }
    }

    public static final class ComplicatedParametersException extends RemoteException {
        private ComplicatedParametersSerializableError error;

        public ComplicatedParametersException(ComplicatedParametersSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public ComplicatedParametersSerializableError error() {
            return error;
        }
    }

    public static final class InvalidArgumentException extends RemoteException {
        private InvalidArgumentSerializableError error;

        public InvalidArgumentException(InvalidArgumentSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public InvalidArgumentSerializableError error() {
            return error;
        }
    }

    public static final class NotFoundException extends RemoteException {
        private NotFoundSerializableError error;

        public NotFoundException(NotFoundSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public NotFoundSerializableError error() {
            return error;
        }
    }
}
