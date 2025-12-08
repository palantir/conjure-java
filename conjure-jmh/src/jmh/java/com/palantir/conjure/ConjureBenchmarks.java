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

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableMap;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.Preconditions;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.jspecify.annotations.NullMarked;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.InternalProfiler;
import org.openjdk.jmh.results.AggregationPolicy;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.ScalarResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@SuppressWarnings({"NullAway", "designforextension", "checkstyle:RegexpSinglelineJava", "checkstyle:VisibilityModifier"
})
public class ConjureBenchmarks {

    private static final ObjectMapper mapper = ObjectMappers.newClientJsonMapper();

    @SuppressWarnings("ImmutableEnumChecker")
    public enum RawJson {
        EMPTY("{}"),
        SINGLETON_MAP("{\"map\":{\"key1\":\"value1\"}}"),
        NORMAL_MAP("{\"map\":{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}}");

        private final byte[] json;

        RawJson(String jsonString) {
            this.json = jsonString.getBytes(StandardCharsets.UTF_8);
        }
    }

    public enum MapImplementation {
        NORMAL(NormalMap.class),
        SINGLETON(SingletonMap.class),
        GUAVA_IMMUTABLE(GuavaImMap.class);

        private final Class<?> clazz;

        MapImplementation(Class<?> clazz) {
            this.clazz = clazz;
        }
    }

    @Param
    public RawJson json;

    @Param
    public MapImplementation mapImpl;

    @Benchmark
    public void testAllocatingBenchmark() throws IOException {
        MemoryProfiler.addRetained(mapper.readValue(json.json, mapImpl.clazz));
    }

    @JsonDeserialize(builder = NormalMap.Builder.class)
    public static final class NormalMap {
        private final Map<String, String> map;

        private NormalMap(Map<String, String> map) {
            this.map = Collections.unmodifiableMap(map);
        }

        public Map<String, String> getMap() {
            return map;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private boolean buildInvoked;

            private Map<String, String> map = new LinkedHashMap<>();

            private Builder() {}

            @JsonSetter(value = "map", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
            public Builder map(Map<String, String> newMap) {
                checkNotBuilt();
                this.map = new LinkedHashMap<>(Preconditions.checkNotNull(newMap, "map cannot be null"));
                return this;
            }

            @CheckReturnValue
            public NormalMap build() {
                checkNotBuilt();
                this.buildInvoked = true;
                return new NormalMap(map);
            }

            private void checkNotBuilt() {
                Preconditions.checkState(!buildInvoked, "Build has already been called");
            }
        }
    }

    private static <K, V> Map<K, V> specialUnmodifiableMap(Map<K, V> in) {
        if (in.isEmpty()) {
            return Map.of();
        }
        if (in.size() == 1) {
            Iterator<Entry<K, V>> itr = in.entrySet().iterator();
            if (itr.hasNext()) {
                Entry<K, V> entry = itr.next();
                if (!itr.hasNext()) {
                    return Map.of(entry.getKey(), entry.getValue());
                }
            }
        }
        return Collections.unmodifiableMap(in);
    }

    @JsonDeserialize(builder = SingletonMap.Builder.class)
    public static final class SingletonMap {
        private final Map<String, String> map;

        private SingletonMap(Map<String, String> map) {
            this.map = specialUnmodifiableMap(map);
        }

        public Map<String, String> getMap() {
            return map;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private boolean buildInvoked;

            private Map<String, String> map = new LinkedHashMap<>();

            private Builder() {}

            @JsonSetter(value = "map", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
            public Builder map(Map<String, String> newMap) {
                checkNotBuilt();
                this.map = new LinkedHashMap<>(Preconditions.checkNotNull(newMap, "map cannot be null"));
                return this;
            }

            @CheckReturnValue
            public SingletonMap build() {
                checkNotBuilt();
                this.buildInvoked = true;
                return new SingletonMap(map);
            }

            private void checkNotBuilt() {
                Preconditions.checkState(!buildInvoked, "Build has already been called");
            }
        }
    }

    @JsonDeserialize(builder = GuavaImMap.Builder.class)
    @NullMarked
    public static final class GuavaImMap {
        private final Map<String, String> map;

        private GuavaImMap(ImmutableMap<String, String> map) {
            this.map = map;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private boolean buildInvoked;

            @Nullable
            private Object map;

            private Builder() {}

            @JsonSetter(value = "map", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
            @JsonDeserialize(as = ImmutableMap.class)
            public Builder map(Map<String, String> newMap) {
                checkNotBuilt();
                this.map = newMap instanceof ImmutableMap<String, String> im
                        ? im
                        : ImmutableMap.<String, String>builder().putAll(newMap);
                return this;
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            @CheckReturnValue
            public GuavaImMap build() {
                checkNotBuilt();
                this.buildInvoked = true;
                ImmutableMap<String, String> finalMap;
                if (map == null) {
                    finalMap = ImmutableMap.of();
                } else if (map instanceof ImmutableMap im) {
                    finalMap = im;
                } else if (map instanceof ImmutableMap.Builder builder) {
                    finalMap = builder.buildOrThrow();
                } else {
                    throw new IllegalStateException("Unexpected map type: " + map.getClass());
                }
                return new GuavaImMap(finalMap);
            }

            private void checkNotBuilt() {
                Preconditions.checkState(!buildInvoked, "Build has already been called");
            }
        }
    }

    public static void main(String[] _args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ConjureBenchmarks.class.getSimpleName())
                .addProfiler(GCProfiler.class, "churn=true")
                .addProfiler(MemoryProfiler.class)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

    public static final class MemoryProfiler implements InternalProfiler {
        private static final List<Object> retained = new ArrayList<>(10_000_000);
        private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        private long beforeUsedMemory = 0L;

        private static void addRetained(Object obj) {
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
            results.add(new ScalarResult(
                    "mem.retained.total", retainedMemory / 1024.0 / 1024.0, "MB", AggregationPolicy.AVG));
            results.add(new ScalarResult(
                    "mem.retained.total.norm",
                    (1.0 * retainedMemory) / result.getMetadata().getAllOps(),
                    "B/op",
                    AggregationPolicy.AVG));
            results.add(new ScalarResult("mem.retained.count", retained.size(), "obj", AggregationPolicy.AVG));
            results.add(new ScalarResult(
                    "mem.retained.count.norm",
                    (1.0 * retained.size()) / result.getMetadata().getAllOps(),
                    "obj",
                    AggregationPolicy.AVG));
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
                System.out.println(
                        "WARNING: MXBeans can not report GC info. System.gc() invoked, pessimistically waiting "
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
}
