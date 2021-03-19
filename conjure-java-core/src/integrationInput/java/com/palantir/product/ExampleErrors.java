package com.palantir.product;

import com.palantir.conjure.java.api.errors.CheckedRemoteException;
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Arg;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class ExampleErrors {
    /**
     * This should be a checked exception.
     */
    public static final ErrorType EXAMPLE_ERROR =
            ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Example:ExampleError");

    private ExampleErrors() {}

    public static ExampleErrorServiceException exampleError() {
        return new ExampleErrorServiceException(EXAMPLE_ERROR);
    }

    public static ExampleErrorServiceException exampleError(Throwable cause) {
        return new ExampleErrorServiceException(EXAMPLE_ERROR, cause);
    }

    /**
     * Throws a {@link ServiceException} of type ExampleError when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     */
    public static void throwIfExampleError(boolean shouldThrow) throws ExampleErrorServiceException {
        if (shouldThrow) {
            throw exampleError();
        }
    }

    public static final class ExampleErrorServiceException extends CheckedServiceException {
        private ExampleErrorServiceException(ErrorType errorType, Arg<?>... parameters) {
            super(errorType, parameters);
        }

        private ExampleErrorServiceException(ErrorType errorType, @Nullable Throwable cause, Arg<?>... parameters) {
            super(errorType, cause, parameters);
        }
    }

    static final class ExampleErrorRemoteException extends CheckedRemoteException {
        ExampleErrorRemoteException(RemoteException remote) {
            super(remote.getError(), remote.getStatus());
        }
    }
}
