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
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * ConjureDoubleList is a boxed list wrapper for the eclipse-collections DoubleArrayList. In eclipse-collections 12,
 * a BoxedMutableDoubleList will be released. Once available, ConjureDoubleList should be replaced with that.
 */
@JsonSerialize(using = ConjureDoubleList.Serializer.class)
@JsonDeserialize(using = ConjureDoubleList.Deserializer.class)
final class ConjureDoubleList extends AbstractList<Double> implements RandomAccess {
    private final DoubleArrayList delegate;

    ConjureDoubleList(DoubleArrayList delegate) {
        this.delegate = delegate;
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
        return delegate.get(index);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Double> collection) {
        double[] target = new double[collection.size()];
        Iterate.forEachWithIndex(collection, (each, parameter) -> target[parameter] = each.doubleValue());
        return delegate.addAllAtIndex(index, target);
    }

    @Override
    public Double remove(int index) {
        return delegate.removeAtIndex(index);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public Double set(int index, Double element) {
        return delegate.set(index, element);
    }

    static final class Serializer extends JsonSerializer<ConjureDoubleList> {
        @Override
        public void serialize(ConjureDoubleList val, JsonGenerator gen, SerializerProvider _serializer)
                throws IOException {
            double[] doubles = val.delegate.toArray();
            gen.writeArray(doubles, 0, doubles.length);
        }
    }

    static final class Deserializer extends JsonDeserializer<ConjureDoubleList> {
        @Override
        public ConjureDoubleList deserialize(JsonParser parser, DeserializationContext _ctxt) throws IOException {
            // Avoid making a copy of the value from jackson
            double[] doubles = parser.readValueAs(double[].class);
            return new ConjureDoubleList(new DoubleArrayList(doubles));
        }
    }
}
