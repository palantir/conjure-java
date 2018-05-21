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

package com.palantir.conjure.java;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.product.BinaryExample;
import java.nio.ByteBuffer;
import org.junit.Test;

public class BinaryExampleTest {

    @Test
    public void cannotMutateForSubsequentGets() {
        ByteBuffer buffer = ByteBuffer.allocate(1).put((byte) 1);
        buffer.rewind();
        BinaryExample binaryExample = BinaryExample.of(buffer);

        ByteBuffer firstGet = binaryExample.getBinary();
        ByteBuffer secondGet = binaryExample.getBinary();

        firstGet.get();
        assertThat(secondGet.remaining()).isNotZero();
    }

    @Test
    public void cannotMutateAfterSetting() {
        byte value = (byte) 1;
        ByteBuffer buffer = ByteBuffer.allocate(1).put(value);
        buffer.rewind();
        BinaryExample binaryExample = BinaryExample.of(buffer);
        buffer.put((byte) (value + 1));
        buffer.rewind();
        assertThat(binaryExample.getBinary().get()).isEqualTo(value);
    }
}
