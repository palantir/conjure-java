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

import static com.palantir.logsafe.testing.Assertions.assertThatLoggableExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.product.EmptyUnionTypeExample;
import com.palantir.product.Union;
import com.palantir.product.UnionTypeExample;
import com.palantir.product.UnionWithUnknownString;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;

class UnionTests {

    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    void testUnknownThrowingVariant() throws IOException {
        EmptyUnionTypeExample value =
                MAPPER.readValue("{\"type\":\"foo\",\"foo\":\"bar\"}", EmptyUnionTypeExample.class);
        EmptyUnionTypeExample.Visitor<?> visitor =
                EmptyUnionTypeExample.Visitor.builder().throwOnUnknown().build();
        assertThatLoggableExceptionThrownBy(() -> value.accept(visitor))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Unknown variant of the 'EmptyUnionTypeExample' union")
                .hasExactlyArgs(SafeArg.of("unknownType", "foo"));
    }

    @Test
    public void testCannotCreateUnknownTypeFromKnownType() {
        assertThatThrownBy(() -> Union.unknown("bar", "value"));
        assertThatThrownBy(() -> UnionTypeExample.unknown("if", "value"));
        assertThatThrownBy(() -> UnionWithUnknownString.unknown("unknown", "value"));
    }

    @Test
    public void testCreateUnknownType() {
        String expectedUnknownType = "qux";
        List<String> expectedUnknownValue = List.of("quux", "quuz");
        Union union = Union.unknown(expectedUnknownType, expectedUnknownValue);

        // test visitor builder
        union.accept(Union.Visitor.<Void>builder()
                .bar(value -> failOnKnownType("bar", value))
                .baz(value -> failOnKnownType("baz", value))
                .foo(value -> failOnKnownType("foo", value))
                .unknown((type, value) -> verifyUnknownType(type, value, expectedUnknownType, expectedUnknownValue))
                .build());

        // test anonymous visitor
        union.accept(new Union.Visitor<Void>() {
            @Override
            public Void visitFoo(String value) {
                return failOnKnownType("foo", value);
            }

            @Override
            public Void visitBar(int value) {
                return failOnKnownType("bar", value);
            }

            @Override
            public Void visitBaz(long value) {
                return failOnKnownType("baz", value);
            }

            @Override
            public Void visitUnknown(String unknownType, Object unknownValue) {
                return verifyUnknownType(unknownType, unknownValue, expectedUnknownType, expectedUnknownValue);
            }
        });
    }

    @Test
    public void testCreateUnknownTypeNamedUnknown() {
        // unknown is the wire type and "unknown_" is actually unknown
        String expectedUnknownType = "unknown_";
        String expectedUnknownValue = "foo";
        UnionWithUnknownString union = UnionWithUnknownString.unknown(expectedUnknownType, expectedUnknownValue);
        union.accept(UnionWithUnknownString.Visitor.<Void>builder()
                .unknown_(value -> failOnKnownType("unknown", value))
                .unknown((type, value) -> verifyUnknownType(type, value, expectedUnknownType, expectedUnknownValue))
                .build());
    }

    private Void failOnKnownType(String type, Object value) {
        Fail.fail(
                "Visited known type when expected unknown type",
                UnsafeArg.of("type", type),
                UnsafeArg.of("value", value));
        return null;
    }

    private Void verifyUnknownType(String actualType, Object actualValue, String expectedType, Object expectedValue) {
        assertThat(actualType).isEqualTo(expectedType);
        assertThat(actualValue).isEqualTo(expectedValue);
        return null;
    }
}
