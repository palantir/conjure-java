/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.lib;

import static com.palantir.logsafe.testing.Assertions.assertThatLoggableExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public final class SafeLongTests {

    private static final long maxValue = 9007199254740991L;
    private static final long minValue = -9007199254740991L;

    @Test
    public void testTooLarge() {
        long tooLarge = maxValue + 1;

        assertThatLoggableExceptionThrownBy(() -> SafeLong.of(tooLarge))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("number must be safely representable in javascript")
                .hasMessageContaining(Long.toString(minValue))
                .hasMessageContaining(Long.toString(maxValue));
    }

    @Test
    public void testTooSmall() {
        long tooSmall = minValue - 1;
        assertThatLoggableExceptionThrownBy(() -> SafeLong.of(tooSmall))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("number must be safely representable in javascript")
                .hasMessageContaining(Long.toString(minValue))
                .hasMessageContaining(Long.toString(maxValue));
    }

    @Test
    public void testOk() {
        SafeLong.of(maxValue);
        SafeLong.of(minValue);
    }

    @Test
    public void testSerde() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SafeLong maxSafeLong = SafeLong.of(maxValue);
        String serialized = mapper.writeValueAsString(maxSafeLong);
        SafeLong deserialized = mapper.readValue(serialized, SafeLong.class);
        assertThat(deserialized).isEqualTo(maxSafeLong);
    }

    @Test
    public void testDeserializationFailsWhenTooLarge() {
        String json = "{\"value\": 9007199254740992}";
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, SafeLong>> typeReference = new TypeReference<Map<String, SafeLong>>() {};

        assertThatThrownBy(() -> mapper.readValue(json, typeReference)).isInstanceOf(JsonMappingException.class);
    }

    @Test
    public void testToString() {
        assertThat(SafeLong.of(maxValue).toString()).isEqualTo("9007199254740991");
        assertThat(SafeLong.of(minValue).toString()).isEqualTo("-9007199254740991");
    }

    @Test
    public void testComparable() {
        assertThat(Stream.of(SafeLong.of(1), SafeLong.of(3), SafeLong.of(2)).max(Comparator.naturalOrder()).get())
                .isEqualTo(SafeLong.of(3));
        assertThat(Stream.of(SafeLong.of(1), SafeLong.of(3), SafeLong.of(2)).min(Comparator.naturalOrder()).get())
                .isEqualTo(SafeLong.of(1));
    }
}
