package com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureJavaErrors {
    /**
     * Failed to compile Conjure definition to Java code.
     */
    public static final ErrorType JAVA_COMPILATION_FAILED =
            ErrorType.create(ErrorType.Code.INTERNAL, "ConjureJava:JavaCompilationFailed");

    private ConjureJavaErrors() {}

    public static ServiceException javaCompilationFailed() {
        return new ServiceException(JAVA_COMPILATION_FAILED);
    }

    public static ServiceException javaCompilationFailed(Throwable cause) {
        return new ServiceException(JAVA_COMPILATION_FAILED, cause);
    }

    /**
     * Throws a {@link ServiceException} of type JavaCompilationFailed when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     */
    public static void throwIfJavaCompilationFailed(boolean shouldThrow) {
        if (shouldThrow) {
            throw javaCompilationFailed();
        }
    }
}
