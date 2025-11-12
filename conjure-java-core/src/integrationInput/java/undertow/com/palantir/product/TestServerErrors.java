package undertow.com.palantir.product;

import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.UnsafeArg;
import java.util.Map;
import javax.annotation.Nullable;
import org.jetbrains.annotations.Contract;

@ConjureGenerated("com.palantir.conjure.java.types.EndpointErrorGenerator")
public final class TestServerErrors {
    private TestServerErrors() {}

    public static ComplicatedParameters complicatedParameters(
            @Safe Map<Integer, ComplicatedObject> complicatedObjectMap) {
        return new ComplicatedParameters(complicatedObjectMap, null);
    }

    public static ComplicatedParameters complicatedParameters(
            @Safe Map<Integer, ComplicatedObject> complicatedObjectMap, @Nullable Throwable cause) {
        return new ComplicatedParameters(complicatedObjectMap, cause);
    }

    public static InvalidArgument invalidArgument(@Safe String field, @Unsafe String value) {
        return new InvalidArgument(field, value, null);
    }

    public static InvalidArgument invalidArgument(@Safe String field, @Unsafe String value, @Nullable Throwable cause) {
        return new InvalidArgument(field, value, cause);
    }

    public static NotFound notFound(@Safe String resource) {
        return new NotFound(resource, null);
    }

    public static NotFound notFound(@Safe String resource, @Nullable Throwable cause) {
        return new NotFound(resource, cause);
    }

    /**
     * Throws a {@link ComplicatedParameters} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param complicatedObjectMap
     */
    @Contract("true, _ -> fail")
    public static void throwIfComplicatedParameters(
            boolean shouldThrow, @Safe Map<Integer, ComplicatedObject> complicatedObjectMap) {
        if (shouldThrow) {
            throw complicatedParameters(complicatedObjectMap);
        }
    }

    /**
     * Throws a {@link InvalidArgument} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param field
     * @param value
     */
    @Contract("true, _, _ -> fail")
    public static void throwIfInvalidArgument(boolean shouldThrow, @Safe String field, @Unsafe String value) {
        if (shouldThrow) {
            throw invalidArgument(field, value);
        }
    }

    /**
     * Throws a {@link NotFound} when {@code shouldThrow} is true.
     *
     * @param shouldThrow Cause the method to throw when true
     * @param resource
     */
    @Contract("true, _ -> fail")
    public static void throwIfNotFound(boolean shouldThrow, @Safe String resource) {
        if (shouldThrow) {
            throw notFound(resource);
        }
    }

    public static final class ComplicatedParameters extends EndpointServiceException {
        private ComplicatedParameters(
                @Safe Map<Integer, ComplicatedObject> complicatedObjectMap, @Nullable Throwable cause) {
            super(TestErrors.COMPLICATED_PARAMETERS, cause, SafeArg.of("complicatedObjectMap", complicatedObjectMap));
        }
    }

    public static final class InvalidArgument extends EndpointServiceException {
        private InvalidArgument(@Safe String field, @Unsafe String value, @Nullable Throwable cause) {
            super(TestErrors.INVALID_ARGUMENT, cause, SafeArg.of("field", field), UnsafeArg.of("value", value));
        }
    }

    public static final class NotFound extends EndpointServiceException {
        private NotFound(@Safe String resource, @Nullable Throwable cause) {
            super(TestErrors.NOT_FOUND, cause, SafeArg.of("resource", resource));
        }
    }
}
