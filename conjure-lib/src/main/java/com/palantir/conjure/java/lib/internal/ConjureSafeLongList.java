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
import com.palantir.conjure.java.lib.SafeLong;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * A boxed {@link java.util.List} wrapper over a dense primitive {@code long[]} array for {@link SafeLong} elements.
 */
final class ConjureSafeLongList extends AbstractList<SafeLong> implements RandomAccess {
    private static final long[] EMPTY = new long[0];
    private static final int DEFAULT_CAPACITY = 10;
    private static final int MAXIMUM_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private final LongStorage storage;
    private final boolean unmodifiable;

    ConjureSafeLongList() {
        this(new LongStorage(EMPTY, 0), false);
    }

    ConjureSafeLongList(int initialCapacity) {
        this(new LongStorage(initialCapacity == 0 ? EMPTY : new long[initialCapacity], 0), false);
    }

    private ConjureSafeLongList(LongStorage storage, boolean unmodifiable) {
        this.storage = storage;
        this.unmodifiable = unmodifiable;
    }

    @Override
    public int size() {
        return storage.size;
    }

    @Override
    public void add(int index, SafeLong toAdd) {
        ensureMutable();
        storage.addAtIndex(index, toAdd.longValue());
    }

    @Override
    public SafeLong get(int index) {
        return SafeLong.of(storage.get(index));
    }

    @Override
    public boolean addAll(int index, Collection<? extends SafeLong> collection) {
        ensureMutable();
        long[] target = new long[collection.size()];
        int idx = 0;
        for (SafeLong each : collection) {
            target[idx++] = each.longValue();
        }
        return storage.addAllAtIndex(index, target);
    }

    @Override
    public SafeLong remove(int index) {
        ensureMutable();
        return SafeLong.of(storage.removeAtIndex(index));
    }

    @Override
    public void clear() {
        ensureMutable();
        storage.clear();
    }

    @Override
    public SafeLong set(int index, SafeLong element) {
        ensureMutable();
        return SafeLong.of(storage.set(index, element.longValue()));
    }

    ConjureSafeLongList asUnmodifiable() {
        return unmodifiable ? this : new ConjureSafeLongList(storage, true);
    }

    // Cannot be named 'toArray' as that conflicts with the #toArray in AbstractList
    // This is a serialization optimization that avoids boxing, but does copy
    @JsonValue
    long[] jacksonSerialize() {
        return storage.toArray();
    }

    private void ensureMutable() {
        if (unmodifiable) {
            throw new UnsupportedOperationException("Cannot modify an unmodifiable list");
        }
    }

    private static final class LongStorage {
        private long[] items;
        private int size;

        LongStorage(long[] items, int size) {
            this.items = items;
            this.size = size;
        }

        long get(int index) {
            Objects.checkIndex(index, size);
            return items[index];
        }

        long set(int index, long element) {
            Objects.checkIndex(index, size);
            long previous = items[index];
            items[index] = element;
            return previous;
        }

        void addAtIndex(int index, long element) {
            checkPositionIndex(index, size);
            ensureCapacity(size + 1);
            int numMoved = size - index;
            if (numMoved > 0) {
                System.arraycopy(items, index, items, index + 1, numMoved);
            }
            items[index] = element;
            size++;
        }

        boolean addAllAtIndex(int index, long[] source) {
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

        long removeAtIndex(int index) {
            Objects.checkIndex(index, size);
            long previous = items[index];
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

        long[] toArray() {
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
