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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public final class BytesTests {
    @Test
    public void testConstructionCopiesInputArray() {
        byte[] input = new byte[]{0};
        Bytes immutable = Bytes.from(input);

        assertThat(immutable.asNewByteArray()[0]).isEqualTo((byte) 0);

        // mutate the input
        input[0] = 1;

        assertThat(immutable.asNewByteArray()[0]).isEqualTo((byte) 0);
    }

    @Test
    public void testConstructionCopiesArrayWithRange() {
        byte[] input = new byte[]{0, 1, 2, 3};
        Bytes immutable = Bytes.from(input, 1, 2);
        assertThat(immutable.asNewByteArray()).isEqualTo(new byte[]{1, 2});
    }

    @Test
    public void testConstructionCopiesArray_badOffset() {
        byte[] input = new byte[0];
        assertThatThrownBy(() -> Bytes.from(input, 1, 0))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testConstructionCopiesArray_badLength() {
        byte[] input = new byte[0];
        assertThatThrownBy(() -> Bytes.from(input, 0, 1))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testConstructionCopiesArray_badRange() {
        byte[] input = new byte[10];
        assertThatThrownBy(() -> Bytes.from(input, 5, 10))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testConstructionCopiesByteBuffer() {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0, 1, 2});
        Bytes immutable = Bytes.from(buffer);

        assertThat(immutable.asReadOnlyByteBuffer()).isEqualTo(buffer.asReadOnlyBuffer());

        buffer.put((byte) 1);
        buffer.rewind();

        assertThat(immutable.asReadOnlyByteBuffer()).isNotEqualTo(buffer.asReadOnlyBuffer());
    }

    @Test
    public void testGetBytes_returnsNewByteArray() {
        Bytes immutable = Bytes.from(new byte[] {0});

        byte[] output = immutable.asNewByteArray();
        assertThat(output[0]).isEqualTo((byte) 0);
        output[0] = 1;

        assertThat(immutable.asNewByteArray()[0]).isEqualTo((byte) 0);
    }

    @Test
    public void testGetByteBuffer_returnsReadOnlyByteBuffer() {
        Bytes immutable = Bytes.from(new byte[] {0});

        ByteBuffer output = immutable.asReadOnlyByteBuffer();
        assertThat(output.isReadOnly()).isTrue();
    }

    @Test
    public void testGetByteBuffer_returnsNewByteBuffer() {
        Bytes immutable = Bytes.from(new byte[] {0});

        ByteBuffer output = immutable.asReadOnlyByteBuffer();
        assertThat(output.get()).isEqualTo((byte) 0);
        assertThat(output.remaining()).isEqualTo(0);

        assertThat(immutable.asReadOnlyByteBuffer().remaining()).isEqualTo(1);
    }

    @Test
    public void testGetSize() {
        assertThat(Bytes.from(new byte[0]).size()).isEqualTo(0);
        assertThat(Bytes.from(new byte[10]).size()).isEqualTo(10);
    }

    @Test
    public void testCopy() {
        byte[] input = new byte[]{0, 1, 2};
        Bytes immutable = Bytes.from(input);

        byte[] test = new byte[input.length];
        immutable.copyTo(test, 0, test.length);

        assertThat(test).isEqualTo(input);
    }

    @Test
    public void testCopy_badOffset() {
        byte[] input = new byte[0];
        Bytes immutable = Bytes.from(input);

        byte[] test = new byte[0];
        assertThatThrownBy(() -> immutable.copyTo(test, 1, test.length))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testCopy_badLength() {
        byte[] input = new byte[0];
        Bytes immutable = Bytes.from(input);

        byte[] test = new byte[0];
        assertThatThrownBy(() -> immutable.copyTo(test, 0, 1))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testCopy_badRange() {
        byte[] input = new byte[10];
        Bytes immutable = Bytes.from(input);

        byte[] test = new byte[5];
        assertThatThrownBy(() -> immutable.copyTo(test, 0, 10))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testSerDe() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        Bytes original = Bytes.from("test".getBytes(StandardCharsets.UTF_8));
        String base64String = "\"dGVzdA==\"";

        assertThat(mapper.writeValueAsString(original)).isEqualTo(base64String);
        assertThat(mapper.readValue(base64String, Bytes.class)).isEqualTo(original);
    }

    @Test
    public void testSerDe_cannotMapEmptyArray() {
        ObjectMapper mapper = new ObjectMapper();
        assertThatThrownBy(() -> mapper.readValue("[]", Bytes.class))
                .isInstanceOf(JsonParseException.class);
    }
}
