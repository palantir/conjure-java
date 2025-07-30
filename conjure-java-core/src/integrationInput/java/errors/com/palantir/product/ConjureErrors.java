package errors.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.palantir.conjure.java.api.errors.AbstractSerializableError;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /** Cause argument conflicts with reserved Throwable cause parameter. */
    public static final ErrorType CONFLICTING_CAUSE_SAFE_ARG =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:ConflictingCauseSafeArg");

    /** Cause argument conflicts with reserved Throwable cause parameter. */
    public static final ErrorType CONFLICTING_CAUSE_UNSAFE_ARG =
            ErrorType.create(ErrorType.Code.INTERNAL, "Conjure:ConflictingCauseUnsafeArg");

    /** Invalid Conjure service definition. */
    public static final ErrorType INVALID_SERVICE_DEFINITION =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:InvalidServiceDefinition");

    /** Invalid Conjure type definition. */
    public static final ErrorType INVALID_TYPE_DEFINITION =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:InvalidTypeDefinition");

    private ConjureErrors() {}

    public static ServiceException conflictingCauseSafeArg(@Safe String cause_, @Safe boolean shouldThrow_) {
        return new ServiceException(
                CONFLICTING_CAUSE_SAFE_ARG, SafeArg.of("cause", cause_), SafeArg.of("shouldThrow", shouldThrow_));
    }

    public static ServiceException conflictingCauseSafeArg(
            @Nullable Throwable cause, @Safe String cause_, @Safe boolean shouldThrow_) {
        return new ServiceException(
                CONFLICTING_CAUSE_SAFE_ARG,
                cause,
                SafeArg.of("cause", cause_),
                SafeArg.of("shouldThrow", shouldThrow_));
    }

    public static ServiceException conflictingCauseUnsafeArg(@Unsafe String cause_, @Unsafe boolean shouldThrow_) {
        return new ServiceException(
                CONFLICTING_CAUSE_UNSAFE_ARG, UnsafeArg.of("cause", cause_), UnsafeArg.of("shouldThrow", shouldThrow_));
    }

    public static ServiceException conflictingCauseUnsafeArg(
            @Nullable Throwable cause, @Unsafe String cause_, @Unsafe boolean shouldThrow_) {
        return new ServiceException(
                CONFLICTING_CAUSE_UNSAFE_ARG,
                cause,
                UnsafeArg.of("cause", cause_),
                UnsafeArg.of("shouldThrow", shouldThrow_));
    }

    /**
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    public static ServiceException invalidServiceDefinition(@Safe String serviceName, @Unsafe Object serviceDef) {
        return new ServiceException(
                INVALID_SERVICE_DEFINITION,
                SafeArg.of("serviceName", serviceName),
                UnsafeArg.of("serviceDef", serviceDef));
    }

    /**
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    public static ServiceException invalidServiceDefinition(
            @Nullable Throwable cause, @Safe String serviceName, @Unsafe Object serviceDef) {
        return new ServiceException(
                INVALID_SERVICE_DEFINITION,
                cause,
                SafeArg.of("serviceName", serviceName),
                UnsafeArg.of("serviceDef", serviceDef));
    }

    public static ServiceException invalidTypeDefinition(@Safe String typeName, @Unsafe Object typeDef) {
        return new ServiceException(
                INVALID_TYPE_DEFINITION, SafeArg.of("typeName", typeName), UnsafeArg.of("typeDef", typeDef));
    }

    public static ServiceException invalidTypeDefinition(
            @Nullable Throwable cause, @Safe String typeName, @Unsafe Object typeDef) {
        return new ServiceException(
                INVALID_TYPE_DEFINITION, cause, SafeArg.of("typeName", typeName), UnsafeArg.of("typeDef", typeDef));
    }

    /**
     * Throws a {@link ServiceException} of type ConflictingCauseSafeArg when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param cause_
     * @param shouldThrow_
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfConflictingCauseSafeArg(
            boolean shouldThrow, @Safe String cause_, @Safe boolean shouldThrow_) {
        if (shouldThrow) {
            throw conflictingCauseSafeArg(cause_, shouldThrow_);
        }
    }

    /**
     * Throws a {@link ServiceException} of type ConflictingCauseUnsafeArg when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param cause_
     * @param shouldThrow_
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfConflictingCauseUnsafeArg(
            boolean shouldThrow, @Unsafe String cause_, @Unsafe boolean shouldThrow_) {
        if (shouldThrow) {
            throw conflictingCauseUnsafeArg(cause_, shouldThrow_);
        }
    }

    /**
     * Throws a {@link ServiceException} of type InvalidServiceDefinition when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfInvalidServiceDefinition(
            boolean shouldThrow, @Safe String serviceName, @Unsafe Object serviceDef) {
        if (shouldThrow) {
            throw invalidServiceDefinition(serviceName, serviceDef);
        }
    }

    /**
     * Throws a {@link ServiceException} of type InvalidTypeDefinition when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param typeName
     * @param typeDef
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfInvalidTypeDefinition(
            boolean shouldThrow, @Safe String typeName, @Unsafe Object typeDef) {
        if (shouldThrow) {
            throw invalidTypeDefinition(typeName, typeDef);
        }
    }

    /** Returns true if the {@link RemoteException} is named Conjure:ConflictingCauseSafeArg */
    public static boolean isConflictingCauseSafeArg(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return CONFLICTING_CAUSE_SAFE_ARG
                .name()
                .equals(remoteException.getError().errorName());
    }

    /** Returns true if the {@link RemoteException} is named Conjure:ConflictingCauseUnsafeArg */
    public static boolean isConflictingCauseUnsafeArg(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return CONFLICTING_CAUSE_UNSAFE_ARG
                .name()
                .equals(remoteException.getError().errorName());
    }

    /** Returns true if the {@link RemoteException} is named Conjure:InvalidServiceDefinition */
    public static boolean isInvalidServiceDefinition(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return INVALID_SERVICE_DEFINITION
                .name()
                .equals(remoteException.getError().errorName());
    }

    /** Returns true if the {@link RemoteException} is named Conjure:InvalidTypeDefinition */
    public static boolean isInvalidTypeDefinition(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return INVALID_TYPE_DEFINITION.name().equals(remoteException.getError().errorName());
    }

    public static record InvalidServiceDefinitionParams(
            @JsonProperty("serviceName") @Safe String serviceName,
            @JsonProperty("serviceDef") @Unsafe Object serviceDef) {}

    public static final class InvalidServiceDefinitionSerializableError
            extends AbstractSerializableError<InvalidServiceDefinitionParams> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        InvalidServiceDefinitionSerializableError(
                @JsonProperty("errorCode") @Safe String errorCode,
                @JsonProperty("errorName") @Safe String errorName,
                @JsonProperty("errorInstanceId") @Safe String errorInstanceId,
                @JsonProperty("parameters") InvalidServiceDefinitionParams parameters) {
            super(errorCode, errorName, errorInstanceId, parameters);
        }

        public SerializableError toSerializableError() {
            return SerializableError.builder()
                    .errorCode(errorCode())
                    .errorName(errorName())
                    .errorInstanceId(errorInstanceId())
                    .putParameters(
                            "serviceName", Objects.toString(errorParameters().serviceName()))
                    .putParameters(
                            "serviceDef", Objects.toString(errorParameters().serviceDef()))
                    .build();
        }
    }

    public static final class InvalidServiceDefinitionException extends RemoteException {
        private InvalidServiceDefinitionSerializableError error;
        private int status;

        public InvalidServiceDefinitionException(InvalidServiceDefinitionSerializableError error, int status) {
            super(error.toSerializableError(), status);
            this.error = error;
            this.status = status;
        }

        public InvalidServiceDefinitionSerializableError error() {
            return error;
        }
    }
}
