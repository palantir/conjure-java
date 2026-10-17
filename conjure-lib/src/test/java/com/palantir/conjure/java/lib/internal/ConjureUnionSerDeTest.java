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
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
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
import java.util.stream.IntStream;
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
    void preservesUnknownPayloadsOnBothPaths() throws IOException {
        for (String json : List.of(
                "{\"type\":\"future\",\"future\":{\"nested\":[1,2]},\"extra\":null}",
                "{\"future\":{\"nested\":[1,2]},\"type\":\"future\",\"extra\":null}",
                "{\"future\":{\"nested\":[1,2]},\"extra\":null,\"type\":\"future\"}",
                "{\"type\":\"future\"}")) {
            SampleUnion union = mapper.readValue(json, SampleUnion.class);
            assertThat(union).isInstanceOf(Unknown.class);
            assertThat(mapper.readTree(mapper.writerFor(SampleUnion.class).writeValueAsBytes(union)))
                    .isEqualTo(mapper.readTree(json));
        }
    }

    @Test
    void rejectsMissingOrNonStringDiscriminators() {
        for (String json : List.of(
                "{}", "[]", "1", "{\"value\":1}", "{\"type\":null}", "{\"type\":1}", "{\"value\":1,\"type\":false}")) {
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
