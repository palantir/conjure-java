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

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.exceptions.SafeNullPointerException;
import com.palantir.product.DoubleAliasExample;
import com.palantir.product.ExternalLongAliasOne;
import com.palantir.product.ExternalLongAliasTwo;
import com.palantir.product.UuidAliasExample;
import org.junit.jupiter.api.Test;

public class AliasTests {
    private static final ObjectMapper TEST_MAPPER = ObjectMappers.newServerObjectMapper();

    @Test
    public void testNullValueSafeLoggable() {
        assertThatThrownBy(() -> UuidAliasExample.of(null))
                .isInstanceOf(SafeNullPointerException.class)
                .hasMessage("value cannot be null");
    }

    @Test
    public void testValueOf_external() {
        assertThat(ExternalLongAliasOne.valueOf("123")).isEqualTo(ExternalLongAliasOne.of(123L));
    }

    @Test
    public void testValueOf_externalNested() {
        assertThat(ExternalLongAliasTwo.valueOf("3")).isEqualTo(ExternalLongAliasTwo.of(ExternalLongAliasOne.of(3L)));
    }

    @Test
    public void testValueOf_largeDouble_lossyDouble() throws JsonProcessingException {
        // So large it doesn't fit in a double; jackson silently drops precision
        assertThat(TEST_MAPPER.readValue("9007199254740992.234", DoubleAliasExample.class))
                .isEqualTo(DoubleAliasExample.of(9007199254740992.0));
    }

    @Test
    public void testValueOf_largeDouble_double() throws JsonProcessingException {
        // Doesn't fit in an int, but does fit comfortably in a double
        assertThat(TEST_MAPPER.readValue("9667500000.0", DoubleAliasExample.class))
                .isEqualTo(DoubleAliasExample.of(9667500000.0));
    }

    @Test
    public void testValueOf_largeDouble_long() throws JsonProcessingException {
        // Doesn't fit in an int, but does fit comfortably in a double; looks like a long
        assertThat(TEST_MAPPER.readValue("9667500000", DoubleAliasExample.class))
                .isEqualTo(DoubleAliasExample.of(9667500000.0));
    }
}
