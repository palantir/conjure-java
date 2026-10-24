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

package com.palantir.conjure.java.lib.internal;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

final class ConjureUnionSerializerCacheTest {
    @Test
    void cachesEveryVariantForEachMapperAndProperty() throws IOException {
        for (int count : new int[] {1, 2, 8, 9, 16}) {
            for (String prefix : List.of("first:", "second:")) {
                AtomicInteger contextualizations = new AtomicInteger();
                ObjectMapper mapper = mapper(prefix, contextualizations);
                ObjectWriter writer = mapper.writerFor(Wide.class);
                List<Wide> values = variants().subList(0, count);
                assertValues(mapper, writer, prefix, values);
                int warmed = contextualizations.get();
                for (int i = 0; i < 3; i++) {
                    assertValues(mapper, writer, prefix, values);
                    // The ninth variant is also hot in a skewed distribution.
                    assertValues(mapper, writer, prefix, List.of(values.get(count - 1)));
                    assertThat(contextualizations.get()).isEqualTo(warmed);
                }
            }
        }
    }

    @Test
    void preservesIsolationDuringConcurrentCacheMisses() throws Exception {
        AtomicInteger firstCount = new AtomicInteger();
        AtomicInteger secondCount = new AtomicInteger();
        ObjectMapper first = mapper("first:", firstCount);
        ObjectMapper second = mapper("second:", secondCount);
        ObjectWriter firstWriter = first.writerFor(Wide.class);
        ObjectWriter secondWriter = second.writerFor(Wide.class);
        List<Wide> values = variants();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            CyclicBarrier start = new CyclicBarrier(4);
            List<Callable<Void>> tasks = IntStream.range(0, 4)
                    .<Callable<Void>>mapToObj(_index -> () -> {
                        start.await(30, TimeUnit.SECONDS);
                        for (int i = 0; i < 10; i++) {
                            assertValues(first, firstWriter, "first:", values);
                            assertValues(second, secondWriter, "second:", values);
                        }
                        return null;
                    })
                    .toList();
            for (Future<Void> result : executor.invokeAll(tasks, 30, TimeUnit.SECONDS)) {
                result.get();
            }
        } finally {
            executor.shutdownNow();
        }
        assertValues(first, firstWriter, "first:", values);
        assertValues(second, secondWriter, "second:", values);
        int firstWarmed = firstCount.get();
        int secondWarmed = secondCount.get();
        assertValues(first, firstWriter, "first:", values);
        assertValues(second, secondWriter, "second:", values);
        assertThat(firstCount.get()).isEqualTo(firstWarmed);
        assertThat(secondCount.get()).isEqualTo(secondWarmed);
    }

    private static void assertValues(ObjectMapper mapper, ObjectWriter writer, String prefix, List<Wide> values)
            throws IOException {
        for (Wide value : values) {
            JsonNode root = mapper.readTree(writer.writeValueAsString(value));
            assertThat(root.get("context").textValue()).isEqualTo(prefix + "root");
            assertThat(root.get("kind").textValue()).isEqualTo(value.getClass().getSimpleName());
            JsonNode holder = mapper.readTree(
                    mapper.writeValueAsString(new Holder(value, value, List.of(value), Map.of("key", value))));
            assertThat(holder.get("first").get("context").textValue()).isEqualTo(prefix + "first");
            assertThat(holder.get("second").get("context").textValue()).isEqualTo(prefix + "second");
            assertThat(holder.get("values").get(0).get("context").textValue()).isEqualTo(prefix + "values");
            assertThat(holder.get("mapped").get("key").get("context").textValue())
                    .isEqualTo(prefix + "mapped");
        }
    }

    private static ObjectMapper mapper(String prefix, AtomicInteger contextualizations) {
        SimpleModule module = new SimpleModule();
        for (Wide value : variants()) {
            module.addSerializer(
                    value.getClass(), new VariantSerializer(prefix, "uncontextualized", contextualizations));
        }
        return new ObjectMapper().registerModule(module);
    }

    private static final class VariantSerializer extends JsonSerializer<Object> implements ContextualSerializer {
        private final String prefix;
        private final String property;
        private final AtomicInteger contextualizations;

        VariantSerializer(String prefix, String property, AtomicInteger contextualizations) {
            this.prefix = prefix;
            this.property = property;
            this.contextualizations = contextualizations;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider _provider, BeanProperty beanProperty) {
            contextualizations.incrementAndGet();
            return new VariantSerializer(
                    prefix, beanProperty == null ? "root" : beanProperty.getName(), contextualizations);
        }

        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider _provider) throws IOException {
            generator.writeStartObject();
            generator.writeStringField("context", prefix + property);
            generator.writeStringField("kind", value.getClass().getSimpleName());
            generator.writeEndObject();
        }
    }

    record Holder(
            @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
            Wide first,

            @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
            Wide second,

            @JsonSerialize(contentUsing = ConjureJacksonSupport.UnionSerializer.class)
            List<Wide> values,

            @JsonSerialize(contentUsing = ConjureJacksonSupport.UnionSerializer.class)
            Map<String, Wide> mapped) {}

    @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
    sealed interface Wide
            permits Value0,
                    Value1,
                    Value2,
                    Value3,
                    Value4,
                    Value5,
                    Value6,
                    Value7,
                    Value8,
                    Value9,
                    Value10,
                    Value11,
                    Value12,
                    Value13,
                    Value14,
                    Value15 {}

    private static List<Wide> variants() {
        return List.of(
                new Value0(),
                new Value1(),
                new Value2(),
                new Value3(),
                new Value4(),
                new Value5(),
                new Value6(),
                new Value7(),
                new Value8(),
                new Value9(),
                new Value10(),
                new Value11(),
                new Value12(),
                new Value13(),
                new Value14(),
                new Value15());
    }

    @JsonSerialize
    record Value0() implements Wide {}

    @JsonSerialize
    record Value1() implements Wide {}

    @JsonSerialize
    record Value2() implements Wide {}

    @JsonSerialize
    record Value3() implements Wide {}

    @JsonSerialize
    record Value4() implements Wide {}

    @JsonSerialize
    record Value5() implements Wide {}

    @JsonSerialize
    record Value6() implements Wide {}

    @JsonSerialize
    record Value7() implements Wide {}

    @JsonSerialize
    record Value8() implements Wide {}

    @JsonSerialize
    record Value9() implements Wide {}

    @JsonSerialize
    record Value10() implements Wide {}

    @JsonSerialize
    record Value11() implements Wide {}

    @JsonSerialize
    record Value12() implements Wide {}

    @JsonSerialize
    record Value13() implements Wide {}

    @JsonSerialize
    record Value14() implements Wide {}

    @JsonSerialize
    record Value15() implements Wide {}
}
