/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

import io.undertow.server.HttpHandler;

/**
 * Add handlers to this registry by calling the method corresponding to the http method
 * with {@link HttpHandler} and the path template of the "route".
 * The purpose of this layer of indirection on top of a {@link EndpointRegistry} is to restrict the exposed API
 * and allow the usage of registered paths for other means than registering to a
 * {@link io.undertow.server.RoutingHandler} (e.g" checking for overlapping paths)
 */
public interface EndpointRegistry {

    void add(Endpoint description, HttpHandler handler);

}
