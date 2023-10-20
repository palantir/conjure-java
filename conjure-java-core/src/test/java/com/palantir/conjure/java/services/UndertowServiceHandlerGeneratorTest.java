/*
 * (c) Copyright 2023 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.spec.HttpPath;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

final class UndertowServiceHandlerGeneratorTest {

    @ParameterizedTest
    @MethodSource("normalizes_http_path_templates")
    void normalizes_http_path_templates(String given, String expected) {
        HttpPath normalized = UndertowServiceHandlerGenerator.normalizeHttpPathTemplates(HttpPath.of(given));

        assertThat(normalized).isEqualTo(HttpPath.of(expected));
    }

    private static Stream<Arguments> normalizes_http_path_templates() {
        return Stream.of(
                Arguments.of("{param:.+}", "{param}"),
                Arguments.of("{param:.*}", "{param}"),
                Arguments.of("{param}", "{param}"),
                // We ignore any other regex, because those are not allowed by the Conjure spec.
                Arguments.of("{param:[a-zA-Z0-9]+}", "{param:[a-zA-Z0-9]+}"),
                Arguments.of("/foo/{paramA:.*}/{paramB:.+}/{paramC}", "/foo/{paramA}/{paramB}/{paramC}"));
    }
}
