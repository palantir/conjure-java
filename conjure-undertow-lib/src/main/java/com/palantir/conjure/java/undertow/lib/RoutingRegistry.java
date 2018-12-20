/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

import io.undertow.server.HttpHandler;

/**
 * Add handlers to this registry by calling the method corresponding to the http method
 * with {@link HttpHandler} and the path template of the "route".
 * The purpose of this layer of indirection on top of a {@link RoutingRegistry} is to restrict the exposed API
 * and allow the usage of registered paths for other means than registering to a
 * {@link io.undertow.server.RoutingHandler} (e.g" checking for overlapping paths)
 */
public interface RoutingRegistry {

    RoutingRegistry get(String template, HttpHandler handler);

    RoutingRegistry post(String template, HttpHandler handler);

    RoutingRegistry put(String template, HttpHandler handler);

    RoutingRegistry delete(String template, HttpHandler handler);
}
