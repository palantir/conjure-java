/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.Test;

public final class SerializableErrorTest {

    @Test
    public void serializationSanity() throws IOException {
        SerializableError error = SerializableError.forException(
                new ServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("foo", 42), UnsafeArg.of("bar", "boom")));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Serializers.json().serialize(error, stream);
        assertThat(stream.toString()).isEqualTo(
                "{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\",\"errorInstanceId\":\""
                        + error.errorInstanceId() + "\",\"parameters\":{\"foo\":\"42\",\"bar\":\"boom\"}}");
    }
}
