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

package com.palantir.conjure.java.undertow.runtime;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * Workaround for UNDERTOW-1460, this class can be removed once 1.4.17.Final is released.
 * @see <a href="https://issues.jboss.org/browse/UNDERTOW-1460">UNDERTOW-1460</a>
 */
final class Undertow1460Handler implements HttpHandler {

    private final HttpHandler delegate;

    Undertow1460Handler(HttpHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // RoutingHandler fails to match empty string to '/'
        if ("".equals(exchange.getRelativePath())) {
            exchange.setRelativePath("/");
        }
        delegate.handleRequest(exchange);
    }
}
