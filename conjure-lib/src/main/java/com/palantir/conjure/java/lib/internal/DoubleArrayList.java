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

import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

public final class DoubleArrayList extends AbstractList<Double> implements RandomAccess {
    private static final double[] EMPTY_ELEMENTDATA = {};
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    private int size = 0;
    private double[] elements;

    public DoubleArrayList() {
        this(16);
    }

    private void resizeIfNecessary(int toAdd) {
        int minCapacity = size + toAdd;
        if (minCapacity < 0) {
            throw new SafeRuntimeException("DoubleArrayList has exceeded max size");
        }

        if (minCapacity > elements.length) {
            elements = Arrays.copyOf(elements, newCapacity(minCapacity));
        }
    }

    @SuppressWarnings("ThrowError")
    private int newCapacity(int minCapacity) {
        int oldCapacity = elements.length;
        // increase by 50%
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity <= 0) {
            if (elements.length == 0) {
                return Math.max(10, minCapacity);
            }
            if (minCapacity < 0) {
                throw new SafeRuntimeException("DoubleArrayList has exceeded max size");
            }
            return minCapacity;
        }
        return (newCapacity - MAX_ARRAY_SIZE <= 0) ? newCapacity : hugeCapacity(minCapacity);
    }

    @SuppressWarnings("ThrowError")
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    public DoubleArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            elements = new double[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elements = EMPTY_ELEMENTDATA;
        } else {
            throw new SafeIllegalArgumentException("Illegal capacity", SafeArg.of("capacity", initialCapacity));
        }
    }

    public DoubleArrayList(Collection<Double> collection) {
        size = collection.size();
        if (size == 0) {
            this.elements = EMPTY_ELEMENTDATA;
        } else {
            elements = new double[collection.size()];
            int idx = 0;
            for (Double value : collection) {
                elements[idx] = value;
                idx++;
            }
        }
    }

    public DoubleArrayList(double[] doubleArray) {
        size = doubleArray.length;
        if (size == 0) {
            this.elements = EMPTY_ELEMENTDATA;
        } else {
            elements = Arrays.copyOf(doubleArray, size);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, Double toAdd) {
        checkIndexInAddRange(index);
        resizeIfNecessary(1);
        if (index != size) {
            // Move over all entries
            System.arraycopy(elements, index, elements, index + 1, size - index);
        }
        size++;

        elements[index] = toAdd;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> collection) {
        checkIndexInAddRange(index);
        int numToAdd = collection.size();
        if (numToAdd == 0) {
            return false;
        }
        resizeIfNecessary(numToAdd);
        System.arraycopy(elements, index, elements, index + numToAdd, size - index);
        int offset = 0;
        for (Double element : collection) {
            elements[index + offset] = element;
            offset++;
        }
        size += numToAdd;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Double> collection) {
        return addAll(size, collection);
    }

    @Override
    public Double remove(int index) {
        Objects.checkIndex(index, size);
        double oldValue = elements[index];
        if (index != size - 1) {
            System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        }
        size--;

        return oldValue;
    }

    @Override
    public void clear() {
        size = 0;
    }

    @Override
    public Double get(int index) {
        Objects.checkIndex(index, size);
        return elements[index];
    }

    @Override
    public Double set(int index, Double element) {
        Objects.checkIndex(index, size);
        double oldValue = elements[index];
        elements[index] = element;
        return oldValue;
    }

    private void checkIndexInAddRange(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }
}
