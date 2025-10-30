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
import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * ConjureIntegerList is a boxed list wrapper for the eclipse-collections IntArrayList. In eclipse-collections 13,
 * a BoxedMutableIntList was released. However, there is an issue where MutableIntList does not implement subList.
 * https://github.com/eclipse-collections/eclipse-collections/issues/1053
 */
final class ConjureIntegerList extends AbstractList<Integer> implements RandomAccess {
    private final MutableIntList delegate;

    ConjureIntegerList(MutableIntList delegate) {
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

    // Primitive optimized overload
    void add(int toAdd) {
        this.delegate.add(toAdd);
    }

    @Override
    public Integer get(int index) {
        return delegate.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Integer> collection) {
        if (collection instanceof ConjureIntegerList primitives) {
            return index == size() - 1
                    ? delegate.addAll(primitives.delegate)
                    : delegate.addAllAtIndex(index, primitives.delegate);
        }
        int[] target = new int[collection.size()];
        Iterate.forEachWithIndex(collection, (each, parameter) -> target[parameter] = each.intValue());
        return delegate.addAllAtIndex(index, target);
    }

    // Primitive optimized overload
    void addAll(int[] source) {
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

    ConjureIntegerList asUnmodifiable() {
        return new ConjureIntegerList(delegate.asUnmodifiable());
    }

    // Cannot be named 'toArray' as that conflicts with the #toArray in AbstractList
    // This is a serialization optimization that avoids boxing, but does copy
    @JsonValue
    int[] jacksonSerialize() {
        return delegate.toArray();
    }
}
