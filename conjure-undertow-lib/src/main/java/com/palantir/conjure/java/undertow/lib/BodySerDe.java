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

package com.palantir.conjure.java.undertow.lib;

import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.io.InputStream;

/** Request and response Deserialization and Serialization functionality used by generated code. */
public interface BodySerDe {

    /** Creates a {@link Serializer} for the requested type. Serializer instances should be reused. */
    <T> Serializer<T> serializer(TypeMarker<T> type);

    default <T> Serializer<T> serializer(TypeMarker<T> type, Endpoint _endpoint) {
        return serializer(type);
    }

    /** Creates a {@link Deserializer} for the requested type. Deserializer instances should be reused. */
    <T> Deserializer<T> deserializer(TypeMarker<T> type);

    default <T> Deserializer<T> deserializer(TypeMarker<T> type, Endpoint _endpoint) {
        return deserializer(type);
    }

    /**
     * Serializes a {@link BinaryResponseBody} to
     *
     * <pre>application/octet-stream</pre>
     *
     * .
     */
    void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException;

    /**
     * Reads an {@link InputStream} from the {@link HttpServerExchange} request body.
     *
     * <p>This method is named
     *
     * <pre>deserializeInputStream</pre>
     *
     * not
     *
     * <pre>deserializeBinary</pre>
     *
     * to support future streaming binary bindings without conflicting method signatures.
     */
    InputStream deserializeInputStream(HttpServerExchange exchange);
}
