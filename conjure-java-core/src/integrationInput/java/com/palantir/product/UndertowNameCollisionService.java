package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowNameCollisionService {
    /**
     * @apiNote {@code POST /{runtime}}
     */
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
    void noContext(String requestContext);

    /**
     * @apiNote {@code POST /context}
     */
    void context(String requestContext_, RequestContext requestContext);
}
