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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.types.Union2.Union2_Bar;
import com.palantir.conjure.java.types.Union2.Union2_Baz;
import com.palantir.conjure.java.types.Union2.Union2_Foo;
import com.palantir.conjure.java.types.Union2.Union2_UnknownVariant;
import com.palantir.logsafe.UnsafeArg;
import com.palantir.product.UnionWithUnknownString;
import java.util.List;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;

class Union2Tests {

    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    public void testCannotCreateUnknownTypeFromKnownType() {
        assertThatThrownBy(() -> Union2.unknown("bar", "value"));
    }

    @Test
    void switch_statement_compiles() {
        Union2 myUnion = Union2.foo("hello");
        switch (myUnion) {
            case Union2_Foo foo -> System.out.println(foo.getValue());
            case Union2_Bar bar -> System.out.println(bar.getValue());
            case Union2_Baz baz -> System.out.println(baz.getValue());
            case Union2_UnknownVariant unknownWrapper -> System.out.println(unknownWrapper);
        }
    }

    @Test
    void throwOnUnknown_allows_narrowing_to_a_specific_subtype() {
        Union2 myUnion = Union2.foo("hello");
        Union2.Known narrowedSubtype = myUnion.throwOnUnknown();
        switch (narrowedSubtype) {
            case Union2_Foo foo -> System.out.println(foo.getValue());
            case Union2_Bar bar -> System.out.println(bar.getValue());
            case Union2_Baz baz -> System.out.println(baz.getValue());
        }
    }

    @Test
    public void testCreateUnknownType() {
        String expectedUnknownType = "qux";
        List<String> expectedUnknownValue = List.of("quux", "quuz");
        Union2 union = Union2.unknown(expectedUnknownType, expectedUnknownValue);

        // test new visitor builder
        union.accept(Union2.Visitor.<Void>builder()
                .bar(value -> failOnKnownType("bar", value))
                .baz(value -> failOnKnownType("baz", value))
                .foo(value -> failOnKnownType("foo", value))
                .unknown(type -> {
                    assertThat(type).isEqualTo(expectedUnknownType);
                    return null;
                })
                .build());

        // test old visitor builder
        union.accept(Union2.Visitor.<Void>builder()
                .bar(value -> failOnKnownType("bar", value))
                .baz(value -> failOnKnownType("baz", value))
                .foo(value -> failOnKnownType("foo", value))
                .unknown(type -> {
                    assertThat(type).isEqualTo(expectedUnknownType);
                    return null;
                })
                .build());

        // test anonymous visitor
        union.accept(new Union2.Visitor<Void>() {
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
            public Void visitUnknown(String unknownType) {
                assertThat(unknownType).isEqualTo(expectedUnknownType);
                return null;
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
                .unknown((type, value) -> {
                    assertThat(type).isEqualTo(expectedUnknownType);
                    assertThat(value).isEqualTo(expectedUnknownValue);
                    return null;
                })
                .build());
    }

    private Void failOnKnownType(String type, Object value) {
        Fail.fail(
                "Visited known type when expected unknown type",
                UnsafeArg.of("type", type),
                UnsafeArg.of("value", value));
        return null;
    }
}
