/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.undertow.lib.Contexts;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.SafeArg;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

final class EndpointRequestArgHandler implements HttpHandler {

    private final Endpoint endpoint;
    private final Contexts contexts;
    private final Arg<String> endpointName;
    private final Arg<String> serviceName;

    EndpointRequestArgHandler(Endpoint endpoint, Contexts contexts) {
        this.endpoint = endpoint;
        this.contexts = contexts;
        this.endpointName = SafeArg.of("_endpointName", endpoint.name());
        this.serviceName = SafeArg.of("_serviceName", endpoint.serviceName());
    }

    /**
     * Adds request args that identify the {@link Endpoint}.
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        RequestContext requestContext = contexts.createContext(exchange, endpoint);
        requestContext.requestArg(endpointName);
        requestContext.requestArg(serviceName);
        endpoint.handler().handleRequest(exchange);
    }
}
