/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import org.junit.Test;

public class MissingFieldWireFormatTests {

    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    public void missing_string_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", StringExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [string]");
    }

    @Test
    public void missing_integer_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", IntegerExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [integer]");
    }

    @Test
    public void missing_safelong_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", SafeLongExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [safeLongValue]");
    }

    @Test
    public void missing_rid_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", RidExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [ridValue]");
    }

    @Test
    public void missing_bearertoken_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", BearerTokenExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [bearerTokenValue]");
    }

    @Test
    public void missing_datetime_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", DateTimeExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [datetime]");
    }

    @Test
    public void missing_double_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", DoubleExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [doubleValue]");
    }

    @Test
    public void missing_binary_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", BinaryExample.class).getBinary())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [binary]");
    }

    @Test
    public void missing_optional_field_should_deserialize_as_empty() throws Exception {
        assertThat(mapper.readValue("{}", OptionalExample.class).getItem().isPresent()).isFalse();
    }

    @Test
    public void missing_collection_fields_should_deserialize_as_empty() throws Exception {
        assertThat(mapper.readValue("{}", SetExample.class).getItems()).isEmpty();
        assertThat(mapper.readValue("{}", ListExample.class).getItems()).isEmpty();
        assertThat(mapper.readValue("{}", MapExample.class).getItems()).isEmpty();
    }

    @Test
    public void missing_enum_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", EnumFieldExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [enum]");
    }

    @Test
    public void missing_boolean_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", BooleanExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [coin]");
    }

    @Test
    public void missing_any_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", AnyExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [any]");
    }

    @Test
    public void missing_uuid_field_should_throw() throws Exception {
        assertThatThrownBy(() -> mapper.readValue("{}", UuidExample.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Some required fields have not been set: [uuid]");
    }
}
