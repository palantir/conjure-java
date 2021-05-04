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

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.BodySerDe;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.tracing.CloseableTracer;
import com.palantir.tracing.TagTranslator;
import com.palantir.tracing.Tracer;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.Protocols;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;

/** Package private internal API. */
final class ConjureBodySerDe implements BodySerDe {

    private static final Logger log = LoggerFactory.getLogger(ConjureBodySerDe.class);
    private static final String BINARY_CONTENT_TYPE = "application/octet-stream";
    private static final Splitter ACCEPT_VALUE_SPLITTER =
            Splitter.on(',').trimResults().omitEmptyStrings();

    private final List<Encoding> encodings;

    /**
     * Selects the first (based on input order) of the provided encodings that
     * {@link Encoding#supportsContentType supports} the serialization format {@link Headers#ACCEPT accepted} by a given
     * request, or the first serializer if no such serializer can be found.
     */
    ConjureBodySerDe(List<Encoding> encodings) {
        // Defensive copy
        this.encodings =
                encodings.stream().map(LazilyInitializedEncoding::new).collect(ImmutableList.toImmutableList());
        Preconditions.checkArgument(encodings.size() > 0, "At least one Encoding is required");
    }

    @Override
    public <T> Serializer<T> serializer(TypeMarker<T> token) {
        return new EncodingSerializerRegistry<>(encodings, token);
    }

    @Override
    public <T> Deserializer<T> deserializer(TypeMarker<T> token) {
        return new EncodingDeserializerRegistry<>(encodings, token);
    }

