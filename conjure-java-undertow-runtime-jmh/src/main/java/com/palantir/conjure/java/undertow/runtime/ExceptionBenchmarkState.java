/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.tracing.Observability;
import com.palantir.tracing.Tracer;
import com.palantir.tracing.api.SpanType;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.slf4j.LoggerFactory;

@State(Scope.Thread)
@SuppressWarnings({"VisibilityModifier", "DesignForExtension"})
public class ExceptionBenchmarkState {
    private static final HttpString RETRIES_EXHAUSTED = new HttpString("Dialogue-Retries-Exhausted");

    @Param
    public ExceptionScenario scenario;

    private Throwable failure;

    @Setup
    public void before() throws IOException {
        if (LoggerFactory.getLogger(ConjureExceptions.class).isErrorEnabled()) {
            throw new IllegalStateException("Benchmarks require logging output to be disabled");
        }
        // Model a handler running inside a request, without creating a new root trace for every serialization.
        Tracer.initTraceWithSpan(
                Observability.DO_NOT_SAMPLE,
                "0123456789abcdef0123456789abcdef",
                "benchmark-request",
                SpanType.SERVER_INCOMING);
        failure = scenario.createFailure();
        if (BenchmarkConjureExceptions.isRetriesExhausted(failure) != scenario.expectsMarker()) {
            throw new IllegalStateException("Scenario does not match the production scanner: " + scenario);
        }
        verifyHandlers();
    }

    @TearDown
    public void after() {
        Tracer.fastCompleteSpan();
    }

    @SuppressWarnings("ReferenceEquality") // Verify that the original Throwable instance is attached.
    private void verifyHandlers() throws IOException {
        // Sanity-check the baseline and measured implementation outside the timed region.
        HttpServerExchange baseline = BenchmarkExchanges.create();
        BenchmarkConjureExceptions.INSTANCE.handle(baseline, failure);
        HttpServerExchange current = BenchmarkExchanges.create();
        ConjureExceptions.INSTANCE.handle(current, failure);
        if (baseline.getStatusCode() < 400 || baseline.getStatusCode() != current.getStatusCode()) {
            throw new IllegalStateException("Handler variants returned different statuses: " + scenario);
        }
        if (baseline.getResponseHeaders().contains(RETRIES_EXHAUSTED)
                || !Objects.equals(
                        current.getResponseHeaders().getFirst(RETRIES_EXHAUSTED),
                        scenario.expectsMarker() ? "true" : null)) {
            throw new IllegalStateException("Unexpected retries-exhausted response header: " + scenario);
        }
        if (baseline.getAttachment(Attachments.FAILURE) != failure
                || current.getAttachment(Attachments.FAILURE) != failure) {
            throw new IllegalStateException("The handler did not record the original failure");
        }
        if (!Objects.equals(responseBody(baseline), responseBody(current))) {
            throw new IllegalStateException("Handler variants returned different error bodies: " + scenario);
        }
    }

    Throwable failure() {
        return failure;
    }

    private static JsonNode responseBody(HttpServerExchange exchange) throws IOException {
        byte[] bytes = ((ByteArrayOutputStream) exchange.getOutputStream()).toByteArray();
        if (bytes.length == 0) {
            if (exchange.getStatusCode() != 503) {
                throw new IllegalStateException("Expected a serialized error body");
            }
            return ObjectMappers.newClientJsonMapper().nullNode();
        }
        ObjectNode body = (ObjectNode) ObjectMappers.newClientJsonMapper().readTree(bytes);
        // Some handler branches create a new ServiceException with a fresh UUID on every call.
        body.remove("errorInstanceId");
        return body;
    }
}
