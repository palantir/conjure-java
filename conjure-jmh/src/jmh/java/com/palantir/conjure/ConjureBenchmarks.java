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
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.GCProfiler;
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

    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

    private static final ObjectMapper mapper = ObjectMappers.newClientJsonMapper();

    // Field to retain objects and measure retained memory
    private List<Object> retainedObjects;
    private long baselineMemory;

    @Setup
    public void before() {
        retainedObjects = new ArrayList<>();

        // Force GC and get baseline memory before benchmark starts
        forceGc();

        baselineMemory = getUsedMemory();
        System.out.println("Baseline memory: " + baselineMemory + " bytes");
    }

    @TearDown
    public void after() {
        // Force GC to see what's actually retained
        forceGc();

        long finalMemory = getUsedMemory();
        long retainedMemory = finalMemory - baselineMemory;

        System.out.println("\n=== Retained Memory Analysis ===");
        System.out.println("Final memory: " + finalMemory + " bytes");
        System.out.println(
                "Retained memory: " + retainedMemory + " bytes (" + (retainedMemory / 1024.0 / 1024.0) + " MB)");
        System.out.println("Objects retained: " + retainedObjects.size());
        System.out.println("================================\n");
    }

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
        retainedObjects.add(mapper.readValue(json.json, mapImpl.clazz));
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

    private void forceGc() {
        System.gc();
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private long getUsedMemory() {
        MemoryUsage heapUsage = memoryBean.getHeapMemoryUsage();
        return heapUsage.getUsed();
    }

    public static void main(String[] _args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ConjureBenchmarks.class.getSimpleName())
                .addProfiler(GCProfiler.class) // Shows allocation rates
                .build();

        new Runner(opt).run();
    }
}