    @Override
    public void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
        Preconditions.checkNotNull(value, "A BinaryResponseBody value is required");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, BINARY_CONTENT_TYPE);
        Tracer.fastStartSpan(TracedEncoding.SERIALIZE_OPERATION);
        try {
            value.write(exchange.getOutputStream());
        } finally {
            Tracer.fastCompleteSpan(SerializeBinaryTagTranslator.INSTANCE, SerializeBinaryTagTranslator.INSTANCE);
        }
    }

    @Override
    public InputStream deserializeInputStream(HttpServerExchange exchange) {
        String contentType = getContentType(exchange);
        if (!contentType.startsWith(BINARY_CONTENT_TYPE)) {
            throw FrameworkException.unsupportedMediaType(
                    "Unsupported Content-Type", SafeArg.of("Content-Type", contentType));
        }
        return exchange.getInputStream();
    }

    private static final class EncodingSerializerRegistry<T> implements Serializer<T> {

        private final EncodingSerializerContainer<T> defaultEncoding;
        private final List<EncodingSerializerContainer<T>> encodings;

        EncodingSerializerRegistry(List<Encoding> encodings, TypeMarker<T> token) {
            this.encodings = encodings.stream()
                    .map(encoding -> new EncodingSerializerContainer<>(encoding, token))
                    .collect(ImmutableList.toImmutableList());
            this.defaultEncoding = this.encodings.get(0);
        }

        @Override
        public void serialize(T value, HttpServerExchange exchange) throws IOException {
            Preconditions.checkNotNull(value, "cannot serialize null value");
            safelyDrainRequestBody(exchange);
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
                    for (String acceptValue : ACCEPT_VALUE_SPLITTER.split(acceptValues.get(i))) {
                        for (int j = 0; j < encodings.size(); j++) {
                            EncodingSerializerContainer<T> container = encodings.get(j);
                            if (container.encoding.supportsContentType(acceptValue)) {
                                return container;
                            }
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

        EncodingSerializerContainer(Encoding encoding, TypeMarker<T> token) {
            this.encoding = encoding;
            this.serializer = TracedEncoding.wrap(encoding).serializer(token);
        }
    }

    private static final class EncodingDeserializerRegistry<T> implements Deserializer<T> {

        private final List<EncodingDeserializerContainer<T>> encodings;
        private final boolean optionalType;
        private final TypeMarker<T> marker;

        EncodingDeserializerRegistry(List<Encoding> encodings, TypeMarker<T> token) {
            this.encodings = encodings.stream()
                    .map(encoding -> new EncodingDeserializerContainer<>(encoding, token))
                    .collect(ImmutableList.toImmutableList());
            this.optionalType = TypeMarkers.isOptional(token);
            this.marker = token;
        }

        @Override
        public T deserialize(HttpServerExchange exchange) throws IOException {
            // If this deserializer is built for an optional root type, Optional<?>, OptionalInt, etc,
            // and the incoming request body might be empty (does not have a content-length greater than zero)
            // we must map from an empty request body to an empty optional.
            // See https://github.com/palantir/conjure/blob/master/docs/spec/wire.md#23-body-parameter
            if (optionalType && maybeEmptyBody(exchange)) {
                return deserializeOptional(exchange);
            }
            return deserializeInternal(exchange, exchange.getInputStream());
        }

        private T deserializeOptional(HttpServerExchange exchange) throws IOException {
            // If the first byte of the request stream is -1 (EOF) we return the empty optional type.
            // We cannot provide the empty stream to jackson because there is no content for jackson
            // to deserialize.
            PushbackInputStream requestStream = new PushbackInputStream(exchange.getInputStream(), 1);
            int firstByte = requestStream.read();
            if (firstByte == -1) {
                return TypeMarkers.getEmptyOptional(marker);
            }
            // Otherwise reset the request stream and deserialize normally.
            requestStream.unread(firstByte);
            return deserializeInternal(exchange, requestStream);
        }

        private T deserializeInternal(HttpServerExchange exchange, InputStream requestStream) throws IOException {
            EncodingDeserializerContainer<T> container = getRequestDeserializer(exchange);
            return container.deserializer.deserialize(requestStream);
        }

        private static boolean maybeEmptyBody(HttpServerExchange exchange) {
            // Content-Length maybe null if "Transfer-Encoding: chunked" is sent with a full body.
            String contentLength = exchange.getRequestHeaders().getFirst(Headers.CONTENT_LENGTH);
            return contentLength == null || "0".equals(contentLength);
        }

        /** Returns the {@link EncodingDeserializerContainer} to use to deserialize the request body. */
        @SuppressWarnings("ForLoopReplaceableByForEach") // performance sensitive code avoids iterator allocation
        EncodingDeserializerContainer<T> getRequestDeserializer(HttpServerExchange exchange) {
            String contentType = getContentType(exchange);
            for (int i = 0; i < encodings.size(); i++) {
                EncodingDeserializerContainer<T> container = encodings.get(i);
                if (container.encoding.supportsContentType(contentType)) {
                    return container;
                }
            }
            throw FrameworkException.unsupportedMediaType(
                    "Unsupported Content-Type", SafeArg.of("Content-Type", contentType));
        }
    }

    private static final class EncodingDeserializerContainer<T> {

        private final Encoding encoding;
        private final Encoding.Deserializer<T> deserializer;

        EncodingDeserializerContainer(Encoding encoding, TypeMarker<T> token) {
            this.encoding = encoding;
            this.deserializer = TracedEncoding.wrap(encoding).deserializer(token);
        }
    }

    /**
     * Ensure the client isn't blocked sending additional data. It's very uncommon for this to be necessary, in most
     * cases exceptional responses are far below the 16k buffer threshold, not even considering socket buffers.
     */
    private static void safelyDrainRequestBody(HttpServerExchange exchange) {
        // No need to impact http/2 which supports out-of-band responses.
        if ((Protocols.HTTP_1_1.equals(exchange.getProtocol()) || Protocols.HTTP_1_0.equals(exchange.getProtocol()))
                && !exchange.isRequestComplete()) {
            try (CloseableTracer ignored = CloseableTracer.startSpan("Undertow: drain request body")) {
                IoUtils.safeClose(exchange.getInputStream());
            }
        }
    }

    private enum SerializeBinaryTagTranslator implements TagTranslator<Object> {
        INSTANCE;

        @Override
        public <T> void translate(TagAdapter<T> adapter, T target, Object _data) {
            adapter.tag(target, "type", "BinaryResponseBody");
            adapter.tag(target, "contentType", BINARY_CONTENT_TYPE);
        }
    }

    /**
     * Gets the request {@code Content-Type} header if exactly one value exists, otherwise logs
     * a warning. This notifies us in the unexpected case when multiple
     * content-type headers are incorrectly sent to the server, it's not clear which should
     * be used.
     */
    private static String getContentType(HttpServerExchange exchange) {
        HeaderValues contentTypeValues = exchange.getRequestHeaders().get(Headers.CONTENT_TYPE);
        if (contentTypeValues == null || contentTypeValues.isEmpty()) {
            throw new SafeIllegalArgumentException("Request is missing Content-Type header");
        } else if (contentTypeValues.size() != 1) {
            log.warn(
                    "Request has too many Content-Type headers",
                    SafeArg.of("contentTypes", ImmutableList.copyOf(contentTypeValues)));
            return contentTypeValues.getFirst();
        }
        return contentTypeValues.get(0);
    }
}
