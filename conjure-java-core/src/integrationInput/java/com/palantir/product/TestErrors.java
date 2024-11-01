package com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class TestErrors {
    public static final ErrorType INVALID_ARGUMENT =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Test:InvalidArgument");

    public static final ErrorType NOT_FOUND = ErrorType.create(ErrorType.Code.NOT_FOUND, "Test:NotFound");

    private TestErrors() {}

    public static ServiceException invalidArgument(@Safe String field, @Unsafe String value) {
        return new ServiceException(INVALID_ARGUMENT, SafeArg.of("field", field), UnsafeArg.of("value", value));
    }

    public static ServiceException invalidArgument(
            @Nullable Throwable cause, @Safe String field, @Unsafe String value) {
        return new ServiceException(INVALID_ARGUMENT, cause, SafeArg.of("field", field), UnsafeArg.of("value", value));
    }

    public static ServiceException notFound(@Safe String resource) {
        return new ServiceException(NOT_FOUND, SafeArg.of("resource", resource));
    }

    public static ServiceException notFound(@Nullable Throwable cause, @Safe String resource) {
        return new ServiceException(NOT_FOUND, cause, SafeArg.of("resource", resource));
    }

    /**
     * Throws a {@link ServiceException} of type InvalidArgument when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param field
     * @param value
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfInvalidArgument(boolean shouldThrow, @Safe String field, @Unsafe String value) {
        if (shouldThrow) {
            throw invalidArgument(field, value);
        }
    }

    /**
     * Throws a {@link ServiceException} of type NotFound when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param resource
     */
    @Contract("true, _ -> fail")
    public static void throwIfNotFound(boolean shouldThrow, @Safe String resource) {
        if (shouldThrow) {
            throw notFound(resource);
        }
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
}
