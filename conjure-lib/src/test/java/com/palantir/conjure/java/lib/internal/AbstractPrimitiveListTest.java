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

package com.palantir.conjure.java.lib.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.lib.SafeLong;
import com.sun.management.ThreadMXBean;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class AbstractPrimitiveListTest {

    @Test
    void backing_storage_uses_primitive_arrays_not_boxed_objects() {
        ConjureIntegerList intList = (ConjureIntegerList) ConjureCollections.newNonNullIntegerList();
        ConjureDoubleList doubleList = (ConjureDoubleList) ConjureCollections.newNonNullDoubleList();
        ConjureSafeLongList longList = (ConjureSafeLongList) ConjureCollections.newNonNullSafeLongList();

        assertThat(intList.storage().items().getClass()).isEqualTo(int[].class);
        assertThat(intList.storage().items().getClass().getComponentType()).isEqualTo(int.class);
        assertThat(doubleList.storage().items().getClass()).isEqualTo(double[].class);
        assertThat(doubleList.storage().items().getClass().getComponentType()).isEqualTo(double.class);
        assertThat(longList.storage().items().getClass()).isEqualTo(long[].class);
        assertThat(longList.storage().items().getClass().getComponentType()).isEqualTo(long.class);

        // Ensure neither AbstractPrimitiveList nor Storage declares any Object[] or Collection fields.
        for (Field field : AbstractPrimitiveList.class.getDeclaredFields()) {
            assertThat(field.getType()).isNotEqualTo(Object[].class);
        }
    }

    @Test
    void empty_lists_share_zero_length_array_and_grow_with_expected_policy() {
        ConjureIntegerList first = new ConjureIntegerList();
        ConjureIntegerList second = new ConjureIntegerList(0);

        assertThat(first.storage().items()).hasSize(0);
        assertThat(first.storage().items()).isSameAs(second.storage().items());

        // First add allocates default capacity of 10
        first.add(100);
        assertThat(first.storage().items()).hasSize(10);

        // Fill up to 10 elements without resizing
        for (int idx = 1; idx < 10; idx++) {
            first.add(100 + idx);
        }
        assertThat(first.storage().items()).hasSize(10);

        // 11th element triggers sizePlusFiftyPercent(10) = 10 + 5 + 1 = 16
        first.add(110);
        assertThat(first.storage().items()).hasSize(16);

        // 17th element triggers sizePlusFiftyPercent(16) = 16 + 8 + 1 = 25
        for (int idx = 11; idx <= 16; idx++) {
            first.add(100 + idx);
        }
        assertThat(first.storage().items()).hasSize(25);
    }

    @Test
    void array_constructor_and_unmodifiable_view_are_zero_copy() {
        int[] rawInts = new int[] {10, 20, 30, 40};
        ConjureIntegerList intList = (ConjureIntegerList) ConjureCollections.newNonNullIntegerList(rawInts);
        assertThat(intList.storage().items()).isSameAs(rawInts);

        ConjureIntegerList unmodifiableInts = intList.asUnmodifiable();
        assertThat(unmodifiableInts.storage()).isSameAs(intList.storage());
        assertThat(unmodifiableInts.storage().items()).isSameAs(rawInts);
        assertThat(unmodifiableInts.asUnmodifiable()).isSameAs(unmodifiableInts);

        double[] rawDoubles = new double[] {1.5, 2.5, 3.5};
        ConjureDoubleList doubleList = (ConjureDoubleList) ConjureCollections.newNonNullDoubleList(rawDoubles);
        assertThat(doubleList.storage().items()).isSameAs(rawDoubles);

        ConjureDoubleList unmodifiableDoubles = doubleList.asUnmodifiable();
        assertThat(unmodifiableDoubles.storage()).isSameAs(doubleList.storage());
        assertThat(unmodifiableDoubles.storage().items()).isSameAs(rawDoubles);
    }

    @Test
    void primitive_add_and_bulk_add_do_not_allocate_boxed_wrappers() {
        ThreadMXBean threadMxBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        if (!threadMxBean.isThreadAllocatedMemorySupported() || !threadMxBean.isThreadAllocatedMemoryEnabled()) {
            return;
        }
        int count = 50_000;

        // Warm up the methods so JIT / classloading allocations do not pollute the measurement.
        ConjureIntegerList warmup = new ConjureIntegerList(16);
        for (int idx = 0; idx < 16; idx++) {
            ConjureCollections.addToIntegerList(warmup, idx + 1_000);
        }

        // Pre-sized ConjureIntegerList: adding 50,000 ints outside the Integer cache [-128, 127] must allocate 0 bytes.
        ConjureIntegerList preSizedInts = new ConjureIntegerList(count);
        long beforeIntAdd = threadMxBean.getCurrentThreadAllocatedBytes();
        for (int idx = 0; idx < count; idx++) {
            ConjureCollections.addToIntegerList(preSizedInts, 10_000 + idx);
        }
        long intAddAllocatedBytes = threadMxBean.getCurrentThreadAllocatedBytes() - beforeIntAdd;
        assertThat(preSizedInts).hasSize(count);
        assertThat(intAddAllocatedBytes)
                .as("Adding 50k primitive ints to a pre-sized ConjureIntegerList should not box Integer objects")
                .isLessThan(1024L);

        // Pre-sized ConjureDoubleList: adding 50,000 primitive doubles must not allocate boxed Double objects
        // (50,000 boxed Doubles would be >= 1,200,000 bytes).
        ConjureDoubleList warmupDoubles = new ConjureDoubleList(16);
        for (int idx = 0; idx < 16; idx++) {
            ConjureCollections.addToDoubleList(warmupDoubles, idx + 0.5);
        }
        ConjureDoubleList preSizedDoubles = new ConjureDoubleList(count);
        long beforeDoubleAdd = threadMxBean.getCurrentThreadAllocatedBytes();
        for (int idx = 0; idx < count; idx++) {
            ConjureCollections.addToDoubleList(preSizedDoubles, idx * 1.25);
        }
        long doubleAddAllocatedBytes = threadMxBean.getCurrentThreadAllocatedBytes() - beforeDoubleAdd;
        assertThat(preSizedDoubles).hasSize(count);
        assertThat(doubleAddAllocatedBytes)
                .as("Adding 50k primitive doubles to a pre-sized ConjureDoubleList should not box Double objects")
                .isLessThan(1024L);

        // Compare total allocation of building a 50k ConjureIntegerList from int[] vs a boxed ArrayList<Integer>:
        // ConjureIntegerList(int[]) wraps the array in ~48 bytes, whereas boxing 50k Integers into ArrayList
        // allocates >= 50,000 * 16B (800 KB) of Integer objects + 200 KB of Object[].
        int[] sourceArray = new int[count];
        Arrays.fill(sourceArray, 42_000);
        long beforeWrap = threadMxBean.getCurrentThreadAllocatedBytes();
        List<Integer> wrapped = ConjureCollections.newNonNullIntegerList(sourceArray);
        long wrapBytes = threadMxBean.getCurrentThreadAllocatedBytes() - beforeWrap;

        long beforeBoxed = threadMxBean.getCurrentThreadAllocatedBytes();
        List<Integer> boxed = new ArrayList<>(count);
        for (int idx = 0; idx < count; idx++) {
            boxed.add(42_000 + idx);
        }
        long boxedBytes = threadMxBean.getCurrentThreadAllocatedBytes() - beforeBoxed;

        assertThat(wrapped).hasSize(count);
        assertThat(boxed).hasSize(count);
        assertThat(wrapBytes).isLessThan(256L);
        assertThat(boxedBytes).isGreaterThan(count * 16L);
    }

    @Test
    void supports_positional_insert_addAll_remove_set_and_subList() {
        ConjureIntegerList ints = new ConjureIntegerList();
        ints.addAll(new int[] {1, 4, 5});
        ints.add(1, 2);
        ints.add(2, 3);
        assertThat(ints).containsExactly(1, 2, 3, 4, 5);

        assertThat(ints.addAll(0, List.of(-1, 0))).isTrue();
        assertThat(ints.addAll(2, Collections.emptyList())).isFalse();
        assertThat(ints).containsExactly(-1, 0, 1, 2, 3, 4, 5);

        assertThat(ints.set(0, -10)).isEqualTo(-1);
        assertThat(ints.remove(/* index */ 0)).isEqualTo(-10);
        assertThat(ints.subList(1, 4)).containsExactly(1, 2, 3);

        ints.subList(1, 3).clear();
        assertThat(ints).containsExactly(0, 3, 4, 5);
    }

    @Test
    void enforces_bounds_null_safety_and_unmodifiable_contract() {
        ConjureDoubleList doubles = new ConjureDoubleList(new double[] {1.0, 2.0});

        assertThatThrownBy(() -> doubles.get(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> doubles.get(2)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> doubles.set(2, 3.0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> doubles.remove(/* index */ 2)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> doubles.add(3, 3.0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> doubles.add(-1, 3.0)).isInstanceOf(IndexOutOfBoundsException.class);

        // Null elements must throw NullPointerException without corrupting size or existing elements
        assertThatThrownBy(() -> doubles.add(1, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> doubles.set(0, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> doubles.addAll(0, Arrays.asList(3.0, null))).isInstanceOf(NullPointerException.class);
        assertThat(doubles).containsExactly(1.0, 2.0);

        ConjureDoubleList unmodifiable = doubles.asUnmodifiable();
        assertThatThrownBy(() -> unmodifiable.add(3.0)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> unmodifiable.add(0, 3.0)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> unmodifiable.addAll(new double[] {3.0}))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> unmodifiable.addAll(0, List.of(3.0)))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> unmodifiable.set(0, 9.0)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> unmodifiable.remove(/* index */ 0)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(unmodifiable::clear).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void jackson_serializes_as_primitive_json_array() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> ints = ConjureCollections.newNonNullIntegerList(new int[] {1, 2, 3});
        List<Double> doubles = ConjureCollections.newNonNullDoubleList(new double[] {1.5, 2.5});
        List<SafeLong> longs = ConjureCollections.newNonNullSafeLongList(List.of(SafeLong.of(10L), SafeLong.of(20L)));

        assertThat(mapper.writeValueAsString(ints)).isEqualTo("[1,2,3]");
        assertThat(mapper.writeValueAsString(doubles)).isEqualTo("[1.5,2.5]");
        assertThat(mapper.writeValueAsString(longs)).isEqualTo("[10,20]");
    }
}
