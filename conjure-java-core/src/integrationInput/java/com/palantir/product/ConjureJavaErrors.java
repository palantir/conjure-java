package com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeLoggable;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.Contract;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ConjureJavaErrors {
    /** Failed to compile Conjure definition to Java code. */
    public static final ErrorType JAVA_COMPILATION_FAILED =
            ErrorType.create(ErrorType.Code.INTERNAL, "ConjureJava:JavaCompilationFailed");

    private ConjureJavaErrors() {}

    public static ServiceException javaCompilationFailed() {
        return new ServiceException(JAVA_COMPILATION_FAILED);
    }

    public static ServiceException javaCompilationFailed(@Nullable Throwable cause) {
        return new ServiceException(JAVA_COMPILATION_FAILED, cause);
    }

    public static JavaCompilationFailed javaCompilationFailedNew() {
        return new JavaCompilationFailed(JAVA_COMPILATION_FAILED);
    }

    public static JavaCompilationFailed javaCompilationFailedNew(@Nullable Throwable cause) {
        return new JavaCompilationFailed(JAVA_COMPILATION_FAILED, cause);
    }

    /**
     * Throws a {@link ServiceException} of type JavaCompilationFailed when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     */
    @Contract("true -> fail")
    public static void throwIfJavaCompilationFailed(boolean shouldThrow) {
        if (shouldThrow) {
            throw javaCompilationFailed();
        }
    }

    @Contract("true -> fail")
    public static void throwIfJavaCompilationFailedNew(boolean shouldThrow) {
        if (shouldThrow) {
            throw javaCompilationFailedNew();
        }
    }

    public static final class JavaCompilationFailed extends RuntimeException implements SafeLoggable {
        private JavaCompilationFailed(ErrorType errorType, Arg<?>... parameters) {}

        private JavaCompilationFailed(ErrorType errorType, @Nullable Throwable cause, Arg<?>... parameters) {}

        // TODO(pm): let's have another interface that extends RuntimeException implements SafeLoggeable, and implements
        //  these two methods. Should be the same for all generated exceptions.
        @Override
        public @Safe String getLogMessage() {
            return "JavaCompilatonFailed: <message>";
        }

        @Override
        public List<Arg<?>> getArgs() {
            return List.of();
        }
    }

    /** Returns true if the {@link RemoteException} is named ConjureJava:JavaCompilationFailed */
    public static boolean isJavaCompilationFailed(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return JAVA_COMPILATION_FAILED.name().equals(remoteException.getError().errorName());
    }
}
