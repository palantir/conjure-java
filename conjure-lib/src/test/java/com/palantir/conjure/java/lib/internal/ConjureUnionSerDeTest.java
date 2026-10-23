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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.sun.management.ThreadMXBean;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

final class ConjureUnionSerDeTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void readsKnownVariantsWithDiscriminatorInAnyPosition() throws IOException {
        for (String json : List.of(
                "{\"type\":\"known\",\"value\":\"hello\"}",
                "{\"value\":\"hello\",\"type\":\"known\"}",
                "{\"ignored\":{\"nested\":[1,2]},\"type\":\"known\",\"value\":\"hello\",\"after\":true}")) {
            assertThat(mapper.readValue(json, SampleUnion.class)).isEqualTo(new Known("hello"));
            assertThat(mapper.readValue(json, Alias.class)).isEqualTo(new Alias(new Known("hello")));
            assertThat(mapper.readValue("{\"values\":[" + json + "," + json + "]}", Holder.class))
                    .isEqualTo(new Holder(List.of(new Known("hello"), new Known("hello"))));
        }
        for (String json : List.of("{\"type\":\"number\",\"value\":42}", "{\"value\":42,\"type\":\"number\"}")) {
            assertThat(mapper.readValue(json, SampleUnion.class)).isEqualTo(new NumberVariant(42));
            assertThat(mapper.readValue(json, Alias.class)).isEqualTo(new Alias(new NumberVariant(42)));
        }
    }

    @Test
    void givesCustomVariantDeserializersAnObjectStart() throws IOException {
        ObjectMapper custom = new ObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(Known.class, new JsonDeserializer<>() {
                    @Override
                    public Known deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                        if (!parser.isExpectedStartObjectToken()) {
                            return context.reportInputMismatch(Known.class, "Expected START_OBJECT");
                        }
                        assertThat(parser.nextToken()).isEqualTo(JsonToken.FIELD_NAME);
                        assertThat(parser.currentName()).isEqualTo("value");
                        assertThat(parser.nextToken()).isEqualTo(JsonToken.VALUE_STRING);
                        Known value = new Known(parser.getText());
                        assertThat(parser.nextToken()).isEqualTo(JsonToken.END_OBJECT);
                        return value;
                    }
                }));
        for (String json :
                List.of("{\"type\":\"known\",\"value\":\"hello\"}", "{\"value\":\"hello\",\"type\":\"known\"}")) {
            assertThat(custom.readValue(json, SampleUnion.class)).isEqualTo(new Known("hello"));
            assertThat(custom.readValue(json, Alias.class)).isEqualTo(new Alias(new Known("hello")));
            assertThat(custom.readValue("{\"values\":[" + json + "," + json + "]}", Holder.class))
                    .isEqualTo(new Holder(List.of(new Known("hello"), new Known("hello"))));
        }
    }

    @Test
    void supportsNextValueAndSkippingBufferedChildren() throws IOException {
        ObjectMapper custom = new ObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(Known.class, new JsonDeserializer<>() {
                    @Override
                    public Known deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
                        assertThat(parser.isExpectedStartObjectToken()).isTrue();
                        JsonToken token = parser.nextValue();
                        if (token == JsonToken.START_OBJECT) {
                            parser.skipChildren();
                            token = parser.nextValue();
                        }
                        assertThat(token).isEqualTo(JsonToken.VALUE_STRING);
                        assertThat(parser.currentName()).isEqualTo("value");
                        Known value = new Known(parser.getText());
                        assertThat(parser.nextValue()).isEqualTo(JsonToken.END_OBJECT);
                        return value;
                    }
                }));
        for (String json : List.of(
                "{\"type\":\"known\",\"value\":\"hello\"}",
                "{\"value\":\"hello\",\"type\":\"known\"}",
                "{\"ignored\":{\"type\":17,\"nested\":{\"type\":\"unused\"}},\"value\":\"hello\",\"type\":\"known\"}")) {
            assertThat(custom.readValue(json, SampleUnion.class)).isEqualTo(new Known("hello"));
            assertThat(custom.readValue(json, Alias.class)).isEqualTo(new Alias(new Known("hello")));
            assertThat(custom.readValue("{\"values\":[" + json + "," + json + "]}", Holder.class))
                    .isEqualTo(new Holder(List.of(new Known("hello"), new Known("hello"))));
        }
    }

    @Test
    void reusesBufferedObjectsAfterSkippingUnrelatedObjects() throws IOException {
        ObjectMapper ignoring = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        String json = """
            {"ignored":{"type":17,"nested":{"type":"unused"}},
             "values":[{"value":"hello","type":"known"},{"type":"number","value":42}],
             "type":"nested"}
            """;
        assertThat(ignoring.readValue(json, SampleUnion.class))
                .isEqualTo(new NestedVariant(List.of(new Known("hello"), new NumberVariant(42))));
        assertThat(ignoring.readValue(json.getBytes(StandardCharsets.UTF_8), Alias.class))
                .isEqualTo(new Alias(new NestedVariant(List.of(new Known("hello"), new NumberVariant(42)))));
    }

    @Test
    void allocationScalesLinearlyForRecursiveDiscriminatorLastUnions() throws IOException {
        Object management = ManagementFactory.getThreadMXBean();
        Assumptions.assumeTrue(management instanceof ThreadMXBean);
        ThreadMXBean allocation = (ThreadMXBean) management;
        Assumptions.assumeTrue(
                allocation.isThreadAllocatedMemorySupported() && allocation.isThreadAllocatedMemoryEnabled());
        ObjectReader reader = mapper.readerFor(SampleUnion.class);
        String smaller = recursiveJson(64);
        String larger = recursiveJson(128);
        for (int i = 0; i < 200; i++) {
            reader.readValue(smaller);
            reader.readValue(larger);
        }
        long smallerAllocation = allocatedBytes(allocation, reader, smaller);
        long largerAllocation = allocatedBytes(allocation, reader, larger);
        assertThat(largerAllocation).isLessThan(smallerAllocation * 3);
    }

    private static String recursiveJson(int depth) {
        return "{\"values\":[".repeat(depth)
                + "{\"type\":\"known\",\"value\":\"hello\"}"
                + "],\"type\":\"nested\"}".repeat(depth);
    }

    private static long allocatedBytes(ThreadMXBean allocation, ObjectReader reader, String json) throws IOException {
        long thread = Thread.currentThread().getId();
        long before = allocation.getThreadAllocatedBytes(thread);
        Object last = null;
        for (int i = 0; i < 100; i++) {
            last = reader.readValue(json);
        }
        long bytes = allocation.getThreadAllocatedBytes(thread) - before;
        assertThat(last).isInstanceOf(NestedVariant.class);
        return bytes;
    }

    @Test
    void preservesUnknownPayloadsOnBothPaths() throws IOException {
        for (String json : List.of(
                "{\"type\":\"future\",\"future\":{\"nested\":[1,2]},\"extra\":null}",
                "{\"future\":{\"nested\":[1,2]},\"type\":\"future\",\"extra\":null}",
                "{\"future\":{\"nested\":[1,2]},\"extra\":null,\"type\":\"future\"}",
                "{\"type\":\"future\",\"future\":{\"type\":17,\"nested\":{\"type\":\"known\"}}}",
                "{\"future\":{\"type\":17,\"nested\":{\"type\":\"known\"}},\"type\":\"future\"}",
                "{\"type\":\"future\"}")) {
            SampleUnion union = mapper.readValue(json, SampleUnion.class);
            assertThat(union).isInstanceOf(Unknown.class);
            assertThat(mapper.readTree(mapper.writerFor(SampleUnion.class).writeValueAsBytes(union)))
                    .isEqualTo(mapper.readTree(json));
        }
    }

    @Test
    void preservesConfiguredNumberPrecisionWhenBuffering() throws IOException {
        ObjectMapper precise = new ObjectMapper()
                .enable(
                        DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
                        DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
        for (String json : List.of(
                "{\"type\":\"future\",\"decimal\":1.234567890123456789,\"integer\":123456789012345678901234567890}",
                "{\"decimal\":1.234567890123456789,\"integer\":123456789012345678901234567890,\"type\":\"future\"}")) {
            Unknown result = (Unknown) precise.readValue(json, SampleUnion.class);
            assertThat(result.values())
                    .containsEntry("decimal", new BigDecimal("1.234567890123456789"))
                    .containsEntry("integer", new BigInteger("123456789012345678901234567890"));
        }
    }

    @Test
    void rejectsMissingOrNonStringDiscriminators() {
        for (String json : List.of(
                "{}",
                "[]",
                "1",
                "{\"value\":1}",
                "{\"type\":null}",
                "{\"type\":1}",
                "{\"value\":1,\"type\":false}",
                "{\"values\":[{\"value\":\"missing\"}],\"type\":\"nested\"}",
                "{\"values\":[{\"value\":\"invalid\",\"type\":null}],\"type\":\"nested\"}")) {
            assertThatThrownBy(() -> mapper.readValue(json, SampleUnion.class))
                    .isInstanceOf(JsonMappingException.class);
        }
    }

    @Test
    void honorsCaseInsensitivePropertiesOnBothPaths() throws IOException {
        ObjectMapper insensitive = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .build();
        for (String json :
                List.of("{\"TYPE\":\"known\",\"VALUE\":\"hello\"}", "{\"VALUE\":\"hello\",\"TYPE\":\"known\"}")) {
            assertThat(insensitive.readValue(json, SampleUnion.class)).isEqualTo(new Known("hello"));
            assertThatThrownBy(() -> mapper.readValue(json, SampleUnion.class))
                    .isInstanceOf(JsonMappingException.class);
        }
    }

    @Test
    void honorsCaseInsensitiveDiscriminatorsInBufferedRecursiveVariants() throws IOException {
        ObjectMapper insensitive = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .build();
        String json = "{\"VALUES\":[{\"VALUE\":\"hello\",\"TYPE\":\"known\"}],\"TYPE\":\"nested\"}";
        assertThat(insensitive.readValue(json, SampleUnion.class))
                .isEqualTo(new NestedVariant(List.of(new Known("hello"))));
    }

    @Test
    void cachesVariantsWithinEachMapper() throws IOException {
        ObjectMapper first = prefixedMapper("first:");
        ObjectMapper second = prefixedMapper("second:");
        for (int i = 0; i < 2; i++) {
            assertThat(first.readValue("{\"type\":\"known\",\"value\":\"hello\"}", SampleUnion.class))
                    .isEqualTo(new Known("first:hello"));
            assertThat(second.readValue("{\"type\":\"known\",\"value\":\"hello\"}", SampleUnion.class))
                    .isEqualTo(new Known("second:hello"));
        }
    }

    @Test
    void resolvesVariantsConcurrently() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            CyclicBarrier start = new CyclicBarrier(4);
            List<Callable<Void>> tasks = IntStream.range(0, 4)
                    .<Callable<Void>>mapToObj(_index -> () -> {
                        start.await(30, TimeUnit.SECONDS);
                        for (int i = 0; i < 100; i++) {
                            assertThat(mapper.readValue("{\"type\":\"known\",\"value\":\"hello\"}", SampleUnion.class))
                                    .isEqualTo(new Known("hello"));
                            assertThat(mapper.readValue("{\"type\":\"number\",\"value\":42}", OtherUnion.class))
                                    .isEqualTo(new NumberVariant(42));
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
    }

    @Test
    void serializesStaticAndDynamicUnionTypes() throws IOException {
        for (SampleUnion value :
                List.of(new Known("hello"), new NumberVariant(42), new Unknown("future", Map.of("future", 42)))) {
            String json = mapper.writerFor(SampleUnion.class).writeValueAsString(value);
            assertThat(json).startsWith("{\"type\":");
            assertThat(mapper.writeValueAsString(value)).isEqualTo(json);
            assertThat(mapper.readValue(json, SampleUnion.class)).isEqualTo(value);
        }
    }

    @Test
    @SuppressWarnings("DangerousJsonTypeInfoUsage") // Exercise typed dispatch with an explicit subtype allowlist.
    void delegatesPolymorphicSerializationToConcreteVariants() throws IOException {
        ObjectMapper typed = new ObjectMapper()
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType(SampleUnion.class)
                                .build(),
                        ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        Known value = new Known("hello");
        String json = typed.writerFor(SampleUnion.class).writeValueAsString(value);
        assertThat(json).contains(Known.class.getName());
        assertThat(typed.readValue(json, SampleUnion.class)).isEqualTo(value);
    }

    @Test
    void contextualizesVariantSerializersForEachMapperAndProperty() throws IOException {
        for (String prefix : List.of("first:", "second:")) {
            AtomicInteger contextualizations = new AtomicInteger();
            ObjectMapper custom = contextualSerializerMapper(prefix, contextualizations);
            Known value = new Known("hello");
            SerializationHolder holder = new SerializationHolder(value, value, List.of(value, value));
            int warmed = 0;
            for (int i = 0; i < 3; i++) {
                String root = custom.writerFor(SampleUnion.class).writeValueAsString(value);
                assertThat(custom.readTree(root).get("value").textValue()).isEqualTo(prefix + "root:hello");
                JsonNode properties = custom.readTree(custom.writeValueAsString(holder));
                assertThat(properties.get("first").get("value").textValue()).isEqualTo(prefix + "first:hello");
                assertThat(properties.get("second").get("value").textValue()).isEqualTo(prefix + "second:hello");
                assertThat(properties.get("values").get(0).get("value").textValue())
                        .isEqualTo(prefix + "values:hello");
                assertThat(properties.get("values").get(1).get("value").textValue())
                        .isEqualTo(prefix + "values:hello");
                if (i == 0) {
                    warmed = contextualizations.get();
                } else {
                    assertThat(contextualizations.get()).isEqualTo(warmed);
                }
            }
        }
    }

    @Test
    @SuppressWarnings("DangerousJsonTypeInfoUsage") // Exercise typed dispatch with an explicit subtype allowlist.
    void contextualizesTypedVariantSerializers() throws IOException {
        ObjectMapper typed = contextualSerializerMapper("typed:", new AtomicInteger())
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType(SampleUnion.class)
                                .build(),
                        ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        SerializerProvider provider = typed.getSerializerProviderInstance();
        StringWriter output = new StringWriter();
        try (JsonGenerator generator = typed.getFactory().createGenerator(output)) {
            provider.findValueSerializer(SampleUnion.class, null)
                    .serializeWithType(
                            new Known("hello"),
                            generator,
                            provider,
                            provider.findTypeSerializer(typed.constructType(SampleUnion.class)));
        }
        assertThat(typed.readValue(output.toString(), SampleUnion.class)).isEqualTo(new Known("typed:root:hello"));
        String json = typed.writerFor(SampleUnion.class).writeValueAsString(new Known("hello"));
        assertThat(typed.readValue(json, SampleUnion.class)).isEqualTo(new Known("typed:root:hello"));
    }

    @Test
    void contextualSerializersRemainIsolatedDuringConcurrentReadsAndWrites() throws Exception {
        ObjectMapper first = contextualSerializerMapper("first:", new AtomicInteger());
        ObjectMapper second = contextualSerializerMapper("second:", new AtomicInteger());
        SerializationHolder holder = new SerializationHolder(new Known("a"), new Known("b"), List.of(new Known("c")));
        ExecutorService executor = Executors.newFixedThreadPool(4);
        try {
            CyclicBarrier start = new CyclicBarrier(4);
            List<Callable<Void>> tasks = IntStream.range(0, 4)
                    .<Callable<Void>>mapToObj(_index -> () -> {
                        start.await(30, TimeUnit.SECONDS);
                        for (int i = 0; i < 50; i++) {
                            for (ObjectMapper configured : List.of(first, second)) {
                                String prefix = configured.equals(first) ? "first:" : "second:";
                                JsonNode json = configured.readTree(configured.writeValueAsString(holder));
                                assertThat(json.get("first").get("value").textValue())
                                        .isEqualTo(prefix + "first:a");
                                assertThat(json.get("second").get("value").textValue())
                                        .isEqualTo(prefix + "second:b");
                                assertThat(json.get("values")
                                                .get(0)
                                                .get("value")
                                                .textValue())
                                        .isEqualTo(prefix + "values:c");
                                assertThat(configured.readValue(
                                                "{\"values\":[{\"type\":\"known\",\"value\":\"a\"},{\"value\":\"b\",\"type\":\"known\"}],\"type\":\"nested\"}",
                                                SampleUnion.class))
                                        .isEqualTo(new NestedVariant(List.of(new Known("a"), new Known("b"))));
                            }
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
    }

    private static ObjectMapper contextualSerializerMapper(String prefix, AtomicInteger contextualizations) {
        return new ObjectMapper()
                .registerModule(new SimpleModule()
                        .addSerializer(Known.class, new ContextualKnownSerializer(prefix, null, contextualizations)));
    }

    private static final class ContextualKnownSerializer extends JsonSerializer<Known> implements ContextualSerializer {
        private final String prefix;
        private final String property;
        private final AtomicInteger contextualizations;

        ContextualKnownSerializer(String prefix, String property, AtomicInteger contextualizations) {
            this.prefix = prefix;
            this.property = property;
            this.contextualizations = contextualizations;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider _provider, BeanProperty beanProperty) {
            contextualizations.incrementAndGet();
            return new ContextualKnownSerializer(
                    prefix, beanProperty == null ? "root" : beanProperty.getName(), contextualizations);
        }

        @Override
        public void serialize(Known value, JsonGenerator generator, SerializerProvider _provider) throws IOException {
            generator.writeStartObject();
            writeFields(value, generator);
            generator.writeEndObject();
        }

        @Override
        public void serializeWithType(
                Known value, JsonGenerator generator, SerializerProvider _provider, TypeSerializer typeSerializer)
                throws IOException {
            WritableTypeId id =
                    typeSerializer.writeTypePrefix(generator, typeSerializer.typeId(value, JsonToken.START_OBJECT));
            writeFields(value, generator);
            typeSerializer.writeTypeSuffix(generator, id);
        }

        private void writeFields(Known value, JsonGenerator generator) throws IOException {
            assertThat(property).isNotNull();
            generator.writeStringField("type", "known");
            generator.writeStringField("value", prefix + property + ":" + value.value());
        }
    }

    record SerializationHolder(SampleUnion first, SampleUnion second, List<SampleUnion> values) {}

    @Test
    void resolvesEachUnionInPropertiesAndContainers() throws IOException {
        String json = """
            {"first":{"type":"known","value":"first"},
             "second":{"type":"number","value":2},
             "values":{"third":{"type":"known","value":"third"}}}
            """;
        MixedHolder expected =
                new MixedHolder(new Known("first"), new NumberVariant(2), Map.of("third", new Known("third")));
        for (int i = 0; i < 2; i++) {
            assertThat(mapper.readValue(json, MixedHolder.class)).isEqualTo(expected);
            assertThat(mapper.readValue("{\"type\":\"number\",\"value\":42}", OtherUnion.class))
                    .isEqualTo(new NumberVariant(42));
            assertThat(mapper.readValue("{\"type\":\"known\",\"value\":\"root\"}", SampleUnion.class))
                    .isEqualTo(new Known("root"));
        }
    }

    @Test
    void readsUnknownVariantsOfEmptyUnions() throws IOException {
        for (String json : List.of(
                "{\"type\":\"known\",\"payload\":[1,2]}",
                "{\"payload\":[1,2],\"type\":\"known\"}",
                "{\"type\":\"future\"}")) {
            EmptyUnion result = mapper.readValue(json, EmptyUnion.class);
            assertThat(mapper.readTree(mapper.writerFor(EmptyUnion.class).writeValueAsBytes(result)))
                    .isEqualTo(mapper.readTree(json));
        }
    }

    @Test
    void snapshotsVariantTypes() throws IOException {
        List<Class<? extends SampleUnion>> variants =
                new ArrayList<>(List.of(Known.class, NumberVariant.class, NestedVariant.class));
        SampleDeserializer deserializer = new SampleDeserializer(variants);
        variants.clear();
        variants.add(NumberVariant.class);
        ObjectMapper configured = new ObjectMapper()
                .addMixIn(SampleUnion.class, DefaultDeserializer.class)
                .registerModule(new SimpleModule().addDeserializer(SampleUnion.class, deserializer));
        assertThat(configured.readValue("{\"type\":\"known\",\"value\":\"hello\"}", SampleUnion.class))
                .isEqualTo(new Known("hello"));
        assertThat(configured.readValue("{\"type\":\"number\",\"value\":42}", SampleUnion.class))
                .isEqualTo(new NumberVariant(42));
    }

    @Test
    void readsRecursiveVariantsWithDiscriminatorInAnyPosition() throws IOException {
        for (String json : List.of(
                "{\"type\":\"nested\",\"values\":[{\"value\":\"hello\",\"type\":\"known\"},{\"type\":\"number\",\"value\":42}]}",
                "{\"values\":[{\"type\":\"known\",\"value\":\"hello\"},{\"value\":42,\"type\":\"number\"}],\"type\":\"nested\"}")) {
            NestedVariant expected = new NestedVariant(List.of(new Known("hello"), new NumberVariant(42)));
            assertThat(mapper.readValue(json, SampleUnion.class)).isEqualTo(expected);
            assertThat(mapper.readValue(json.getBytes(StandardCharsets.UTF_8), SampleUnion.class))
                    .isEqualTo(expected);
            assertThat(mapper.readValue("{\"values\":[" + json + "," + json + "]}", Holder.class))
                    .isEqualTo(new Holder(List.of(expected, expected)));
        }
    }

    private static ObjectMapper prefixedMapper(String prefix) {
        return new ObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(String.class, new JsonDeserializer<>() {
                    @Override
                    public String deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
                        return prefix + parser.getText();
                    }
                }));
    }

    @JsonDeserialize(using = SampleDeserializer.class)
    @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
    sealed interface SampleUnion permits Known, NumberVariant, NestedVariant, Unknown {}

    @JsonDeserialize
    @JsonSerialize
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder("type")
    record Known(String value) implements SampleUnion {
        @JsonProperty("type")
        String type() {
            return "known";
        }
    }

    @JsonDeserialize
    @JsonSerialize
    @JsonPropertyOrder("type")
    record NestedVariant(List<SampleUnion> values) implements SampleUnion {
        @JsonProperty("type")
        String type() {
            return "nested";
        }
    }

    @JsonDeserialize
    @JsonSerialize
    @JsonPropertyOrder("type")
    record Unknown(String type, @JsonAnyGetter Map<String, Object> values) implements SampleUnion {}

    record Alias(@JsonValue SampleUnion value) {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        Alias {}
    }

    record Holder(List<SampleUnion> values) {}

    record MixedHolder(SampleUnion first, OtherUnion second, Map<String, SampleUnion> values) {}

    @JsonDeserialize(using = OtherDeserializer.class)
    @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
    sealed interface OtherUnion permits NumberVariant, OtherUnknown {}

    @JsonDeserialize
    @JsonSerialize
    @JsonPropertyOrder("type")
    record NumberVariant(int value) implements SampleUnion, OtherUnion {
        @JsonProperty("type")
        String type() {
            return "number";
        }
    }

    @JsonDeserialize
    @JsonSerialize
    @JsonPropertyOrder("type")
    record OtherUnknown(String type, @JsonAnyGetter Map<String, Object> values) implements OtherUnion {}

    static final class OtherDeserializer extends ConjureJacksonSupport.UnionDeserializer<OtherUnion> {
        OtherDeserializer() {
            super(
                    OtherUnion.class,
                    List.of(NumberVariant.class),
                    type -> "number".equals(type) ? 0 : -1,
                    OtherUnknown::new);
        }
    }

    static final class SampleDeserializer extends ConjureJacksonSupport.UnionDeserializer<SampleUnion> {
        SampleDeserializer() {
            this(List.of(Known.class, NumberVariant.class, NestedVariant.class));
        }

        SampleDeserializer(List<Class<? extends SampleUnion>> variants) {
            super(
                    SampleUnion.class,
                    variants,
                    type -> switch (type) {
                        case "known" -> 0;
                        case "number" -> 1;
                        case "nested" -> 2;
                        default -> -1;
                    },
                    Unknown::new);
        }
    }

    @JsonDeserialize
    private interface DefaultDeserializer {}

    @JsonDeserialize(using = EmptyDeserializer.class)
    @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
    sealed interface EmptyUnion permits EmptyUnknown {}

    @JsonDeserialize
    @JsonSerialize
    @JsonPropertyOrder("type")
    record EmptyUnknown(String type, @JsonAnyGetter Map<String, Object> values) implements EmptyUnion {}

    static final class EmptyDeserializer extends ConjureJacksonSupport.UnionDeserializer<EmptyUnion> {
        EmptyDeserializer() {
            super(EmptyUnion.class, List.of(), _type -> -1, EmptyUnknown::new);
        }
    }
}
