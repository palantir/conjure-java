package com.palantir.product;

import com.palantir.tokens.auth.AuthHeader;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowNameCollisionService {
    String int_(
            AuthHeader authHeader,
            String authHeader_,
            String long_,
            String runtime,
            String serializer,
            String deserializer,
            String delegate,
            String result);
}
