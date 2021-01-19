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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.product.EmptyUnionTypeExample;
import java.io.IOException;
import org.junit.jupiter.api.Test;

class UnionTests {

    private static final ObjectMapper MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    void testUnknownThrowingVariant() throws IOException {
        EmptyUnionTypeExample value =
                MAPPER.readValue("{\"type\":\"foo\",\"foo\":\"bar\"}", EmptyUnionTypeExample.class);
        EmptyUnionTypeExample.Visitor<?> visitor =
                EmptyUnionTypeExample.Visitor.builder().unknownThrows().build();
        assertThatLoggableExceptionThrownBy(() -> value.accept(visitor))
                .isInstanceOf(SafeIllegalStateException.class)
                .hasLogMessage("Unknown variant of the 'EmptyUnionTypeExample' union")
                .hasExactlyArgs(SafeArg.of("unknownType", "foo"));
    }
}
