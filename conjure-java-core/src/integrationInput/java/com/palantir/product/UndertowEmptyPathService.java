package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowEmptyPathService extends UndertowService {
    /**
     * @apiNote {@code GET /}
     */
    boolean emptyPath();

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return EmptyPathServiceEndpoints.of(this).endpoints(runtime);
    }
}
