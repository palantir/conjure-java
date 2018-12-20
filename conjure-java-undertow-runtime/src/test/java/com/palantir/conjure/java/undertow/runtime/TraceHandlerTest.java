/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.tracing.TraceSampler;
import com.palantir.tracing.Tracer;
import com.palantir.tracing.Tracers;
import com.palantir.tracing.api.Span;
import com.palantir.tracing.api.SpanObserver;
import com.palantir.tracing.api.TraceHttpHeaders;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

@RunWith(MockitoJUnitRunner.class)
public final class TraceHandlerTest {

    @Captor
    private ArgumentCaptor<Span> spanCaptor;

    @Mock
    private SpanObserver observer;
    @Mock
    private TraceSampler traceSampler;
    @Mock
    private HttpHandler delegate;
    private HttpServerExchange exchange = HttpServerExchanges.createStub();
    private String traceId;

    private TraceHandler handler;

    @Before
    public void before() {
        Tracer.subscribe("TEST_OBSERVER", observer);
        Tracer.setSampler(traceSampler);

        MDC.clear();

        exchange.setRequestMethod(HttpString.tryFromString("GET"));
        when(traceSampler.sample()).thenReturn(true);

        traceId = Tracers.randomId();
        handler = new TraceHandler("GET /foo", delegate);
    }

    @After
    public void after() {
        Tracer.unsubscribe("TEST_OBSERVER");
    }

    @Ignore("TODO(rfink): This is pretty hard to mock")
    @Test
    public void failsWhenRunOnIoThread() {}

    @Test
    public void whenNoTraceIsInHeader_generatesNewTrace() throws Exception {
        handler.handleRequest(exchange);
        verify(observer).consume(spanCaptor.capture());
        Span span = spanCaptor.getValue();

        assertThat(Tracer.hasTraceId()).isFalse();
        assertThat(span.getOperation()).isEqualTo("GET /foo");
        HeaderMap responseHeaders = exchange.getResponseHeaders();
        assertThat(responseHeaders.get(TraceHttpHeaders.PARENT_SPAN_ID)).isNull();
        assertThat(responseHeaders.get(TraceHttpHeaders.SPAN_ID)).isNull();
        assertThat(responseHeaders.get(HttpString.tryFromString(TraceHttpHeaders.TRACE_ID)))
                .containsExactly(span.getTraceId());
    }

    @Test
    public void whenTraceIsInHeader_usesGivenTraceId() throws Exception {
        setRequestTraceId(traceId);
        handler.handleRequest(exchange);
        assertThat(exchange.getResponseHeaders().getFirst(TraceHttpHeaders.TRACE_ID)).isEqualTo(traceId);
    }

    @Test
    public void whenParentSpanIsGiven_usesParentSpan() throws Exception {
        setRequestTraceId(traceId);
        String parentSpanId = Tracers.randomId();
        setRequestSpanId(parentSpanId);

        handler.handleRequest(exchange);
        verify(observer).consume(spanCaptor.capture());
        Span span = spanCaptor.getValue();
        assertThat(span.getParentSpanId()).contains(parentSpanId);
        assertThat(span.getSpanId()).isNotEqualTo(parentSpanId);
    }

    @Test
    public void whenTraceIsAlreadySampled_doesNotCallSampler() throws Exception {
        exchange.getRequestHeaders().put(HttpString.tryFromString(TraceHttpHeaders.IS_SAMPLED), "1");
        handler.handleRequest(exchange);

        exchange.getRequestHeaders().put(HttpString.tryFromString(TraceHttpHeaders.IS_SAMPLED), "0");
        handler.handleRequest(exchange);

        setRequestTraceId(traceId);
        handler.handleRequest(exchange);

        verify(traceSampler, never()).sample();
    }

    @Test
    public void whenSamplingDecisionHasNotBeenMade_callsSampler() throws Exception {
        handler.handleRequest(exchange);
        verify(traceSampler).sample();
    }

    @Test
    public void completesSpanEvenIfDelegateThrows() throws Exception {
        doThrow(new RuntimeException()).when(delegate).handleRequest(any());
        try {
            handler.handleRequest(exchange);
        } catch (RuntimeException e) {
            // expected
        }
        assertThat(Tracer.completeSpan()).isEmpty();
    }

    @Test
    public void populatesSlf4jMdc() throws Exception {
        setRequestTraceId(traceId);
        AtomicReference<String> mdcTraceValue = new AtomicReference<>();
        new TraceHandler("GET /traced", exc -> mdcTraceValue.set(MDC.get(Tracers.TRACE_ID_KEY)))
                .handleRequest(exchange);
        assertThat(mdcTraceValue).hasValue(traceId);
        // Value should be cleared when the handler returns
        assertThat(MDC.get(Tracers.TRACE_ID_KEY)).isNull();
    }

    private void setRequestTraceId(String theTraceId) {
        exchange.getRequestHeaders().put(HttpString.tryFromString(TraceHttpHeaders.TRACE_ID), theTraceId);
    }

    private void setRequestSpanId(String spanId) {
        exchange.getRequestHeaders().put(HttpString.tryFromString(TraceHttpHeaders.SPAN_ID), spanId);
    }
}
