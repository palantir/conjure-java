package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class NameCollisionServiceEndpoints implements UndertowService {
    private final UndertowNameCollisionService delegate;

    private NameCollisionServiceEndpoints(UndertowNameCollisionService delegate) {
        this.delegate = delegate;
    }

    /**
     * @Deprecated: You can now use {@link UndertowNameCollisionService} directly as it implements {@link UndertowService}.
     */
    @Deprecated
    public static UndertowService of(UndertowNameCollisionService delegate) {
        return new NameCollisionServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return delegate.endpoints(runtime);
    }
}
