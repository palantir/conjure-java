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

import com.google.common.collect.ImmutableList;
import io.undertow.server.HttpServerExchange;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class ContentTypeAcceptingDeserializerFactoryTest {

    @ParameterizedTest
    @MethodSource("getContentTypesToTest")
    void accepts_content_type_starting_with_supported(String contentType, boolean matches) {
        TestDeserializerFactory jsonDeserializer = new TestDeserializerFactory(MediaType.APPLICATION_JSON);
        assertThat(jsonDeserializer.accepts(contentType))
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

    private static final class TestDeserializerFactory implements ContentTypeAcceptingDeserializerFactory<String> {
        private final String supportedContentType;

        TestDeserializerFactory(String supportedContentType) {
            this.supportedContentType = supportedContentType;
        }

        @Override
        public String supportedContentType() {
            return supportedContentType;
        }

        @Override
        public String deserialize(HttpServerExchange _exchange) {
            return "success";
        }
    }
}
