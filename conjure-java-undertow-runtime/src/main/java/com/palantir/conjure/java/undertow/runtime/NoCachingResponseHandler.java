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
import io.undertow.util.Headers;

/**
 * Adds HTTP response headers to GET requests to disable caching.
 * <p>
 * https://tools.ietf.org/html/rfc7234#page-5
 * "Although caching is an entirely OPTIONAL feature of HTTP, it can be assumed that reusing a cached response is
 * desirable and that such reuse is the default behavior when no requirement or local configuration prevents it."
 */
final class NoCachingResponseHandler implements HttpHandler {

    private static final String DO_NOT_CACHE = "no-cache, no-store, must-revalidate";

    private final HttpHandler next;

    NoCachingResponseHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(Headers.CACHE_CONTROL, DO_NOT_CACHE);
        next.handleRequest(exchange);
    }
}
