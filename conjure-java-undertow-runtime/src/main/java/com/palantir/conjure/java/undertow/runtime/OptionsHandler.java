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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import com.palantir.logsafe.Preconditions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import java.util.Set;

final class OptionsHandler implements HttpHandler {

    private static final HttpString HEADER_ACCESS_CONTROL_ALLOW_HEADERS =
            HttpString.tryFromString(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
    private static final HttpString HEADER_ACCESS_CONTROL_ALLOW_METHODS =
            HttpString.tryFromString(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
    private static final HttpString HEADER_ACCESS_CONTROL_MAX_AGE =
            HttpString.tryFromString(HttpHeaders.ACCESS_CONTROL_MAX_AGE);

    private static final int ACCESS_CONTROL_MAX_AGE = 600;

    private final String methods;

    OptionsHandler(Set<HttpString> methods) {
        this.methods = Joiner.on(", ")
                .join(ImmutableSet.<HttpString>builder()
                        .add(Methods.OPTIONS)
                        .addAll(methods)
                        .build());
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        Preconditions.checkState(Methods.OPTIONS.equals(exchange.getRequestMethod()), "Expected an OPTIONS request");
        exchange.getResponseHeaders().put(Headers.ALLOW, methods);
        exchange.getResponseHeaders().put(HEADER_ACCESS_CONTROL_ALLOW_HEADERS, Headers.AUTHORIZATION_STRING);
        exchange.getResponseHeaders().put(HEADER_ACCESS_CONTROL_ALLOW_METHODS, methods);
        exchange.getResponseHeaders().put(HEADER_ACCESS_CONTROL_MAX_AGE, ACCESS_CONTROL_MAX_AGE);
        exchange.setStatusCode(StatusCodes.NO_CONTENT);
    }
}
