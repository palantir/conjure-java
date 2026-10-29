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

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureJacksonSupport;
import com.palantir.conjure.java.serialization.ObjectMappers;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import sealedunions.com.palantir.product.SimpleUnion;
import sealedunions.com.palantir.product.SimpleUnionAlias;

final class ConjureJacksonSupportIntegrationTest {
    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper()
            .addMixIn(SimpleUnion.class, RuntimeUnion.class)
            .addMixIn(SimpleUnion.Foo.class, ConcreteVariant.class)
            .addMixIn(SimpleUnion.Bar.class, ConcreteVariant.class)
            .addMixIn(SimpleUnion.Baz.class, ConcreteVariant.class)
            .addMixIn(SimpleUnion.Unknown.class, ConcreteVariant.class);

    @Test
    void readsAndWritesGeneratedVariantsAndAliases() throws IOException {
        for (Example example : List.of(
                new Example("foo", "\"hello\"", SimpleUnion.foo("hello")),
                new Example("bar", "42", SimpleUnion.bar(42)),
                new Example("baz", "42", SimpleUnion.baz(SafeLong.of(42))))) {
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

    record Example(String type, String jsonValue, SimpleUnion value) {}

    record Holder(List<SimpleUnionAlias> values) {}

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonDeserialize(using = RuntimeDeserializer.class)
    @JsonSerialize(using = ConjureJacksonSupport.UnionSerializer.class)
    private interface RuntimeUnion {}

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonDeserialize
    @JsonSerialize
    private interface ConcreteVariant {}

    static final class RuntimeDeserializer extends ConjureJacksonSupport.UnionDeserializer<SimpleUnion> {
        RuntimeDeserializer() {
            super(
                    SimpleUnion.class,
                    Map.of("foo", SimpleUnion.Foo.class, "bar", SimpleUnion.Bar.class, "baz", SimpleUnion.Baz.class),
                    (type, values) -> SimpleUnion.unknown(type, values.get(type)));
        }
    }
}
