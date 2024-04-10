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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidNullException;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.product.CovariantListExample;
import com.palantir.product.ListExample;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class NonNullCollectionsTest {
    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();

    @Test
    public void throwsNpe() {
        // ListExample is using code generated with the nonNullCollections flag set to true. Thus, we should not be able
        // to add a null to the collection in any way.
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() ->
                        ListExample.builder().items(Collections.singleton(null)).build());
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> ListExample.builder()
                .addAllItems(Collections.singleton(null))
                .build());
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

        assertThat(objectMapper.readValue(objectMapper.writeValueAsString(listExample), ListExample.class))
                .isEqualTo(listExample);

        // non-null collections will add "contentNulls = Nulls.FAIL" to the JsonSetter annotation. This will cause deser
        // to fail.
        CovariantListExample covariantListExample = CovariantListExample.builder()
                .addAllItems(Collections.singleton(Optional.empty()))
                .build();
        assertThatExceptionOfType(InvalidNullException.class)
                .isThrownBy(() -> objectMapper.readValue(
                        objectMapper.writeValueAsString(covariantListExample), CovariantListExample.class));

        // Similarly, setting a null in the builder also breaks
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> CovariantListExample.builder()
                .addAllItems(Collections.singleton(null))
                .build());
    }
}
