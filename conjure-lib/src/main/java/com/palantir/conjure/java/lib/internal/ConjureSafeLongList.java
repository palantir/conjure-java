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

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.exceptions.SafeUnsupportedOperationException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;

public final class ConjureSafeLongList extends AbstractList<SafeLong> implements RandomAccess {
    private final LongArrayList delegate;

    public ConjureSafeLongList() {
        this.delegate = new LongArrayList();
    }

    public ConjureSafeLongList(Iterable<SafeLong> iterable) {
        if (iterable instanceof Collection) {
            this.delegate = new LongArrayList(((Collection<SafeLong>) iterable).size());
        } else {
            this.delegate = new LongArrayList();
        }
        for (SafeLong e : iterable) {
            delegate.add(e.longValue());
        }
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void add(int index, SafeLong toAdd) {
        delegate.addAtIndex(index, toAdd.longValue());
    }

    @Override
    public SafeLong get(int index) {
        Objects.checkIndex(index, delegate.size());
        return SafeLong.of(delegate.get(index));
    }

    @Override
    public boolean addAll(int _index, Collection<? extends SafeLong> _collection) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public boolean addAll(Collection<? extends SafeLong> collection) {
        delegate.ensureCapacity(collection.size() + delegate.size());
        boolean added = true;
        for (SafeLong element : collection) {
            added &= delegate.add(element.longValue());
        }
        return added;
    }

    @Override
    public SafeLong remove(int _index) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public void clear() {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }

    @Override
    public SafeLong set(int _index, SafeLong _element) {
        throw new SafeUnsupportedOperationException("This operation is unsupported");
    }
}
