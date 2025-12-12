/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

import static com.palantir.logsafe.testing.Assertions.assertThatLoggableExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import allexamples.com.palantir.product.EmptyUnionTypeExample;
import allexamples.com.palantir.product.EmptyUnionTypeExample.Visitor;
import allexamples.com.palantir.product.Union;
import allexamples.com.palantir.product.UnionTypeExample;
import allexamples.com.palantir.product.UnionWithUnknownString;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveCollectionListsUnion;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveCollectionMapsUnion;
import defensivenonnullcollections.com.palantir.product.ExampleDefensiveCollectionSetsUnion;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;
import sealedunions.com.palantir.product.SimpleUnion;

class UnionTests {

    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    void testUnknownThrowingVariant() throws IOException {
        EmptyUnionTypeExample value =
                MAPPER.readValue("{\"type\":\"foo\",\"foo\":\"bar\"}", EmptyUnionTypeExample.class);
        Visitor<?> visitor = Visitor.builder().throwOnUnknown().build();
        assertThatLoggableExceptionThrownBy(() -> value.accept(visitor))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Unknown variant of the 'EmptyUnionTypeExample' union")
                .hasExactlyArgs(SafeArg.of("unknownType", "foo"));
    }

    @Test
    public void testCannotCreateUnknownTypeFromKnownType() {
        assertThatThrownBy(() -> Union.unknown("bar", "value"));
        assertThatThrownBy(() -> UnionTypeExample.unknown("if", "value"));
        assertThatThrownBy(() -> UnionWithUnknownString.unknown("unknown", "value"));
    }

    @Test
    public void testCreateUnknownType() {
        String expectedUnknownType = "qux";
        List<String> expectedUnknownValue = List.of("quux", "quuz");
        Union union = Union.unknown(expectedUnknownType, expectedUnknownValue);

        // test new visitor builder
        union.accept(Union.Visitor.<Void>builder()
                .bar(value -> failOnKnownType("bar", value))
                .baz(value -> failOnKnownType("baz", value))
                .foo(value -> failOnKnownType("foo", value))
                .unknown((type, value) -> verifyUnknownType(type, value, expectedUnknownType, expectedUnknownValue))
                .build());

        // test old visitor builder
        union.accept(Union.Visitor.<Void>builder()
                .bar(value -> failOnKnownType("bar", value))
                .baz(value -> failOnKnownType("baz", value))
                .foo(value -> failOnKnownType("foo", value))
                .unknown(type -> {
                    assertThat(type).isEqualTo(expectedUnknownType);
                    return null;
                })
                .build());

        // test anonymous visitor
        union.accept(new Union.Visitor<Void>() {
            @Override
            public Void visitFoo(String value) {
                return failOnKnownType("foo", value);
            }

            @Override
            public Void visitBar(int value) {
                return failOnKnownType("bar", value);
            }

            @Override
            public Void visitBaz(long value) {
                return failOnKnownType("baz", value);
            }

            @Override
            public Void visitUnknown(String unknownType, Object unknownValue) {
                return verifyUnknownType(unknownType, unknownValue, expectedUnknownType, expectedUnknownValue);
            }
        });
    }

    @Test
    public void testCreateUnknownTypeNamedUnknown() {
        // unknown is the wire type and "unknown_" is actually unknown
        String expectedUnknownType = "unknown_";
        String expectedUnknownValue = "foo";
        UnionWithUnknownString union = UnionWithUnknownString.unknown(expectedUnknownType, expectedUnknownValue);
        union.accept(UnionWithUnknownString.Visitor.<Void>builder()
                .unknown_(value -> failOnKnownType("unknown", value))
                .unknown((type, value) -> verifyUnknownType(type, value, expectedUnknownType, expectedUnknownValue))
                .build());
    }

    @Test
    void unionWithDefensiveCollectionsCopiesCollection_set() {
        Set<String> original = Set.of("foo", "bar");
        Set<String> mutableSet = new HashSet<>(original);

        ExampleDefensiveCollectionSetsUnion union = ExampleDefensiveCollectionSetsUnion.set(mutableSet);
        mutableSet.add("update");
        assertThat(union).isEqualTo(ExampleDefensiveCollectionSetsUnion.set(original));
    }

