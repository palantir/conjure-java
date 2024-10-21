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
import com.palantir.conjure.java.lib.SafeLong;
import java.io.IOException;
import java.util.AbstractList;
import java.util.Collection;
import java.util.RandomAccess;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * ConjureSafeLongList is a boxed list wrapper for the eclipse-collections LongArrayList. This handles boxing/unboxing
 * with SafeLongs.
 */
@JsonSerialize(using = ConjureSafeLongList.Serializer.class)
@JsonDeserialize(using = ConjureSafeLongList.Deserializer.class)
final class ConjureSafeLongList extends AbstractList<SafeLong> implements RandomAccess {
    private final LongArrayList delegate;

    ConjureSafeLongList(LongArrayList delegate) {
        this.delegate = delegate;
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
        return SafeLong.of(delegate.get(index));
    }

    @Override
    public boolean addAll(int index, Collection<? extends SafeLong> collection) {
        long[] target = new long[collection.size()];
        Iterate.forEachWithIndex(collection, (each, parameter) -> target[parameter] = each.longValue());
        return delegate.addAllAtIndex(index, target);
    }

    @Override
    public SafeLong remove(int index) {
        return SafeLong.of(delegate.removeAtIndex(index));
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public SafeLong set(int index, SafeLong element) {
        return SafeLong.of(delegate.set(index, element.longValue()));
    }

    static final class Serializer extends JsonSerializer<ConjureSafeLongList> {
        @Override
        public void serialize(ConjureSafeLongList val, JsonGenerator gen, SerializerProvider _serializer)
                throws IOException {
            // This is safe to serialize without checking because ConjureSafeLongList
            // can only store SafeLong's
            long[] longs = val.delegate.toArray();
            gen.writeArray(longs, 0, longs.length);
        }
    }

    static final class Deserializer extends JsonDeserializer<ConjureSafeLongList> {
        @Override
        public ConjureSafeLongList deserialize(JsonParser parser, DeserializationContext _ctxt) throws IOException {
            // Avoid making a copy of the value from jackson
            long[] longs = parser.readValueAs(long[].class);
            return new ConjureSafeLongList(new LongArrayList(longs));
        }
    }
}
