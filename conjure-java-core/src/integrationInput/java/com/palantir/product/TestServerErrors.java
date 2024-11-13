package com.palantir.product;

import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class TestServerErrors {
    private TestServerErrors() {}

    public static InvalidArgument invalidArgument(@Safe String field, @Unsafe String value) {
        return new InvalidArgument(field, value, null);
    }

    public static InvalidArgument invalidArgument(@Safe String field, @Unsafe String value, @Nullable Throwable cause) {
        return new InvalidArgument(field, value, cause);
    }

    public static NotFound notFound(@Safe String resource) {
        return new NotFound(resource, null);
    }

    public static NotFound notFound(@Safe String resource, @Nullable Throwable cause) {
        return new NotFound(resource, cause);
    }

    /**
     * Throws a {@link InvalidArgument} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param field
     * @param value
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfInvalidArgument(boolean shouldThrow, @Safe String field, @Unsafe String value)
            throws InvalidArgument {
        if (shouldThrow) {
            throw invalidArgument(field, value);
        }
    }

    /**
     * Throws a {@link NotFound} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param resource
     */
    @Contract("true, _ -> fail")
    public static void throwIfNotFound(boolean shouldThrow, @Safe String resource) throws NotFound {
        if (shouldThrow) {
            throw notFound(resource);
        }
    }

    public static final class InvalidArgument extends CheckedServiceException {
        private InvalidArgument(@Safe String field, @Unsafe String value, @Nullable Throwable cause) {
            super(TestErrors.INVALID_ARGUMENT, cause, SafeArg.of("field", field), UnsafeArg.of("value", value));
        }
    }

    public static final class NotFound extends CheckedServiceException {
        private NotFound(@Safe String resource, @Nullable Throwable cause) {
            super(TestErrors.NOT_FOUND, cause, SafeArg.of("resource", resource));
        }
    }
}
