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
import java.util.RandomAccess;
import org.eclipse.collections.impl.list.mutable.primitive.BooleanArrayList;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * ConjureBooleanList is a boxed list wrapper for the eclipse-collections BooleanArrayList. In eclipse-collections 12,
 * a BoxedMutableBooleanList will be released. Once available, ConjureBooleanList should be replaced with that.
 */
final class ConjureBooleanList extends AbstractList<Boolean> implements RandomAccess {
    private final BooleanArrayList delegate;

    ConjureBooleanList(BooleanArrayList delegate) {
        this.delegate = delegate;
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
        return delegate.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Boolean> collection) {
        boolean[] target = new boolean[collection.size()];
        Iterate.forEachWithIndex(collection, (each, parameter) -> target[parameter] = each.booleanValue());
        return delegate.addAllAtIndex(index, target);
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
