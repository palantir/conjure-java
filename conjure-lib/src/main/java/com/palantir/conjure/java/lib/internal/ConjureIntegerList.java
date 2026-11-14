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

import java.util.Arrays;
import java.util.Collection;

/**
 * A boxed {@link java.util.List} wrapper over a dense primitive {@code int[]} array.
 */
final class ConjureIntegerList extends AbstractPrimitiveList<Integer, int[]> {
    private static final int[] EMPTY = new int[0];
    private static final ArrayOps<int[]> OPS = new ArrayOps<>() {
        @Override
        public int length(int[] array) {
            return array.length;
        }

        @Override
        public int[] copyOf(int[] array, int newLength) {
            return Arrays.copyOf(array, newLength);
        }
    };

    ConjureIntegerList() {
        this(new Storage<>(OPS, EMPTY, 0), false);
    }

    ConjureIntegerList(int initialCapacity) {
        this(new Storage<>(OPS, initialCapacity == 0 ? EMPTY : new int[initialCapacity], 0), false);
    }

    ConjureIntegerList(int[] elements) {
        this(new Storage<>(OPS, elements, elements.length), false);
    }

    private ConjureIntegerList(Storage<int[]> storage, boolean unmodifiable) {
        super(storage, unmodifiable);
    }

    @Override
    public Integer get(int index) {
        storage().checkIndex(index);
        return storage().items()[index];
    }

    @Override
    public Integer set(int index, Integer element) {
        ensureMutable();
        int value = element;
        storage().checkIndex(index);
        int[] items = storage().items();
        int previous = items[index];
        items[index] = value;
        return previous;
    }

    @Override
    public void add(int index, Integer toAdd) {
        ensureMutable();
        int value = toAdd;
        storage().prepareInsertAt(index);
        storage().items()[index] = value;
    }

    // Primitive optimized overload
    void add(int toAdd) {
        ensureMutable();
        int idx = storage().prepareAdd();
        storage().items()[idx] = toAdd;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> collection) {
        ensureMutable();
        int[] target = new int[collection.size()];
        int idx = 0;
        for (Integer each : collection) {
            target[idx++] = each;
        }
        return storage().addAllAtIndex(index, target);
    }

    @Override
    public Integer remove(int index) {
        ensureMutable();
        storage().checkIndex(index);
        int previous = storage().items()[index];
        storage().removeAt(index);
        return previous;
    }

    ConjureIntegerList asUnmodifiable() {
        return isUnmodifiable() ? this : new ConjureIntegerList(storage(), true);
    }
}
