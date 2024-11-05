package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class ServerEndpointSpecificErrors {
    private ServerEndpointSpecificErrors() {}

    public static EndpointError endpointError(@Safe String typeName, @Unsafe Object typeDef) {
        return new EndpointError(typeName, typeDef);
    }

    public static EndpointError endpointError(
            @Nullable Throwable cause, @Safe String typeName, @Unsafe Object typeDef) {
        return new EndpointError(cause, typeName, typeDef);
    }

    public static final class EndpointError extends CheckedServiceException {
        private EndpointError(@Safe String typeName, @Unsafe Object typeDef) {
            super(
                    com.palantir.product.EndpointSpecificErrors.ENDPOINT_ERROR,
                    SafeArg.of("typeName", typeName),
                    UnsafeArg.of("typeDef", typeDef));
        }

        private EndpointError(@Nullable Throwable cause, @Safe String typeName, @Unsafe Object typeDef) {
            super(
                    com.palantir.product.EndpointSpecificErrors.ENDPOINT_ERROR,
                    cause,
                    SafeArg.of("typeName", typeName),
                    UnsafeArg.of("typeDef", typeDef));
        }
    }
}
