package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.services.CheckedErrorGenerator")
public final class ServerTestErrors {
    private ServerTestErrors() {}

    public final class InvalidArgument extends CheckedServiceException {
        public InvalidArgument(@Safe String field, @Unsafe String value) {
            super(
                    com.palantir.product.TestErrors.INVALID_ARGUMENT,
                    SafeArg.of("field", field),
                    UnsafeArg.of("value", value));
        }
    }

    public final class NotFound extends CheckedServiceException {
        public NotFound(@Safe String resource) {
            super(com.palantir.product.TestErrors.NOT_FOUND, SafeArg.of("resource", resource));
        }
    }
}
