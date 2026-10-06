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

package defensivenonnullcollections.com.palantir.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

final class ConjureSetDeserializerTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void preservesOrderDeduplicationAndNulls() throws IOException {
        Value value = mapper.readValue("{\"values\":[2,1,2,null]}", Value.class);
        assertThat(value.values()).containsExactly(2, 1, null);
        assertThat(mapper.readValue("{\"values\":null}", Value.class).values()).isNull();
        assertThat(mapper.readValue("{\"values\":[]}", Value.class).values()).isEmpty();
    }

    @Test
    @SuppressWarnings({"NonApiType", "checkstyle:IllegalType"
    }) // Jackson registration requires the concrete collection type.
    void copiesCustomCollectionResultsIncludingNullAndAbsentValues() throws IOException {
        LinkedHashSet<Integer> shared = new LinkedHashSet<>(List.of(1));
        mapper.registerModule(new SimpleModule().addDeserializer(LinkedHashSet.class, new JsonDeserializer<>() {
            @Override
            public LinkedHashSet<Integer> deserialize(JsonParser parser, DeserializationContext _context)
                    throws IOException {
                parser.skipChildren();
                return shared;
            }

            @Override
            public LinkedHashSet<Integer> getNullValue(DeserializationContext _context) {
                return shared;
            }
        }));
        for (String json : List.of("{\"values\":[2]}", "{\"values\":null}", "{}")) {
            Set<Integer> result = mapper.readValue(json, Value.class).values();
            assertThat(result).isEqualTo(shared).isNotSameAs(shared);
            shared.add(3);
            assertThat(result).containsExactly(1);
            shared.remove(3);
        }
    }

    @Test
    void copiesCollectionsRetainedByCustomElementDeserializers() throws IOException {
        AtomicReference<Object> retained = new AtomicReference<>();
        mapper.registerModule(new SimpleModule().addDeserializer(Integer.class, new JsonDeserializer<>() {
            @Override
            public Integer deserialize(JsonParser parser, DeserializationContext _context) throws IOException {
                retained.set(parser.currentValue());
                return parser.getIntValue();
            }
        }));
        Set<Integer> result =
                mapper.readValue("{\"values\":[1,2]}", Value.class).values();
        assertThat(retained.get()).isInstanceOf(Set.class).isNotSameAs(result);
        ((Set<?>) retained.get()).clear();
        assertThat(result).containsExactly(1, 2);
    }

    record Value(
            @JsonDeserialize(using = ConjureSetDeserializer.class)
            Set<Integer> values) {}
}
