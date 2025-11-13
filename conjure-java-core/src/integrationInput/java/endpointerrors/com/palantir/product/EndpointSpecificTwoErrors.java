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
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class EndpointSpecificTwoErrors {
    /** An error in a different namespace. */
    public static final ErrorType DIFFERENT_NAMESPACE =
            ErrorType.create(ErrorType.Code.INTERNAL, "EndpointSpecificTwo:DifferentNamespace");

    private EndpointSpecificTwoErrors() {}

    /** Returns true if the {@link RemoteException} is named EndpointSpecificTwo:DifferentNamespace */
    public static boolean isDifferentNamespace(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return DIFFERENT_NAMESPACE.name().equals(remoteException.getError().errorName());
    }

    public static record DifferentNamespaceParameters() {}

    public static final class DifferentNamespaceSerializableError
            extends AbstractSerializableError<DifferentNamespaceParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        DifferentNamespaceSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") DifferentNamespaceParameters parameters) {
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

    public static final class DifferentNamespaceException extends RemoteException
            implements SerializableErrorProvider<DifferentNamespaceParameters> {
        private DifferentNamespaceSerializableError error;

        public DifferentNamespaceException(DifferentNamespaceSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public DifferentNamespaceSerializableError error() {
            return error;
        }
    }

    /**
     * This utility class is provided to make creating error-specific exceptions easier for tests. It should not be used
     * outside of tests!
     */
    public static final class DifferentNamespaceTestUtility {
        public static DifferentNamespaceSerializableError createSerializableError(
                String errorInstanceId, DifferentNamespaceParameters parameters) {
            return new DifferentNamespaceSerializableError(
                    DIFFERENT_NAMESPACE.code().name(), DIFFERENT_NAMESPACE.name(), errorInstanceId, parameters);
        }

        public static DifferentNamespaceException createException(
                String errorInstanceId, DifferentNamespaceParameters parameters) {
            return new DifferentNamespaceException(
                    createSerializableError(errorInstanceId, parameters), DIFFERENT_NAMESPACE.httpErrorCode());
        }
    }
}
