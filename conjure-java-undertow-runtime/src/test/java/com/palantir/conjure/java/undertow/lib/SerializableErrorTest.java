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

package com.palantir.conjure.java.undertow.lib;

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.runtime.Encodings;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.UnsafeArg;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

public final class SerializableErrorTest {

    @Test
    public void serializationSanity() throws IOException {
        SerializableError error = SerializableError.forException(
                new ServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("foo", 42), UnsafeArg.of("bar", "boom")));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(error, stream);
        assertThat(stream.toString(StandardCharsets.UTF_8))
                .isEqualTo("{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\","
                        + "\"errorInstanceId\":\""
                        + error.errorInstanceId()
                        + "\",\"parameters\":{\"foo\":\"42\",\"bar\":\"boom\"}}");
    }

    // Discussion of potentially changing the parameter format in the below serialization_ tests is at
    // https://github.com/palantir/conjure-java/issues/1812
    @Test
    public void serialization_listArgs() throws IOException {
        SerializableError error = SerializableError.forException(new ServiceException(
                ErrorType.INVALID_ARGUMENT,
                SafeArg.of("safe-list", List.of("1", "2")),
                UnsafeArg.of("unsafe-list", List.of("A", "B"))));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(error, stream);
        assertThat(stream.toString(StandardCharsets.UTF_8))
                .isEqualTo("{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\","
                        + "\"errorInstanceId\":\""
                        + error.errorInstanceId()
                        + "\",\"parameters\":{\"safe-list\":\"[1, 2]\",\"unsafe-list\":\"[A, B]\"}}");
    }

    @Test
    public void serialization_mapArgs() throws IOException {
        SerializableError error = SerializableError.forException(new ServiceException(
                ErrorType.INVALID_ARGUMENT,
                SafeArg.of("safe-map", Map.of("1", "2")),
                UnsafeArg.of("unsafe-map", Map.of("ABC", "DEF"))));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(error, stream);
        assertThat(stream.toString(StandardCharsets.UTF_8))
                .isEqualTo("{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\","
                        + "\"errorInstanceId\":\""
                        + error.errorInstanceId()
                        + "\",\"parameters\":{\"safe-map\":\"{1=2}\",\"unsafe-map\":\"{ABC=DEF}\"}}");
    }

    @Test
    public void serialization_arrayArgs() throws IOException {
        SerializableError error = SerializableError.forException(new ServiceException(
                ErrorType.INVALID_ARGUMENT,
                SafeArg.of("safe-array", new long[] {1L, 2L, 3L}),
                UnsafeArg.of("unsafe-array", new String[] {"A", "B", "C"})));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(error, stream);
        assertThat(stream.toString(StandardCharsets.UTF_8))
                // have to use regex since the serialized arrays have runtime-specific memory addresses in them
                .matches(Pattern.quote("{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\","
                                + "\"errorInstanceId\":\""
                                + error.errorInstanceId()
                                + "\",\"parameters\":{")
                        // Example: [J@714afbd4
                        + Pattern.quote("\"safe-array\":\"[J@") + "[0-9a-f]+" + Pattern.quote("\",")
                        // Example: [Ljava.lang.String;@769a49e3
                        + Pattern.quote("\"unsafe-array\":\"[Ljava.lang.String;@") + "[0-9a-f]+"
                        + Pattern.quote("\"}}"));
    }

    @Test
    public void serialization_optionalArgs() throws IOException {
        SerializableError error = SerializableError.forException(new ServiceException(
                ErrorType.INVALID_ARGUMENT,
                SafeArg.of("safe-optional-present", Optional.of("hello")),
                UnsafeArg.of("unsafe-optional-empty", Optional.empty())));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(error, stream);
        assertThat(stream.toString(StandardCharsets.UTF_8))
                .isEqualTo("{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\","
                        + "\"errorInstanceId\":\""
                        + error.errorInstanceId()
                        + "\",\"parameters\":{\"safe-optional-present\":\"Optional[hello]\","
                        + "\"unsafe-optional-empty\":\"Optional.empty\"}}");
    }
}
