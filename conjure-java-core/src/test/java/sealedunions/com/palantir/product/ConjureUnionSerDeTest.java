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

package sealedunions.com.palantir.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
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
                "{\"type\":\"foo\",\"foo\":\"hello\"}",
                "{\"foo\":\"hello\",\"type\":\"foo\"}",
                "{\"ignored\":{\"nested\":[1,2]},\"type\":\"foo\",\"foo\":\"hello\",\"after\":true}")) {
            assertThat(mapper.readValue(json, SimpleUnion.class)).isEqualTo(SimpleUnion.foo("hello"));
            assertThat(mapper.readValue(json, Alias.class)).isEqualTo(new Alias(SimpleUnion.foo("hello")));
            assertThat(mapper.readValue("{\"values\":[" + json + "," + json + "]}", Holder.class))
                    .isEqualTo(new Holder(List.of(SimpleUnion.foo("hello"), SimpleUnion.foo("hello"))));
        }
    }

    @Test
    void preservesUnknownPayloadsOnBothPaths() throws IOException {
        for (String json : List.of(
                "{\"type\":\"future\",\"future\":{\"nested\":[1,2]},\"extra\":null}",
                "{\"future\":{\"nested\":[1,2]},\"type\":\"future\",\"extra\":null}",
                "{\"future\":{\"nested\":[1,2]},\"extra\":null,\"type\":\"future\"}",
                "{\"type\":\"future\"}")) {
            SimpleUnion union = mapper.readValue(json, SimpleUnion.class);
            assertThat(union).isInstanceOf(SimpleUnion.Unknown.class);
            assertThat(mapper.readTree(mapper.writerFor(SimpleUnion.class).writeValueAsBytes(union)))
                    .isEqualTo(mapper.readTree(json));
        }
    }

    @Test
    void rejectsMissingOrNonStringDiscriminators() {
        for (String json : List.of(
                "{}", "[]", "1", "{\"foo\":1}", "{\"type\":null}", "{\"type\":1}", "{\"foo\":1,\"type\":false}")) {
            assertThatThrownBy(() -> mapper.readValue(json, SimpleUnion.class))
                    .isInstanceOf(JsonMappingException.class);
        }
    }

    @Test
    void honorsCaseInsensitivePropertiesOnBothPaths() throws IOException {
        ObjectMapper insensitive = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                .build();
        for (String json : List.of("{\"TYPE\":\"foo\",\"FOO\":\"hello\"}", "{\"FOO\":\"hello\",\"TYPE\":\"foo\"}")) {
            assertThat(insensitive.readValue(json, SimpleUnion.class)).isEqualTo(SimpleUnion.foo("hello"));
            assertThatThrownBy(() -> mapper.readValue(json, SimpleUnion.class))
                    .isInstanceOf(JsonMappingException.class);
        }
    }

    @Test
    void cachesVariantsWithinEachMapper() throws IOException {
        ObjectMapper first = prefixedMapper("first:");
        ObjectMapper second = prefixedMapper("second:");
        for (int i = 0; i < 2; i++) {
            assertThat(first.readValue("{\"type\":\"foo\",\"foo\":\"hello\"}", SimpleUnion.class))
                    .isEqualTo(SimpleUnion.foo("first:hello"));
            assertThat(second.readValue("{\"type\":\"foo\",\"foo\":\"hello\"}", SimpleUnion.class))
                    .isEqualTo(SimpleUnion.foo("second:hello"));
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
                            assertThat(mapper.readValue("{\"type\":\"foo\",\"foo\":\"hello\"}", SimpleUnion.class))
                                    .isEqualTo(SimpleUnion.foo("hello"));
                            assertThat(mapper.readValue(
                                            "{\"type\":\"camelCasedField\",\"camelCasedField\":\"hello\"}",
                                            CamelCaseUnion.class))
                                    .isEqualTo(CamelCaseUnion.camelCasedField("hello"));
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
        for (SimpleUnion value : List.of(SimpleUnion.foo("hello"), SimpleUnion.unknown("future", 42))) {
            String json = mapper.writerFor(SimpleUnion.class).writeValueAsString(value);
            assertThat(json).startsWith("{\"type\":");
            assertThat(mapper.writeValueAsString(value)).isEqualTo(json);
            assertThat(mapper.readValue(json, SimpleUnion.class)).isEqualTo(value);
        }
    }

    @Test
    @SuppressWarnings("DangerousJsonTypeInfoUsage") // Exercise typed dispatch with an explicit subtype allowlist.
    void delegatesPolymorphicSerializationToConcreteVariants() throws IOException {
        ObjectMapper typed = new ObjectMapper()
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfSubType(SimpleUnion.class)
                                .build(),
                        ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
        SimpleUnion value = SimpleUnion.foo("hello");
        String json = typed.writerFor(SimpleUnion.class).writeValueAsString(value);
        assertThat(json).contains(SimpleUnion.Foo.class.getName());
        assertThat(typed.readValue(json, SimpleUnion.class)).isEqualTo(value);
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

    record Alias(@JsonValue SimpleUnion value) {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        Alias {}
    }

    record Holder(List<SimpleUnion> values) {}

    record MixedHolder(SimpleUnion first, CamelCaseUnion second, Map<String, SimpleUnion> values) {}

    @Test
    void contextualizesEachUnionInPropertiesAndContainers() throws IOException {
        String json = """
            {"first":{"type":"foo","foo":"first"},
             "second":{"type":"camelCasedField","camelCasedField":"second"},
             "values":{"third":{"type":"bar","bar":3}}}
            """;
        MixedHolder expected = new MixedHolder(
                SimpleUnion.foo("first"),
                CamelCaseUnion.camelCasedField("second"),
                Map.of("third", SimpleUnion.bar(3)));
        for (int i = 0; i < 2; i++) {
            assertThat(mapper.readValue(json, MixedHolder.class)).isEqualTo(expected);
            assertThat(mapper.readValue(
                            "{\"type\":\"camelCasedField\",\"camelCasedField\":\"root\"}", CamelCaseUnion.class))
                    .isEqualTo(CamelCaseUnion.camelCasedField("root"));
            assertThat(mapper.readValue("{\"type\":\"foo\",\"foo\":\"root\"}", SimpleUnion.class))
                    .isEqualTo(SimpleUnion.foo("root"));
        }
    }
}
