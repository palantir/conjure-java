/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.product.AnyExample;
import com.palantir.product.BearerTokenExample;
import com.palantir.product.BinaryExample;
import com.palantir.product.BooleanExample;
import com.palantir.product.DateTimeExample;
import com.palantir.product.DoubleExample;
import com.palantir.product.EnumFieldExample;
import com.palantir.product.IntegerExample;
import com.palantir.product.ListExample;
import com.palantir.product.MapExample;
import com.palantir.product.OptionalExample;
import com.palantir.product.RidExample;
import com.palantir.product.SafeLongExample;
import com.palantir.product.SetExample;
import com.palantir.product.StringExample;
import com.palantir.product.UuidExample;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
public class NullFieldWireFormatTests {

    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    public void null_string_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"string\":null}", StringExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("string cannot be null");
    }

    @Test
    public void null_integer_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"integer\":null}", IntegerExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("Cannot map `null` into type int");
    }

    @Test
    public void null_safelong_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"safeLongValue\":null}", SafeLongExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("safeLongValue cannot be null");
    }

    @Test
    public void null_rid_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"ridValue\":null}", RidExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("ridValue cannot be null");
    }

    @Test
    public void null_bearertoken_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"bearerTokenValue\":null}", BearerTokenExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("bearerTokenValue cannot be null");
    }

    @Test
    public void null_datetime_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"datetime\":null}", DateTimeExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("datetime cannot be null");
    }

    @Test
    public void null_double_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"doubleValue\":null}", DoubleExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("Cannot map `null` into type double");
    }

    @Test
    public void null_binary_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"binary\":null}", BinaryExample.class)
                        .getBinary())
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("binary cannot be null");
    }

    @Test
    public void null_optional_field_should_deserialize_as_empty() throws Exception {
        assertThat(mapper.readValue("{\"item\":null}", OptionalExample.class).getItem())
                .isNotPresent();
    }

    @Test
    public void null_collection_fields_should_deserialize_as_empty() throws Exception {
        assertThat(mapper.readValue("{\"items\":null}", SetExample.class).getItems())
                .isEmpty();
        assertThat(mapper.readValue("{\"items\":null}", ListExample.class).getItems())
                .isEmpty();
        assertThat(mapper.readValue("{\"items\":null}", MapExample.class).getItems())
                .isEmpty();
    }

    @Test
    public void null_enum_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"enum\":null}", EnumFieldExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("enum cannot be null");
    }

    @Test
    public void null_boolean_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"coin\":null}", BooleanExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("Cannot map `null` into type boolean");
    }

    @Test
    public void null_any_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"any\":null}", AnyExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("any cannot be null");
    }

    @Test
    public void null_uuid_field_should_throw() {
        assertThatThrownBy(() -> mapper.readValue("{\"uuid\":null}", UuidExample.class))
                .isInstanceOf(JsonMappingException.class)
                .hasMessageContaining("uuid cannot be null");
    }
}
