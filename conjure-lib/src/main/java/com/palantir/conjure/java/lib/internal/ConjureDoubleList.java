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
 * A boxed {@link java.util.List} wrapper over a dense primitive {@code double[]} array.
 */
final class ConjureDoubleList extends AbstractList<Double> implements RandomAccess {
    private static final double[] EMPTY = new double[0];
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAXIMUM_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private final DoubleStorage storage;
    private final boolean unmodifiable;

    ConjureDoubleList() {
        this(new DoubleStorage(EMPTY, 0), false);
    }

    ConjureDoubleList(int initialCapacity) {
        this(new DoubleStorage(initialCapacity == 0 ? EMPTY : new double[initialCapacity], 0), false);
    }

    ConjureDoubleList(double[] elements) {
        this(new DoubleStorage(elements, elements.length), false);
    }

    private ConjureDoubleList(DoubleStorage storage, boolean unmodifiable) {
        this.storage = storage;
        this.unmodifiable = unmodifiable;
    }

    @Override
    public int size() {
        return storage.size;
    }

    @Override
    public void add(int index, Double toAdd) {
        ensureMutable();
        storage.addAtIndex(index, toAdd);
    }

    // Primitive optimized overload
    void add(double toAdd) {
        ensureMutable();
        storage.add(toAdd);
    }

    @Override
    public Double get(int index) {
        return storage.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> collection) {
        ensureMutable();
        double[] target = new double[collection.size()];
        int idx = 0;
        for (Double each : collection) {
            target[idx++] = each;
        }
        return storage.addAllAtIndex(index, target);
    }

    // Primitive optimized overload
    void addAll(double[] source) {
        ensureMutable();
        storage.addAll(source);
    }

    @Override
    public Double remove(int index) {
        ensureMutable();
        return storage.removeAtIndex(index);
    }

    @Override
    public void clear() {
        ensureMutable();
        storage.clear();
    }

    @Override
    public Double set(int index, Double element) {
        ensureMutable();
        return storage.set(index, element);
    }

    ConjureDoubleList asUnmodifiable() {
        return unmodifiable ? this : new ConjureDoubleList(storage, true);
    }

    // Cannot be named 'toArray' as that conflicts with the #toArray in AbstractList
    // This is a serialization optimization that avoids boxing, but does copy
    @JsonValue
    double[] jacksonSerialize() {
        return storage.toArray();
    }

    private void ensureMutable() {
        if (unmodifiable) {
            throw new UnsupportedOperationException("Cannot modify an unmodifiable list");
        }
    }

    private static final class DoubleStorage {
        private double[] items;
        private int size;

        DoubleStorage(double[] items, int size) {
            this.items = items;
            this.size = size;
        }

        double get(int index) {
            Objects.checkIndex(index, size);
            return items[index];
        }

        double set(int index, double element) {
            Objects.checkIndex(index, size);
            double previous = items[index];
            items[index] = element;
            return previous;
        }

        void add(double element) {
            ensureCapacity(size + 1);
            items[size++] = element;
        }

        void addAtIndex(int index, double element) {
            checkPositionIndex(index, size);
            ensureCapacity(size + 1);
            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(items, index, items, index + 1, numMoved);
            }
            items[index] = element;
            size++;
        }

        void addAll(double[] source) {
            int len = source.length;
            if (len == 0) {
                return;
            }
            ensureCapacity(size + len);
            System.arraycopy(source, 0, items, size, len);
            size += len;
        }

        boolean addAllAtIndex(int index, double[] source) {
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

        double removeAtIndex(int index) {
            Objects.checkIndex(index, size);
            double previous = items[index];
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

        double[] toArray() {
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
