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

import com.google.common.base.Strings;
import com.palantir.tracing.Tracer;
import com.palantir.tracing.Tracers;
import com.palantir.tracing.api.SpanType;
import com.palantir.tracing.api.TraceHttpHeaders;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import java.util.Optional;

/**
 * Extracts Zipkin-style trace information from the given HTTP request and sets up a corresponding
 * {@link com.palantir.tracing.Trace} and {@link com.palantir.tracing.api.Span} for delegating to the configured
 * {@link #delegate} handler. This handler must never run on an IO thread. See
 * https://github.com/openzipkin/b3-propagation.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
final class TraceHandler implements HttpHandler {

    private static final HttpString TRACE_ID = HttpString.tryFromString(TraceHttpHeaders.TRACE_ID);
    private static final HttpString SPAN_ID = HttpString.tryFromString(TraceHttpHeaders.SPAN_ID);
    private static final HttpString IS_SAMPLED = HttpString.tryFromString(TraceHttpHeaders.IS_SAMPLED);

    private static final Optional<Boolean> SAMPLED = Optional.of(Boolean.TRUE);
    private static final Optional<Boolean> NOT_SAMPLED = Optional.of(Boolean.FALSE);

    private final String operation;
    private final HttpHandler delegate;

    TraceHandler(String operation, HttpHandler delegate) {
        this.operation = operation;
        this.delegate = delegate;
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        String traceId = initializeTrace(exchange);
        // Populate response before calling delegate since delegate might commit the response.
        exchange.getResponseHeaders().put(TRACE_ID, traceId);
        try {
            delegate.handleRequest(exchange);
        } finally {
            Tracer.fastCompleteSpan();
        }
    }

    // Returns true iff the context contains a "1" X-B3-Sampled header, false if the header contains another value,
    // or absent if there is no such header.
    private static Optional<Boolean> hasSampledHeader(HeaderMap headers) {
        String header = headers.getFirst(IS_SAMPLED);
        if (header == null) {
            return Optional.empty();
        } else {
            // No need to box the resulting boolean and allocate
            // a new Optional wrapper for each invocation.
            return header.equals("1") ? SAMPLED : NOT_SAMPLED;
        }
    }

    /** Initializes trace state and a root span for this request, returning the traceId. */
    private String initializeTrace(HttpServerExchange exchange) {
        HeaderMap headers = exchange.getRequestHeaders();
        // TODO(rfink): Log/warn if we find multiple headers?
        String traceId = headers.getFirst(TRACE_ID); // nullable

        // Set up thread-local span that inherits state from HTTP headers
        if (Strings.isNullOrEmpty(traceId)) {
            return initializeNewTrace(headers);
        } else {
            initializeTraceFromExisting(headers, traceId);
        }
        return traceId;
    }

    /** Initializes trace state given a trace-id header from the client. */
    private void initializeTraceFromExisting(HeaderMap headers, String traceId) {
        Tracer.initTrace(hasSampledHeader(headers), traceId);
        String spanId = headers.getFirst(SPAN_ID); // nullable
        if (spanId == null) {
            Tracer.startSpan(operation, SpanType.SERVER_INCOMING);
        } else {
            // caller's span is this span's parent.
            Tracer.startSpan(operation, spanId, SpanType.SERVER_INCOMING);
        }
    }

    /** Initializes trace state for a request without tracing headers. */
    private String initializeNewTrace(HeaderMap headers) {
        // HTTP request did not indicate a trace; initialize trace state and create a span.
        String newTraceId = Tracers.randomId();
        Tracer.initTrace(hasSampledHeader(headers), newTraceId);
        Tracer.startSpan(operation, SpanType.SERVER_INCOMING);
        return newTraceId;
    }
}
