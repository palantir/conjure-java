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
import org.slf4j.MDC;

/** {@link LoggingContextHandler} clears all slf4j {@link MDC} before and after each request is handled. */
final class LoggingContextHandler implements HttpHandler {

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
            delegate.handleRequest(exchange);
        } finally {
            MDC.clear();
        }
    }
}
