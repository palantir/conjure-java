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
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.MapType;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

final class ConjureMapDeserializerTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final MapType scalarMap = mapType(String.class, Integer.class);

    @Test
    void transfersStandardScalarMaps() throws IOException {
        for (Class<?> key : List.of(String.class, Integer.class, UUID.class)) {
            JavaType type = mapType(key, Integer.class);
            assertThat(contextualize(context(mapper, null), type)).isExactlyInstanceOf(MapDeserializer.class);
        }
    }

    @TestFactory
    List<DynamicTest> copiesCustomAndNonScalarEntries() {
        ObjectMapper keys = mapper.copy()
                .registerModule(new SimpleModule().addKeyDeserializer(String.class, new CustomKeyDeserializer()));
        ObjectMapper values = mapper.copy()
                .registerModule(new SimpleModule().addDeserializer(Integer.class, new CustomValueDeserializer()));
        JavaType lists = mapper.getTypeFactory()
                .constructMapType(
                        LinkedHashMap.class,
                        mapper.constructType(String.class),
                        mapper.getTypeFactory().constructCollectionType(List.class, Integer.class));
        JavaType attachedKeys = scalarMap.withKeyValueHandler(new CustomKeyDeserializer());
        JavaType attachedValues = scalarMap.withContentValueHandler(new CustomValueDeserializer());
        return List.of(
                copying("registered key", keys, scalarMap, "{\"key\":1}", Map.of("custom-key", 1)),
                copying("registered scalar value", values, scalarMap, "{\"key\":1}", Map.of("key", 2)),
                copying("attached key", mapper, attachedKeys, "{\"key\":1}", Map.of("custom-key", 1)),
                copying("attached scalar value", mapper, attachedValues, "{\"key\":1}", Map.of("key", 2)),
                copying("non-scalar value", mapper, lists, "{\"key\":[1]}", Map.of("key", List.of(1))));
    }

    @TestFactory
    List<DynamicTest> copiesPolymorphicEntries() throws IOException {
        ObjectMapper keys = mapper.copy().addMixIn(UUID.class, Polymorphic.class);
        ObjectMapper values = mapper.copy().addMixIn(Number.class, Polymorphic.class);
        values.registerSubtypes(new NamedType(Integer.class, "integer"));
        MapType keyMap = mapType(UUID.class, String.class);
        MapType valueMap = mapType(String.class, Number.class);
        JavaType attachedKeys =
                keyMap.withKeyTypeHandler(keys.getDeserializationConfig().findTypeDeserializer(keyMap.getKeyType()));
        JavaType attachedValues = valueMap.withContentTypeHandler(
                values.getDeserializationConfig().findTypeDeserializer(valueMap.getContentType()));
        String keyJson = "{\"00000000-0000-0000-0000-000000000000\":\"value\"}";
        String valueJson = "{\"key\":[\"integer\",1]}";
        return List.of(
                copying("polymorphic key", keys, keyMap, keyJson, Map.of(new UUID(0, 0), "value")),
                copying("polymorphic value", values, valueMap, valueJson, Map.of("key", 1)),
                copying("attached key type handler", mapper, attachedKeys, keyJson, Map.of(new UUID(0, 0), "value")),
                copying("attached content type handler", mapper, attachedValues, valueJson, Map.of("key", 1)));
    }

    @Test
    void copiesCustomMapResultsIncludingNullAndAbsentValues() throws IOException {
        Map<String, Integer> shared = new LinkedHashMap<>(Map.of("original", 1));
        ObjectMapper custom = new ObjectMapper()
                .registerModule(new SimpleModule().addDeserializer(Map.class, new JsonDeserializer<>() {
                    @Override
                    public Map<String, Integer> deserialize(JsonParser parser, DeserializationContext _context)
                            throws IOException {
                        parser.skipChildren();
                        return shared;
                    }

                    @Override
                    public Map<String, Integer> getNullValue(DeserializationContext _context) {
                        return shared;
                    }
                }));
        for (String json : List.of("{\"values\":{}}", "{\"values\":null}", "{}")) {
            Map<String, Integer> result = custom.readValue(json, Value.class).values();
            assertThat(result).isEqualTo(shared).isNotSameAs(shared);
            shared.put("added", 2);
            assertThat(result).containsExactlyEntriesOf(Map.of("original", 1));
            shared.remove("added");
        }
    }

    record Value(
            @JsonDeserialize(using = ConjureMapDeserializer.class)
            Map<String, Integer> values) {}

    private MapType mapType(Class<?> key, Class<?> value) {
        return mapper.getTypeFactory().constructMapType(LinkedHashMap.class, key, value);
    }

    private static JsonDeserializer<?> contextualize(DeserializationContext context, JavaType type) throws IOException {
        BeanProperty property =
                new BeanProperty.Std(PropertyName.construct("map"), type, null, null, PropertyMetadata.STD_OPTIONAL);
        return new ConjureMapDeserializer().createContextual(context, property);
    }

    private static DeserializationContext context(ObjectMapper mapper, JsonParser parser) {
        return ((DefaultDeserializationContext) mapper.getDeserializationContext())
                .createInstance(mapper.getDeserializationConfig(), parser, mapper.getInjectableValues());
    }

    private static DynamicTest copying(
            String name, ObjectMapper mapper, JavaType type, String json, Map<?, ?> expected) {
        return dynamicTest(name, () -> {
            try (JsonParser parser = mapper.createParser(json)) {
                parser.nextToken();
                DeserializationContext context = context(mapper, parser);
                JsonDeserializer<?> deserializer = contextualize(context, type);
                // Jackson does not expose newly created maps via currentValue(), so check the copying wrapper directly.
                assertThat(deserializer).isInstanceOf(DelegatingDeserializer.class);
                assertThat(deserializer.getDelegatee()).isExactlyInstanceOf(MapDeserializer.class);
                assertThat(deserializer.deserialize(parser, context)).isEqualTo(expected);
            }
        });
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_ARRAY)
    private interface Polymorphic {}

    private static final class CustomKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext _context) {
            return "custom-" + key;
        }
    }

    private static final class CustomValueDeserializer extends StdScalarDeserializer<Integer> {
        CustomValueDeserializer() {
            super(Integer.class);
        }

        @Override
        public Integer deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
            return parser.getIntValue() + 1;
        }
    }
}
