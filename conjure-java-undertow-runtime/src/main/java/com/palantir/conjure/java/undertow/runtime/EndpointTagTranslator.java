/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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
import com.palantir.tracing.TagTranslator;
import io.undertow.server.HttpServerExchange;

/** A {@link TagTranslator} which populates {@link Endpoint} metadata. */
final class EndpointTagTranslator implements TagTranslator<HttpServerExchange> {

    private final String serviceName;
    private final String endpointName;
    private final String httpMethod;
    private final String httpPath;
    private final boolean deprecated;
    private final boolean incubating;

    EndpointTagTranslator(Endpoint endpoint) {
        this.serviceName = endpoint.serviceName();
        this.endpointName = endpoint.name();
        this.httpMethod = endpoint.method().toString();
        this.httpPath = endpoint.template();
        this.deprecated = endpoint.deprecated().isPresent();
        this.incubating = endpoint.tags().contains("incubating");
    }

    @Override
    public <T> void translate(TagAdapter<T> adapter, T target, HttpServerExchange _data) {
        adapter.tag(target, "serviceName", serviceName);
        adapter.tag(target, "endpointName", endpointName);
        adapter.tag(target, "method", httpMethod);
        adapter.tag(target, "path", httpPath);
        if (deprecated) {
            adapter.tag(target, "deprecated", "true");
        }
        if (incubating) {
            adapter.tag(target, "incubating", "true");
        }
    }

    @Override
    public String toString() {
        return "TracedRequestTagTranslator{serviceName='"
                + serviceName + "', endpointName='"
                + endpointName + "', httpMethod='"
                + httpMethod + "', httpPath='"
                + httpPath + "', deprecated="
                + deprecated + ", incubating="
                + incubating + "}";
    }
}
