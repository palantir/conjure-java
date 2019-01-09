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

package com.palantir.conjure.java.verification.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.google.common.collect.ImmutableMap;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.verification.types.IntegerAliasExample;
import com.palantir.conjure.verification.types.IntegerExample;
import com.palantir.conjure.verification.types.ObjectExample;
import com.palantir.conjure.verification.types.OptionalBearerTokenAliasExample;
import com.palantir.conjure.verification.types.OptionalIntegerAliasExample;
import com.palantir.conjure.verification.types.OptionalIntegerExample;
import java.io.IOException;
import org.junit.Test;

public class OneOffDeserializationTests {
    private static final ObjectMapper mapper = ObjectMappers.newClientObjectMapper();

    @Test
    public void testOptionalAliasEmptyValue() throws IOException {
        String jsonBody = "null";

        OptionalBearerTokenAliasExample value =
                ObjectMappers.newClientObjectMapper().readValue(jsonBody, OptionalBearerTokenAliasExample.class);

        assertThat(value).isNotNull();
    }

    @Test
    public void testFloatyOptionalIntegerAlias() {
        testFloatIntegerConversion(OptionalIntegerAliasExample.class);
    }

    @Test
    public void testFloatyIntegerAlias() {
        testFloatIntegerConversion(IntegerAliasExample.class);
    }

    @Test
    public void testFloatyInteger() {
        testFloatIntegerConversion(IntegerExample.class);
    }

    @Test
    public void testFloatyOptionalInteger() {
        testFloatIntegerConversion(OptionalIntegerExample.class);
    }

    @Test
    public void testFloatyIntegerInObject() throws IOException {
        String json = mapper.writeValueAsString(ImmutableMap.<String, Object>of(
                        "string", "testString",
                        "integer", 123.4,
                        "doubleValue", 234.5,
                        "alias", "testAlias"));

        assertThatThrownBy(() -> mapper.readValue(json, ObjectExample.class))
                .isInstanceOf(MismatchedInputException.class);
    }

    private void testFloatIntegerConversion(Class<?> objectClass) {
        assertThatThrownBy(() -> mapper.readValue("123.4", objectClass))
            .isInstanceOf(MismatchedInputException.class);
    }
}
