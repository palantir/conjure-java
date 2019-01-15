package com.palantir.conjure.verification.server;

import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowAutoDeserializeConfirmService {
    /**
     * Send the response received for positive test cases here to verify that it has been serialized
     * and deserialized properly.
     */
    void confirm(EndpointName endpoint, int index, Object body);
}
