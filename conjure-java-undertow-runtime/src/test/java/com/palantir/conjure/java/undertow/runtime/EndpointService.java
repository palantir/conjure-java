/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.lib.UndertowService;
import java.util.List;

/** Test utility to migrate away from deprecated methods. */
final class EndpointService implements UndertowService {

    private final Endpoint endpoint;

    static UndertowService of(Endpoint endpoint) {
        return new EndpointService(endpoint);
    }

    private EndpointService(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public List<Endpoint> endpoints(UndertowRuntime _runtime) {
        return ImmutableList.of(endpoint);
    }
}
