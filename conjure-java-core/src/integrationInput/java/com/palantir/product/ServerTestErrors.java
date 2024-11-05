package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class ServerTestErrors {
    private ServerTestErrors() {}

    public static InvalidArgument invalidArgument(@Safe String field, @Unsafe String value) {
        return new InvalidArgument(field, value);
    }

    public static InvalidArgument invalidArgument(@Nullable Throwable cause, @Safe String field, @Unsafe String value) {
        return new InvalidArgument(cause, field, value);
    }

    public static NotFound notFound(@Safe String resource) {
        return new NotFound(resource);
    }

    public static NotFound notFound(@Nullable Throwable cause, @Safe String resource) {
        return new NotFound(cause, resource);
    }

    public static final class InvalidArgument extends CheckedServiceException {
        private InvalidArgument(@Safe String field, @Unsafe String value) {
            super(
                    com.palantir.product.TestErrors.INVALID_ARGUMENT,
                    SafeArg.of("field", field),
                    UnsafeArg.of("value", value));
        }

        private InvalidArgument(@Nullable Throwable cause, @Safe String field, @Unsafe String value) {
            super(
                    com.palantir.product.TestErrors.INVALID_ARGUMENT,
                    cause,
                    SafeArg.of("field", field),
                    UnsafeArg.of("value", value));
        }
    }

    public static final class NotFound extends CheckedServiceException {
        private NotFound(@Safe String resource) {
            super(com.palantir.product.TestErrors.NOT_FOUND, SafeArg.of("resource", resource));
        }

        private NotFound(@Nullable Throwable cause, @Safe String resource) {
            super(com.palantir.product.TestErrors.NOT_FOUND, cause, SafeArg.of("resource", resource));
        }
    }
}
