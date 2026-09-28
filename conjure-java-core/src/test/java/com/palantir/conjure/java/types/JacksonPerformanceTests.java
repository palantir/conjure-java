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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import allexamples.com.palantir.product.ManyFieldExample;
import allexamples.com.palantir.product.Union;
import allexamples.com.palantir.product.UnionTypeExample;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.palantir.conjure.java.serialization.ObjectMappers;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sealedunions.com.palantir.product.SimpleUnion;

final class JacksonPerformanceTests {

    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    void sharedUnionCodeKeepsMapperCustomizationIsolated() throws IOException {
        ObjectMapper first = mapperWithStringPrefix("first:");
        ObjectMapper second = mapperWithStringPrefix("second:");
        String json = "{\"type\":\"foo\",\"foo\":\"value\"}";
        for (int iteration = 0; iteration < 2; iteration++) {
            assertThat(first.readValue(json, Union.class)).isEqualTo(Union.foo("first:value"));
            assertThat(second.readValue(json, Union.class)).isEqualTo(Union.foo("second:value"));
            assertThat(first.readValue(json, SimpleUnion.class)).isEqualTo(SimpleUnion.foo("first:value"));
            assertThat(second.readValue(json, SimpleUnion.class)).isEqualTo(SimpleUnion.foo("second:value"));
            assertThat(first.writerFor(SimpleUnion.class).writeValueAsString(SimpleUnion.foo("value")))
                    .isEqualTo("{\"type\":\"foo\",\"foo\":\"first:value\"}");
            assertThat(second.writerFor(SimpleUnion.class).writeValueAsString(SimpleUnion.foo("value")))
                    .isEqualTo("{\"type\":\"foo\",\"foo\":\"second:value\"}");
        }
    }

