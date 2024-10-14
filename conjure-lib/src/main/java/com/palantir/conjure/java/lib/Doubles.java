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

package com.palantir.conjure.java.lib;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.palantir.conjure.java.lib.internal.ImmutableList;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/** An immutable {@code double[]} wrapper. */
@JsonSerialize(using = Doubles.Serializer.class)
@JsonDeserialize(using = Doubles.Deserializer.class)
public final class Doubles extends ImmutableList<Double> {
    private final double[] safe;
    private int hashCode;

    /** Constructs a new {@link Doubles} assuming the provided array is not held by any other class. */
    private Doubles(double[] array) {
        safe = array;
    }

    /** Returns a new double array containing the same content as this object's underlying {@code double[]}. */
    public double[] asNewDoubleArray() {
        double[] unsafe = new double[safe.length];
        System.arraycopy(safe, 0, unsafe, 0, safe.length);
        return unsafe;
    }

    @Override
    public Double get(int index) {
        Objects.checkIndex(index, safe.length);
        return safe[index];
    }

    /** Returns the size of this double array. */
    @Override
    public int size() {
        return safe.length;
    }

    @Override
    public int hashCode() {
        // same implementation as java.lang.String except Arrays.hashCode(new double[0]) == 1 so no length check.
        int hash = hashCode;
        if (hash == 0) {
            hash = Arrays.hashCode(safe);
            hashCode = hash;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (obj instanceof Doubles && Arrays.equals(safe, ((Doubles) obj).safe));
    }

    @Override
    public String toString() {
        return "Doubles{size: " + safe.length + '}';
    }

    /** Constructs a new {@link Doubles} from the provided array. */
    public static Doubles from(double[] array) {
        double[] safe = new double[array.length];
        System.arraycopy(array, 0, safe, 0, array.length);

        return new Doubles(safe);
    }

    static final class Serializer extends JsonSerializer<Doubles> {
        @Override
        public void serialize(Doubles value, JsonGenerator gen, SerializerProvider _serializer) throws IOException {
            gen.writeArray(value.safe, 0, value.safe.length);
        }
    }

    static final class Deserializer extends JsonDeserializer<Doubles> {
        @Override
        public Doubles deserialize(JsonParser parser, DeserializationContext _ctxt) throws IOException {
            // Avoid making a copy of the value from jackson
            return new Doubles(parser.readValueAs(double[].class));
        }
    }
}
