package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class ServerEndpointErrorsTwoErrors {
    private ServerEndpointErrorsTwoErrors() {}

    public static DifferentNamespace differentNamespace() {
        return new DifferentNamespace();
    }

    public static DifferentNamespace differentNamespace(@Nullable Throwable cause) {
        return new DifferentNamespace(cause);
    }

    public static final class DifferentNamespace extends CheckedServiceException {
        private DifferentNamespace() {
            super(com.palantir.product.EndpointErrorsTwoErrors.DIFFERENT_NAMESPACE);
        }

        private DifferentNamespace(@Nullable Throwable cause) {
            super(com.palantir.product.EndpointErrorsTwoErrors.DIFFERENT_NAMESPACE, cause);
        }
    }
}
