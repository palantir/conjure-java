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

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * Shared base class for Conjure's primitive-backed {@link java.util.List} implementations ({@link ConjureIntegerList},
 * {@link ConjureDoubleList}, {@link ConjureSafeLongList}), parameterized over the boxed element type {@code E} and the
 * primitive array type {@code A} ({@code int[]}, {@code double[]}, {@code long[]}).
 */
abstract class AbstractPrimitiveList<E, A> extends AbstractList<E> implements RandomAccess {
    private final Storage<A> storage;
    private final boolean unmodifiable;

    AbstractPrimitiveList(Storage<A> storage, boolean unmodifiable) {
        this.storage = storage;
        this.unmodifiable = unmodifiable;
    }

    final Storage<A> storage() {
        return storage;
    }

    final boolean isUnmodifiable() {
        return unmodifiable;
    }

    @Override
    public final int size() {
        return storage.size();
    }

    @Override
    public final void clear() {
        ensureMutable();
        storage.clear();
    }

    // Primitive optimized overload
    final void addAll(A source) {
        ensureMutable();
        storage.addAll(source);
    }

    // Cannot be named 'toArray' as that conflicts with the #toArray in AbstractList.
    // This is a serialization optimization that avoids boxing, but does copy.
    @JsonValue
    final A jacksonSerialize() {
        return storage.toArray();
    }

    final void ensureMutable() {
        if (unmodifiable) {
            throw new UnsupportedOperationException("Cannot modify an unmodifiable list");
        }
    }

    interface ArrayOps<A> {
        int length(A array);

        A copyOf(A array, int newLength);
    }

    static final class Storage<A> {
        private static final int DEFAULT_CAPACITY = 10;
        private static final int MAXIMUM_ARRAY_SIZE = Integer.MAX_VALUE - 8;

        private final ArrayOps<A> ops;
        private A items;
        private int size;

        Storage(ArrayOps<A> ops, A items, int size) {
            this.ops = ops;
            this.items = items;
            this.size = size;
        }

        A items() {
            return items;
        }

        int size() {
            return size;
        }

        void checkIndex(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
            }
        }

        int prepareAdd() {
            ensureCapacity(size + 1);
            return size++;
        }

        void prepareInsertAt(int index) {
            checkPositionIndex(index, size);
            ensureCapacity(size + 1);
            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(items, index, items, index + 1, numMoved);
            }
            size++;
        }

        void addAll(A source) {
            int len = ops.length(source);
            if (len == 0) {
                return;
            }
            ensureCapacity(size + len);
            System.arraycopy(source, 0, items, size, len);
            size += len;
        }

        boolean addAllAtIndex(int index, A source) {
            checkPositionIndex(index, size);
            int len = ops.length(source);
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

        void removeAt(int index) {
            int numMoved = size - index - 1;
            if (numMoved > 0) {
                System.arraycopy(items, index + 1, items, index, numMoved);
            }
            size--;
        }

        void clear() {
            size = 0;
        }

        A toArray() {
            return ops.copyOf(items, size);
        }

        private void ensureCapacity(int minCapacity) {
            int oldCapacity = ops.length(items);
            if (minCapacity > oldCapacity) {
                int defaultGrowth = oldCapacity == 0 ? DEFAULT_CAPACITY : sizePlusFiftyPercent(oldCapacity);
                int newCapacity = Math.max(minCapacity, defaultGrowth);
                items = ops.copyOf(items, newCapacity);
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
