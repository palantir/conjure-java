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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.logsafe.exceptions.SafeNullPointerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.Test;

public final class SerializersTest {

    private final Serializer json = Serializers.json();

    // TODO(rfink): Wire tests for JSON serializer

    @Test
    public void json_deserialize_throwsDeserializationErrorsAsIllegalArgumentException() {
        assertThatThrownBy(() -> json.deserialize(asStream("\"2018-08-bogus\""), new TypeToken<OffsetDateTime>() {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Failed to deserialize");
    }

    @Test
    public void json_serialize_rejectsNulls() {
        assertThatThrownBy(() -> json.serialize(null /* under test: null value throws */, null /* unused stream */))
                .isInstanceOf(SafeNullPointerException.class);
    }

    @Test
    public void json_deserialize_rejectsNulls() throws IOException {
        // TODO(rfink): Do we need to test this for all primitive types?
        assertThatThrownBy(() -> json.deserialize(asStream("null"), new TypeToken<String>() {}))
                .isInstanceOf(SafeNullPointerException.class);
        assertThat(json.deserialize(asStream("null"), new TypeToken<Optional<String>>() {})).isEmpty();
    }

    @Test
    public void json_serialize_doesNotCloseOutputStream() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        json.serialize("test", outputStream);
        verify(outputStream, never()).close();
    }

    private static InputStream asStream(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }
}
