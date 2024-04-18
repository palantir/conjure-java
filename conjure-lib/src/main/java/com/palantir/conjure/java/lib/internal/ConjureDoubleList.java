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

import com.palantir.logsafe.exceptions.SafeUnsupportedOperationException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;

public final class ConjureDoubleList extends AbstractList<Double> implements RandomAccess {
    private final DoubleArrayList delegate;

    public ConjureDoubleList() {
        this.delegate = new DoubleArrayList();
    }

    public ConjureDoubleList(Iterable<Double> iterable) {
        if (iterable instanceof Collection) {
            this.delegate = new DoubleArrayList(((Collection<Double>) iterable).size());
        } else {
            this.delegate = new DoubleArrayList();
        }
        for (double e : iterable) {
            delegate.add(e);
        }
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void add(int index, Double toAdd) {
        delegate.addAtIndex(index, toAdd);
    }

    @Override
    public Double get(int index) {
        Objects.checkIndex(index, delegate.size());
        return delegate.get(index);
    }

    @Override
    public boolean addAll(int _index, Collection<? extends Double> _collection) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public boolean addAll(Collection<? extends Double> collection) {
        delegate.ensureCapacity(collection.size() + delegate.size());
        boolean added = true;
        for (double element : collection) {
            added &= delegate.add(element);
        }
        return added;
    }

    @Override
    public Double remove(int _index) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public void clear() {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public Double set(int _index, Double _element) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }
}
