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
 * A boxed {@link java.util.List} wrapper over a dense primitive {@code double[]} array.
 */
final class ConjureDoubleList extends AbstractPrimitiveList<Double, double[]> {
    private static final double[] EMPTY = new double[0];
    private static final ArrayOps<double[]> OPS = new ArrayOps<>() {
        @Override
        public int length(double[] array) {
            return array.length;
        }

        @Override
        public double[] copyOf(double[] array, int newLength) {
            return Arrays.copyOf(array, newLength);
        }
    };

    ConjureDoubleList() {
        this(new Storage<>(OPS, EMPTY, 0), false);
    }

    ConjureDoubleList(int initialCapacity) {
        this(new Storage<>(OPS, initialCapacity == 0 ? EMPTY : new double[initialCapacity], 0), false);
    }

    ConjureDoubleList(double[] elements) {
        this(new Storage<>(OPS, elements, elements.length), false);
    }

    private ConjureDoubleList(Storage<double[]> storage, boolean unmodifiable) {
        super(storage, unmodifiable);
    }

    @Override
    public Double get(int index) {
        storage().checkIndex(index);
        return storage().items()[index];
    }

    @Override
    public Double set(int index, Double element) {
        ensureMutable();
        double value = element;
        storage().checkIndex(index);
        double[] items = storage().items();
        double previous = items[index];
        items[index] = value;
        return previous;
    }

    @Override
    public void add(int index, Double toAdd) {
        ensureMutable();
        double value = toAdd;
        storage().prepareInsertAt(index);
        storage().items()[index] = value;
    }

    // Primitive optimized overload
    void add(double toAdd) {
        ensureMutable();
        int idx = storage().prepareAdd();
        storage().items()[idx] = toAdd;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> collection) {
        ensureMutable();
        double[] target = new double[collection.size()];
        int idx = 0;
        for (Double each : collection) {
            target[idx++] = each;
        }
        return storage().addAllAtIndex(index, target);
    }

    @Override
    public Double remove(int index) {
        ensureMutable();
        storage().checkIndex(index);
        double previous = storage().items()[index];
        storage().removeAt(index);
        return previous;
    }

    ConjureDoubleList asUnmodifiable() {
        return isUnmodifiable() ? this : new ConjureDoubleList(storage(), true);
    }
}
