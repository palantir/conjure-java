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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.impl.JDKValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

/** Jackson support for generated code; not intended for application use. */
public final class ConjureJacksonSupport {
    private ConjureJacksonSupport() {}

    /**
     * Gives generated code ownership of maps and sets. Standard Jackson containers with scalar entries may be transferred
     * directly; other results are defensively copied. Generated code validates nulls and exposes an unmodifiable view.
     */
    public static final class ContainerDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property)
                throws JsonMappingException {
            JavaType type = property.getType();
            boolean map = type.isMapLikeType();
            if (!map) {
                // Jackson skips `as` refinement on delegating creators that also specify `using`.
                type = context.constructSpecializedType(type, LinkedHashSet.class);
            }
            JsonDeserializer<?> delegate = context.findContextualValueDeserializer(type, property);
            // Custom constructors, entries, and problem handlers can expose shared containers.
            if (context.getConfig().getProblemHandlers() == null
                    && !hasTypeDeserializer(context, type)
                    && !hasTypeDeserializer(context, type.getContentType())
                    && (map ? isStandardMap(delegate, context, property) : isStandardSet(delegate, context))) {
                return delegate;
            }
            return new CopyingDeserializer(delegate, map);
        }

        private static boolean hasTypeDeserializer(DeserializationContext context, JavaType type)
                throws JsonMappingException {
            return type.getTypeHandler() != null || context.getConfig().findTypeDeserializer(type) != null;
        }

        private static boolean isStandardMap(
                JsonDeserializer<?> delegate, DeserializationContext context, BeanProperty property)
                throws JsonMappingException {
            if (delegate.getClass() != MapDeserializer.class) {
                return false;
            }
            MapDeserializer deserializer = (MapDeserializer) delegate;
            JavaType keyType = property.getType().getKeyType();
            return deserializer.getValueInstantiator().getClass()
                            == JDKValueInstantiators.findStdValueInstantiator(context.getConfig(), LinkedHashMap.class)
                                    .getClass()
                    && hasStandardElements(deserializer)
                    && keyType.getValueHandler() == null
                    && ClassUtil.isJacksonStdImpl(context.findKeyDeserializer(keyType, property))
                    && !hasTypeDeserializer(context, keyType);
        }

        private static boolean isStandardSet(JsonDeserializer<?> delegate, DeserializationContext context) {
            if (delegate.getClass() != CollectionDeserializer.class
                    && delegate.getClass() != StringCollectionDeserializer.class) {
                return false;
            }
            ContainerDeserializerBase<?> deserializer = (ContainerDeserializerBase<?>) delegate;
            return deserializer.getValueInstantiator().getClass()
                            == JDKValueInstantiators.findStdValueInstantiator(context.getConfig(), LinkedHashSet.class)
                                    .getClass()
                    && hasStandardElements(deserializer);
        }

        private static boolean hasStandardElements(ContainerDeserializerBase<?> deserializer) {
            // Custom element deserializers can retain the container exposed by JsonParser.currentValue().
            JsonDeserializer<?> content = deserializer.getContentDeserializer();
            return content == null
                    || (content instanceof StdScalarDeserializer<?> && ClassUtil.isJacksonStdImpl(content));
        }

        @Override
        public Object deserialize(JsonParser _parser, DeserializationContext _context) {
            throw new SafeIllegalStateException("Container deserializer must be contextualized");
        }

        private static final class CopyingDeserializer extends DelegatingDeserializer {
            private final boolean map;

            CopyingDeserializer(JsonDeserializer<?> delegate, boolean map) {
                super(delegate);
                this.map = map;
            }

            @Override
            protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
                return new CopyingDeserializer(delegate, map);
            }

            @Override
            public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                return copy(super.deserialize(parser, context));
            }

            @Override
            public Object deserializeWithType(
                    JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer)
                    throws IOException {
                return copy(super.deserializeWithType(parser, context, typeDeserializer));
            }

            @Override
            public Object getNullValue(DeserializationContext context) throws JsonMappingException {
                return copy(super.getNullValue(context));
            }

            @Override
            public Object getEmptyValue(DeserializationContext context) throws JsonMappingException {
                return copy(super.getEmptyValue(context));
            }

            @Override
            public Object getAbsentValue(DeserializationContext context) throws JsonMappingException {
                return copy(super.getAbsentValue(context));
            }

            private Object copy(Object value) {
                if (value == null) {
                    return null;
                }
                return map ? new LinkedHashMap<>((Map<?, ?>) value) : new LinkedHashSet<>((Collection<?>) value);
            }
        }
    }

    /**
     * Deserializes generated sealed unions using constructor-supplied metadata. Subclasses only provide a no-argument
     * constructor for Jackson; parsing, variant resolution, and caching are handled by the runtime.
     *
     * <p>Concrete variants must override inherited Jackson serializer and deserializer annotations. Let Jackson create
     * instances for each mapper; do not share deserializer instances between mappers.
     */
    public abstract static class UnionDeserializer<T> extends JsonDeserializer<T> {
        private final Class<T> unionClass;
        private final List<Class<? extends T>> variantTypes;
        private final AtomicReferenceArray<JsonDeserializer<? extends T>> variantDeserializers;
        private final ToIntFunction<String> variantIndex;
        private final BiFunction<String, Map<String, Object>, ? extends T> unknownFactory;

        /**
         * @param unionClass the generated union's base type
         * @param variantTypes concrete variant classes in selector order; the runtime copies this list
         * @param variantIndex maps a discriminator to its variant's index, or a negative value for an unknown variant;
         *     must be thread-safe and stable for the lifetime of this deserializer
         * @param unknownFactory constructs an unknown variant from its discriminator and an owned map of payload fields;
         *     must be thread-safe and may retain the map
         */
        protected UnionDeserializer(
                Class<T> unionClass,
                List<Class<? extends T>> variantTypes,
                ToIntFunction<String> variantIndex,
                BiFunction<String, Map<String, Object>, ? extends T> unknownFactory) {
            this.unionClass = Preconditions.checkNotNull(unionClass, "unionClass");
            this.variantTypes = List.copyOf(Preconditions.checkNotNull(variantTypes, "variantTypes"));
            this.variantDeserializers = new AtomicReferenceArray<>(this.variantTypes.size());
            this.variantIndex = Preconditions.checkNotNull(variantIndex, "variantIndex");
            this.unknownFactory = Preconditions.checkNotNull(unknownFactory, "unknownFactory");
        }

        @Override
        public final boolean isCachable() {
            return true;
        }

        @Override
        public final T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            // Delegating creators may consume START_OBJECT before invoking this deserializer.
            JsonToken firstToken = parser.currentToken();
            if (parser.isExpectedStartObjectToken()) {
                firstToken = parser.nextToken();
            } else if (firstToken != JsonToken.FIELD_NAME && firstToken != JsonToken.END_OBJECT) {
                return context.reportInputMismatch(unionClass, "Expected a JSON object for union deserialization");
            }
            boolean acceptCaseInsensitiveProperties =
                    context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
            if (firstToken == JsonToken.FIELD_NAME
                    && isTypeField(parser.currentName(), acceptCaseInsensitiveProperties)) {
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
                // Replay fields directly, matching the parser position on the discriminator-first path.
                JsonToken token = parser.currentToken();
                while (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    JsonToken valueToken = parser.nextToken();
                    if (isTypeField(fieldName, acceptCaseInsensitiveProperties)) {
                        if (valueToken != JsonToken.VALUE_STRING) {
                            return context.reportInputMismatch(
                                    unionClass, "Union discriminator 'type' must be a string");
                        }
                        String type = parser.getText();
                        parser.nextToken();
                        try (JsonParser bufferedParser = buffer.asParser(parser)) {
                            JsonParser combinedParser =
                                    JsonParserSequence.createFlattened(true, bufferedParser, parser);
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

        private T deserializeSelected(JsonParser parser, DeserializationContext context, String type)
                throws IOException {
            int index = variantIndex.applyAsInt(type);
            if (index < 0) {
                return unknownFactory.apply(type, deserializeUnknown(parser, context));
            }
            JsonDeserializer<? extends T> deserializer = variantDeserializers.get(index);
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, index);
            }
            return deserializer.deserialize(parser, context);
        }

        @SuppressWarnings("unchecked") // The constructor restricts variant classes to subtypes of T.
        private JsonDeserializer<? extends T> resolveDeserializer(DeserializationContext context, int index)
                throws JsonMappingException {
            JsonDeserializer<? extends T> deserializer = (JsonDeserializer<? extends T>)
                    context.findContextualValueDeserializer(context.constructType(variantTypes.get(index)), null);
            if (variantDeserializers.compareAndSet(index, null, deserializer)) {
                return deserializer;
            }
            return variantDeserializers.get(index);
        }

        private Map<String, Object> deserializeUnknown(JsonParser parser, DeserializationContext context)
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

    /** Internal Jackson serializer that delegates from a sealed union to its concrete variant. */
    public static final class UnionSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            serializers.findValueSerializer(value.getClass()).serialize(value, generator, serializers);
        }

        @Override
        public void serializeWithType(
                Object value, JsonGenerator generator, SerializerProvider serializers, TypeSerializer typeSerializer)
                throws IOException {
            serializers
                    .findValueSerializer(value.getClass())
                    .serializeWithType(value, generator, serializers, typeSerializer);
        }
    }
}
