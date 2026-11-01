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

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * A boxed {@link java.util.List} wrapper over a dense primitive {@code int[]} array.
 */
final class ConjureIntegerList extends AbstractList<Integer> implements RandomAccess {
    private static final int[] EMPTY = new int[0];
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAXIMUM_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private final IntStorage storage;
    private final boolean unmodifiable;

    ConjureIntegerList() {
        this(new IntStorage(EMPTY, 0), false);
    }

    ConjureIntegerList(int initialCapacity) {
        this(new IntStorage(initialCapacity == 0 ? EMPTY : new int[initialCapacity], 0), false);
    }

    ConjureIntegerList(int[] elements) {
        this(new IntStorage(elements, elements.length), false);
    }

    private ConjureIntegerList(IntStorage storage, boolean unmodifiable) {
        this.storage = storage;
        this.unmodifiable = unmodifiable;
    }

    @Override
    public int size() {
        return storage.size;
    }

    @Override
    public void add(int index, Integer toAdd) {
        ensureMutable();
        storage.addAtIndex(index, toAdd);
    }

    // Primitive optimized overload
    void add(int toAdd) {
        ensureMutable();
        storage.add(toAdd);
    }

    @Override
    public Integer get(int index) {
        return storage.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> collection) {
        ensureMutable();
        int[] target = new int[collection.size()];
        int idx = 0;
        for (Integer each : collection) {
            target[idx++] = each;
        }
        return storage.addAllAtIndex(index, target);
    }

    // Primitive optimized overload
    void addAll(int[] source) {
        ensureMutable();
        storage.addAll(source);
    }

    @Override
    public Integer remove(int index) {
        ensureMutable();
        return storage.removeAtIndex(index);
    }

    @Override
    public void clear() {
        ensureMutable();
        storage.clear();
    }

    @Override
    public Integer set(int index, Integer element) {
        ensureMutable();
        return storage.set(index, element);
    }

    ConjureIntegerList asUnmodifiable() {
        return unmodifiable ? this : new ConjureIntegerList(storage, true);
    }

    // Cannot be named 'toArray' as that conflicts with the #toArray in AbstractList
    // This is a serialization optimization that avoids boxing, but does copy
    @JsonValue
    int[] jacksonSerialize() {
        return storage.toArray();
    }

    private void ensureMutable() {
        if (unmodifiable) {
            throw new UnsupportedOperationException("Cannot modify an unmodifiable list");
        }
    }

    private static final class IntStorage {
        private int[] items;
        private int size;

        IntStorage(int[] items, int size) {
            this.items = items;
            this.size = size;
        }

        int get(int index) {
            Objects.checkIndex(index, size);
            return items[index];
        }

        int set(int index, int element) {
            Objects.checkIndex(index, size);
            int previous = items[index];
            items[index] = element;
            return previous;
        }

        void add(int element) {
            ensureCapacity(size + 1);
            items[size++] = element;
        }

        void addAtIndex(int index, int element) {
            checkPositionIndex(index, size);
            ensureCapacity(size + 1);
            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(items, index, items, index + 1, numMoved);
            }
            items[index] = element;
            size++;
        }

        void addAll(int[] source) {
            int len = source.length;
            if (len == 0) {
                return;
            }
            ensureCapacity(size + len);
            System.arraycopy(source, 0, items, size, len);
            size += len;
        }

        boolean addAllAtIndex(int index, int[] source) {
            checkPositionIndex(index, size);
            int len = source.length;
            if (len == 0) {
                return false;
            }
            ensureCapacity(size + len);
            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(items, index, items, index + len, numMoved);
            }
            System.arraycopy(source, 0, items, index, len);
            size += len;
            return true;
        }

        int removeAtIndex(int index) {
            Objects.checkIndex(index, size);
            int previous = items[index];
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(items, index + 1, items, index, numMoved);
            }
            size--;
            return previous;
        }

        void clear() {
            size = 0;
        }

        int[] toArray() {
            return Arrays.copyOf(items, size);
        }

        private void ensureCapacity(int minCapacity) {
            int oldCapacity = items.length;
            if (minCapacity > oldCapacity) {
                int defaultGrowth = oldCapacity == 0 ? DEFAULT_CAPACITY : sizePlusFiftyPercent(oldCapacity);
                int newCapacity = Math.max(minCapacity, defaultGrowth);
                items = Arrays.copyOf(items, newCapacity);
            }
        }

        private static int sizePlusFiftyPercent(int oldSize) {
            int result = oldSize + (oldSize >> 1) + 1;
            return result < oldSize ? MAXIMUM_ARRAY_SIZE : result;
        }

        private static void checkPositionIndex(int index, int size) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }
    }
}