    @Test
    void sharedUnionCodeSupportsConcurrentColdMappers() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        try {
            for (int trial = 0; trial < 4; trial++) {
                ObjectMapper mapper = ObjectMappers.newServerObjectMapper();
                CyclicBarrier start = new CyclicBarrier(8);
                List<Callable<Void>> tasks = IntStream.range(0, 8)
                        .<Callable<Void>>mapToObj(_worker -> () -> {
                            start.await(30, TimeUnit.SECONDS);
                            for (int iteration = 0; iteration < 250; iteration++) {
                                assertThat(mapper.readValue("{\"type\":\"foo\",\"foo\":\"value\"}", Union.class))
                                        .isEqualTo(Union.foo("value"));
                                assertThat(mapper.readValue("{\"bar\":42,\"type\":\"bar\"}", SimpleUnion.class))
                                        .isEqualTo(SimpleUnion.bar(42));
                            }
                            return null;
                        })
                        .toList();
                for (Future<Void> result : executor.invokeAll(tasks, 30, TimeUnit.SECONDS)) {
                    result.get();
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private static ObjectMapper mapperWithStringPrefix(String prefix) {
        return ObjectMappers.newServerObjectMapper()
                .registerModule(new SimpleModule()
                        .addDeserializer(String.class, new JsonDeserializer<>() {
                            @Override
                            public String deserialize(JsonParser parser, DeserializationContext _context)
                                    throws IOException {
                                return prefix + parser.getText();
                            }
                        })
                        .addSerializer(SimpleUnion.Foo.class, new JsonSerializer<>() {
                            @Override
                            public void serialize(
                                    SimpleUnion.Foo value, JsonGenerator generator, SerializerProvider _provider)
                                    throws IOException {
                                generator.writeStartObject();
                                generator.writeStringField("type", "foo");
                                generator.writeStringField("foo", prefix + value.value());
                                generator.writeEndObject();
                            }
                        }));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void serializedUnionsUseTheFastPath(boolean sortAlphabetically) throws IOException {
        ObjectMapper mapper = JsonMapper.builder()
                .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, sortAlphabetically)
                .build();
        assertTypeFirst(mapper, UnionTypeExample.thisFieldIsAnInteger(42), UnionTypeExample.class);
        assertTypeFirst(mapper, SimpleUnion.foo("value"), SimpleUnion.class);
        assertTypeFirst(mapper, SimpleUnion.foo("value"), SimpleUnion.Foo.class);
        assertTypeFirst(mapper, UnionTypeExample.unknown("future", 42), UnionTypeExample.class);
        assertTypeFirst(mapper, SimpleUnion.unknown("future", 42), SimpleUnion.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {UnionTypeExample.class, SimpleUnion.class})
    void unknownDeserializerIsResolvedLazilyOncePerUnion(Class<?> unionType) throws IOException {
        AtomicInteger resolutions = new AtomicInteger();
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new SimpleModule()
                        .addDeserializer(Object.class, new CountingDeserializer(resolutions, "null")));

        mapper.readValue("{\"type\":\"future\"}", unionType);
        assertThat(resolutions).hasValue(0);
        String json = "{\"type\":\"future\",\"a\":1,\"b\":2,\"c\":null}";
        Object union = mapper.readValue(json, unionType);
        assertThat(resolutions).hasValue(1);
        assertThat(mapper.readTree(mapper.writeValueAsBytes(union)))
                .isEqualTo(mapper.readTree("{\"type\":\"future\",\"a\":1,\"b\":2,\"c\":\"null\"}"));
        mapper.readValue("{\"a\":1,\"type\":\"future\",\"b\":2}", unionType);
        assertThat(resolutions).hasValue(2);

        ObjectMapper otherMapper = new ObjectMapper()
                .registerModule(new SimpleModule()
                        .addDeserializer(Object.class, new CountingDeserializer(new AtomicInteger(), "other")));
        Object other = otherMapper.readValue("{\"type\":\"future\",\"c\":null}", unionType);
        assertThat(otherMapper
                        .readTree(otherMapper.writeValueAsBytes(other))
                        .get("c")
                        .asText())
                .isEqualTo("other");
    }

    @ParameterizedTest
    @ValueSource(classes = {UnionTypeExample.class, SimpleUnion.class})
    void unknownPayloadsRetainRootTyping(Class<?> unionType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerSubtypes(new NamedType(LinkedHashMap.class, "map"));
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            @Override
            public TypeResolverBuilder<?> findTypeResolver(
                    MapperConfig<?> config, AnnotatedClass annotated, JavaType type) {
                return type.hasRawClass(Object.class)
                        ? new StdTypeResolverBuilder()
                                .init(JsonTypeInfo.Id.NAME, null)
                                .inclusion(JsonTypeInfo.As.WRAPPER_ARRAY)
                        : super.findTypeResolver(config, annotated, type);
            }
        });
        for (String json : new String[] {
            "{\"type\":\"future\",\"future\":[\"map\",{\"key\":\"value\"}]}",
            "{\"future\":[\"map\",{\"key\":\"value\"}],\"type\":\"future\"}"
        }) {
            Object union = mapper.readValue(json, unionType);
            assertThat(MAPPER.readTree(MAPPER.writeValueAsBytes(union)))
                    .isEqualTo(MAPPER.readTree("{\"type\":\"future\",\"future\":{\"key\":\"value\"}}"));
        }
    }

    private static void assertTypeFirst(ObjectMapper mapper, Object value, Class<?> type) throws IOException {
        String json = mapper.writerFor(type).writeValueAsString(value);
        assertThat(json).startsWith("{\"type\":");
        AtomicInteger bufferProbes = new AtomicInteger();
        assertThat(readWithBufferProbe(json, type, bufferProbes)).isEqualTo(value);
        assertThat(bufferProbes).hasValue(0);
    }

    private static final class CountingDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
        private final AtomicInteger resolutions;
        private final String nullValue;

        CountingDeserializer(AtomicInteger resolutions, String nullValue) {
            this.resolutions = resolutions;
            this.nullValue = nullValue;
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext _context, BeanProperty _property) {
            resolutions.incrementAndGet();
            return this;
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
            return parser.getIntValue();
        }

        @Override
        public Object getNullValue(DeserializationContext _context) {
            return nullValue;
        }
    }

    @Test
    void typeFirstUnionsDoNotCreateInputBuffers() throws IOException {
        AtomicInteger bufferProbes = new AtomicInteger();

        UnionTypeExample union = readWithBufferProbe(
                "{\"type\":\"thisFieldIsAnInteger\",\"thisFieldIsAnInteger\":42}",
                UnionTypeExample.class,
                bufferProbes);
        SimpleUnion sealed =
                readWithBufferProbe("{\"type\":\"foo\",\"foo\":\"value\"}", SimpleUnion.class, bufferProbes);

        assertThat(union).isEqualTo(UnionTypeExample.thisFieldIsAnInteger(42));
        assertThat(sealed).isEqualTo(SimpleUnion.foo("value"));
        assertThat(bufferProbes).hasValue(0);
    }

