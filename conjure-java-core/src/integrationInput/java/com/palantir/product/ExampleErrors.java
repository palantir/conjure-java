package com.palantir.product;

import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
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

    public static ExampleErrorException exampleError() {
        return new ExampleErrorException(EXAMPLE_ERROR);
    }

    public static ExampleErrorException exampleError(Throwable cause) {
        return new ExampleErrorException(EXAMPLE_ERROR, cause);
    }

    /**
     * Throws a {@link ServiceException} of type ExampleError when {@code shouldThrow} is true.
     * @param shouldThrow Cause the method to throw when true
     */
    public static void throwIfExampleError(boolean shouldThrow) throws ExampleErrorException {
        if (shouldThrow) {
            throw exampleError();
        }
    }

    public static final class ExampleErrorException extends CheckedServiceException {
        ExampleErrorException(ErrorType errorType, Arg<?>... parameters) {
            super(errorType, parameters);
        }

        ExampleErrorException(ErrorType errorType, @Nullable Throwable cause, Arg<?>... parameters) {
            super(errorType, cause, parameters);
        }
    }
}
