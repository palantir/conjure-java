/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import com.palantir.tracing.TagTranslator;
import com.palantir.tracing.api.TraceTags;
import com.palantir.tracing.undertow.TracingAttachments;
import io.undertow.server.HttpServerExchange;

/** Request tracing {@link TagTranslator} which applies tag data to a top level request span. */
enum CompletedRequestTagTranslator implements TagTranslator<HttpServerExchange> {
    INSTANCE;

    private static final String FETCH_USER_AGENT = "Fetch-User-Agent";

    @Override
    public <T> void translate(TagAdapter<T> adapter, T target, HttpServerExchange exchange) {
        String maybeRequestId = exchange.getAttachment(TracingAttachments.REQUEST_ID);
        if (maybeRequestId != null) {
            adapter.tag(target, TraceTags.HTTP_REQUEST_ID, maybeRequestId);
        }
        String agent = conjureUserAgent(exchange);
        if (!Strings.isNullOrEmpty(agent)) {
            adapter.tag(target, TraceTags.HTTP_USER_AGENT, agent);
        }
        adapter.tag(target, TraceTags.HTTP_STATUS_CODE, statusString(exchange.getStatusCode()));
        adapter.tag(target, TraceTags.HTTP_METHOD, exchange.getRequestMethod().toString());
        adapter.tag(target, TraceTags.HTTP_URL_SCHEME, exchange.getRequestScheme());
        adapter.tag(target, TraceTags.HTTP_VERSION, exchange.getProtocol().toString());
    }

    static String statusString(int statusCode) {
        // handle common cases quickly
        switch (statusCode) {
            case 200:
                return "200";
            case 204:
                return "204";
        }
        return Integer.toString(statusCode);
    }

    // Loads the User-Agent from the request, preferring the 'Fetch-User-Agent' if present.
    static String conjureUserAgent(HttpServerExchange exchange) {
        String fetchUserAgent = exchange.getRequestHeader(FETCH_USER_AGENT);
        return Strings.isNullOrEmpty(fetchUserAgent)
                ? exchange.getRequestHeader(HttpHeaders.USER_AGENT)
                : fetchUserAgent;
    }
}
