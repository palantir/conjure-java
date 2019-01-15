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

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Serializes and deserializes Java objects into the corresponding wire format. */
public interface Serializer {
    /**
     * Serializes the given object and writes the serialized representation to the given output stream.
     * Implementations must not close the stream. Inputs must never be null.
     */
    void serialize(Object value, OutputStream output) throws IOException;

    /**
     * Reads a serialized type-{@link T} object representation from the given input stream and returns the
     * corresponding object. Implementations should read the entire input stream, but must not close it.
     * Format-related deserialization errors surface as {@link IllegalArgumentException}. Inputs and outputs
     * must never be null.
     */
    <T> T deserialize(InputStream input, TypeToken<T> type) throws IOException;

    String getContentType();

    boolean supportsContentType(String contentType);
}
