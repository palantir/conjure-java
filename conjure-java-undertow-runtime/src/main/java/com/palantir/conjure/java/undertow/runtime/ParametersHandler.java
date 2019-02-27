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

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.palantir.conjure.java.undertow.lib.Parameters;
import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ParametersHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(ParametersHandler.class);

    private final HttpHandler delegate;

    ParametersHandler(HttpHandler delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "Delegate handler is required");
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Multimap<String, String> safePathParams = HashMultimap.create();
        Multimap<String, String> unsafePathParams = HashMultimap.create();
        Multimap<String, String> safeQueryParams = HashMultimap.create();
        Multimap<String, String> unsafeQueryParams = HashMultimap.create();
        exchange.putAttachment(Parameters.SAFE_PATH_PARAMS_ATTACH_KEY, safePathParams);
        exchange.putAttachment(Parameters.UNSAFE_PATH_PARAMS_ATTACH_KEY, unsafePathParams);
        exchange.putAttachment(Parameters.SAFE_QUERY_PARAMS_ATTACH_KEY, safeQueryParams);
        exchange.putAttachment(Parameters.UNSAFE_QUERY_PARAMS_ATTACH_KEY, unsafeQueryParams);
        delegate.handleRequest(exchange);
    }
}