    @Test
    void outOfOrderUnionsUseBufferedCompatibilityPath() throws IOException {
        AtomicInteger bufferProbes = new AtomicInteger();

        UnionTypeExample union = readWithBufferProbe(
                "{\"thisFieldIsAnInteger\":42,\"type\":\"thisFieldIsAnInteger\"}",
                UnionTypeExample.class,
                bufferProbes);
        SimpleUnion sealed =
                readWithBufferProbe("{\"foo\":\"value\",\"type\":\"foo\"}", SimpleUnion.class, bufferProbes);

        assertThat(union).isEqualTo(UnionTypeExample.thisFieldIsAnInteger(42));
        assertThat(sealed).isEqualTo(SimpleUnion.foo("value"));
        assertThat(bufferProbes).hasValueGreaterThanOrEqualTo(2);
    }

    @Test
    void bufferedPathCombinesPropertiesBeforeAndAfterType() throws IOException {
        UnionTypeExample union = MAPPER.readValue(
                "{\"ignoredBefore\":{\"nested\":[1,2]},\"type\":\"thisFieldIsAnInteger\","
                        + "\"thisFieldIsAnInteger\":42,\"ignoredAfter\":true}",
                UnionTypeExample.class);
        SimpleUnion sealed = MAPPER.readValue(
                "{\"ignoredBefore\":[1,2],\"type\":\"foo\",\"foo\":\"value\",\"ignoredAfter\":true}",
                SimpleUnion.class);

        assertThat(union).isEqualTo(UnionTypeExample.thisFieldIsAnInteger(42));
        assertThat(sealed).isEqualTo(SimpleUnion.foo("value"));
    }

    @Test
    void unknownUnionsPreserveAllPropertiesOnBothPaths() throws IOException {
        assertUnknownRoundTrips("{\"type\":\"future\",\"extra\":\"value\",\"future\":42}", UnionTypeExample.class);
        assertUnknownRoundTrips("{\"extra\":\"value\",\"future\":42,\"type\":\"future\"}", UnionTypeExample.class);
        assertUnknownRoundTrips("{\"extra\":\"value\",\"type\":\"future\",\"future\":42}", UnionTypeExample.class);
        assertUnknownRoundTrips("{\"type\":\"future\",\"extra\":\"value\",\"future\":42}", SimpleUnion.class);
        assertUnknownRoundTrips("{\"extra\":\"value\",\"future\":42,\"type\":\"future\"}", SimpleUnion.class);
        assertUnknownRoundTrips("{\"extra\":\"value\",\"type\":\"future\",\"future\":42}", SimpleUnion.class);
    }

    @Test
    void jacksonPopulatesBuilderMapsWithoutCallingCopyingSetter() throws ReflectiveOperationException {
        Class<?> builderClass = ManyFieldExample.builder().getClass();
        Field mapField = builderClass.getDeclaredField("map");
        Method publicSetter = builderClass.getDeclaredMethod("map", Map.class);

        assertThat(mapField.getAnnotation(JsonSetter.class)).isNotNull();
        assertThat(mapField.getAnnotation(JsonSetter.class).value()).isEqualTo("map");
        assertThat(publicSetter.getAnnotation(JsonSetter.class)).isNull();
    }

    private static <T> T readWithBufferProbe(String input, Class<T> type, AtomicInteger bufferProbes)
            throws IOException {
        try (JsonParser parser = new JsonParserDelegate(MAPPER.createParser(input)) {
            @Override
            public boolean canReadTypeId() {
                bufferProbes.incrementAndGet();
                return super.canReadTypeId();
            }
        }) {
            return MAPPER.readValue(parser, type);
        }
    }

    private static void assertUnknownRoundTrips(String input, Class<?> type) throws IOException {
        Object value = MAPPER.readValue(input, type);
        JsonNode expected = MAPPER.readTree(input);
        JsonNode actual = MAPPER.readTree(MAPPER.writeValueAsBytes(value));
        assertThat(actual).isEqualTo(expected);
    }
}
