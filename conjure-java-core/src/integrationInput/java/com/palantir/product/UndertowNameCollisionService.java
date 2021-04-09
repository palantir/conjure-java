package com.palantir.product;

import com.palantir.conjure.java.lib.internal.ConjureEndpoint;
import com.palantir.conjure.java.lib.internal.ConjureService;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
@ConjureService(name = "NameCollisionService", package_ = "com.palantir.product")
public interface UndertowNameCollisionService {
    /**
     * @apiNote {@code POST /{runtime}}
     */
    @ConjureEndpoint(path = "/{runtime}", method = "POST")
    String int_(
            AuthHeader authHeader,
            String serializer,
            String runtime,
            String authHeader_,
            String long_,
            String delegate,
            String result,
            String deserializer);

    /**
     * @apiNote {@code POST /no/context}
     */
    @ConjureEndpoint(path = "/no/context", method = "POST")
    void noContext(String requestContext);

    /**
     * @apiNote {@code POST /context}
     */
    @ConjureEndpoint(path = "/context", method = "POST")
    void context(String requestContext_, RequestContext requestContext);
}
