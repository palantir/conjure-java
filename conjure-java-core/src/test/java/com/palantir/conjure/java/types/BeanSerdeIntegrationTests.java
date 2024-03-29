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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidNullException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.product.BearerTokenExample;
import com.palantir.product.BooleanExample;
import com.palantir.product.CollectionsTestAliasList;
import com.palantir.product.CollectionsTestAliasMap;
import com.palantir.product.CollectionsTestAliasSet;
import com.palantir.product.CollectionsTestObject;
import com.palantir.product.EnumExample;
import com.palantir.product.ListExample;
import com.palantir.product.MapExample;
import com.palantir.product.RidExample;
import com.palantir.product.SafeLongExample;
import com.palantir.product.SetExample;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public final class BeanSerdeIntegrationTests {
    private static final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    public void testSetExampleSerde() throws Exception {
        testSerde("{\"items\": [\"one\", \"two\"]}", SetExample.class);
    }

    @Test
    public void testSetFailsOnNulls() {
        assertThatThrownBy(() -> mapper.readValue("{\"items\": [null}", SetExample.class))
                .isInstanceOf(InvalidNullException.class);
    }

    @Test
    public void testListExampleSerde() throws Exception {
        testSerde("{\"items\": [\"one\", \"two\"]}", ListExample.class);
    }

    @Test
    public void testListFailsOnNulls() {
        assertThatThrownBy(() -> mapper.readValue("{\"items\": [null]}", ListExample.class))
                .isInstanceOf(InvalidNullException.class);
    }

    @Test
    public void testListFailsOnNestedNulls() {
        assertThatThrownBy(() -> mapper.readValue("{\"nestedItems\": [[null]]}", ListExample.class))
                .isInstanceOf(InvalidNullException.class);
    }

    @Test
    public void testListOptionalItems() throws Exception {
        String json = "{\"optionalItems\": [null]}";
        ListExample example = mapper.readValue(json, ListExample.class);
        assertThat(example.getOptionalItems()).isNotEmpty();
        String serialized = mapper.writeValueAsString(example);
        ListExample deserialized = mapper.readValue(serialized, ListExample.class);
        assertThat(deserialized).isEqualTo(example);
    }

    @Test
    public void testListAliasOptionalItems() throws Exception {
        String json = "{\"aliasOptionalItems\": [null]}";
        ListExample example = mapper.readValue(json, ListExample.class);
        assertThat(example.getAliasOptionalItems()).isNotEmpty();
        assertThat(Iterables.getOnlyElement(example.getAliasOptionalItems()).get())
                .isEmpty();
        String serialized = mapper.writeValueAsString(example);
        ListExample deserialized = mapper.readValue(serialized, ListExample.class);
        assertThat(deserialized).isEqualTo(example);
    }

    @Test
    public void testMapExampleSerde() throws Exception {
        testSerde("{\"items\": {\"one\": \"eins\", \"two\": \"二\"}}", MapExample.class);
    }

    @Test
    public void testMapFailsOnNullValue() {
        assertThatThrownBy(() -> mapper.readValue("{\"items\": {\"one\": null } }", MapExample.class))
                .isInstanceOf(InvalidNullException.class);
    }

    @Test
    public void testMapOptionalItems() throws Exception {
        String json = "{\"optionalItems\": { \"one\": null } }";
        MapExample example = mapper.readValue(json, MapExample.class);
        assertThat(example.getOptionalItems()).isNotEmpty();
        String serialized = mapper.writeValueAsString(example);
        MapExample deserialized = mapper.readValue(serialized, MapExample.class);
        assertThat(deserialized).isEqualTo(example);
    }

    @Test
    public void testMapAliasOptionalItems() throws Exception {
        String json = "{\"aliasOptionalItems\": { \"one\": null } }";
        MapExample example = mapper.readValue(json, MapExample.class);
        assertThat(example.getAliasOptionalItems()).isNotEmpty();
        assertThat(Iterables.getOnlyElement(example.getAliasOptionalItems().values())
                        .get())
                .isEmpty();
        String serialized = mapper.writeValueAsString(example);
        MapExample deserialized = mapper.readValue(serialized, MapExample.class);
        assertThat(deserialized).isEqualTo(example);
    }

    @Test
    public void testSafeLongExampleSerde() throws Exception {
        testSerde("{\"safeLongValue\": 9007199254740991}", SafeLongExample.class);
    }

    @Test
    public void testRidExampleSerde() throws Exception {
        testSerde("{\"ridValue\": \"ri.foundry.main.dataset.0\"}", RidExample.class);
    }

    @Test
    public void testBearerTokenExampleSerde() throws Exception {
        testSerde("{\"bearerTokenValue\": \"anything\"}", BearerTokenExample.class);
    }

    @Test
    public void testSafeLongExampleSerde_tooLarge() {
        assertThatThrownBy(() -> testSerde("{\"safeLongValue\": 9007199254740992}", SafeLongExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("number must be safely representable in javascript");
    }

    @Test
    public void testIgnoreProperties() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{\"coin\": true, \"ignored\": \"field\"}", BooleanExample.class))
                .isInstanceOf(UnrecognizedPropertyException.class);
        // Important for ensuring additive changes don't affect clients adversely
        BooleanExample boolExample = ObjectMappers.newClientObjectMapper()
                .readValue("{\"coin\": true, \"ignored\": \"field\"}", BooleanExample.class);
        assertThat(boolExample.getCoin()).isTrue();
    }

    @Test
    public void testEnumKeyDeserialization() throws IOException {
        Map<EnumExample, String> value =
                mapper.readValue("{\"TWO\": \"foo\"}", new TypeReference<Map<EnumExample, String>>() {});
        assertThat(Iterables.getOnlyElement(value.keySet()).get()).isEqualTo(EnumExample.Value.TWO);
    }

    @Test
    public void testExcludeEmptyCollections_empty() throws IOException {
        CollectionsTestObject initial = CollectionsTestObject.builder()
                .alist(CollectionsTestAliasList.of(ImmutableList.of()))
                .aset(CollectionsTestAliasSet.of(ImmutableSet.of()))
                .amap(CollectionsTestAliasMap.of(ImmutableMap.of()))
                .build();
        String stringValue = mapper.writeValueAsString(initial);
        assertThat(stringValue)
                .as("Type does not set the 'excludeEmptyOptionals' flag, only the optional should be serialized")
                .isEqualTo("{\"optionalItem\":null}");
        assertThat(mapper.readValue(stringValue, CollectionsTestObject.class)).isEqualTo(initial);
    }

    @Test
    public void testExcludeEmptyCollections_nonempty() throws IOException {
        CollectionsTestObject initial = CollectionsTestObject.builder()
                .alist(CollectionsTestAliasList.of(ImmutableList.of(0)))
                .aset(CollectionsTestAliasSet.of(ImmutableSet.of(0)))
                .amap(CollectionsTestAliasMap.of(ImmutableMap.of("", 0)))
                .items("")
                .itemsMap("", 0)
                .optionalItem("")
                .itemsSet("")
                .build();
        String stringValue = mapper.writeValueAsString(initial);
        assertThat(mapper.readValue(stringValue, CollectionsTestObject.class)).isEqualTo(initial);
    }

    private static <T> void testSerde(String json, Class<T> clazz) throws Exception {
        T example = mapper.readValue(json, clazz);
        String serialized = mapper.writeValueAsString(example);
        T deserialized = mapper.readValue(serialized, clazz);

        assertThat(deserialized).isEqualTo(example);
    }
}
