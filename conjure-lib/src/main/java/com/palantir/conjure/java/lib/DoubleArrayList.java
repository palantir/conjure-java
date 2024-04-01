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

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.RandomAccess;

public final class DoubleArrayList extends AbstractList<Double> implements RandomAccess {
    private int size = 0;
    private double[] elements;

    public DoubleArrayList() {
        this(16);
    }

    private void resizeIfNecessary(int toAdd) {
        if (size + toAdd > elements.length) {
            elements = Arrays.copyOf(elements, Math.max(size + toAdd, elements.length * 2));
        }
    }

    public DoubleArrayList(int initialCapacity) {
        elements = new double[initialCapacity];
    }

    public DoubleArrayList(Collection<Double> collection) {
        elements = new double[collection.size()];
        Double[] array = collection.toArray(Double[]::new);
        size = collection.size();
        if (size != 0) {
            for (int idx = 0; idx < size; idx++) {
                elements[idx] = array[idx];
            }
        }
    }

    public DoubleArrayList(double[] doubleArray) {
        elements = new double[doubleArray.length];
        size = doubleArray.length;
        if (size != 0) {
            elements = Arrays.copyOf(doubleArray, size);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void add(int index, Double toAdd) {
        if (index == size) {
            resizeIfNecessary(1);
            size++;
        } else if (index > size) {
            resizeIfNecessary(index - size + 1);
            size = index + 1;
        } else {
            resizeIfNecessary(1);
            // Move over all entries
            System.arraycopy(elements, index, elements, index + 1, size - index - 1);
        }

        elements[index] = toAdd;
    }

    @Override
    public boolean remove(Object obj) {
        int idx = indexOf(obj);
        if (idx == -1) {
            return false;
        }

        System.arraycopy(elements, idx + 1, elements, idx, size - idx - 1);
        // Technically not necessary, doing for hygiene
        elements[size] = 0;
        size--;

        return false;
    }

    @Override
    public Double remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        double oldValue = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[size] = 0;
        size--;

        return oldValue;
    }

    @Override
    public Iterator<Double> iterator() {
        // TODO: Implement as the default one doesn't do deletion
        return super.iterator();
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = 0;
        }
        size = 0;
    }

    @Override
    public Double get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        return elements[index];
    }

    @Override
    public Double set(int index, Double element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        double oldValue = elements[index];
        elements[index] = element;
        return oldValue;
    }
}
