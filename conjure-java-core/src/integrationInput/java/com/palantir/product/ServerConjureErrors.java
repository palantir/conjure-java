package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class ServerConjureErrors {
    private ServerConjureErrors() {}

    public static InvalidTypeDefinition invalidTypeDefinition(@Safe String typeName, @Unsafe Object typeDef) {
        return new InvalidTypeDefinition(typeName, typeDef);
    }

    public static InvalidTypeDefinition invalidTypeDefinition(
            @Nullable Throwable cause, @Safe String typeName, @Unsafe Object typeDef) {
        return new InvalidTypeDefinition(cause, typeName, typeDef);
    }

    public static final class InvalidTypeDefinition extends CheckedServiceException {
        private InvalidTypeDefinition(@Safe String typeName, @Unsafe Object typeDef) {
            super(
                    com.palantir.product.ConjureErrors.INVALID_TYPE_DEFINITION,
                    SafeArg.of("typeName", typeName),
                    UnsafeArg.of("typeDef", typeDef));
        }

        private InvalidTypeDefinition(@Nullable Throwable cause, @Safe String typeName, @Unsafe Object typeDef) {
            super(
                    com.palantir.product.ConjureErrors.INVALID_TYPE_DEFINITION,
                    cause,
                    SafeArg.of("typeName", typeName),
                    UnsafeArg.of("typeDef", typeDef));
        }
    }
}
