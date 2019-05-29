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

import com.codahale.metrics.Meter;
import com.palantir.conjure.java.undertow.lib.DeprecationCallback;
import com.palantir.tritium.metrics.registry.MetricName;
import com.palantir.tritium.metrics.registry.TaggedMetricRegistry;
import io.undertow.server.HttpServerExchange;

class ConjureDeprecationCallback implements DeprecationCallback {

    private final Meter meter;

    ConjureDeprecationCallback(TaggedMetricRegistry registry) {
        this.meter = registry.meter(MetricName.builder()
                .safeName("server.request.deprecated")
                .putSafeTags()
                .build());

    }

    @Override
    public void deprecation(
            String serviceName, String endpointName, String deprecatedDoc, HttpServerExchange exchange) {
        registry
    }
}
