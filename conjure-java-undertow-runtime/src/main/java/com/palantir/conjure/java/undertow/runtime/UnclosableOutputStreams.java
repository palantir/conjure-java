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

import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.exceptions.SafeIoException;
import io.undertow.io.BufferWritableOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/** Helper stream used in {@link ConjureBodySerDe} to make error propagation easier. */
final class UnclosableOutputStreams {

    /**
     * Wraps the provided {@link OutputStream} such that {@link OutputStream#close()} is a no-op.
     * This implementation ensures that streams like {@code UndertowOutputStream} are wrapped in a
     * way that preserves the {@link BufferWritableOutputStream} interface.
     */
    static OutputStream wrap(OutputStream delegate) {
        if (delegate instanceof BufferWritableOutputStream) {
            return new UnclosableBufferWritableOutputStream(delegate, (BufferWritableOutputStream) delegate);
        } else {
            return new UnclosableOutputStream(delegate);
        }
    }

    private static class UnclosableOutputStream extends OutputStream {

        private final OutputStream delegate;
        private boolean closeCalled;

        UnclosableOutputStream(OutputStream delegate) {
            this.delegate = Preconditions.checkNotNull(delegate, "Delegate is required");
        }

        @Override
        public final void write(int value) throws IOException {
            assertOpen();
            delegate.write(value);
        }

        @Override
        public final void write(byte[] buffer) throws IOException {
            assertOpen();
            delegate.write(buffer);
        }

        @Override
        public final void write(byte[] buffer, int off, int len) throws IOException {
            assertOpen();
            delegate.write(buffer, off, len);
        }

        @Override
        public final void flush() throws IOException {
            delegate.flush();
        }

        @Override
        public final void close() {
            closeCalled = true;
        }

        /** Asserts {@link #close()} has not been called. */
        protected final void assertOpen() throws IOException {
            if (closeCalled) {
                throw new SafeIoException("Stream is closed");
            }
        }

        @Override
        public final String toString() {
            return getClass().getSimpleName() + '{' + delegate + '}';
        }
    }

    private static final class UnclosableBufferWritableOutputStream extends UnclosableOutputStream
            implements BufferWritableOutputStream {
        private final BufferWritableOutputStream bufferWritable;

        UnclosableBufferWritableOutputStream(OutputStream outputStream, BufferWritableOutputStream bufferWritable) {
            super(outputStream);
            this.bufferWritable = bufferWritable;
        }

        @Override
        public void write(ByteBuffer[] buffers) throws IOException {
            assertOpen();
            bufferWritable.write(buffers);
        }

        @Override
        public void write(ByteBuffer byteBuffer) throws IOException {
            assertOpen();
            bufferWritable.write(byteBuffer);
        }

        @Override
        public void transferFrom(FileChannel source) throws IOException {
            assertOpen();
            bufferWritable.transferFrom(source);
        }
    }

    private UnclosableOutputStreams() {}
}
