package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
@ConjureService(name = "EmptyPathService", package_ = "com.palantir.product")
public interface UndertowEmptyPathService {
    /**
     * @apiNote {@code GET /}
     */
    @ConjureEndpoint(path = "/", method = "GET")
    boolean emptyPath();
}
