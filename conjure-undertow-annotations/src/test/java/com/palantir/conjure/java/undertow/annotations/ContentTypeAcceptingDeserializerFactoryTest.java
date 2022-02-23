/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.annotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.logsafe.exceptions.SafeIoException;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ContentTypeAcceptingDeserializerFactoryTest {

    @Test
    void accepts_first_matching_content_type() throws IOException {
        String matchingHeader = "matching-header";
        HttpServerExchange exchange = exchangeWithContentTypeHeader(matchingHeader);

        TestDeserializerFactory testDeserializerFactory =
                new TestDeserializerFactory(ImmutableList.of("no-match-1", "no-match-2", matchingHeader, "no-match-3"));

        String response = testDeserializerFactory.deserializer(null, null, null).deserialize(exchange);
        assertThat(response).isEqualTo(matchingHeader);
    }

    @Test
    void throws_if_no_content_type_header_matches() {
        HttpServerExchange exchange = exchangeWithContentTypeHeader("other-content-type");

        TestDeserializerFactory testDeserializerFactory =
                new TestDeserializerFactory(ImmutableList.of("no-match-1", "no-match-2"));

        Deserializer<String> deserializer = testDeserializerFactory.deserializer(null, null, null);
        assertThatThrownBy(() -> deserializer.deserialize(exchange))
                .isInstanceOf(SafeIoException.class)
                .hasMessageContaining("Unsupported content-type");
    }

    @Test
    void throws_if_no_content_type_header_is_set() {
        HttpServerExchange exchange = new HttpServerExchange(null, new HeaderMap(), new HeaderMap(), 200);

        TestDeserializerFactory testDeserializerFactory = new TestDeserializerFactory(ImmutableList.of("no-match-1"));

        Deserializer<String> deserializer = testDeserializerFactory.deserializer(null, null, null);
        assertThatThrownBy(() -> deserializer.deserialize(exchange))
                .isInstanceOf(SafeIoException.class)
                .hasMessageContaining("Unsupported content-type");
    }

    @ParameterizedTest
    @MethodSource("getContentTypesToTest")
    void accepts_content_type_starting_with_supported(String contentType, boolean matches) {
        TestDeserializerFactory testDeserializerFactory = new TestDeserializerFactory(Collections.emptyList());
        assertThat(testDeserializerFactory.accepts(contentType, MediaType.APPLICATION_JSON))
                .describedAs(contentType)
                .isEqualTo(matches);
    }

    private static List<Arguments> getContentTypesToTest() {
        return ImmutableList.of(
                Arguments.of(MediaType.APPLICATION_JSON, true),
                Arguments.of(MediaType.APPLICATION_JSON + "; charset=UTF-8", true),
                Arguments.of(MediaType.APPLICATION_OCTET_STREAM, false),
                Arguments.of("application/jso", false));
    }

    private static HttpServerExchange exchangeWithContentTypeHeader(String headerValue) {
        HeaderMap requestHeaders = new HeaderMap();
        requestHeaders.put(Headers.CONTENT_TYPE, headerValue);
        return new HttpServerExchange(null, requestHeaders, new HeaderMap(), 200);
    }

    private static final class TestDeserializerFactory implements ContentTypeAcceptingDeserializerFactory<String> {
        private final List<String> supportedContentType;

        TestDeserializerFactory(List<String> supportedContentType) {
            this.supportedContentType = supportedContentType;
        }

        @Override
        public List<String> supportedContentTypes() {
            return supportedContentType;
        }

        @Override
        public String deserialize(HttpServerExchange _exchange, String contentType) {
            return contentType;
        }
    }
}
