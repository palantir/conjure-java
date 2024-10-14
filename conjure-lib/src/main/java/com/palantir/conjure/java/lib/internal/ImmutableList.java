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

import com.google.errorprone.annotations.DoNotCall;
import java.util.AbstractList;
import java.util.Collection;

public abstract class ImmutableList<T> extends AbstractList<T> {
    @Override
    public abstract T get(int index);

    @Override
    public abstract int size();

    @DoNotCall
    @Override
    public final void add(int index, T toAdd) {
        throw new UnsupportedOperationException();
    }

    @DoNotCall
    @Override
    public final boolean addAll(int index, Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }

    @DoNotCall
    @Override
    public final T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @DoNotCall
    @Override
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @DoNotCall
    @Override
    public final T set(int index, T element) {
        throw new UnsupportedOperationException();
    }
}
