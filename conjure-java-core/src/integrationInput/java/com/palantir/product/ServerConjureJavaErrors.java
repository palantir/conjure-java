package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class ServerConjureJavaErrors {
    private ServerConjureJavaErrors() {}

    public static JavaCompilationFailed javaCompilationFailed() {
        return new JavaCompilationFailed();
    }

    public static JavaCompilationFailed javaCompilationFailed(@Nullable Throwable cause) {
        return new JavaCompilationFailed(cause);
    }

    public static final class JavaCompilationFailed extends CheckedServiceException {
        private JavaCompilationFailed() {
            super(com.palantir.product.ConjureJavaErrors.JAVA_COMPILATION_FAILED);
        }

        private JavaCompilationFailed(@Nullable Throwable cause) {
            super(com.palantir.product.ConjureJavaErrors.JAVA_COMPILATION_FAILED, cause);
        }
    }
}
