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

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import java.util.stream.Collectors;

public final class UndertowServices {

    private UndertowServices() {}

    /** build a new service by wrapping every {@link com.palantir.conjure.java.undertow.lib.Endpoint} with the
     * {@link io.undertow.server.HandlerWrapper} resulting of the application of each
     * {@link com.palantir.conjure.java.undertow.lib.Endpoint} to an
     * {@link EndpointHandlerWrapper}.
     */
    public static UndertowService map(UndertowService service, EndpointHandlerWrapper wrapper) {
        return runtime ->
                ImmutableList.copyOf(
                        service.endpoints(runtime).stream()
                                .map(endpoint -> Endpoints.mapHandler(endpoint, wrapper.wrap(endpoint)))
                                .collect(ImmutableList.toImmutableList()));
    }
}
