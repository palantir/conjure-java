/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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
import static org.assertj.core.api.Assertions.assertThatComparable;

import com.google.common.collect.ImmutableMap;
import com.palantir.product.MapExample;
import com.palantir.product.OptionalAlias;
import com.palantir.product.StringExample;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public final class ObjectMethodsTest {
    @Test
    void equalsWithMemoizedHashCode() {
        MapExample example = MapExample.builder()
                .items("item1", "value1")
                .optionalItems("optItem1", Optional.empty())
                .aliasOptionalItems("aliasOpt1", OptionalAlias.of(Optional.of("alias1")))
                .build();
        checkEqualsHashCodeContractForDifferentInstances(
                example, MapExample.builder().from(example).build());
        checkEqualsHashCodeContractForDifferentInstances(
                example,
                MapExample.builder()
                        .from(example)
                        .putAllItems(ImmutableMap.of("item2", "value2"))
                        .build());
    }

    @Test
    void equalsWithMemoizedHashCodeCollision() {
        MapExample hypoplankton =
                MapExample.builder().items("hypoplankton", "test").build();
        MapExample unheavenly = MapExample.builder().items("unheavenly", "test").build();
        assertThat(hypoplankton).hasSameHashCodeAs(unheavenly);
        checkEqualsHashCodeContractForDifferentInstances(hypoplankton, unheavenly);
    }

    @Test
    void equalsWithNonMemoizedHashCode() {
        checkEqualsHashCodeContractForDifferentInstances(StringExample.of("test"), StringExample.of("test"));
        checkEqualsHashCodeContractForDifferentInstances(StringExample.of("test"), StringExample.of("test2"));
    }

    @Test
    void equalsWithNonMemoizedHashCodeCollision() {
        StringExample hypoplankton = StringExample.of("hypoplankton");
        StringExample unheavenly = StringExample.of("unheavenly");
        assertThat(hypoplankton).hasSameHashCodeAs(unheavenly);
        checkEqualsHashCodeContractForDifferentInstances(hypoplankton, unheavenly);
    }

    @SuppressWarnings("unchecked")
    private static void checkEqualsHashCodeContractForDifferentInstances(Object o1, Object o2) {
        assertThat(o1).isNotSameAs(o2);
        assertThat(o2).isNotSameAs(o1);

        // memoize hash codes and ensure they are not default
        assertThat(o1.hashCode()).isNotZero();
        assertThat(o2.hashCode()).isNotZero();

        if (Objects.equals(o1, o2)) {
            assertThat(o1.getClass()).isEqualTo(o2.getClass()).isSameAs(o2.getClass());
            assertThat(o1).hasSameHashCodeAs(o2);
            assertThat(o1).describedAs("equals is not reflexive").isEqualTo(o1);
            assertThat(o2).describedAs("equals is not reflexive").isEqualTo(o2);
            assertThat(o2).describedAs("equals is not symmetric").isEqualTo(o1);
            assertThat(o1).describedAs("equals is not consistent").isEqualTo(o2);
            if (o1 instanceof Comparable) {
                assertThat(o1.getClass()).isInstanceOf(Comparable.class);
                assertThat(o2.getClass()).isInstanceOf(Comparable.class);
                assertThatComparable((Comparable<Object>) o1).isEqualByComparingTo(o2);
                assertThatComparable((Comparable<Object>) o2).isEqualByComparingTo(o1);
            }
        } else {
            assertThat(o2).isNotEqualTo(o1);
            if (o1 instanceof Comparable) {
                assertThat(o1.getClass()).isInstanceOf(Comparable.class);
                assertThat(o2.getClass()).isInstanceOf(Comparable.class);
                assertThatComparable((Comparable<Object>) o1).isNotEqualByComparingTo(o2);
                assertThatComparable((Comparable<Object>) o2).isNotEqualByComparingTo(o1);
            }
        }
    }
}
