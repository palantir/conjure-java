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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * ConjureIntegerList is a boxed list wrapper for the eclipse-collections IntArrayList. In eclipse-collections 12,
 * a BoxedMutableIntList will be released. Once available, ConjureIntegerList should be replaced with that.
 */
@JsonSerialize(using = ConjureIntegerList.Serializer.class)
@JsonDeserialize(using = ConjureIntegerList.Deserializer.class)
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

    static final class Serializer extends JsonSerializer<ConjureIntegerList> {
        @Override
        public void serialize(ConjureIntegerList val, JsonGenerator gen, SerializerProvider _serializer)
                throws IOException {
            int[] integers = val.delegate.toArray();
            gen.writeArray(integers, 0, integers.length);
        }
    }

    static final class Deserializer extends JsonDeserializer<ConjureIntegerList> {
        @Override
        public ConjureIntegerList deserialize(JsonParser parser, DeserializationContext _ctxt) throws IOException {
            // Avoid making a copy of the value from jackson
            int[] integers = parser.readValueAs(int[].class);
            return new ConjureIntegerList(new IntArrayList(integers));
        }
    }
}
