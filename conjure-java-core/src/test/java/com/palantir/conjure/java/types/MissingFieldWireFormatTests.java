/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.types;

import static com.palantir.logsafe.testing.Assertions.assertThatLoggableExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
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

public class MissingFieldWireFormatTests {

    private final ObjectMapper mapper = ObjectMappers.newServerObjectMapper();

    @Test
    public void missing_string_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", StringExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("string")));
    }

    @Test
    public void missing_integer_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", IntegerExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("integer")));
    }

    @Test
    public void missing_safelong_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", SafeLongExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("safeLongValue")));
    }

    @Test
    public void missing_rid_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", RidExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("ridValue")));
    }

    @Test
    public void missing_bearertoken_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", BearerTokenExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("bearerTokenValue")));
    }

    @Test
    public void missing_datetime_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", DateTimeExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("datetime")));
    }

    @Test
    public void missing_double_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", DoubleExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("doubleValue")));
    }

    @Test
    public void missing_binary_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", BinaryExample.class).getBinary())
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("binary")));
    }

    @Test
    public void missing_optional_field_should_deserialize_as_empty() throws Exception {
        assertThat(mapper.readValue("{}", OptionalExample.class).getItem()).isNotPresent();
    }

    @Test
    public void missing_collection_fields_should_deserialize_as_empty() throws Exception {
        assertThat(mapper.readValue("{}", SetExample.class).getItems()).isEmpty();
        assertThat(mapper.readValue("{\"items\":null}", SetExample.class).getItems())
                .isEmpty();
        assertThat(mapper.readValue("{}", ListExample.class).getItems()).isEmpty();
        assertThat(mapper.readValue("{\"items\":null}", ListExample.class).getItems())
                .isEmpty();
        assertThat(mapper.readValue("{}", MapExample.class).getItems()).isEmpty();
        assertThat(mapper.readValue("{\"items\":null}", MapExample.class).getItems())
                .isEmpty();
    }

    @Test
    public void missing_enum_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", EnumFieldExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("enum")));
    }

    @Test
    public void missing_boolean_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", BooleanExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("coin")));
    }

    @Test
    public void missing_any_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", AnyExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("any")));
    }

    @Test
    public void missing_uuid_field_should_throw() throws Exception {
        assertThatLoggableExceptionThrownBy(() -> mapper.readValue("{}", UuidExample.class))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasLogMessage("Some required fields have not been set")
                .hasExactlyArgs(SafeArg.of("missingFields", ImmutableList.of("uuid")));
    }
}
