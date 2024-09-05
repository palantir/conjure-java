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
import java.io.IOException;
import java.io.OutputStream;

/** Helper stream used in {@link ConjureBodySerDe} to make error propagation easier. */
final class UnclosableOutputStream extends OutputStream {

    private final OutputStream delegate;
    private boolean closeCalled;

    UnclosableOutputStream(OutputStream delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "Delegate is required");
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
        return "CloseShieldingOutputStream{" + delegate + '}';
    }
}
