/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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
        public final void serialize(Object value, OutputStream output) throws IOException {
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

    private static final Supplier<Serializer> jsonInstance = Suppliers.memoize(() -> new AbstractJacksonSerializer(
            // TODO(rfink): Remove http-remoting dependency
            ObjectMappers.newServerObjectMapper()
                    // See documentation on Serializer#serialize: Implementations must not close the stream.
                    .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {

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

    private static final Supplier<Serializer> cborInstance = Suppliers.memoize(() -> new AbstractJacksonSerializer(
            // TODO(rfink): Remove http-remoting dependency
            ObjectMappers.newCborServerObjectMapper()
                    // See documentation on Serializer#serialize: Implementations must not close the stream.
                    .disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET)) {

        private static final String CONTENT_TYPE = "application/cbor";

        @Override
        public String getContentType() {
            return CONTENT_TYPE;
        }

        @Override
        public boolean supportsContentType(String contentType) {
            return contentType != null && contentType.startsWith(CONTENT_TYPE);
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
}
