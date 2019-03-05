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

import io.undertow.server.HttpHandler;
import io.undertow.util.HttpString;

/**
 * An {@link Endpoint} represents a single rpc method. End points provide a location, tuple of
 * {@link Endpoint#method()} and {@link Endpoint#template()}, as well as an implementation, the
 * {@link Endpoint#handler()}.
 */
public interface Endpoint {

    /** HTTP method which matches this {@link Endpoint}. See {@link io.undertow.util.Methods}. */
    HttpString method();

    /**
     * Conjure formatted http path template.
     * For example, this may take the form <pre>/ping</pre> or <pre>/object/{objectId}</pre>.
     * For more information, see the
     * <a href="https://github.com/palantir/conjure/blob/master/docs/spec/conjure_definitions.md#pathstring">
     * specification for conjure path strings</a>.
     */
    String template();

    /** Undertow {@link HttpHandler} which provides the endpoint implementation. */
    HttpHandler handler();

    /** Simple name of the service which provides this endpoint. This data may be used for metric instrumentation. */
    String serviceName();

    /** Simple name of the endpoint method. This data may be used for metric instrumentation. */
    String name();
}
