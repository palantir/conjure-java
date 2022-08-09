package com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.processing.Generated;

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

    private ConjureErrors() {}

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
            Throwable cause, @Safe String serviceName, @Unsafe Object serviceDef) {
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
            Throwable cause, @Safe String typeName, @Unsafe Object typeDef) {
        return new ServiceException(
                INVALID_TYPE_DEFINITION, cause, SafeArg.of("typeName", typeName), UnsafeArg.of("typeDef", typeDef));
    }

    /**
     * Throws a {@link ServiceException} of type InvalidServiceDefinition when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     * @param serviceName Name of the invalid service definition.
     * @param serviceDef Details of the invalid service definition.
     */
    public static void throwIfInvalidServiceDefinition(
            boolean shouldThrow, @Safe String serviceName, @Unsafe Object serviceDef) {
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
    public static void throwIfInvalidTypeDefinition(
            boolean shouldThrow, @Safe String typeName, @Unsafe Object typeDef) {
        if (shouldThrow) {
            throw invalidTypeDefinition(typeName, typeDef);
        }
    }

    /**
     * Returns true if the {@link RemoteException} is named Conjure:InvalidServiceDefinition
     */
    public static boolean isInvalidServiceDefinition(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return INVALID_SERVICE_DEFINITION
                .name()
                .equals(remoteException.getError().errorName());
    }

    /**
     * Returns true if the {@link RemoteException} is named Conjure:InvalidTypeDefinition
     */
    public static boolean isInvalidTypeDefinition(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return INVALID_TYPE_DEFINITION.name().equals(remoteException.getError().errorName());
    }
}
