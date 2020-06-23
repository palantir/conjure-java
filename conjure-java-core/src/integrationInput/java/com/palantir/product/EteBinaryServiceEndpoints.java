package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceHandlerGenerator")
public final class EteBinaryServiceEndpoints implements UndertowService {
    private final UndertowEteBinaryService delegate;

    private EteBinaryServiceEndpoints(UndertowEteBinaryService delegate) {
        this.delegate = delegate;
    }

    /**
     * @Deprecated: You can now use {@link UndertowEteBinaryService} directly as it implements {@link UndertowService}.
     */
    @Deprecated
    public static UndertowService of(UndertowEteBinaryService delegate) {
        return new EteBinaryServiceEndpoints(delegate);
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime runtime) {
        return delegate.endpoints(runtime);
    }
}
