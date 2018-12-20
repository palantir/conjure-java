/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

import io.undertow.server.RoutingHandler;

/**
 * Provides registration functionality for Undertow services. A service implementing this interface is given the
 * opportunity to {@link #register} its (path, implementation handler) pairs with the given
 * {@link RoutingHandler Undertow router}.
 */
public interface Routable {
    void register(RoutingRegistry routingRegistry);
}
