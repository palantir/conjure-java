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

import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.undertow.lib.ExceptionHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

/**
 * Delegates to the given {@link HttpHandler}, and catches&forwards all {@link Throwable}s. Any exception thrown in the
 * delegate handler is caught and serialized using the Conjure JSON format into a {@link SerializableError}. The result
 * is written it into the exchange's output stream, and an appropriate HTTP status code is set.
 */
final class ConjureExceptionHandler implements HttpHandler {

    private final HttpHandler delegate;
    private final ExceptionHandler exceptionHandler;

    ConjureExceptionHandler(HttpHandler delegate, ExceptionHandler exceptionHandler) {
        this.delegate = delegate;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            delegate.handleRequest(exchange);
        } catch (Throwable throwable) {
            exceptionHandler.handle(exchange, throwable);
        }
    }
}
