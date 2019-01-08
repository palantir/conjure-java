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

import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import io.undertow.util.URLUtils;
import java.util.Map;

/**
 * Undertow does not decode slashes in path parameters by default.
 * This should be removed once Undertow 2.0.17.Final is released.
 *
 * @see <a href="https://issues.jboss.org/browse/UNDERTOW-1476">UNDERTOW-1476</a>
 */
class PathParamDecodingHandler implements HttpHandler {

    private final HttpHandler next;

    PathParamDecodingHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        if (!isDecodingComplete(exchange)) {
            PathTemplateMatch pathTemplateMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
            if (pathTemplateMatch != null) {
                Map<String, String> parameters = pathTemplateMatch.getParameters();
                if (parameters != null && !parameters.isEmpty()) {
                    StringBuilder buffer = new StringBuilder();
                    for (Map.Entry<String, String> entry : parameters.entrySet()) {
                        entry.setValue(URLUtils.decode(
                                entry.getValue(), "UTF-8", true, true, buffer));
                    }
                }
            }
        }
        next.handleRequest(exchange);
    }

    private static boolean isDecodingComplete(HttpServerExchange exchange) {
        return exchange.getConnection()
                .getUndertowOptions().get(UndertowOptions.DECODE_URL, true);
    }
}
