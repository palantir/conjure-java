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

import com.palantir.conjure.java.lib.SafeLong;
import java.util.Arrays;
import java.util.Collection;

/**
 * A boxed {@link java.util.List} wrapper over a dense primitive {@code long[]} array for {@link SafeLong} elements.
 */
final class ConjureSafeLongList extends AbstractPrimitiveList<SafeLong, long[]> {
    private static final long[] EMPTY = new long[0];
    private static final ArrayOps<long[]> OPS = new ArrayOps<>() {
        @Override
        public int length(long[] array) {
            return array.length;
        }

        @Override
        public long[] copyOf(long[] array, int newLength) {
            return Arrays.copyOf(array, newLength);
        }
    };

    ConjureSafeLongList() {
        this(new Storage<>(OPS, EMPTY, 0), false);
    }

    ConjureSafeLongList(int initialCapacity) {
        this(new Storage<>(OPS, initialCapacity == 0 ? EMPTY : new long[initialCapacity], 0), false);
    }

    private ConjureSafeLongList(Storage<long[]> storage, boolean unmodifiable) {
        super(storage, unmodifiable);
    }

    @Override
    public SafeLong get(int index) {
        storage().checkIndex(index);
        return SafeLong.of(storage().items()[index]);
    }

    @Override
    public SafeLong set(int index, SafeLong element) {
        ensureMutable();
        long value = element.longValue();
        storage().checkIndex(index);
        long[] items = storage().items();
        long previous = items[index];
        items[index] = value;
        return SafeLong.of(previous);
    }

    @Override
    public void add(int index, SafeLong toAdd) {
        ensureMutable();
        long value = toAdd.longValue();
        storage().prepareInsertAt(index);
        storage().items()[index] = value;
    }

    @Override
    public boolean addAll(int index, Collection<? extends SafeLong> collection) {
        ensureMutable();
        long[] target = new long[collection.size()];
        int idx = 0;
        for (SafeLong each : collection) {
            target[idx++] = each.longValue();
        }
        return storage().addAllAtIndex(index, target);
    }

    @Override
    public SafeLong remove(int index) {
        ensureMutable();
        storage().checkIndex(index);
        long previous = storage().items()[index];
        storage().removeAt(index);
        return SafeLong.of(previous);
    }

    ConjureSafeLongList asUnmodifiable() {
        return isUnmodifiable() ? this : new ConjureSafeLongList(storage(), true);
    }
}
