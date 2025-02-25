/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import allexamples.com.palantir.product.CovariantListExample;
import allexamples.com.palantir.product.ListExample;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidNullException;
import com.palantir.conjure.java.serialization.ObjectMappers;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import primitivecollections.com.palantir.product.PrimitiveExample;
import strictprimitivecollections.com.palantir.product.PrimitiveStrictExample;

public class NonNullCollectionsTest {
    private static final ObjectMapper clientMapper = ObjectMappers.newClientObjectMapper();
    private static final ObjectMapper serverMapper = ObjectMappers.newServerJsonMapper();

    @Test
    public void throwsNpe() {
        // ListExample is using code generated with the nonNullCollections flag set to true. Thus, we should not be able
        // to add a null to the collection in any way.
        Iterable<String> nullCollection = Collections.singleton(null);

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ListExample.builder().items(nullCollection).build());
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(
                        () -> ListExample.builder().addAllItems(nullCollection).build());
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> ListExample.builder().items((String) null).build());
    }

    @Test
    public void testOptionalSerialization() throws JsonProcessingException {
        // Check that this passes serde even if list contains optional.empty() which serializes to null.
        // OptionalItems is of type list<optional<string>>
        ListExample listExample = ListExample.builder()
                .optionalItems(Collections.singleton(Optional.empty()))
                .build();

        assertThat(clientMapper.readValue(clientMapper.writeValueAsString(listExample), ListExample.class))
                .isEqualTo(listExample);

        // non-null collections will add "contentNulls = Nulls.FAIL" to the JsonSetter annotation. This will cause deser
        // to fail.
        CovariantListExample covariantListExample = CovariantListExample.builder()
                .addAllItems(Collections.singleton(Optional.empty()))
                .build();
        assertThatExceptionOfType(InvalidNullException.class)
                .isThrownBy(() -> clientMapper.readValue(
                        clientMapper.writeValueAsString(covariantListExample), CovariantListExample.class));

        // Similarly, setting a null in the builder also breaks
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> CovariantListExample.builder()
                .addAllItems(Collections.singleton(null))
                .build());
    }

    @Test
    public void testSerDeOptimizationRespectsConjureEmptyCollections() throws JsonProcessingException {
        PrimitiveStrictExample expected = PrimitiveStrictExample.builder()
                .ints(Collections.emptyList())
                .doubles(Collections.emptyList())
                .build();
        assertThat(clientMapper.writeValueAsString(expected))
                .describedAs("Does not serialize any empty collections, even when optimizing for primitives")
                .isEqualTo("{}");
    }

    @Test
    public void testSerializationRoundtrip() throws JsonProcessingException {
        PrimitiveExample expected = PrimitiveExample.builder()
                .field(1)
                .addAllInts(1, 2, 3)
                .addAllDoubles(1.1, 2.2, 3.3)
                .build();
        String serialized = serverMapper.writeValueAsString(expected);
        assertThat(expected).isEqualTo(clientMapper.readValue(serialized, PrimitiveExample.class));
    }

    // We had an issue where the primitive optimization removed the JsonSetter from the final builder
    // which would fail this test
    @Test
    public void testStrictStagedDeserializationRoundtrips() throws JsonProcessingException {
        PrimitiveStrictExample expected = PrimitiveStrictExample.builder()
                .ints(List.of(1, 2, 3))
                .doubles(List.of(1.1, 2.2, 3.3))
                .build();
        String serialized = serverMapper.writeValueAsString(expected);
        assertThat(expected).isEqualTo(clientMapper.readValue(serialized, PrimitiveStrictExample.class));
    }
}
