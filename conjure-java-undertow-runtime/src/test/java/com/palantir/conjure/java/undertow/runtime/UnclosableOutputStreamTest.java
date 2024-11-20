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

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.undertow.io.BufferWritableOutputStream;
import io.undertow.io.UndertowOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UnclosableOutputStreamTest {

    @Test
    void testClosure() throws IOException {
        OutputStream delegate = Mockito.mock(OutputStream.class);
        OutputStream stream = UnclosableOutputStreams.wrap(delegate);
        stream.write(1);
        Mockito.verify(delegate).write(1);
        // Close the unclosable stream
        stream.close();
        // Closure mustn't be passed through
        Mockito.verify(delegate, Mockito.never()).close();
        // Writes to the wrapper must fail because that "view" is closed
        assertThatThrownBy(() -> stream.write(1)).isInstanceOf(IOException.class);
        // Flush may still be passed through
        stream.flush();
        Mockito.verify(delegate).flush();
        Mockito.verifyNoMoreInteractions(delegate);
    }

    @Test
    void testBufferWriteableClosure() throws IOException {
        OutputStream delegate = Mockito.mock(UndertowOutputStream.class);
        OutputStream stream = UnclosableOutputStreams.wrap(delegate);
        stream.write(1);
        Mockito.verify(delegate).write(1);
        // Close the unclosable stream
        stream.close();
        // Closure mustn't be passed through
        Mockito.verify(delegate, Mockito.never()).close();
        // Writes to the wrapper must fail because that "view" is closed
        assertThat(stream)
                .isInstanceOfSatisfying(BufferWritableOutputStream.class, bufferWritable -> assertThatThrownBy(
                                () -> bufferWritable.write(ByteBuffer.wrap(new byte[1])))
                        .isInstanceOf(IOException.class));

        // Flush may still be passed through
        stream.flush();
        Mockito.verify(delegate).flush();
        Mockito.verifyNoMoreInteractions(delegate);
    }
}
