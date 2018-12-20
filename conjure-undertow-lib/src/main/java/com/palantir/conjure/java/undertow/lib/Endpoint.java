/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

/**
 * Creates a {@link Routable} which may be registered with a web server. The server is responsible
 * for providing a {@link HandlerContext} allowing API implementors to register APIs using
 * <code>server.api(MyServiceRoutableFactory.of(myServiceImpl)</code>.
 */
public interface Endpoint {

    Routable create(HandlerContext context);

}
