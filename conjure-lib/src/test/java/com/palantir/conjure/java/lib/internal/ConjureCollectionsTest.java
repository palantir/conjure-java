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

package com.palantir.conjure.java.lib.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConjureCollectionsTest {

    @Test
    public void newNonNullDoubleList() {
        List<Double> doubleList = ConjureCollections.newNonNullDoubleList();
        assertThat(doubleList).hasSize(0);
        ConjureCollections.addAll(doubleList, List.of(1.0, 2.0));
        assertThat(doubleList).hasSize(2);
        doubleList.add(3.0);
        assertThat(doubleList).hasSize(3);
        assertThat(doubleList.get(0)).isEqualTo(1.0);
        assertThat(doubleList.get(1)).isEqualTo(2.0);
        assertThat(doubleList.get(2)).isEqualTo(3.0);

        doubleList = ConjureCollections.newNonNullDoubleList(List.of(1.0, 2.0, 3.0));
        assertThat(doubleList).hasSize(3);
        assertThat(doubleList.get(0)).isEqualTo(1.0);
        assertThat(doubleList.get(1)).isEqualTo(2.0);
        assertThat(doubleList.get(2)).isEqualTo(3.0);

        double setValue = doubleList.set(2, 4.0);
        assertThat(setValue).isEqualTo(3.0);
        double removedValue = doubleList.remove(1);
        assertThat(removedValue).isEqualTo(2.0);
        assertThat(doubleList.get(0)).isEqualTo(1.0);
        assertThat(doubleList.get(1)).isEqualTo(4.0);

        doubleList.clear();
        assertThat(doubleList).hasSize(0);

        double[] rawList = new double[] {0.1, 0.2, 0.3};
        doubleList = ConjureCollections.newNonNullDoubleList(rawList);
        assertThat(doubleList).hasSize(3);
        assertThat(doubleList.get(0)).isEqualTo(0.1);
        assertThat(doubleList.get(1)).isEqualTo(0.2);
        assertThat(doubleList.get(2)).isEqualTo(0.3);

        // Check we made a copy of the array
        rawList[0] = 0.4;
        assertThat(doubleList.get(2)).isEqualTo(0.3);
    }

    @Test
    public void newNonNullSafeLongList() {
        List<SafeLong> safeLongList = ConjureCollections.newNonNullSafeLongList();
        assertThat(safeLongList).hasSize(0);
        ConjureCollections.addAll(safeLongList, List.of(SafeLong.of(1L), SafeLong.of(2L)));
        assertThat(safeLongList).hasSize(2);
        safeLongList.add(SafeLong.of(3L));
        assertThat(safeLongList).hasSize(3);
        assertThat(safeLongList.get(0)).isEqualTo(SafeLong.of(1L));
        assertThat(safeLongList.get(1)).isEqualTo(SafeLong.of(2L));
        assertThat(safeLongList.get(2)).isEqualTo(SafeLong.of(3L));

        safeLongList =
                ConjureCollections.newNonNullSafeLongList(List.of(SafeLong.of(1L), SafeLong.of(2L), SafeLong.of(3L)));
        assertThat(safeLongList).hasSize(3);
        assertThat(safeLongList.get(0)).isEqualTo(SafeLong.of(1L));
        assertThat(safeLongList.get(1)).isEqualTo(SafeLong.of(2L));
        assertThat(safeLongList.get(2)).isEqualTo(SafeLong.of(3L));

        SafeLong setValue = safeLongList.set(2, SafeLong.of(4L));
        assertThat(setValue).isEqualTo(SafeLong.of(3L));
        SafeLong removedValue = safeLongList.remove(1);
        assertThat(removedValue).isEqualTo(SafeLong.of(2L));
        assertThat(safeLongList.get(0)).isEqualTo(SafeLong.of(1L));
        assertThat(safeLongList.get(1)).isEqualTo(SafeLong.of(4L));

        safeLongList.clear();
        assertThat(safeLongList).hasSize(0);

        long[] rawList = new long[] {1L, 2L, 3L};
        safeLongList = ConjureCollections.newNonNullSafeLongList(rawList);
        assertThat(safeLongList).hasSize(3);
        assertThat(safeLongList.get(0)).isEqualTo(SafeLong.of(1L));
        assertThat(safeLongList.get(1)).isEqualTo(SafeLong.of(2L));
        assertThat(safeLongList.get(2)).isEqualTo(SafeLong.of(3L));

        rawList[0] = 42L;
        assertThat(safeLongList.get(0)).isEqualTo(SafeLong.of(1L));

        assertThatExceptionOfType(SafeIllegalArgumentException.class)
                .isThrownBy(() -> ConjureCollections.newNonNullSafeLongList(
                        new long[] {1L, 2L, SafeLong.MAX_VALUE.longValue() + 1}));
    }
}
