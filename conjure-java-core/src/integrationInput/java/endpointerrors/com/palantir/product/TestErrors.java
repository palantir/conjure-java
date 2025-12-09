package endpointerrors.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.SerializableErrorProvider;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.Unsafe;
import java.util.Map;
import java.util.Objects;
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
            @JsonProperty("field") @Safe String field,
            @JsonProperty("value") @Unsafe String value) {}

    public static record NotFoundParameters(
            @JsonProperty("resource") @Safe String resource) {}

    public static final class ComplicatedParametersSerializableError
            extends AbstractSerializableError<ComplicatedParametersParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        ComplicatedParametersSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") ComplicatedParametersParameters parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            return SerializableError.builder()
                    .putParameters(
                            "complicatedObjectMap",
                            Objects.toString(parameters().complicatedObjectMap()))
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId())
                    .build();
        }
    }

    public static final class InvalidArgumentSerializableError
            extends AbstractSerializableError<InvalidArgumentParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        InvalidArgumentSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") InvalidArgumentParameters parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            return SerializableError.builder()
                    .putParameters("field", Objects.toString(parameters().field()))
                    .putParameters("value", Objects.toString(parameters().value()))
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId())
                    .build();
        }
    }

    public static final class NotFoundSerializableError extends AbstractSerializableError<NotFoundParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        NotFoundSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") NotFoundParameters parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            return SerializableError.builder()
                    .putParameters("resource", Objects.toString(parameters().resource()))
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId())
                    .build();
        }
    }

    public static final class ComplicatedParametersException extends RemoteException
            implements SerializableErrorProvider<ComplicatedParametersParameters> {
        private static final Long serialVersionUID = 1L;

        private ComplicatedParametersSerializableError error;

        public ComplicatedParametersException(ComplicatedParametersSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public ComplicatedParametersSerializableError error() {
            return error;
        }
    }

    public static final class InvalidArgumentException extends RemoteException
            implements SerializableErrorProvider<InvalidArgumentParameters> {
        private static final Long serialVersionUID = 1L;

        private InvalidArgumentSerializableError error;

        public InvalidArgumentException(InvalidArgumentSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public InvalidArgumentSerializableError error() {
            return error;
        }
    }

    public static final class NotFoundException extends RemoteException
            implements SerializableErrorProvider<NotFoundParameters> {
        private static final Long serialVersionUID = 1L;

        private NotFoundSerializableError error;

        public NotFoundException(NotFoundSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public NotFoundSerializableError error() {
            return error;
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class ComplicatedParametersTestUtility {
        public static ComplicatedParametersSerializableError createSerializableError(
                String errorInstanceId, ComplicatedParametersParameters parameters) {
            return new ComplicatedParametersSerializableError(
                    COMPLICATED_PARAMETERS.code().name(), COMPLICATED_PARAMETERS.name(), errorInstanceId, parameters);
        }

        public static ComplicatedParametersException createException(
                String errorInstanceId, ComplicatedParametersParameters parameters) {
            return new ComplicatedParametersException(
                    createSerializableError(errorInstanceId, parameters), COMPLICATED_PARAMETERS.httpErrorCode());
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class InvalidArgumentTestUtility {
        public static InvalidArgumentSerializableError createSerializableError(
                String errorInstanceId, InvalidArgumentParameters parameters) {
            return new InvalidArgumentSerializableError(
                    INVALID_ARGUMENT.code().name(), INVALID_ARGUMENT.name(), errorInstanceId, parameters);
        }

        public static InvalidArgumentException createException(
                String errorInstanceId, InvalidArgumentParameters parameters) {
            return new InvalidArgumentException(
                    createSerializableError(errorInstanceId, parameters), INVALID_ARGUMENT.httpErrorCode());
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class NotFoundTestUtility {
        public static NotFoundSerializableError createSerializableError(
                String errorInstanceId, NotFoundParameters parameters) {
            return new NotFoundSerializableError(
                    NOT_FOUND.code().name(), NOT_FOUND.name(), errorInstanceId, parameters);
        }

        public static NotFoundException createException(String errorInstanceId, NotFoundParameters parameters) {
            return new NotFoundException(
                    createSerializableError(errorInstanceId, parameters), NOT_FOUND.httpErrorCode());
        }
    }
}
