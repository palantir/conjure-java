/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.RoutingRegistry;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.util.function.BiFunction;

/**
 * Default Conjure implementation of a {@link RoutingRegistry} that wraps and delegates to a {@link RoutingHandler}.
 */
public final class ConjureRoutingRegistry implements RoutingRegistry {

    private static final ImmutableList<BiFunction<String, HttpHandler, HttpHandler>> WRAPPERS =
            ImmutableList.<BiFunction<String, HttpHandler, HttpHandler>>of(
            // It is vitally important to never run blocking operations on the initial IO thread otherwise
            // the server will not process new requests. all handlers executed after BlockingHandler
            // use the larger task pool which is allowed to block. Any operation which sets thread
            // state (e.g. SLF4J MDC or Tracer) must execute on the blocking thread otherwise state
            // will not propagate to the wrapped service.
            (endpoint, handler) -> new BlockingHandler(handler),
            // Bearer token and trace handler must execute prior to the exception
            // to provide user and trace information on exceptions.
            (endpoint, handler) -> new BearerTokenLoggingHandler(handler),
            TraceHandler::new,
            (endpoint, handler) -> new ConjureExceptionHandler(handler)
    ).reverse();

    private final RoutingHandler routingHandler;

    public ConjureRoutingRegistry(RoutingHandler routingHandler) {
        this.routingHandler = routingHandler;
    }

    @Override
    public ConjureRoutingRegistry get(String template, HttpHandler handler) {
        return register(Methods.GET, template, handler);
    }

    @Override
    public ConjureRoutingRegistry post(String template, HttpHandler handler) {
        return register(Methods.POST, template, handler);
    }

    @Override
    public ConjureRoutingRegistry put(String template, HttpHandler handler) {
        return register(Methods.PUT, template, handler);
    }

    @Override
    public ConjureRoutingRegistry delete(String template, HttpHandler handler) {
        return register(Methods.DELETE, template, handler);
    }

    private ConjureRoutingRegistry register(HttpString method, String template, HttpHandler handler) {
        HttpHandler current = handler;
        String endpoint = method + " " + template;
        for (BiFunction<String, HttpHandler, HttpHandler> wrapper : WRAPPERS) {
            current = wrapper.apply(endpoint, current);
        }
        routingHandler.add(method, template, current);
        return this;
    }
}
