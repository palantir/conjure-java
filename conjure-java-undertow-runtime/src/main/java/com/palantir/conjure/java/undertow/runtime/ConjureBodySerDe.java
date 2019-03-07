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

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.BinaryResponseBody;
import com.palantir.conjure.java.undertow.lib.BodySerDe;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Serializer;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import java.io.InputStream;

/** Package private internal API. */
final class ConjureBodySerDe implements BodySerDe {

    private final EncodingRegistry encodingRegistry;

    ConjureBodySerDe(EncodingRegistry encodingRegistry) {
        this.encodingRegistry = encodingRegistry;
    }

    @Override
    public <T> Serializer<T> serializer(TypeToken<T> type) {
        return encodingRegistry.serializer(type);
    }

    @Override
    public <T> Deserializer<T> deserializer(TypeToken<T> type) {
        return encodingRegistry.deserializer(type);
    }

    @Override
    public void serialize(BinaryResponseBody value, HttpServerExchange exchange) throws IOException {
        BinarySerializers.serialize(value, exchange);
    }

    @Override
    public InputStream deserializeInputStream(HttpServerExchange exchange) {
        return BinarySerializers.deserializeInputStream(exchange);
    }
}
