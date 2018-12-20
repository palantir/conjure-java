/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;

public class SerializerRegistryTest {

    @Test
    public void testRequestContentType() {
        Serializer json = new StubSerializer("application/json");
        Serializer plain = new StubSerializer("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        SerializerRegistry serializers = new SerializerRegistry(json, plain);
        Serializer serializer = serializers.getRequestDeserializer(exchange);
        assertThat(serializer).isSameAs(plain);
    }

    @Test
    public void testRequestNoContentType() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        SerializerRegistry serializers = new SerializerRegistry(new StubSerializer("application/json"));
        assertThatThrownBy(() -> serializers.getRequestDeserializer(exchange))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("Request is missing Content-Type header");
    }

    @Test
    public void testUnsupportedRequestContentType() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.CONTENT_TYPE, "application/unknown");
        SerializerRegistry serializers = new SerializerRegistry(new StubSerializer("application/json"));
        assertThatThrownBy(() -> serializers.getRequestDeserializer(exchange))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("Unsupported Content-Type");
    }

    @Test
    public void testResponseContentType() {
        Serializer json = new StubSerializer("application/json");
        Serializer plain = new StubSerializer("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.ACCEPT, "text/plain");
        SerializerRegistry serializers = new SerializerRegistry(json, plain);
        Serializer serializer = serializers.getResponseSerializer(exchange);
        assertThat(serializer).isSameAs(plain);
    }

    @Test
    public void testResponseNoContentType() {
        Serializer json = new StubSerializer("application/json");
        Serializer plain = new StubSerializer("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        SerializerRegistry serializers = new SerializerRegistry(json, plain);
        Serializer serializer = serializers.getResponseSerializer(exchange);
        assertThat(serializer).isSameAs(json);
    }

    @Test
    public void testResponseUnknownContentType() {
        Serializer json = new StubSerializer("application/json");
        Serializer plain = new StubSerializer("text/plain");

        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().put(Headers.ACCEPT, "application/unknown");
        SerializerRegistry serializers = new SerializerRegistry(json, plain);
        Serializer serializer = serializers.getResponseSerializer(exchange);
        assertThat(serializer).isSameAs(json);
    }

    public static final class StubSerializer implements Serializer {

        private final String contentType;

        StubSerializer(String contentType) {
            this.contentType = contentType;
        }

        @Override
        public void serialize(Object value, OutputStream output) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> T deserialize(InputStream input, TypeToken<T> type) {
            throw new UnsupportedOperationException();
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
            return "StubSerializer{" + contentType + '}';
        }
    }
}
