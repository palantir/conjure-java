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

import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.tracing.Tracer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/** Encoding implementation which wraps serialization and deserialization with tracing spans. */
final class TracedEncoding implements Encoding {

    static final String DESERIALIZE_OPERATION = "Undertow: deserialize";
    static final String SERIALIZE_OPERATION = "Undertow: serialize";

    private final Encoding encoding;

    private TracedEncoding(Encoding encoding) {
        this.encoding = encoding;
    }

    static Encoding wrap(Encoding encoding) {
        return new TracedEncoding(encoding);
    }

    @Override
    public <T> Serializer<T> serializer(TypeMarker<T> type) {
        return new TracedSerializer<>(encoding.serializer(type), SERIALIZE_OPERATION, getTags(type));
    }

    @Override
    public <T> Serializer<T> serializer(TypeMarker<T> type, Endpoint endpoint) {
        return new TracedSerializer<>(encoding.serializer(type, endpoint), SERIALIZE_OPERATION, getTags(type));
    }

    @Override
    public <T> Deserializer<T> deserializer(TypeMarker<T> type) {
        return new TracedDeserializer<>(encoding.deserializer(type), DESERIALIZE_OPERATION, getTags(type));
    }

    @Override
    public <T> Deserializer<T> deserializer(TypeMarker<T> type, Endpoint endpoint) {
        return new TracedDeserializer<>(encoding.deserializer(type, endpoint), DESERIALIZE_OPERATION, getTags(type));
    }

    /**
     * Builds a human readable type string. Class types use the classes simple name, however complex types do not have
     * this optimization because it is more complex than it's worth for now.
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

    private <T> ImmutableMap<String, String> getTags(TypeMarker<T> type) {
        return ImmutableMap.of("type", toString(type), "contentType", getContentType());
    }

    private static final class TracedSerializer<T> implements Serializer<T> {

        private final Serializer<T> delegate;
        private final String operation;
        private final ImmutableMap<String, String> tags;

        TracedSerializer(Serializer<T> delegate, String operation, ImmutableMap<String, String> tags) {
            this.delegate = delegate;
            this.operation = operation;
            this.tags = tags;
        }

        @Override
        public void serialize(T value, OutputStream output) throws IOException {
            Tracer.fastStartSpan(operation);
            try {
                delegate.serialize(value, output);
            } finally {
                Tracer.fastCompleteSpan(tags);
            }
        }
    }

    private static final class TracedDeserializer<T> implements Deserializer<T> {

        private final Deserializer<T> delegate;
        private final String operation;
        private final ImmutableMap<String, String> tags;

        TracedDeserializer(Deserializer<T> delegate, String operation, ImmutableMap<String, String> tags) {
            this.delegate = delegate;
            this.operation = operation;
            this.tags = tags;
        }

        @Override
        public T deserialize(InputStream input) throws IOException {
            Tracer.fastStartSpan(operation);
            try {
                return delegate.deserialize(input);
            } finally {
                Tracer.fastCompleteSpan(tags);
            }
        }
    }
}
