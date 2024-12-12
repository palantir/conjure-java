package com.palantir.product;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.logsafe.Preconditions;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.ErrorGenerator")
public final class EndpointSpecificTwoErrors {
    /** An error in a different namespace. */
    public static final ErrorType DIFFERENT_NAMESPACE =
            ErrorType.create(ErrorType.Code.INTERNAL, "EndpointSpecificTwo:DifferentNamespace");

    private EndpointSpecificTwoErrors() {}

    /** Returns true if the {@link RemoteException} is named EndpointSpecificTwo:DifferentNamespace */
    public static boolean isDifferentNamespace(RemoteException remoteException) {
        Preconditions.checkNotNull(remoteException, "remote exception must not be null");
        return DIFFERENT_NAMESPACE.name().equals(remoteException.getError().errorName());
    }

    public static record DifferentNamespaceParameters() {}
}
