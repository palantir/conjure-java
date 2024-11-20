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

import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.exceptions.SafeIoException;
import io.undertow.io.BufferWritableOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Helper stream used in {@link ConjureBodySerDe} to make error propagation easier.
 * <p>
 * Note that this implements {@link BufferWritableOutputStream} as some consumers leverage the knowledge that the
 * {@link OutputStream} passed to {@link BinaryResponseBody#write} is an {@link io.undertow.io.UndertowOutputStream}
 * for more efficient writes with {@link ByteBuffer}.
 */
final class UnclosableOutputStream extends OutputStream implements BufferWritableOutputStream {

    private final OutputStream delegate;
    private final BufferWritableOutputStream bufferWritableDelegate;
    private boolean closeCalled;

    UnclosableOutputStream(OutputStream delegate) {
        Preconditions.checkNotNull(delegate, "Delegate is required");
        this.delegate = delegate;
        this.bufferWritableDelegate = delegate instanceof BufferWritableOutputStream bufferWritableOutputStream
                ? bufferWritableOutputStream
                : SimpleBufferWritableOutputStream.wrap(delegate);
    }

    @Override
    public void write(ByteBuffer[] buffers) throws IOException {
        bufferWritableDelegate.write(buffers);
    }

    @Override
    public void write(ByteBuffer byteBuffer) throws IOException {
        bufferWritableDelegate.write(byteBuffer);
    }

    @Override
    public void write(int value) throws IOException {
        assertOpen();
        delegate.write(value);
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        assertOpen();
        delegate.write(buffer);
    }

    @Override
    public void write(byte[] buffer, int off, int len) throws IOException {
        assertOpen();
        delegate.write(buffer, off, len);
    }

    @Override
    public void transferFrom(FileChannel source) throws IOException {
        bufferWritableDelegate.transferFrom(source);
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() {
        closeCalled = true;
    }

    private void assertOpen() throws IOException {
        if (closeCalled) {
            throw new SafeIoException("Stream is closed");
        }
    }

    @Override
    public String toString() {
        return "UnclosableOutputStream{" + delegate + '}';
    }

    private record SimpleBufferWritableOutputStream(WritableByteChannel channel) implements BufferWritableOutputStream {

        static SimpleBufferWritableOutputStream wrap(OutputStream outputStream) {
            return new SimpleBufferWritableOutputStream(Channels.newChannel(outputStream));
        }

        @Override
        public void write(ByteBuffer[] buffers) throws IOException {
            for (ByteBuffer buffer : buffers) {
                channel.write(buffer);
            }
        }

        @Override
        public void write(ByteBuffer byteBuffer) throws IOException {
            channel.write(byteBuffer);
        }

        @Override
        public void transferFrom(FileChannel source) throws IOException {
            source.transferTo(0, source.size(), channel);
        }
    }
}
