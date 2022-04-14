package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.logsafe.Safe;
import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowNameCollisionService {
    /**
     * @apiNote {@code POST /{runtime}}
     */
    String int_(
            AuthHeader authHeader,
            @Safe String serializer,
            @Safe String runtime,
            @Safe String authHeader_,
            @Safe String long_,
            @Safe String delegate,
            @Safe String result,
            @Safe String deserializer);

    /**
     * @apiNote {@code POST /no/context}
     */
    void noContext(String requestContext);

    /**
     * @apiNote {@code POST /context}
     */
    void context(String requestContext_, RequestContext requestContext);
}
