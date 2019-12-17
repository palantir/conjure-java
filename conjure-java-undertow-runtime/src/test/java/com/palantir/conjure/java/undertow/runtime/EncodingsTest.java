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

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeNullPointerException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;

final class EncodingsTest {

    private final Encoding json = Encodings.json();
    private final Encoding cbor = Encodings.cbor();

    // TODO(rfink): Wire tests for JSON serializer

    @Test
    void json_supportsContentType() {
        assertThat(json.supportsContentType("application/json")).isTrue();
        assertThat(json.supportsContentType("application/json; charset=utf-8")).isTrue();
        assertThat(json.supportsContentType("Application/json")).isTrue();
        assertThat(json.supportsContentType("application/Json")).isTrue();

        assertThat(json.supportsContentType("application/unknown")).isFalse();
    }

    @Test
    void json_deserialize_throwsDeserializationErrorsAsIllegalArgumentException() {
        assertThatThrownBy(() -> deserialize(asStream("\"2018-08-bogus\""), new TypeMarker<OffsetDateTime>() {}))
                .isInstanceOf(FrameworkException.class)
                .hasMessageContaining("Failed to deserialize")
                .matches(exception -> ((FrameworkException) exception).getStatusCode() == 422, "Expected 422 status");
    }

    @Test
    void json_deserialize_missingField() {
        assertThatThrownBy(() -> deserialize(asStream("{\"value\":null}"), new TypeMarker<SimpleObject>() {}))
                .isInstanceOf(FrameworkException.class)
                .hasMessageContaining("Failed to deserialize")
                .matches(exception -> ((FrameworkException) exception).getStatusCode() == 422, "Expected 422 status");
    }

    @Test
    void json_deserialize_invalidToken() {
        assertThatThrownBy(() -> deserialize(asStream("{\"invalid\"}"),
                new TypeMarker<SimpleObject>() {}))
                // SafeIllegalArgumentException is mapped to a 400 response status
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessageContaining("Failed to parse request due to malformed content");
    }

    /** Approximation of a generated bean object. */
    @JsonDeserialize(builder = SimpleObject.Builder.class)
    public static final class SimpleObject {

        private String value;

        private SimpleObject(String value) {
            this.value = Preconditions.checkNotNull(value, "value");
        }

        @JsonProperty("value")
        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            SimpleObject that = (SimpleObject) other;
            return value.equals(that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static final class Builder {
            private String value;

            @JsonSetter("value")
            public Builder value(String val) {
                this.value = Preconditions.checkNotNull(val, "value");
                return this;
            }

            public SimpleObject build() {
                return new SimpleObject(value);
            }
        }
    }

    @Test
    public void json_serialize_rejectsNulls() {
        assertThatThrownBy(() -> serialize(null /* under test: null value throws */, null /* unused stream */))
                .isInstanceOf(SafeNullPointerException.class);
    }

    @Test
    public void json_deserialize_rejectsNulls() throws IOException {
        // TODO(rfink): Do we need to test this for all primitive types?
        assertThatThrownBy(() -> deserialize(asStream("null"), new TypeMarker<String>() {}))
                .isInstanceOf(SafeIllegalArgumentException.class);
        assertThat(deserialize(asStream("null"), new TypeMarker<Optional<String>>() {})).isEmpty();
    }

    @Test
    public void json_serialize_doesNotCloseOutputStream() throws IOException {
        OutputStream outputStream = mock(OutputStream.class);
        serialize("test", outputStream);
        verify(outputStream, never()).close();
    }

    @Test
    void cbor_supportsContentType() {
        assertThat(cbor.supportsContentType("application/cbor")).isTrue();
        assertThat(cbor.supportsContentType("application/cbor; charset=utf-8")).isTrue();
        assertThat(cbor.supportsContentType("Application/cbor")).isTrue();
        assertThat(cbor.supportsContentType("application/Cbor")).isTrue();

        assertThat(cbor.supportsContentType("application/unknown")).isFalse();
    }

    private static InputStream asStream(String data) {
        return new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
    }

    private void serialize(Object object, OutputStream stream) throws IOException {
        json.serializer(new TypeMarker<Object>() {}).serialize(object, stream);
    }

    private <T> T deserialize(InputStream stream, TypeMarker<T> token) throws IOException {
        return json.deserializer(token).deserialize(stream);
    }
}
