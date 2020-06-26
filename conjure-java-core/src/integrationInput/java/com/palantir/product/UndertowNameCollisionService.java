package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowNameCollisionService {
    /**
     * @apiNote POST /{runtime}
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
}
