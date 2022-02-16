/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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
import io.undertow.server.handlers.URLDecodingHandler;
import io.undertow.util.PathTemplateMatch;
import java.util.Map;
import java.util.Optional;

final class GlobRetainingDecodingHandler implements HttpHandler {

    static final EndpointHandlerWrapper WRAPPER = endpoint -> {
        URLDecodingHandler decodingHandler = new URLDecodingHandler(endpoint.handler(), "UTF-8");
        return endpoint.template().endsWith("/*")
                ? Optional.of(new GlobRetainingDecodingHandler(decodingHandler))
                : Optional.of(decodingHandler);
    };

    private final URLDecodingHandler next;

    private GlobRetainingDecodingHandler(URLDecodingHandler next) {
        this.next = next;
    }

    /**
     * Restores the url-encoded glob value after the {@link URLDecodingHandler} has processed the request.
     * This is required to disambiguate slashes delimiting URL segments, and encoded slashes ({@code %2F})
     * which represent slashes within a single URL segment. For example, {@code /foo/bar} represents
     * {@code ['foo', 'bar']} while {@code /foo%2Fbar} represents {@code ['foo/bar']}.
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        Map<String, String> pathParams =
                exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY).getParameters();
        String originalValue = pathParams.get("*");
        next.handleRequest(exchange);
        if (originalValue != null) {
            pathParams.put("*", originalValue);
        }
    }

    @Override
    public String toString() {
        return "GlobRetainingDecodingHandler{" + next + '}';
    }
}
