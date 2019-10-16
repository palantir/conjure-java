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
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

/**
 * Applies security headers. These headers include:
 *
 * <dl>
 *   <dt>Content Security Policy (including support for IE 10 + 11)
 *   <dd>https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP
 *   <dt>Referrer Policy
 *   <dd>https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Referrer-Policy
 *   <dt>Content Type Options
 *   <dd>https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Content-Type-Options
 *   <dt>Frame Options
 *   <dd>https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-Frame-Options
 *   <dt>XSS Protection
 *   <dd>https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/X-XSS-Protection
 * </dl>
 */
final class WebSecurityHandler implements HttpHandler {

    private static final String CONTENT_SECURITY_POLICY = "default-src 'self'; img-src 'self' data:; style-src 'self' 'unsafe-inline'; frame-ancestors 'self';";
    private static final String CONTENT_TYPE_OPTIONS = "nosniff";
    private static final String FRAME_OPTIONS = "sameorigin";
    private static final String XSS_PROTECTION = "1; mode=block";

    private static final String REFERRER_POLICY = "strict-origin-when-cross-origin";

    private static final HttpString HEADER_IE_X_CONTENT_SECURITY_POLICY =
            HttpString.tryFromString("X-Content-Security-Policy");
    private static final String USER_AGENT_IE_10 = "MSIE 10";
    private static final String USER_AGENT_IE_11 = "rv:11.0";

    private final HttpHandler next;

    WebSecurityHandler(HttpHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        HeaderMap headers = exchange.getResponseHeaders();
        headers.put(Headers.CONTENT_SECURITY_POLICY, CONTENT_SECURITY_POLICY);
        headers.put(Headers.REFERRER_POLICY, REFERRER_POLICY);
        headers.put(Headers.X_CONTENT_TYPE_OPTIONS, CONTENT_TYPE_OPTIONS);
        headers.put(Headers.X_FRAME_OPTIONS, FRAME_OPTIONS);
        headers.put(Headers.X_XSS_PROTECTION, XSS_PROTECTION);
        String userAgent = exchange.getRequestHeaders().getFirst(Headers.USER_AGENT);
        if (userAgent != null) {
            // send the CSP header so that IE10 and IE11 recognise it
            if (userAgent.contains(USER_AGENT_IE_10) || userAgent.contains(USER_AGENT_IE_11)) {
                headers.put(HEADER_IE_X_CONTENT_SECURITY_POLICY, CONTENT_SECURITY_POLICY);
            }
        }
        next.handleRequest(exchange);
    }
}
