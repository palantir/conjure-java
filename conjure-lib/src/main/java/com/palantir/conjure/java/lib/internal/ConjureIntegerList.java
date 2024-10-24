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

import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * ConjureIntegerList is a boxed list wrapper for the eclipse-collections IntArrayList. In eclipse-collections 12,
 * a BoxedMutableIntList will be released. Once available, ConjureIntegerList should be replaced with that.
 */
final class ConjureIntegerList extends AbstractList<Integer> implements RandomAccess {
    private final IntArrayList delegate;

    ConjureIntegerList(IntArrayList delegate) {
        this.delegate = delegate;
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public void add(int index, Integer toAdd) {
        delegate.addAtIndex(index, toAdd);
    }

    @Override
    public Integer get(int index) {
        return delegate.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> collection) {
        int[] target = new int[collection.size()];
        Iterate.forEachWithIndex(collection, (each, parameter) -> target[parameter] = each.intValue());
        return delegate.addAllAtIndex(index, target);
    }

    public void addAll(int... source) {
        this.delegate.addAll(source);
    }

    @Override
    public Integer remove(int index) {
        return delegate.removeAtIndex(index);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Integer set(int index, Integer element) {
        return delegate.set(index, element);
    }
}
