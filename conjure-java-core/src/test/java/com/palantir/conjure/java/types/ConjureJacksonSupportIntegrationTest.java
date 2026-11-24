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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import allexamples.com.palantir.product.ManyFieldExample;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.serialization.ObjectMappers;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sealedunions.com.palantir.product.SimpleUnion;
import sealedunions.com.palantir.product.SimpleUnionAlias;

/**
 * Verifies that generated code is wired to {@code ConjureJacksonSupport}. Runtime edge cases are covered by the
 * conjure-lib tests for the helpers themselves.
 */
final class ConjureJacksonSupportIntegrationTest {
    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    void readsAndWritesGeneratedVariantsAndAliases() throws IOException {
        for (Example example : List.of(
                new Example("foo", "\"hello\"", SimpleUnion.foo("hello")),
                new Example("bar", "42", SimpleUnion.bar(42)),
                new Example("baz", "42", SimpleUnion.baz(SafeLong.of(42))),
                new Example("future", "42", SimpleUnion.unknown("future", 42)))) {
            String type = "\"type\":\"" + example.type() + "\"";
            String payload = "\"" + example.type() + "\":" + example.jsonValue();
            for (String json : List.of("{" + type + "," + payload + "}", "{" + payload + "," + type + "}")) {
                assertThat(mapper.readValue(json, SimpleUnion.class)).isEqualTo(example.value());
                SimpleUnionAlias alias = SimpleUnionAlias.of(example.value());
                assertThat(mapper.readValue(json, SimpleUnionAlias.class)).isEqualTo(alias);
                assertThat(mapper.readValue("{\"values\":[" + json + "," + json + "]}", Holder.class))
                        .isEqualTo(new Holder(List.of(alias, alias)));
                assertThat(mapper.readTree(mapper.writerFor(SimpleUnion.class).writeValueAsString(example.value())))
                        .isEqualTo(mapper.readTree(json));
                assertThat(mapper.readTree(mapper.writeValueAsString(alias))).isEqualTo(mapper.readTree(json));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void writesDiscriminatorFirst(boolean sortAlphabetically) throws IOException {
        ObjectMapper sorting = JsonMapper.builder()
                .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, sortAlphabetically)
                .build();
        for (SimpleUnion value : List.of(SimpleUnion.foo("value"), SimpleUnion.unknown("future", 42))) {
            for (Map.Entry<Class<?>, Object> entry : Map.<Class<?>, Object>of(
                            SimpleUnion.class,
                            value,
                            value.getClass(),
                            value,
                            SimpleUnionAlias.class,
                            SimpleUnionAlias.of(value))
                    .entrySet()) {
                assertThat(sorting.writerFor(entry.getKey()).writeValueAsString(entry.getValue()))
                        .startsWith("{\"type\":");
            }
        }
    }

    @Test
    void preservesGeneratedPayloadValidation() {
        for (String json :
                List.of("{\"type\":\"foo\"}", "{\"type\":\"foo\",\"foo\":null}", "{\"foo\":null,\"type\":\"foo\"}")) {
            assertThatThrownBy(() -> mapper.readValue(json, SimpleUnion.class))
                    .isInstanceOf(JsonMappingException.class);
            assertThatThrownBy(() -> mapper.readValue(json, SimpleUnionAlias.class))
                    .isInstanceOf(JsonMappingException.class);
        }
    }

    @Test
    void beanBuildersBindMapFieldsDirectly() throws ReflectiveOperationException {
        Class<?> builder = ManyFieldExample.builder().getClass();
        assertThat(builder.getDeclaredField("map").getAnnotation(JsonSetter.class))
                .extracting(JsonSetter::value)
                .isEqualTo("map");
        assertThat(builder.getDeclaredMethod("map", Map.class).getAnnotation(JsonSetter.class))
                .isNull();
    }

    record Example(String type, String jsonValue, SimpleUnion value) {}

    record Holder(List<SimpleUnionAlias> values) {}
}
