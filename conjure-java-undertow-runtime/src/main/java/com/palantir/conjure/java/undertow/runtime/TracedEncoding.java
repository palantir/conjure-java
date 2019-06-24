/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.tracing.Tracer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/** Encoding implementation which wraps serialization and deserialization with tracing spans. */
final class TracedEncoding implements Encoding {

    private final Encoding encoding;

    private TracedEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    static Encoding wrap(Encoding encoding) {
        return new TracedEncoding(encoding);
    }

    @Override
    public <T> Serializer<T> serializer(TypeMarker<T> type) {
        String operation = "Undertow: serialize " + toString(type) + " to " + getContentType();
        return new TracedSerializer<>(encoding.serializer(type), operation);
    }

    @Override
    public <T> Deserializer<T> deserializer(TypeMarker<T> type) {
        String operation = "Undertow: deserialize " + toString(type) + " from " + getContentType();
        return new TracedDeserializer<>(encoding.deserializer(type), operation);
    }

    /**
     * Builds a human readable type string. Class types use the classes simple name, however complex types
     * do not have this optimization because it is more complex than it's worth for now.
     */
    static String toString(TypeMarker<?> typeMarker) {
        Type type = typeMarker.getType();
        if (type instanceof Class) {
            return ((Class<?>) type).getSimpleName();
        }
        return type.toString();
    }

    @Override
    public String getContentType() {
        return encoding.getContentType();
    }

    @Override
    public boolean supportsContentType(String contentType) {
        return encoding.supportsContentType(contentType);
    }

    private static final class TracedSerializer<T> implements Serializer<T> {

        private final Serializer<T> delegate;
        private final String operation;

        TracedSerializer(Serializer<T> delegate, String operation) {
            this.delegate = delegate;
            this.operation = operation;
        }

        @Override
        public void serialize(T value, OutputStream output) throws IOException {
            Tracer.fastStartSpan(operation);
            try {
                delegate.serialize(value, output);
            } finally {
                Tracer.fastCompleteSpan();
            }
        }
    }

    private static final class TracedDeserializer<T> implements Deserializer<T> {

        private final Deserializer<T> delegate;
        private final String operation;

        TracedDeserializer(Deserializer<T> delegate, String operation) {
            this.delegate = delegate;
            this.operation = operation;
        }

        @Override
        public T deserialize(InputStream input) throws IOException {
            Tracer.fastStartSpan(operation);
            try {
                return delegate.deserialize(input);
            } finally {
                Tracer.fastCompleteSpan();
            }
        }
    }
}
