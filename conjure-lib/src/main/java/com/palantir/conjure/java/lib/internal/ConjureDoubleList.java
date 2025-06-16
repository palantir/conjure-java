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
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.impl.list.mutable.primitive.BoxedMutableDoubleList;

/**
 * ConjureDoubleList is a boxed list wrapper for the eclipse-collections DoubleArrayList. In eclipse-collections 12,
 * a BoxedMutableDoubleList will be released. Once available, ConjureDoubleList should be replaced with that.
 */
final class ConjureDoubleList extends BoxedMutableDoubleList {
    private final MutableDoubleList delegate;

    ConjureDoubleList(MutableDoubleList delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    // Primitive optimized overloads
    void add(double toAdd) {
        this.delegate.add(toAdd);
    }

    void addAll(double[] source) {
        this.delegate.addAll(source);
    }

    // Cannot be named 'toArray' as that conflicts with the #toArray in AbstractList
    // This is a serialization optimization that avoids boxing, but does copy
    @JsonValue
    double[] jacksonSerialize() {
        return delegate.toArray();
    }
}
