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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.base.Suppliers;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeIoException;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Supplier;

// TODO(rfink): Consider async Jackson, see
//              https://github.com/spring-projects/spring-framework/commit/31e0e537500c0763a36d3af2570d5c253a374690
//              and https://groups.google.com/forum/#!topic/jackson-user/m_prSo8d_Pw
public final class Serializers {

    private Serializers() {}

    private abstract static class AbstractJacksonSerializer implements Serializer {

        private final ObjectMapper mapper;

        AbstractJacksonSerializer(ObjectMapper mapper) {
            this.mapper = Preconditions.checkNotNull(mapper, "ObjectMapper is required");
        }

        @Override
        public void serialize(Object value, OutputStream output) throws IOException {
            Preconditions.checkNotNull(value, "cannot serialize null value");
            mapper.writeValue(output, value);
        }

        @Override
        public final <T> T deserialize(InputStream input, TypeToken<T> type) throws IOException {
            try {
                T value = mapper.readValue(input, mapper.constructType(type.getType()));
                Preconditions.checkNotNull(value, "cannot deserialize a JSON null value");
                return value;
            } catch (InvalidFormatException e) {
                throw new SafeIllegalArgumentException(
                        "Failed to deserialize response stream. Syntax error?", e, SafeArg.of("type", type.getType()));
            } catch (IOException e) {
                throw new SafeIoException(
                        "Failed to deserialize response stream", e, SafeArg.of("type", type.getType()));
            }
        }
    }

    private static final Supplier<Serializer> jsonInstance = Suppliers.memoize(() ->
            new AbstractJacksonSerializer(configure(ObjectMappers.newServerObjectMapper())) {

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
    });

    private static final Supplier<Serializer> cborInstance = Suppliers.memoize(() ->
            new AbstractJacksonSerializer(configure(ObjectMappers.newCborServerObjectMapper())) {

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
        public void serialize(Object value, OutputStream output) throws IOException {
            super.serialize(value, new ShieldingOutputStream(output));
        }
    });

    /** Returns a serializer for the Conjure JSON wire format. */
    public static Serializer json() {
        return jsonInstance.get();
    }

    /** Returns a serializer for the Conjure CBOR wire format. */
    public static Serializer cbor() {
        return cborInstance.get();
    }

    private static ObjectMapper configure(ObjectMapper mapper) {
        // See documentation on Serializer#serialize: Implementations must not close the stream.
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
