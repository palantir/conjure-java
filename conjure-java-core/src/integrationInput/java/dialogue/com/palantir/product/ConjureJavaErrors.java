package dialogue.com.palantir.product;

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

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureJavaErrors {
    /** Failed to compile Conjure definition to Java code. */
    public static final ErrorType JAVA_COMPILATION_FAILED =
            ErrorType.create(ErrorType.Code.INTERNAL, "ConjureJava:JavaCompilationFailed");

    private ConjureJavaErrors() {}

    public static ServiceException javaCompilationFailed() {
        return new ServiceException(JAVA_COMPILATION_FAILED);
    }

    public static ServiceException javaCompilationFailed(@Nullable Throwable cause) {
        return new ServiceException(JAVA_COMPILATION_FAILED, cause);
    }

    /**
     * Throws a {@link ServiceException} of type JavaCompilationFailed when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     */
    public static void throwIfJavaCompilationFailed(boolean shouldThrow) {
        if (shouldThrow) {
            throw javaCompilationFailed();
        }
    }

    /** Returns true if the {@link RemoteException} is named ConjureJava:JavaCompilationFailed */
    public static boolean isJavaCompilationFailed(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return JAVA_COMPILATION_FAILED.name().equals(remoteException.getError().errorName());
    }

    public static record JavaCompilationFailedParameters() {}

    public static final class JavaCompilationFailedSerializableError
            extends AbstractSerializableError<JavaCompilationFailedParameters> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        JavaCompilationFailedSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") JavaCompilationFailedParameters parameters) {
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

    public static final class JavaCompilationFailedException extends RemoteException {
        private JavaCompilationFailedSerializableError error;

        public JavaCompilationFailedException(JavaCompilationFailedSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
        }

        public JavaCompilationFailedSerializableError error() {
            return error;
        }
    }
}
