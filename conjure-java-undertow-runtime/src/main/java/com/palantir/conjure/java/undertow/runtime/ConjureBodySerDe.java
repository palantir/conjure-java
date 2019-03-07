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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ObjectArrays;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.BodySerDe;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/** Package private internal API. */
final class ConjureBodySerDe implements BodySerDe {

    private static final String BINARY_CONTENT_TYPE = "application/octet-stream";

    private final List<Encoding> encodings;

    /**
     * Selects the first (based on input order) of the provided encodings that
     * {@link Encoding#supportsContentType supports} the serialization format {@link Headers#ACCEPT accepted}
     * by a given request, or the first serializer if no such serializer can be found.
     */
    ConjureBodySerDe(List<Encoding> encodings) {
        // Defensive copy
        this.encodings = ImmutableList.copyOf(encodings);
        Preconditions.checkArgument(encodings.size() > 0, "At least one Encoding is required");
    }

    ConjureBodySerDe(Encoding defaultSerializer, Encoding... encodings) {
        this(Arrays.asList(ObjectArrays.concat(defaultSerializer, encodings)));
    }

    @Override
    public <T> Serializer<T> serializer(TypeToken<T> token) {
        return new EncodingSerializerRegistry<>(encodings, token);
    }

    @Override
    public <T> Deserializer<T> deserializer(TypeToken<T> token) {
        return new EncodingDeserializerRegistry<>(encodings, token);
    }

    @Override
    public void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
        Preconditions.checkNotNull(value, "A BinaryResponseBody value is required");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, BINARY_CONTENT_TYPE);
        value.write(exchange.getOutputStream());
    }

    @Override
    public InputStream deserializeInputStream(HttpServerExchange exchange) {
        String contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
        if (contentType == null) {
            throw new SafeIllegalArgumentException("Request is missing Content-Type header");
        }
        if (!contentType.startsWith(BINARY_CONTENT_TYPE)) {
            throw new SafeIllegalArgumentException("Unsupported Content-Type",
                    SafeArg.of("Content-Type", contentType));
        }
        return exchange.getInputStream();
    }

    private static final class EncodingSerializerRegistry<T> implements Serializer<T> {

        private final EncodingSerializerContainer<T> defaultEncoding;
        private final List<EncodingSerializerContainer<T>> encodings;

        EncodingSerializerRegistry(List<Encoding> encodings, TypeToken<T> token) {
            this.encodings = encodings.stream()
                    .map(encoding -> new EncodingSerializerContainer<>(encoding, token))
                    .collect(ImmutableList.toImmutableList());
            this.defaultEncoding = this.encodings.get(0);
        }

        @Override
        public void serialize(T value, HttpServerExchange exchange) throws IOException {
            Preconditions.checkNotNull(value, "cannot serialize null value");
            EncodingSerializerContainer<T> container = getResponseSerializer(exchange);
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, container.encoding.getContentType());
            container.serializer.serialize(value, exchange.getOutputStream());
        }

        /** Returns the {@link EncodingSerializerContainer} to use for the exchange response. */
        @SuppressWarnings("ForLoopReplaceableByForEach") // performance sensitive code avoids iterator allocation
        EncodingSerializerContainer<T> getResponseSerializer(HttpServerExchange exchange) {
            HeaderValues acceptValues = exchange.getRequestHeaders().get(Headers.ACCEPT);
            if (acceptValues != null) {
                // This implementation prefers the client "Accept" order
                for (int i = 0; i < acceptValues.size(); i++) {
                    String acceptValue = acceptValues.get(i);
                    for (int j = 0; j < encodings.size(); j++) {
                        EncodingSerializerContainer<T> container = encodings.get(j);
                        if (container.encoding.supportsContentType(acceptValue)) {
                            return container;
                        }
                    }
                }
            }
            // Fall back to the default
            return defaultEncoding;
        }
    }

    private static final class EncodingSerializerContainer<T> {

        private final Encoding encoding;
        private final Encoding.Serializer<T> serializer;

        EncodingSerializerContainer(Encoding encoding, TypeToken<T> token) {
            this.encoding = encoding;
            this.serializer = encoding.serializer(token);
        }
    }

    private static final class EncodingDeserializerRegistry<T> implements Deserializer<T> {

        private final List<EncodingDeserializerContainer<T>> encodings;

        EncodingDeserializerRegistry(List<Encoding> encodings, TypeToken<T> token) {
            this.encodings = encodings.stream()
                    .map(encoding -> new EncodingDeserializerContainer<>(encoding, token))
                    .collect(ImmutableList.toImmutableList());
        }

        @Override
        public T deserialize(HttpServerExchange exchange) throws IOException {
            EncodingDeserializerContainer<T> container = getRequestDeserializer(exchange);
            return container.deserializer.deserialize(exchange.getInputStream());
        }

        /** Returns the {@link EncodingDeserializerContainer} to use to deserialize the request body. */
        @SuppressWarnings("ForLoopReplaceableByForEach") // performance sensitive code avoids iterator allocation
        EncodingDeserializerContainer<T> getRequestDeserializer(HttpServerExchange exchange) {
            String contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
            if (contentType == null) {
                throw new SafeIllegalArgumentException("Request is missing Content-Type header");
            }
            for (int i = 0; i < encodings.size(); i++) {
                EncodingDeserializerContainer<T> container = encodings.get(i);
                if (container.encoding.supportsContentType(contentType)) {
                    return container;
                }
            }
            throw FrameworkException.unsupportedMediaType("Unsupported Content-Type",
                    SafeArg.of("Content-Type", contentType));
        }
    }

    private static final class EncodingDeserializerContainer<T> {

        private final Encoding encoding;
        private final Encoding.Deserializer<T> deserializer;

        EncodingDeserializerContainer(Encoding encoding, TypeToken<T> token) {
            this.encoding = encoding;
            this.deserializer = encoding.deserializer(token);
        }
    }
}
