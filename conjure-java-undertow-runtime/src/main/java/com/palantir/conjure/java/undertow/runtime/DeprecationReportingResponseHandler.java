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

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

/**
 * Adds HTTP response headers to indicate endpoint deprecation.
 *
 * <p>https://tools.ietf.org/id/draft-dalal-deprecation-header-01.html#rfc.section.2.1
 */
final class DeprecationReportingResponseHandler implements HttpHandler {

    private static final HttpString DEPRECATION = HttpString.tryFromString("deprecation");
    private static final String IS_DEPRECATED = "true";

    private final HttpHandler next;

    DeprecationReportingResponseHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(DEPRECATION, IS_DEPRECATED);
        next.handleRequest(exchange);
    }
}
