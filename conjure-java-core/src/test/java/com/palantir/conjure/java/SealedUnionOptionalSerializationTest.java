/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.palantir.conjure.java.serialization.ObjectMappers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import sealedunions.com.palantir.product.SimpleUnion;

final class SealedUnionOptionalSerializationTest {

    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    void directMapperWriteValue_succeeds() throws IOException {
        SimpleUnion union = SimpleUnion.foo("test-value");
        Optional<SimpleUnion> optional = Optional.of(union);

        String json = mapper.writeValueAsString(optional);
        assertThat(json).isEqualTo("{\"type\":\"foo\",\"foo\":\"test-value\"}");
    }

    @Test
    void writerForWithOptional_fails() throws IOException {
        SimpleUnion union = SimpleUnion.foo("test-value");
        Optional<SimpleUnion> optional = Optional.of(union);

        ObjectWriter writer = mapper.writerFor(new TypeReference<Optional<SimpleUnion>>() {});
        String json = writer.writeValueAsString(optional);
        assertThat(json).isEqualTo("{\"type\":\"foo\",\"foo\":\"test-value\"}");
    }

    @Test
    void writerForWithList_fails() throws IOException {
        SimpleUnion union = SimpleUnion.foo("test-value");
        List<SimpleUnion> list = new ArrayList<>();
        list.add(union);

        ObjectWriter writer = mapper.writerFor(new TypeReference<List<SimpleUnion>>() {});
        String json = writer.writeValueAsString(list);
        assertThat(json).isEqualTo("[{\"type\":\"foo\",\"foo\":\"test-value\"}]");
    }

    @Test
    void writerForWithDirectUnion_succeeds() throws IOException {
        SimpleUnion union = SimpleUnion.foo("test-value");

        ObjectWriter writer = mapper.writerFor(SimpleUnion.class);
        String json = writer.writeValueAsString(union);
        assertThat(json).isEqualTo("{\"type\":\"foo\",\"foo\":\"test-value\"}");
    }
}
