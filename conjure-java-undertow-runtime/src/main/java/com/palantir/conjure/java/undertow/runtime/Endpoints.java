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

package com.palantir.conjure.java.undertow.runtime;

import com.palantir.conjure.java.undertow.lib.Endpoint;

/** Utility functionality for conjure {@link Endpoint} objects. */
final class Endpoints {

    private Endpoints() {}

    /**
     * Build an {@link Endpoint} who is a copy of the {@link Endpoint} but whose handler has been wrapped by an
     * {@link EndpointHandlerWrapper}.
     */
    static Endpoint map(Endpoint endpoint, EndpointHandlerWrapper wrapper) {
        return wrapper.wrap(endpoint)
                .map(handler ->
                        Endpoint.builder().from(endpoint).handler(handler).build())
                .orElse(endpoint);
    }
}
