/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.lib.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;

/** Internal Jackson support shared by generated union deserializers. */
public abstract class ConjureUnionDeserializer<T> extends JsonDeserializer<T> {
    private final Class<T> unionClass;
    private final Class<?>[] variantTypes;
    private final AtomicReferenceArray<JsonDeserializer<?>> variantDeserializers;

    protected ConjureUnionDeserializer(Class<T> unionClass, Class<?>[] variantTypes) {
        this.unionClass = unionClass;
        this.variantTypes = variantTypes;
        this.variantDeserializers = new AtomicReferenceArray<>(variantTypes.length);
    }

    @Override
    public final boolean isCachable() {
        return true;
    }

    @Override
    public final T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (!parser.isExpectedStartObjectToken()) {
            return context.reportInputMismatch(unionClass, "Expected a JSON object for union deserialization");
        }
        boolean acceptCaseInsensitiveProperties = context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        JsonToken firstToken = parser.nextToken();
        if (firstToken == JsonToken.FIELD_NAME && isTypeField(parser.currentName(), acceptCaseInsensitiveProperties)) {
            if (parser.nextToken() != JsonToken.VALUE_STRING) {
                return context.reportInputMismatch(unionClass, "Union discriminator 'type' must be a string");
            }
            String type = parser.getText();
            parser.nextToken();
            return deserializeSelected(parser, context, type);
        }
        return deserializeBuffered(parser, context, acceptCaseInsensitiveProperties);
    }

    private T deserializeBuffered(
            JsonParser parser, DeserializationContext context, boolean acceptCaseInsensitiveProperties)
            throws IOException {
        try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
            buffer.writeStartObject();
            JsonToken token = parser.currentToken();
            while (token == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                JsonToken valueToken = parser.nextToken();
                if (isTypeField(fieldName, acceptCaseInsensitiveProperties)) {
                    if (valueToken != JsonToken.VALUE_STRING) {
                        return context.reportInputMismatch(unionClass, "Union discriminator 'type' must be a string");
                    }
                    String type = parser.getText();
                    parser.nextToken();
                    try (JsonParser bufferedParser = buffer.asParser(parser)) {
                        JsonParser combinedParser = JsonParserSequence.createFlattened(true, bufferedParser, parser);
                        combinedParser.nextToken();
                        return deserializeSelected(combinedParser, context, type);
                    }
                }
                buffer.writeFieldName(fieldName);
                buffer.copyCurrentStructure(parser);
                token = parser.nextToken();
            }
            if (token != JsonToken.END_OBJECT) {
                return context.reportInputMismatch(
                        unionClass, "Expected the end of a JSON object while deserializing a union");
            }
        }
        return context.reportInputMismatch(unionClass, "Union discriminator 'type' is required");
    }

    private static boolean isTypeField(String fieldName, boolean acceptCaseInsensitiveProperties) {
        return "type".equals(fieldName) || (acceptCaseInsensitiveProperties && "type".equalsIgnoreCase(fieldName));
    }

    protected abstract T deserializeSelected(JsonParser parser, DeserializationContext context, String type)
            throws IOException;

    protected final Object deserializeVariant(JsonParser parser, DeserializationContext context, int variantIndex)
            throws IOException {
        JsonDeserializer<?> deserializer = variantDeserializers.get(variantIndex);
        if (deserializer == null) {
            deserializer = resolveDeserializer(context, variantIndex);
        }
        return deserializer.deserialize(parser, context);
    }

    private JsonDeserializer<?> resolveDeserializer(DeserializationContext context, int variantIndex)
            throws JsonMappingException {
        JsonDeserializer<?> deserializer =
                context.findContextualValueDeserializer(context.constructType(variantTypes[variantIndex]), null);
        if (variantDeserializers.compareAndSet(variantIndex, null, deserializer)) {
            return deserializer;
        }
        return variantDeserializers.get(variantIndex);
    }

    protected final Map<String, Object> deserializeUnknown(JsonParser parser, DeserializationContext context)
            throws IOException {
        Map<String, Object> values = new HashMap<>();
        JsonDeserializer<Object> valueDeserializer = null;
        if (parser.currentToken() == JsonToken.START_OBJECT) {
            parser.nextToken();
        }
        while (parser.currentToken() == JsonToken.FIELD_NAME) {
            String fieldName = parser.currentName();
            parser.nextToken();
            if (valueDeserializer == null) {
                valueDeserializer = context.findRootValueDeserializer(context.constructType(Object.class));
            }
            values.put(
                    fieldName,
                    parser.currentToken() == JsonToken.VALUE_NULL
                            ? valueDeserializer.getNullValue(context)
                            : valueDeserializer.deserialize(parser, context));
            parser.nextToken();
        }
        if (parser.currentToken() != JsonToken.END_OBJECT) {
            return context.reportInputMismatch(
                    unionClass, "Expected the end of a JSON object while deserializing a union");
        }
        return values;
    }
}
