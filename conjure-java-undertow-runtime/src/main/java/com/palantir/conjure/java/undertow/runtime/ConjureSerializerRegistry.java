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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers;
import com.google.common.collect.ObjectArrays;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.SerializerRegistry;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import java.io.IOException;
import java.util.function.Supplier;

/** Orchestrates serialization and deserialization of response and request bodies. */
public final class ConjureSerializerRegistry implements SerializerRegistry {

    private static final Supplier<SerializerRegistry> DEFAULT_SUPPLIER = Suppliers.memoize(() ->
            new ConjureSerializerRegistry(Serializers.json(), Serializers.cbor()));

    private final Serializer defaultSerializer;
    private final Serializer[] serializers;

    /**
     * Creates a registry that {@link #getResponseSerializer selects} the first (based on input order) of the
     * provided serializers that  {@link Serializer#supportsContentType supports} the serialization format
     * {@link Headers#ACCEPT accepted} by a given request, or the first serializer if no such serializer can be found.
     */
    public ConjureSerializerRegistry(Serializer defaultSerializer, Serializer... serializers) {
        this.defaultSerializer = defaultSerializer;
        this.serializers = ObjectArrays.concat(defaultSerializer, serializers);
    }

    /**
     * Provides a default configuration of the {@link SerializerRegistry}.
     */
    public static SerializerRegistry getDefault() {
        return DEFAULT_SUPPLIER.get();
    }

    /** Returns the {@link Serializer} to use to deserialize the request body. */
    @VisibleForTesting
    Serializer getRequestDeserializer(HttpServerExchange exchange) {
        String contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
        if (contentType == null) {
            throw new SafeIllegalArgumentException("Request is missing Content-Type header");
        }
        for (Serializer serializer : serializers) {
            if (serializer.supportsContentType(contentType)) {
                return serializer;
            }
        }
        throw new SafeIllegalArgumentException("Unsupported Content-Type",
                SafeArg.of("Content-Type", contentType));
    }

    /** Returns the {@link Serializer} to use for the exchange response. */
    @VisibleForTesting
    Serializer getResponseSerializer(HttpServerExchange exchange) {
        HeaderValues acceptValues = exchange.getRequestHeaders().get(Headers.ACCEPT);
        if (acceptValues != null) {
            // This implementation prefers the client "Accept" order
            for (String acceptValue : acceptValues) {
                for (Serializer serializer : serializers) {
                    if (serializer.supportsContentType(acceptValue)) {
                        return serializer;
                    }
                }
            }
        }
        // Fall back to the default
        return defaultSerializer;
    }

    /** Serialize a value to a provided exchange. */
    @Override
    public void serialize(Object value, HttpServerExchange exchange) throws IOException {
        Preconditions.checkNotNull(value, "cannot serialize null value");
        Serializer serializer = getResponseSerializer(exchange);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, serializer.getContentType());
        serializer.serialize(value, exchange.getOutputStream());
    }

    @Override
    public <T> T deserialize(TypeToken<T> type, HttpServerExchange exchange) throws IOException {
        Serializer serializer = getRequestDeserializer(exchange);
        return serializer.deserialize(exchange.getInputStream(), type);
    }
}
