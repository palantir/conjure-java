/*
 * (c) Copyright 2025 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.profile.InternalProfiler;
import org.openjdk.jmh.results.AggregationPolicy;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.ScalarResult;

public final class MemoryProfiler implements InternalProfiler {
    private static final List<Object> retained = new ArrayList<>();
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    private long beforeUsedMemory = 0L;

    /**
     * Used to pre-allocate retained objects positions in the list to avoid resizing overhead during the benchmark.
     */
    public static void ensureRetainedCapacity(int capacity) {
        ((ArrayList<Object>) retained).ensureCapacity(capacity);
    }

    public static void addRetained(Object obj) {
        retained.add(obj);
    }

    private static void clearRetained() {
        retained.clear();
    }

    @Override
    public void beforeIteration(BenchmarkParams _benchmarkParams, IterationParams _iterationParams) {
        clearRetained();
        runSystemGC();
        beforeUsedMemory = getUsedMemory();
    }

    @Override
    public Collection<? extends Result> afterIteration(
            BenchmarkParams _benchmarkParams, IterationParams _iterationParams, IterationResult result) {
        runSystemGC();
        long afterUsedMemory = getUsedMemory();
        long retainedMemory = afterUsedMemory - beforeUsedMemory;

        List<ScalarResult> results = new ArrayList<>();
        results.add(
                new ScalarResult("mem.retained.total", retainedMemory / 1024.0 / 1024.0, "MB", AggregationPolicy.AVG));
        results.add(new ScalarResult(
                "mem.retained.total.norm",
                (1.0 * retainedMemory) / result.getMetadata().getAllOps(),
                "B/op",
                AggregationPolicy.AVG));
        results.add(new ScalarResult("mem.retained.count", retained.size(), "obj", AggregationPolicy.AVG));
        return results;
    }

    @Override
    public String getDescription() {
        return "Measures retained memory after each iteration";
    }

    private static final int MAX_WAIT_MSEC = 20 * 1000;

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    // Same as BaseRunner#runSystemGC
    private boolean runSystemGC() {
        List<GarbageCollectorMXBean> enabledBeans = new ArrayList<>();

        long beforeGcCount = 0;
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            long count = bean.getCollectionCount();
            if (count != -1) {
                enabledBeans.add(bean);
            }
        }

        for (GarbageCollectorMXBean bean : enabledBeans) {
            beforeGcCount += bean.getCollectionCount();
        }

        // Run the GC twice, and force finalization before each GCs.
        System.runFinalization();
        System.gc();
        System.runFinalization();
        System.gc();

        // Now make sure GC actually happened. We have to wait for two things:
        //   a) That at least two collections happened, indicating GC work.
        //   b) That counter updates have not happened for a while, indicating GC work had ceased.
        //
        // Note there is an opportunity window for a concurrent GC to happen before the first
        // System.gc() call, which would get counted towards our GCs. This race is unresolvable
        // unless we have GC-specific information about the collection cycles, and verify those
        // were indeed GCs triggered by us.

        if (enabledBeans.isEmpty()) {
            System.out.println("WARNING: MXBeans can not report GC info. System.gc() invoked, pessimistically waiting "
                    + MAX_WAIT_MSEC + " msecs");
            try {
                TimeUnit.MILLISECONDS.sleep(MAX_WAIT_MSEC);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        }

        boolean gcHappened = false;

        long start = System.nanoTime();
        while (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start) < MAX_WAIT_MSEC) {
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long afterGcCount = 0;
            for (GarbageCollectorMXBean bean : enabledBeans) {
                afterGcCount += bean.getCollectionCount();
            }

            if (!gcHappened) {
                if (afterGcCount - beforeGcCount >= 2) {
                    gcHappened = true;
                }
            } else {
                if (afterGcCount == beforeGcCount) {
                    // Stable!
                    return true;
                }
                beforeGcCount = afterGcCount;
            }
        }

        if (gcHappened) {
            System.out.println("WARNING: System.gc() was invoked but unable to wait while GC stopped, is GC too"
                    + " asynchronous?");
        } else {
            System.out.println("WARNING: System.gc() was invoked but couldn't detect a GC occurring, is System.gc()"
                    + " disabled?");
        }
        return false;
    }

    private long getUsedMemory() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed();
    }
}
