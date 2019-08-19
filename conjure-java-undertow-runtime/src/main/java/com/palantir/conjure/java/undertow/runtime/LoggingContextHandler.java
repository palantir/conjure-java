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

import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import javax.annotation.Nullable;
import org.slf4j.MDC;

/** {@link LoggingContextHandler} clears all slf4j {@link MDC} before and after each request is handled. */
final class LoggingContextHandler implements HttpHandler {

    private static final HttpString FETCH_USER_AGENT = HttpString.tryFromString("Fetch-User-Agent");

    private final HttpHandler delegate;

    LoggingContextHandler(HttpHandler delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "Delegate handler is required");
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        // Jersey filter implementations often fail to clean up MDC state after each request.
        // In environments using both Jersey and generated Undertow handlers, the MDC may
        // contain stale values.
        MDC.clear();
        try {
            initializeContextFromRequest(exchange);
            delegate.handleRequest(exchange);
        } finally {
            MDC.clear();
        }
    }

    /**
     * Initialize standard {@link MDC} values specific to conjure.
     * Note that this includes neither tracing nor JWT values which are handler by other components.
     */
    private static void initializeContextFromRequest(HttpServerExchange exchange) {
        String userAgent = getUserAgent(exchange);
        if (userAgent != null) {
            MDC.put("_userAgent", userAgent);
        }
    }

    @Nullable
    private static String getUserAgent(HttpServerExchange exchange) {
        HeaderMap requestHeaders = exchange.getRequestHeaders();
        // Check for a Conjure user agent first. The javascript fetch client
        // cannot override the browser user agent.
        String fetchUserAgent = requestHeaders.getFirst(FETCH_USER_AGENT);
        if (fetchUserAgent != null) {
            return fetchUserAgent;
        }
        return requestHeaders.getFirst(Headers.USER_AGENT);
    }
}
