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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import org.junit.Test;

public class EncodingRegistryTest {

    private static final TypeToken<String> TYPE = new TypeToken<String>() {};

    @Test
    public void testRequestContentType() throws IOException {
        Encoding json = new StubEncoding("application/json");
        Encoding plain = new StubEncoding("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        EncodingRegistry serializers = new EncodingRegistry(json, plain);
        String value = serializers.deserializer(TYPE).deserialize(exchange);
        assertThat(value).isEqualTo(plain.getContentType());
    }

    @Test
    public void testRequestNoContentType() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        EncodingRegistry serializers = new EncodingRegistry(new StubEncoding("application/json"));
        assertThatThrownBy(() -> serializers.deserializer(TYPE).deserialize(exchange))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("Request is missing Content-Type header");
    }

    @Test
    public void testUnsupportedRequestContentType() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/unknown");
        EncodingRegistry serializers = new EncodingRegistry(new StubEncoding("application/json"));
        assertThatThrownBy(() -> serializers.deserializer(TYPE).deserialize(exchange))
                .isInstanceOf(FrameworkException.class)
                .hasMessageContaining("Unsupported Content-Type");
    }

    @Test
    public void testResponseContentType() throws IOException  {
        Encoding json = new StubEncoding("application/json");
        Encoding plain = new StubEncoding("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.ACCEPT, "text/plain");
        EncodingRegistry serializers = new EncodingRegistry(json, plain);
        serializers.serializer(TYPE).serialize("test", exchange);
        assertThat(exchange.getResponseHeaders().getFirst(Headers.CONTENT_TYPE)).isSameAs(plain.getContentType());
    }

    @Test
    public void testResponseNoContentType() throws IOException  {
        Encoding json = new StubEncoding("application/json");
        Encoding plain = new StubEncoding("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        EncodingRegistry serializers = new EncodingRegistry(json, plain);
        serializers.serializer(TYPE).serialize("test", exchange);
        assertThat(exchange.getResponseHeaders().getFirst(Headers.CONTENT_TYPE)).isEqualTo(json.getContentType());
    }

    @Test
    public void testResponseUnknownContentType() throws IOException  {
        Encoding json = new StubEncoding("application/json");
        Encoding plain = new StubEncoding("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.ACCEPT, "application/unknown");
        EncodingRegistry serializers = new EncodingRegistry(json, plain);
        serializers.serializer(TYPE).serialize("test", exchange);
        assertThat(exchange.getResponseHeaders().getFirst(Headers.CONTENT_TYPE)).isEqualTo(json.getContentType());
    }

    /** Deserializes requests as the configured content type. */
    public static final class StubEncoding implements Encoding {

        private final String contentType;

        StubEncoding(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public <T> Serializer<T> serializer(TypeToken<T> type) {
            return (value, output) -> {
                // nop
            };
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> Deserializer<T> deserializer(TypeToken<T> type) {
            return input -> {
                Preconditions.checkArgument(TYPE.equals(type), "This stub encoding only supports String");
                return (T) getContentType();
            };
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean supportsContentType(String input) {
            return contentType.equals(input);
        }

        @Override
        public String toString() {
            return "StubEncoding{" + contentType + '}';
        }
    }
}
