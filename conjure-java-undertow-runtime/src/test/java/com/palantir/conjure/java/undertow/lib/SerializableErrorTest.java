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
import org.junit.jupiter.api.Test;

public final class SerializableErrorTest {

    @Test
    public void serializationSanity() throws IOException {
        SerializableError error = SerializableError.forException(
                new ServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("foo", 42), UnsafeArg.of("bar", "boom")));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(error, stream);
        assertThat(stream.toString())
                .isEqualTo("{\"errorCode\":\"INVALID_ARGUMENT\",\"errorName\":\"Default:InvalidArgument\","
                        + "\"errorInstanceId\":\""
                        + error.errorInstanceId()
                        + "\",\"parameters\":{\"foo\":\"42\",\"bar\":\"boom\"}}");
    }
}
