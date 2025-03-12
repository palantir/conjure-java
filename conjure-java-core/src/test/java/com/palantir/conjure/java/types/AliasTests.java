/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import allexamples.com.palantir.product.DoubleAliasExample;
import allexamples.com.palantir.product.ExternalLongAliasOne;
import allexamples.com.palantir.product.ExternalLongAliasTwo;
import allexamples.com.palantir.product.SafeDoubleAliasExample;
import allexamples.com.palantir.product.UuidAliasExample;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.exceptions.SafeNullPointerException;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveAliasedList;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveAliasedMap;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveAliasedPrimitiveList;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveAliasedSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class AliasTests {
    private static final ObjectMapper TEST_MAPPER = ObjectMappers.newServerObjectMapper()
            .configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature(), true);

    @Test
    public void testNullValueSafeLoggable() {
        assertThatThrownBy(() -> UuidAliasExample.of(null))
                .isInstanceOf(SafeNullPointerException.class)
                .hasMessage("value cannot be null");
    }

    @Test
    public void testValueOf_external() {
        assertThat(ExternalLongAliasOne.valueOf("123")).isEqualTo(ExternalLongAliasOne.of(123L));
    }

    @Test
    public void testValueOf_externalNested() {
        assertThat(ExternalLongAliasTwo.valueOf("3")).isEqualTo(ExternalLongAliasTwo.of(ExternalLongAliasOne.of(3L)));
    }

    @Test
    public void testValueOf_largeDouble_lossyDouble() throws JsonProcessingException {
        // So large it doesn't fit in a double; jackson silently drops precision
        assertThat(TEST_MAPPER.readValue("9007199254740992.234", DoubleAliasExample.class))
                .isEqualTo(DoubleAliasExample.of(9007199254740992.0));
    }

    @Test
    public void testValueOf_largeDouble_double() throws JsonProcessingException {
        // Doesn't fit in an int, but does fit comfortably in a double
        assertThat(TEST_MAPPER.readValue("9667500000.0", DoubleAliasExample.class))
                .isEqualTo(DoubleAliasExample.of(9667500000.0));
    }

    @Test
    public void testValueOf_largeDouble_long() throws JsonProcessingException {
        // Doesn't fit in an int, but does fit comfortably in a double; looks like a long
        assertThat(TEST_MAPPER.readValue("9667500000", DoubleAliasExample.class))
                .isEqualTo(DoubleAliasExample.of(9667500000.0));
    }

    @Test
    public void testNaNEqualityOnSafeDoubleAlias() throws JsonProcessingException {
        assertThat(TEST_MAPPER.readValue("NaN", SafeDoubleAliasExample.class))
                .isEqualTo(SafeDoubleAliasExample.of(Double.NaN));
    }

    @Test
    void aliasWithDefensiveCollectionsCopiesCollection_set() {
        Set<Integer> original = Set.of(1, 2);
        Set<Integer> mutableSet = new HashSet<>(original);

        ExampleDefensiveAliasedSet alias = ExampleDefensiveAliasedSet.of(mutableSet);
        mutableSet.add(3);
        assertThat(alias).isEqualTo(ExampleDefensiveAliasedSet.of(original));
    }

    @Test
    void aliasWithDefensiveCollectionsIsImmutable_set() {
        Set<Integer> original = Set.of(1, 2);

        ExampleDefensiveAliasedSet alias = ExampleDefensiveAliasedSet.of(original);
        Set<Integer> internal = alias.get();
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> internal.add(3));
    }

    @Test
    void aliasWithDefensiveCollectionsCopiesCollection_list() {
        List<String> original = List.of("foo", "bar");
        List<String> mutableList = new ArrayList<>(original);

        ExampleDefensiveAliasedList alias = ExampleDefensiveAliasedList.of(mutableList);
        mutableList.add("update");
        assertThat(alias).isEqualTo(ExampleDefensiveAliasedList.of(original));
    }

    @Test
    void aliasWithDefensiveCollectionsCopiesCollection_primitiveList() {
        List<Double> original = List.of(1.0);
        List<Double> mutableList = new ArrayList<>(original);

        ExampleDefensiveAliasedPrimitiveList alias = ExampleDefensiveAliasedPrimitiveList.of(mutableList);
        mutableList.add(2.0);
        assertThat(alias).isEqualTo(ExampleDefensiveAliasedPrimitiveList.of(original));
    }

    @Test
    void aliasWithDefensiveCollectionsIsImmutable_list() {
        List<String> original = List.of("foo", "bar");
        ExampleDefensiveAliasedList alias = ExampleDefensiveAliasedList.of(original);

        List<String> internal = alias.get();
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> internal.add("update"));
    }

    @Test
    void aliasWithDefensiveCollectionsCopiesCollection_map() {
        Map<String, Boolean> original = Map.of("foo", true);
        Map<String, Boolean> mutableMap = new HashMap<>(original);

        ExampleDefensiveAliasedMap alias = ExampleDefensiveAliasedMap.of(mutableMap);
        mutableMap.put("bar", true);
        assertThat(alias).isEqualTo(ExampleDefensiveAliasedMap.of(original));
    }

    @Test
    void aliasWithDefensiveCollectionsIsImmutable_map() {
        Map<String, Boolean> original = Map.of("foo", true);
        ExampleDefensiveAliasedMap alias = ExampleDefensiveAliasedMap.of(original);

        Map<String, Boolean> internal = alias.get();
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> internal.put("bar", true));
    }
}
