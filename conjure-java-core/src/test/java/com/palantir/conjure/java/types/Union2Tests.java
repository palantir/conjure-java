/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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
package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import org.junit.jupiter.api.Test;

class Union2Tests {
    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    public void testCannotCreateUnknownTypeFromKnownType() {
        assertThatThrownBy(() -> Union2.unknown("bar", "value"));
    }

    @Test
    void record_equality() {
        Union2 foo = Union2.foo("hello");
        Union2 helloAgain = Union2.foo("hello");
        Union2 bar = Union2.bar(123);
        // Amusingly, java.lang.Record#equals says "the precise algorithm used in the implicitly provided
        // implementation is unspecified and is subject to change"
        assertThat(foo.equals(bar)).isFalse();
        assertThat(foo.equals(helloAgain)).isTrue();
    }

    // These tests require Java 17 AND --enable-preview, see https://github.com/palantir/gradle-baseline/pull/2319
    // @Test
    // void switch_statement_compiles() {
    //     Union2 myUnion = Union2.foo("hello");
    //     switch (myUnion) {
    //         case Union2.Foo foo -> System.out.println(foo.getValue());
    //         case Union2.Bar bar -> System.out.println(bar.getValue());
    //         case Union2.Baz baz -> System.out.println(baz.getValue());
    //         case Union2.UnknownVariant unknownWrapper -> System.out.println(unknownWrapper);
    //     }
    // }
    //
    // @Test
    // void throwOnUnknown_allows_narrowing_to_a_specific_subtype() {
    //     Union2 myUnion = Union2.foo("hello");
    //     Union2.Known narrowedSubtype = myUnion.throwOnUnknown();
    //     switch (narrowedSubtype) {
    //         case Union2.Foo foo -> System.out.println(foo.getValue());
    //         case Union2.Bar bar -> System.out.println(bar.getValue());
    //         case Union2.Baz baz -> System.out.println(baz.getValue());
    //     }
    // }

    @Test
    void serialization() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMappers.newServerObjectMapper();
        Union2 foo = Union2.foo("hello");
        assertThat(mapper.writeValueAsString(foo)).isEqualTo("{\"type\":\"foo\",\"foo\":\"hello\"}");
        Union2 unknownVariant = Union2.unknown("asdf", 12345);
        assertThat(mapper.writeValueAsString(unknownVariant)).isEqualTo("{\"type\":\"asdf\",\"asdf\":12345}");
    }

    @Test
    void deserialization() throws JsonProcessingException {
        ObjectMapper mapper = ObjectMappers.newServerObjectMapper();
        assertThat(mapper.readValue("{\"type\":\"foo\",\"foo\":\"hello\"}", Union2.class))
                .isEqualTo(Union2.foo("hello"));
        assertThat(mapper.readValue("{\"type\":\"asdf\",\"asdf\":12345}", Union2.class))
                .isEqualTo(Union2.unknown("asdf", 12345));
    }
}
