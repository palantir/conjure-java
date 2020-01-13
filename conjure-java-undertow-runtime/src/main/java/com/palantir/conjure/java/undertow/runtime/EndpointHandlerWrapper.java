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

import com.palantir.conjure.java.undertow.lib.Endpoint;
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import java.util.Optional;

/**
 * Warning: this should be used with great care as {@link HttpHandler} are powerful and can easily be implemented
 * incorrectly. Make sure to not forget passing the request to the next handlers through calling the endpoint's {@link
 * HttpHandler#handleRequest(HttpServerExchange)}. This function is used to implement filter-like behavior: - Adding
 * logic pre and post handling by the next handlers, by adding the logic before and after {@link
 * HttpHandler#handleRequest(HttpServerExchange)} - Adding logic post completion of the request by all handlers {@link
 * io.undertow.server.HttpServerExchange#addExchangeCompleteListener(ExchangeCompletionListener)} - Catching and
 * handling exceptions surrounding {@link HttpHandler#handleRequest(HttpServerExchange)} with try ... catch ... finally.
 */
public interface EndpointHandlerWrapper {

    /** May wrap {@link Endpoint#handler()} if this wrapper applies, otherwise empty. */
    Optional<HttpHandler> wrap(Endpoint endpoint);
}