    @Test
    void unionWithDefensiveCollectionsIsImmutable_set() {
        Set<String> original = Set.of("foo", "bar");
        ExampleDefensiveCollectionSetsUnion union = ExampleDefensiveCollectionSetsUnion.set(original);

        Set<String> internal = union.accept(ExampleDefensiveCollectionSetsUnion.Visitor.<Set<String>>builder()
                .set(set -> set)
                .throwOnUnknown()
                .build());
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> internal.add("update"));
    }

    @Test
    void unionWithDefensiveCollectionsCopiesCollection_list() {
        List<String> original = List.of("foo", "bar");
        List<String> mutableList = new ArrayList<>(original);

        ExampleDefensiveCollectionListsUnion union = ExampleDefensiveCollectionListsUnion.list(mutableList);
        mutableList.add("update");
        assertThat(union).isEqualTo(ExampleDefensiveCollectionListsUnion.list(original));
    }

    @Test
    void unionWithDefensiveCollectionsCopiesCollection_primitiveList() {
        List<Double> original = List.of(1.0);
        List<Double> mutableList = new ArrayList<>(original);

        ExampleDefensiveCollectionListsUnion union = ExampleDefensiveCollectionListsUnion.primitiveList(mutableList);
        mutableList.add(2.0);
        assertThat(union).isEqualTo(ExampleDefensiveCollectionListsUnion.primitiveList(original));
    }

    @Test
    void unionWithDefensiveCollectionsIsImmutable_list() {
        List<String> original = List.of("foo", "bar");
        ExampleDefensiveCollectionListsUnion union = ExampleDefensiveCollectionListsUnion.list(original);

        List<String> internal = union.accept(ExampleDefensiveCollectionListsUnion.Visitor.<List<String>>builder()
                .list(list -> list)
                .listOptional(_x -> List.of())
                .primitiveList(_x -> List.of())
                .throwOnUnknown()
                .build());
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> internal.add("update"));
    }

    @Test
    void unionWithDefensiveCollectionsCopiesCollection_map() {
        Map<String, String> original = Map.of("foo", "bar");
        Map<String, String> mutableMap = new HashMap<>(original);

        ExampleDefensiveCollectionMapsUnion union = ExampleDefensiveCollectionMapsUnion.map(mutableMap);
        mutableMap.put("test", "update");
        assertThat(union).isEqualTo(ExampleDefensiveCollectionMapsUnion.map(original));
    }

    @Test
    void unionWithDefensiveCollectionsIsImmutable_map() {
        Map<String, String> original = Map.of("foo", "bar");
        ExampleDefensiveCollectionMapsUnion union = ExampleDefensiveCollectionMapsUnion.map(original);

        Map<String, String> internal =
                union.accept(ExampleDefensiveCollectionMapsUnion.Visitor.<Map<String, String>>builder()
                        .map(map -> map)
                        .mapOptional(_x -> Map.of())
                        .throwOnUnknown()
                        .build());
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> internal.put("test", "update"));
    }

    @Test
    void testUnknownThrowingVariant_sealedUnion() throws IOException {
        SimpleUnion value = MAPPER.readValue("{\"type\":\"abc\",\"abc\":\"123\"}", SimpleUnion.class);
        assertThatLoggableExceptionThrownBy(value::throwOnUnknown)
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Unknown variant of the 'SimpleUnion' union")
                .hasExactlyArgs(SafeArg.of("unknownType", "abc"));
    }

    @Test
    public void testCannotCreateUnknownTypeFromKnownType_sealedUnion() {
        assertThatThrownBy(() -> SimpleUnion.unknown("foo", "value"));
    }

    @Test
    void sealedUnionCanUseAllExistingPatterns() {
        String expected = "foo";
        SimpleUnion simpleUnion = SimpleUnion.foo(expected);
        SimpleUnion unknown = SimpleUnion.unknown("test", expected);

        String actual = simpleUnion.accept(SimpleUnion.Visitor.<String>builder()
                .bar(String::valueOf)
                .baz(String::valueOf)
                .foo(Function.identity())
                .throwOnUnknown()
                .build());
        assertThat(actual).isEqualTo(expected);

        String unknownString = unknown.accept(SimpleUnion.Visitor.<String>builder()
                .bar(String::valueOf)
                .baz(String::valueOf)
                .foo(_x -> "")
                .unknown((_type, value) -> value.toString())
                .build());
        assertThat(unknownString).isEqualTo(expected);

        assertThat(simpleUnion.equals(SimpleUnion.foo(expected))).isTrue();
        assertThat(simpleUnion.toString()).contains(expected);
    }

    @Test
    void sealedUnionsAreSerializedTheSameAsLegacyUnions() throws IOException {
        String expected = "foo";
        SimpleUnion simpleUnion = SimpleUnion.foo(expected);
        undertow.com.palantir.product.SimpleUnion simpleLegacyUnion =
                undertow.com.palantir.product.SimpleUnion.foo(expected);
        String serialized = MAPPER.writeValueAsString(simpleUnion);
        String serializedLegacy = MAPPER.writeValueAsString(simpleLegacyUnion);
        assertThat(serialized).isEqualTo(serializedLegacy);
    }

    private Void failOnKnownType(String type, Object value) {
        Fail.fail(
                "Visited known type when expected unknown type",
                UnsafeArg.of("type", type),
                UnsafeArg.of("value", value));
        return null;
    }

    private Void verifyUnknownType(String actualType, Object actualValue, String expectedType, Object expectedValue) {
        assertThat(actualType).isEqualTo(expectedType);
        assertThat(actualValue).isEqualTo(expectedValue);
        return null;
    }
}
