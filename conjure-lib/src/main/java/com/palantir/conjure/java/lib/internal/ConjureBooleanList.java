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
import org.eclipse.collections.impl.list.mutable.primitive.BooleanArrayList;

@SuppressWarnings("checkstyle:OuterTypeFilename")
public final class ConjureBooleanList extends AbstractList<Boolean> implements RandomAccess {
    private final BooleanArrayList delegate;

    public ConjureBooleanList() {
        this.delegate = new BooleanArrayList();
    }

    public ConjureBooleanList(Iterable<Boolean> iterable) {
        if (iterable instanceof Collection) {
            this.delegate = new BooleanArrayList(((Collection<Boolean>) iterable).size());
        } else {
            this.delegate = new BooleanArrayList();
        }
        for (boolean e : iterable) {
            delegate.add(e);
        }
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void add(int index, Boolean toAdd) {
        delegate.addAtIndex(index, toAdd);
    }

    @Override
    public Boolean get(int index) {
        Objects.checkIndex(index, delegate.size());
        return delegate.get(index);
    }

    @Override
    public boolean addAll(int _index, Collection<? extends Boolean> _collection) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public boolean addAll(Collection<? extends Boolean> collection) {
        for (boolean element : collection) {
            delegate.add(element);
        }
        return true;
    }

    @Override
    public Boolean remove(int _index) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public void clear() {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public Boolean set(int _index, Boolean _element) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }
}
