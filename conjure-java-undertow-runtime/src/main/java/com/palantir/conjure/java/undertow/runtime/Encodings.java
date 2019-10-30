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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIoException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// TODO(rfink): Consider async Jackson, see
//              https://github.com/spring-projects/spring-framework/commit/31e0e537500c0763a36d3af2570d5c253a374690
//              and https://groups.google.com/forum/#!topic/jackson-user/m_prSo8d_Pw
public final class Encodings {

    private Encodings() {}

    private abstract static class AbstractJacksonEncoding implements Encoding {

        private final ObjectMapper mapper;

        AbstractJacksonEncoding(ObjectMapper mapper) {
            this.mapper = Preconditions.checkNotNull(mapper, "ObjectMapper is required");
        }

        @Override
        public <T> Serializer<T> serializer(TypeMarker<T> type) {
            ObjectWriter writer = mapper.writerFor(mapper.constructType(type.getType()));
            return (value, output) -> {
                Preconditions.checkNotNull(value, "cannot serialize null value");
                writer.writeValue(output, value);
            };
        }

        @Override
        public <T> Deserializer<T> deserializer(TypeMarker<T> type) {
            ObjectReader reader = mapper.readerFor(mapper.constructType(type.getType()));
            return input -> {
                try {
                    T value = reader.readValue(input);
                    // Bad input should result in a 4XX response status, throw IAE rather than NPE.
                    Preconditions.checkArgument(value != null, "cannot deserialize a JSON null value");
                    return value;
                } catch (JsonMappingException | JsonParseException e) {
                    // JsonMappingException includes both MismatchedInputException and InvalidDefinitionException
                    // which is important for us to detect when both parsing fails (in jackson code) and when object
                    // validation (setter null checks) fail in our objects.
                    // JsonParseException is thrown when the input cannot be parsed as JSON, for example '{"value"}'.
                    throw FrameworkException.unprocessableEntity("Failed to deserialize request", e,
                            SafeArg.of("contentType", getContentType()),
                            SafeArg.of("type", type));
                } catch (IOException e) {
                    throw new SafeIoException("Failed to deserialize request", e,
                            SafeArg.of("contentType", getContentType()),
                            SafeArg.of("type", type));
                }
            };
        }
    }

    /** Returns a serializer for the Conjure JSON wire format. */
    public static Encoding json() {
        return new AbstractJacksonEncoding(configure(ObjectMappers.newServerObjectMapper())) {
            private static final String CONTENT_TYPE = "application/json";

            @Override
            public String getContentType() {
                return CONTENT_TYPE;
            }

            @Override
            public boolean supportsContentType(String contentType) {
                // TODO(ckozak): support wildcards? See javax.ws.rs.core.MediaType.isCompatible
                return contentType != null
                        // Use startsWith to avoid failures due to charset
                        && contentType.startsWith(CONTENT_TYPE);
            }
        };
    }

    /** Returns a serializer for the Conjure CBOR wire format. */
    public static Encoding cbor() {
        return new AbstractJacksonEncoding(configure(ObjectMappers.newCborServerObjectMapper())) {
            private static final String CONTENT_TYPE = "application/cbor";

            @Override
            public String getContentType() {
                return CONTENT_TYPE;
            }

            @Override
            public boolean supportsContentType(String contentType) {
                return contentType != null && contentType.startsWith(CONTENT_TYPE);
            }

            @Override
            public <T> Serializer<T> serializer(TypeMarker<T> type) {
                Serializer<T> delegate = super.serializer(type);
                return (value, output) -> delegate.serialize(value, new ShieldingOutputStream(output));
            }
        };
    }

    private static ObjectMapper configure(ObjectMapper mapper) {
        // See documentation on Encoding.Serializer#serialize: Implementations must not close the stream.
        return mapper.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)
                // Avoid flushing, allowing us to set content-length if the length is below the buffer size.
                .disable(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM);
    }

    /**
     * Work around a CBORGenerator bug.
     * For more information: https://github.com/FasterXML/jackson-dataformats-binary/issues/155
     */
    private static final class ShieldingOutputStream extends FilterOutputStream {
        ShieldingOutputStream(OutputStream out) {
            super(out);
        }

        @Override
        public void flush() {
            // nop
        }

        @Override
        public void close() {
            // nop
        }
    }
}
