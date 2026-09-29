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
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import sealedunions.com.palantir.product.SimpleUnion;
import sealedunions.com.palantir.product.SimpleUnionAlias;

final class JacksonPerformanceTests {

    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    void sharedUnionCodeKeepsMapperCustomizationIsolated() throws IOException {
        Map<String, ObjectMapper> mappers =
                Map.of("first:", mapperWithStringPrefix("first:"), "second:", mapperWithStringPrefix("second:"));
        String json = "{\"type\":\"foo\",\"foo\":\"value\"}";
        for (int iteration = 0; iteration < 2; iteration++) {
            for (String prefix : List.of("first:", "second:")) {
                ObjectMapper mapper = mappers.get(prefix);
                assertThat(mapper.readValue(json, Union.class)).isEqualTo(Union.foo(prefix + "value"));
                assertThat(mapper.readValue(json, SimpleUnion.class)).isEqualTo(SimpleUnion.foo(prefix + "value"));
                assertThat(mapper.writerFor(SimpleUnion.class).writeValueAsString(SimpleUnion.foo("value")))
                        .isEqualTo("{\"type\":\"foo\",\"foo\":\"" + prefix + "value\"}");
            }
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
        assertTypeFirst(mapper, SimpleUnionAlias.of(SimpleUnion.foo("value")), SimpleUnionAlias.class);
        assertTypeFirst(mapper, UnionTypeExample.unknown("future", 42), UnionTypeExample.class);
        assertTypeFirst(mapper, SimpleUnion.unknown("future", 42), SimpleUnion.class);
        assertTypeFirst(mapper, SimpleUnionAlias.of(SimpleUnion.unknown("future", 42)), SimpleUnionAlias.class);
    }

    @ParameterizedTest
    @ValueSource(classes = {UnionTypeExample.class, SimpleUnion.class, SimpleUnionAlias.class})
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
    @ValueSource(classes = {UnionTypeExample.class, SimpleUnion.class, SimpleUnionAlias.class})
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
        assertThat(readWithBufferProbe(MAPPER, json, type, bufferProbes)).isEqualTo(value);
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

    @ParameterizedTest
    @MethodSource("knownUnionCases")
    void knownUnionsUseExpectedBuffering(Class<?> type, Object expected, boolean caseInsensitive) throws IOException {
        ObjectMapper mapper = MAPPER.copy();
        if (caseInsensitive) {
            mapper.setConfig(mapper.getDeserializationConfig().with(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
        }
        boolean sealed = type != UnionTypeExample.class;
        String discriminator =
                "\"%s\":\"%s\"".formatted(caseInsensitive ? "TYPE" : "type", sealed ? "foo" : "thisFieldIsAnInteger");
        String payload = sealed ? "\"foo\":\"value\"" : "\"thisFieldIsAnInteger\":42";
        String ignored = sealed ? "[1,2]" : "{\"nested\":[1,2]}";
        for (String json : List.of(
                "{" + discriminator + "," + payload + "}",
                "{" + payload + "," + discriminator + "}",
                "{\"ignoredBefore\":" + ignored + "," + discriminator + "," + payload + ",\"ignoredAfter\":true}")) {
            AtomicInteger bufferProbes = new AtomicInteger();
            assertThat(readWithBufferProbe(mapper, json, type, bufferProbes)).isEqualTo(expected);
            if (json.startsWith("{" + discriminator)) {
                assertThat(bufferProbes).hasValue(0);
            } else {
                assertThat(bufferProbes.get()).isPositive();
            }
        }
    }

    private static Stream<Arguments> knownUnionCases() {
        return Stream.of(false, true)
                .flatMap(caseInsensitive -> Stream.of(
                        Arguments.of(
                                UnionTypeExample.class, UnionTypeExample.thisFieldIsAnInteger(42), caseInsensitive),
                        Arguments.of(SimpleUnion.class, SimpleUnion.foo("value"), caseInsensitive),
                        Arguments.of(
                                SimpleUnionAlias.class,
                                SimpleUnionAlias.of(SimpleUnion.foo("value")),
                                caseInsensitive)));
    }

    @ParameterizedTest
    @ValueSource(classes = {UnionTypeExample.class, SimpleUnion.class, SimpleUnionAlias.class})
    void unknownUnionsPreserveAllPropertiesOnBothPaths(Class<?> type) throws IOException {
        for (String json : List.of(
                "{\"type\":\"future\",\"extra\":\"value\",\"future\":42}",
                "{\"extra\":\"value\",\"future\":42,\"type\":\"future\"}",
                "{\"extra\":\"value\",\"type\":\"future\",\"future\":42}")) {
            assertThat(MAPPER.readTree(MAPPER.writeValueAsBytes(MAPPER.readValue(json, type))))
                    .isEqualTo(MAPPER.readTree(json));
        }
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

    private static <T> T readWithBufferProbe(
            ObjectMapper mapper, String input, Class<T> type, AtomicInteger bufferProbes) throws IOException {
        try (JsonParser parser = new JsonParserDelegate(mapper.createParser(input)) {
            @Override
            public boolean canReadTypeId() {
                bufferProbes.incrementAndGet();
                return super.canReadTypeId();
            }
        }) {
            return mapper.readValue(parser, type);
        }
    }
}
