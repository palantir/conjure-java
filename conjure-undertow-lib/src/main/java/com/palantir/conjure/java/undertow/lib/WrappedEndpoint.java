/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.java.undertow.lib;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import java.util.function.BiFunction;

/**
 * Wraps all the {@link HttpHandler} of an {@link Endpoint} with an {@link HandlerWrapper}.
 * Useful when you need to add a custom wrapper to an individual endpoint.
 */
public final class WrappedEndpoint implements Endpoint {

    private final Endpoint endpoint;
    private final BiFunction<EndpointDetails, HttpHandler, HttpHandler> wrapper;

    private WrappedEndpoint(Endpoint endpoint, BiFunction<EndpointDetails, HttpHandler, HttpHandler> wrapper) {
        this.endpoint = endpoint;
        this.wrapper = wrapper;
    }

    public WrappedEndpoint(Endpoint endpoint, HandlerWrapper wrapper) {
        this(endpoint, (ignored, handler) -> wrapper.wrap(handler));
    }

    public static WrappedEndpoint of(Endpoint endpoint, HandlerWrapper wrapper) {
        return new WrappedEndpoint(endpoint, wrapper);
    }

    @Override
    public Routable create(HandlerContext context) {
        return routingRegistry ->
                endpoint.create(context)
                        .register(new WrappedRoutingRegistry(routingRegistry, wrapper));
    }
}
