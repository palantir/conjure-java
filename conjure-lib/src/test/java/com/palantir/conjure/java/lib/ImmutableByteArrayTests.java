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

import java.nio.ByteBuffer;
import org.junit.Test;

public final class ImmutableByteArrayTests {
    @Test
    public void testConstructionCopiesInputArray() {
        byte[] input = new byte[]{0};
        ImmutableByteArray immutable = ImmutableByteArray.from(input);

        assertThat(immutable.getBytes()[0]).isEqualTo((byte) 0);

        input[0] = 1;

        assertThat(immutable.getBytes()[0]).isEqualTo((byte) 0);
    }

    @Test
    public void testConstructionCopiesArrayWithRange() {
        byte[] input = new byte[]{0, 1, 2};
        ImmutableByteArray immutable = ImmutableByteArray.from(input, 1, 2);
        assertThat(immutable.getBytes()).isEqualTo(new byte[]{1, 2});
    }

    @Test
    public void testConstructionCopiesArray_badOffset() {
        byte[] input = new byte[0];
        assertThatThrownBy(() -> ImmutableByteArray.from(input, 1, 0))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testConstructionCopiesArray_badLength() {
        byte[] input = new byte[0];
        assertThatThrownBy(() -> ImmutableByteArray.from(input, 0, 1))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testConstructionCopiesArray_badRange() {
        byte[] input = new byte[10];
        assertThatThrownBy(() -> ImmutableByteArray.from(input, 5, 10))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testConstructionCopiesByteBuffer() {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[]{0, 1, 2});
        ImmutableByteArray immutable = ImmutableByteArray.from(buffer);

        assertThat(immutable.getByteBuffer()).isEqualTo(buffer.asReadOnlyBuffer());

        buffer.put((byte) 1);
        buffer.rewind();

        assertThat(immutable.getByteBuffer()).isNotEqualTo(buffer.asReadOnlyBuffer());
    }

    @Test
    public void testGetBytes_returnsNewByteArray() {
        ImmutableByteArray immutable = ImmutableByteArray.from(new byte[] {0});

        byte[] output = immutable.getBytes();
        assertThat(output[0]).isEqualTo((byte) 0);
        output[0] = 1;

        assertThat(immutable.getBytes()[0]).isEqualTo((byte) 0);
    }

    @Test
    public void testGetByteBuffer_returnsReadOnlyByteBuffer() {
        ImmutableByteArray immutable = ImmutableByteArray.from(new byte[] {0});

        ByteBuffer output = immutable.getByteBuffer();
        assertThat(output.isReadOnly()).isTrue();
    }

    @Test
    public void testGetByteBuffer_returnsNewByteBuffer() {
        ImmutableByteArray immutable = ImmutableByteArray.from(new byte[] {0});

        ByteBuffer output = immutable.getByteBuffer();
        assertThat(output.get()).isEqualTo((byte) 0);
        assertThat(output.remaining()).isEqualTo(0);

        assertThat(immutable.getByteBuffer().remaining()).isEqualTo(1);
    }

    @Test
    public void testGetSize() {
        assertThat(ImmutableByteArray.from(new byte[0]).size()).isEqualTo(0);
        assertThat(ImmutableByteArray.from(new byte[10]).size()).isEqualTo(10);
    }

    @Test
    public void testCopy() {
        byte[] input = new byte[]{0, 1, 2};
        ImmutableByteArray immutable = ImmutableByteArray.from(input);

        byte[] test = new byte[input.length];
        immutable.copy(test, 0, test.length);

        assertThat(test).isEqualTo(input);
    }

    @Test
    public void testCopy_badOffset() {
        byte[] input = new byte[0];
        ImmutableByteArray immutable = ImmutableByteArray.from(input);

        byte[] test = new byte[0];
        assertThatThrownBy(() -> immutable.copy(test, 1, test.length))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testCopy_badLength() {
        byte[] input = new byte[0];
        ImmutableByteArray immutable = ImmutableByteArray.from(input);

        byte[] test = new byte[0];
        assertThatThrownBy(() -> immutable.copy(test, 0, 1))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }

    @Test
    public void testCopy_badRange() {
        byte[] input = new byte[10];
        ImmutableByteArray immutable = ImmutableByteArray.from(input);

        byte[] test = new byte[5];
        assertThatThrownBy(() -> immutable.copy(test, 0, 10))
                .isInstanceOf(ArrayIndexOutOfBoundsException.class);
    }
}
