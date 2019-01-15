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
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.TreeMap;

/**
 * Backport of the Undertow URLDecodingHandler proposed for from Undertow 2.0.18.Final.
 * This may be replaced with the Undertow URLDecodingHandler once
 * <a href="https://github.com/undertow-io/undertow/pull/710">Undertow pull/710</a> merges.
 * Fixes matched path parameter decoding which should not allow form encoding.
 */
final class ConjureUrlDecodingHandler implements HttpHandler {

    private static final ThreadLocal<StringBuilder> DECODING_BUFFER_CACHE = ThreadLocal.withInitial(StringBuilder::new);

    private final HttpHandler next;
    private final String charset;

    ConjureUrlDecodingHandler(final HttpHandler next, final String charset) {
        this.next = next;
        this.charset = charset;
    }

    @Override
    public void handleRequest(final HttpServerExchange exchange) throws Exception {
        if (shouldDecode(exchange)) {
            final StringBuilder sb = getStringBuilderForDecoding(exchange);
            decodePath(exchange, charset, sb);
            decodeQueryString(exchange, charset, sb);
            decodePathTemplateMatch(exchange, charset, sb);
        }
        next.handleRequest(exchange);
    }

    // Returns true if the exchange should be decoded.  This method updates the ALREADY_DECODED
    // attachment so that subsequent invocations will always return false.
    private static boolean shouldDecode(final HttpServerExchange exchange) {
        return !exchange.getConnection().getUndertowOptions().get(UndertowOptions.DECODE_URL, true);
    }

    private static void decodePath(HttpServerExchange exchange, String charset, StringBuilder sb) {
        final boolean decodeSlash = exchange.getConnection().getUndertowOptions().get(
                UndertowOptions.ALLOW_ENCODED_SLASH, false);
        exchange.setRequestPath(URLUtils.decode(exchange.getRequestPath(), charset, decodeSlash, false, sb));
        exchange.setRelativePath(URLUtils.decode(exchange.getRelativePath(), charset, decodeSlash, false, sb));
        exchange.setResolvedPath(URLUtils.decode(exchange.getResolvedPath(), charset, decodeSlash, false, sb));
    }

    private static void decodeQueryString(HttpServerExchange exchange, String charset, StringBuilder sb) {
        if (!exchange.getQueryString().isEmpty()) {
            Map<String, Deque<String>> newParams = new TreeMap<>();
            for (Map.Entry<String, Deque<String>> param : exchange.getQueryParameters().entrySet()) {
                final Deque<String> newVales = new ArrayDeque<>(param.getValue().size());
                for (String val : param.getValue()) {
                    newVales.add(URLUtils.decode(val, charset, true, true, sb));
                }
                newParams.put(URLUtils.decode(param.getKey(), charset, true, true, sb), newVales);
            }
            exchange.getQueryParameters().clear();
            exchange.getQueryParameters().putAll(newParams);
        }
    }

    private static void decodePathTemplateMatch(HttpServerExchange exchange, String charset, StringBuilder sb) {
        PathTemplateMatch pathTemplateMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
        if (pathTemplateMatch != null) {
            Map<String, String> parameters = pathTemplateMatch.getParameters();
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    entry.setValue(URLUtils.decode(entry.getValue(), charset, true, false, sb));
                }
            }
        }
    }

    private static StringBuilder getStringBuilderForDecoding(HttpServerExchange exchange) {
        if (exchange.isInIoThread()) {
            // Unnecessary to clear the buffer here, URLUtils.decode updates the buffer length before usage.
            // We don't need to check the size after use because decoded size is bounded to the request line,
            // which cannot exceed one buffer.
            return DECODING_BUFFER_CACHE.get();
        }
        return new StringBuilder();
    }
}
