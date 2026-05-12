/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types.fastunion;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.sun.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import jersey.com.palantir.product.SimpleUnion;
import org.junit.jupiter.api.Test;

/**
 * Compares per-deserialization allocation between the stock generated union (visible=true)
 * and a hand-modified copy that uses {@link ConjureUnionTypeResolverBuilder}. Uses
 * {@code ThreadMXBean.getThreadAllocatedBytes} for a direct, deterministic measurement
 * without depending on JMH or JFR.
 */
final class UnionFastPathAllocationTest {

    private static final ObjectMapper MAPPER = ObjectMappers.newClientObjectMapper();
    private static final ThreadMXBean THREAD_MX_BEAN =
            (ThreadMXBean) ManagementFactory.getThreadMXBean();

    // type discriminator FIRST — the common, optimizable case.
    private static final String JSON_TYPE_FIRST_FOO = "{\"type\":\"foo\",\"foo\":\"hello\"}";
    private static final String JSON_TYPE_FIRST_BAR = "{\"type\":\"bar\",\"bar\":42}";
    // unknown variant — must still round-trip via the slow path.
    private static final String JSON_UNKNOWN = "{\"type\":\"qux\",\"qux\":\"unknown-payload\"}";

    private static final int WARMUP = 2_000;
    private static final int ITERATIONS = 20_000;

    @Test
    void knownVariantTypeFirstAllocatesLess() throws Exception {
        long stock = measureBytesPerOp(JSON_TYPE_FIRST_FOO, SimpleUnion.class);
        long fast = measureBytesPerOp(JSON_TYPE_FIRST_FOO, FastSimpleUnion.class);
        report("known variant (foo, type first)", stock, fast);
        assertThat(fast)
                .as("fast path should allocate less than stock for known variants")
                .isLessThan(stock);
    }

    @Test
    void knownVariantWithIntPayload() throws Exception {
        long stock = measureBytesPerOp(JSON_TYPE_FIRST_BAR, SimpleUnion.class);
        long fast = measureBytesPerOp(JSON_TYPE_FIRST_BAR, FastSimpleUnion.class);
        report("known variant (bar int, type first)", stock, fast);
        assertThat(fast).isLessThan(stock);
    }

    @Test
    void unknownVariantUnchanged() throws Exception {
        // For the unknown branch, the fast deserializer SHOULD still buffer (to feed Unknown's
        // constructor). The point is parity, not a win — assert no regression.
        long stock = measureBytesPerOp(JSON_UNKNOWN, SimpleUnion.class);
        long fast = measureBytesPerOp(JSON_UNKNOWN, FastSimpleUnion.class);
        report("unknown variant", stock, fast);
        // Allow up to 10% drift either way — should be essentially identical.
        assertThat(fast)
                .as("unknown variant allocation should be ~equal between stock and fast")
                .isBetween((stock * 9) / 10, (stock * 11) / 10);
    }

    @Test
    void unknownVariantRoundTripPreserved() throws Exception {
        FastSimpleUnion value = MAPPER.readValue(JSON_UNKNOWN, FastSimpleUnion.class);
        String reSerialized = MAPPER.writeValueAsString(value);
        assertThat(reSerialized).isEqualTo(JSON_UNKNOWN);
    }

    // ----- sealed-class form -----

    @Test
    void sealed_knownVariantTypeFirstAllocatesLess() throws Exception {
        long stock = measureBytesPerOp(JSON_TYPE_FIRST_FOO, sealedunions.com.palantir.product.SimpleUnion.class);
        long fast = measureBytesPerOp(JSON_TYPE_FIRST_FOO, FastSealedSimpleUnion.class);
        report("sealed known variant (foo, type first)", stock, fast);
        assertThat(fast)
                .as("fast path should allocate less than stock for known variants (sealed)")
                .isLessThan(stock);
    }

    @Test
    void sealed_knownVariantWithIntPayload() throws Exception {
        long stock = measureBytesPerOp(JSON_TYPE_FIRST_BAR, sealedunions.com.palantir.product.SimpleUnion.class);
        long fast = measureBytesPerOp(JSON_TYPE_FIRST_BAR, FastSealedSimpleUnion.class);
        report("sealed known variant (bar int, type first)", stock, fast);
        assertThat(fast).isLessThan(stock);
    }

    @Test
    void sealed_unknownVariantUnchanged() throws Exception {
        long stock = measureBytesPerOp(JSON_UNKNOWN, sealedunions.com.palantir.product.SimpleUnion.class);
        long fast = measureBytesPerOp(JSON_UNKNOWN, FastSealedSimpleUnion.class);
        report("sealed unknown variant", stock, fast);
        assertThat(fast)
                .as("sealed unknown variant allocation should be ~equal between stock and fast")
                .isBetween((stock * 9) / 10, (stock * 11) / 10);
    }

    @Test
    void sealed_unknownVariantRoundTripPreserved() throws Exception {
        FastSealedSimpleUnion value = MAPPER.readValue(JSON_UNKNOWN, FastSealedSimpleUnion.class);
        String reSerialized = MAPPER.writeValueAsString(value);
        assertThat(reSerialized).isEqualTo(JSON_UNKNOWN);
    }

    private long measureBytesPerOp(String json, Class<?> targetType) throws Exception {
        // Warmup so JIT and Jackson deserializer caches are populated.
        for (int i = 0; i < WARMUP; i++) {
            MAPPER.readValue(json, targetType);
        }
        System.gc();
        Thread.sleep(50); // let GC settle

        long threadId = Thread.currentThread().getId();
        long startBytes = THREAD_MX_BEAN.getThreadAllocatedBytes(threadId);

        Object sink = null;
        for (int i = 0; i < ITERATIONS; i++) {
            sink = MAPPER.readValue(json, targetType);
        }
        // Keep `sink` reachable so JIT doesn't dead-code-eliminate the readValue call.
        if (sink == null) {
            throw new AssertionError("dead-code-eliminated");
        }

        long endBytes = THREAD_MX_BEAN.getThreadAllocatedBytes(threadId);
        return (endBytes - startBytes) / ITERATIONS;
    }

    @SuppressWarnings("checkstyle:RegexpSinglelineJava")
    private void report(String label, long stock, long fast) {
        long delta = stock - fast;
        double pct = stock > 0 ? (100.0 * delta) / stock : 0.0;
        System.out.printf(
                "[%s]  stock=%5d B/op  fast=%5d B/op  delta=%+5d B/op  (%+.1f%%)%n",
                label, stock, fast, -delta, -pct);
    }
}
