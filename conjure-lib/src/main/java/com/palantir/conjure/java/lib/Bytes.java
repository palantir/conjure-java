/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

/** An immutable {@code byte[]} wrapper. */
@JsonSerialize(using = Bytes.Serializer.class)
@JsonDeserialize(using = Bytes.Deserializer.class)
public final class Bytes {
    private final byte[] safe;

    /** Constructs a new {@link Bytes} assuming the provided array is not held by any other class. */
    private Bytes(byte[] array) {
        safe = array;
    }

    /** Returns a new read-only {@link ByteBuffer} backed by this byte array. */
    public ByteBuffer asReadOnlyByteBuffer() {
        return ByteBuffer.wrap(safe).asReadOnlyBuffer();
    }

    /** Returns a new byte array containing the same content as this object's underlying {@code byte[]}. */
    public byte[] asNewByteArray() {
        byte[] unsafe = new byte[safe.length];
        System.arraycopy(safe, 0, unsafe, 0, safe.length);
        return unsafe;
    }

    /** Copies this byte array into the provided byte array beginning at offset and up to the provided length. */
    public void copyTo(byte[] destination, int offset, int length) {
        System.arraycopy(safe, 0, destination, offset, length);
    }

    /** Returns a new {@link InputStream} that reads this byte array. */
    public InputStream getInputStream() {
        return new ByteArrayInputStream(safe);
    }

    /** Returns the size of this byte array. */
    public int size() {
        return safe.length;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(safe);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
                || (obj instanceof Bytes && Arrays.equals(safe, ((Bytes) obj).safe));
    }

    @Override
    public String toString() {
        return "Bytes{size: " + safe.length + '}';
    }

    /** Constructs a new {@link Bytes} from the provided array. */
    public static Bytes from(byte[] array) {
        return from(array, 0, array.length);
    }

    /** Constructs a new {@link Bytes} read from provided offset for the provided length. */
    public static Bytes from(byte[] array, int offset, int length) {
        byte[] safe = new byte[length];
        System.arraycopy(array, offset, safe, 0, length);

        return new Bytes(safe);
    }

    /** Constructs a new {@link Bytes} read from the provided {@link ByteBuffer}. */
    public static Bytes from(ByteBuffer buffer) {
        // call duplicate to ensure we don't mutate the provided ByteBuffer while copying
        ByteBuffer local = buffer.duplicate();

        byte[] safe = new byte[local.remaining()];
        local.get(safe);

        return new Bytes(safe);
    }

    static final class Serializer extends JsonSerializer<Bytes> {
        @Override
        public void serialize(Bytes value, JsonGenerator gen, SerializerProvider _serializer) throws IOException {
            gen.writeBinary(value.safe);
        }
    }

    static final class Deserializer extends JsonDeserializer<Bytes> {
        @Override
        public Bytes deserialize(JsonParser parser, DeserializationContext _ctxt) throws IOException {
            // Avoid making a copy of the value from jackson
            return new Bytes(parser.getBinaryValue());
        }
    }
}
