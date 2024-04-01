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

package com.palantir.conjure.java.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIndexOutOfBoundsException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DoubleArrayListTest {

    @Test
    void initialization_sanity() {
        DoubleArrayList doubleArrayList = new DoubleArrayList(5);
        assertThat(doubleArrayList.getElements()).hasSize(5);
        assertThat(doubleArrayList.size()).isEqualTo(0);

        doubleArrayList = new DoubleArrayList(List.of(1.0, 2.0, 3.0));
        assertThat(doubleArrayList.getElements()).hasSize(3);
        assertThat(doubleArrayList.size()).isEqualTo(3);

        double[] doubles = new double[] {1.0, 2.0, 3.0};
        doubleArrayList = new DoubleArrayList(doubles);
        assertThat(doubleArrayList.getElements()).hasSize(3);
        assertThat(doubleArrayList.size()).isEqualTo(3);
        // Assert we aren't just holding a reference to this array.
        doubles[0] = 2.0;
        assertThat(doubleArrayList.get(0)).isEqualTo(1.0);
    }

    @Test
    void addToEnd_test() {
        DoubleArrayList doubleArrayList = new DoubleArrayList(0);
        assertThat(doubleArrayList.getElements()).hasSize(0);

        doubleArrayList.add(1.0);
        assertThat(doubleArrayList.getElements()).hasSize(1);
        assertThat(doubleArrayList.get(0)).isEqualTo(1.0);

        doubleArrayList.add(2.0);
        assertThat(doubleArrayList.getElements()).hasSize(2);
        assertThat(doubleArrayList.get(1)).isEqualTo(2.0);

        doubleArrayList.add(3.0);
        assertThat(doubleArrayList.getElements()).hasSize(4); // doubling size
        assertThat(doubleArrayList.get(2)).isEqualTo(3.0);
        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.get(3));
    }

    @Test
    void addToIndex_test() {
        DoubleArrayList doubleArrayList = new DoubleArrayList(0);
        assertThat(doubleArrayList.getElements()).hasSize(0);
        List<Double> expected = new ArrayList<>();

        doubleArrayList.add(0, 1.0);
        expected.add(0, 1.0);
        assertThat(doubleArrayList.size()).isEqualTo(1);
        assertThat(doubleArrayList.getElements()).hasSize(1);
        assertThat(doubleArrayList.get(0)).isEqualTo(1.0);

        doubleArrayList.add(0, 2.0);
        expected.add(0, 2.0);
        assertThat(doubleArrayList.size()).isEqualTo(2);
        assertThat(doubleArrayList.getElements()).hasSize(2);
        assertThat(doubleArrayList.get(0)).isEqualTo(2.0);

        doubleArrayList.add(1, 3.0);
        expected.add(1, 3.0);
        assertThat(doubleArrayList.size()).isEqualTo(3);
        assertThat(doubleArrayList.getElements()).hasSize(4);
        assertThat(doubleArrayList.get(1)).isEqualTo(3.0);

        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.add(4, 4.0));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> expected.add(4, 4.0));

        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);
    }

    @Test
    void remove_sanity() {
        DoubleArrayList doubleArrayList = new DoubleArrayList(List.of(0.1, 0.2, 0.3, 0.4));
        List<Double> expected = new ArrayList<>(List.of(0.1, 0.2, 0.3, 0.4));

        Double removedValue = doubleArrayList.remove(1);
        Double expectedRemovedValue = expected.remove(1);
        assertThat(removedValue).isEqualTo(expectedRemovedValue);
        assertThat(doubleArrayList.size()).isEqualTo(3);
        assertThat(doubleArrayList.getElements()).hasSize(4);
        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);

        doubleArrayList.remove(2);
        expected.remove(2);
        assertThat(doubleArrayList.size()).isEqualTo(2);
        assertThat(doubleArrayList.getElements()).hasSize(4);
        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);

        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.remove(2));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> expected.remove(2));

        doubleArrayList.remove(0);
        expected.remove(0);
        assertThat(doubleArrayList.size()).isEqualTo(1);
        assertThat(doubleArrayList.getElements()).hasSize(4);
        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);

        doubleArrayList.remove(0);
        expected.remove(0);
        assertThat(doubleArrayList.size()).isEqualTo(0);
        assertThat(doubleArrayList.getElements()).hasSize(4);
        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);

        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.remove(0));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> expected.remove(0));
    }

    @Test
    void clear_sanity() {
        DoubleArrayList doubleArrayList = new DoubleArrayList(List.of(0.1, 0.2, 0.3, 0.4));
        List<Double> expected = new ArrayList<>(List.of(0.1, 0.2, 0.3, 0.4));

        doubleArrayList.clear();
        expected.clear();
        assertThat(doubleArrayList.getElements()).hasSize(4);
        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);

        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.remove(0));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.add(1, 1.0));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> expected.remove(0));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> expected.add(1, 1.0));
    }

    @Test
    void set_sanity() {
        DoubleArrayList doubleArrayList = new DoubleArrayList(List.of(0.1, 0.2, 0.3, 0.4));
        List<Double> expected = new ArrayList<>(List.of(0.1, 0.2, 0.3, 0.4));

        Double setValue = doubleArrayList.set(3, 2.0);
        Double expectedSetValue = expected.set(3, 2.0);
        assertThat(setValue).isEqualTo(expectedSetValue);
        assertDoubleArrayListAndDoubleListEqual(doubleArrayList, expected);

        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.set(4, 1.0));
        assertThatIndexOutOfBoundsException().isThrownBy(() -> expected.set(4, 1.0));
    }

    private static void assertDoubleArrayListAndDoubleListEqual(DoubleArrayList doubleArrayList, List<Double> list) {
        assertThat(doubleArrayList.size()).isEqualTo(list.size());
        for (int idx = 0; idx < doubleArrayList.size(); idx++) {
            assertThat(doubleArrayList.get(idx)).isEqualTo(list.get(idx));
        }
        assertThatIndexOutOfBoundsException().isThrownBy(() -> doubleArrayList.get(list.size()));
    }
}
