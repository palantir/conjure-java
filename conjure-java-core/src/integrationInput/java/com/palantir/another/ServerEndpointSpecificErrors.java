package com.palantir.another;

import com.palantir.conjure.java.undertow.lib.CheckedServiceException;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.CheckedErrorGenerator")
public final class ServerEndpointSpecificErrors {
    private ServerEndpointSpecificErrors() {}

    public static DifferentPackage differentPackage() {
        return new DifferentPackage();
    }

    public static DifferentPackage differentPackage(@Nullable Throwable cause) {
        return new DifferentPackage(cause);
    }

    public static final class DifferentPackage extends CheckedServiceException {
        private DifferentPackage() {
            super(com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE);
        }

        private DifferentPackage(@Nullable Throwable cause) {
            super(com.palantir.another.EndpointSpecificErrors.DIFFERENT_PACKAGE, cause);
        }
    }
}
