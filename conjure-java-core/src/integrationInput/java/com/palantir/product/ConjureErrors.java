package com.palantir.product;

import com.palantir.conjure.java.api.errors.CheckedRemoteException;
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureErrors {
    /**
     * Invalid Conjure service definition.
     */
    public static final ErrorType INVALID_SERVICE_DEFINITION =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:InvalidServiceDefinition");

    /**
     * Invalid Conjure type definition.
     */
    public static final ErrorType INVALID_TYPE_DEFINITION =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:InvalidTypeDefinition");

    /**
     * This should be a checked exception.
     */
    public static final ErrorType SHOULD_BE_CHECKED =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Conjure:ShouldBeChecked");

    private ConjureErrors() {}

    /**
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    public static ServiceException invalidServiceDefinition(String serviceName, Object serviceDef) {
        return new ServiceException(
                INVALID_SERVICE_DEFINITION,
                SafeArg.of("serviceName", serviceName),
                UnsafeArg.of("serviceDef", serviceDef));
    }

    /**
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    public static ServiceException invalidServiceDefinition(Throwable cause, String serviceName, Object serviceDef) {
        return new ServiceException(
                INVALID_SERVICE_DEFINITION,
                cause,
                SafeArg.of("serviceName", serviceName),
                UnsafeArg.of("serviceDef", serviceDef));
    }

    public static ServiceException invalidTypeDefinition(String typeName, Object typeDef) {
        return new ServiceException(
                INVALID_TYPE_DEFINITION, SafeArg.of("typeName", typeName), UnsafeArg.of("typeDef", typeDef));
    }

    public static ServiceException invalidTypeDefinition(Throwable cause, String typeName, Object typeDef) {
        return new ServiceException(
                INVALID_TYPE_DEFINITION, cause, SafeArg.of("typeName", typeName), UnsafeArg.of("typeDef", typeDef));
    }

    public static ShouldBeCheckedServiceException shouldBeChecked() {
        return new ShouldBeCheckedServiceException(SHOULD_BE_CHECKED);
    }

    public static ShouldBeCheckedServiceException shouldBeChecked(Throwable cause) {
        return new ShouldBeCheckedServiceException(SHOULD_BE_CHECKED, cause);
    }

    /**
     * Throws a {@link ServiceException} of type InvalidServiceDefinition when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    public static void throwIfInvalidServiceDefinition(boolean shouldThrow, String serviceName, Object serviceDef) {
        if (shouldThrow) {
            throw invalidServiceDefinition(serviceName, serviceDef);
        }
    }

    /**
     * Throws a {@link ServiceException} of type InvalidTypeDefinition when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     * @param typeName
     * @param typeDef
     */
    public static void throwIfInvalidTypeDefinition(boolean shouldThrow, String typeName, Object typeDef) {
        if (shouldThrow) {
            throw invalidTypeDefinition(typeName, typeDef);
        }
    }

    /**
     * Throws a {@link ServiceException} of type ShouldBeChecked when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     */
    public static void throwIfShouldBeChecked(boolean shouldThrow) throws ShouldBeCheckedServiceException {
        if (shouldThrow) {
            throw shouldBeChecked();
        }
    }

    public static final class ShouldBeCheckedServiceException extends CheckedServiceException {
        private ShouldBeCheckedServiceException(ErrorType errorType, Arg<?>... parameters) {
            super(errorType, parameters);
        }

        private ShouldBeCheckedServiceException(ErrorType errorType, @Nullable Throwable cause, Arg<?>... parameters) {
            super(errorType, cause, parameters);
        }
    }

    public static final class ShouldBeCheckedRemoteException extends CheckedRemoteException {
        ShouldBeCheckedRemoteException(RemoteException remote) {
            super(remote.getError(), remote.getStatus());
        }
    }
}
