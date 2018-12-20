/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

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
