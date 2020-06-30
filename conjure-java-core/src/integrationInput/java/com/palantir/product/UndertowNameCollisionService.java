package com.palantir.product;

import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import com.palantir.tokens.auth.AuthHeader;
import java.util.List;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.services.UndertowServiceInterfaceGenerator")
public interface UndertowNameCollisionService extends UndertowService {
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

    @Override
    default List<Endpoint> endpoints(UndertowRuntime runtime) {
        return NameCollisionServiceEndpoints.of(this).endpoints(runtime);
    }
}
