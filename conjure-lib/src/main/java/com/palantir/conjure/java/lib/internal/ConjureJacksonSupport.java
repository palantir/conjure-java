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
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
import com.fasterxml.jackson.databind.deser.impl.JDKValueInstantiators;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.util.AccessPattern;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;

/** Jackson support for generated code; not intended for application use. */
public final class ConjureJacksonSupport {
    private ConjureJacksonSupport() {}

    /**
     * Gives generated code ownership of maps and sets. Standard Jackson containers with scalar entries may be transferred
     * directly; other results are defensively copied. Generated code validates nulls and exposes an unmodifiable view.
     * Object-id references must resolve before their container entry is read; object-id entries in polymorphic containers
     * are unsupported.
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
            JsonDeserializer<?> delegate = resolveDelegate(context, type, property);
            // Custom constructors, entries, and problem handlers can expose shared containers.
            if (property.getMetadata().getMergeInfo() == null
                    && context.getConfig().getProblemHandlers() == null
                    && !hasTypeDeserializer(context, type)
                    && !hasTypeDeserializer(context, type.getContentType())
                    && (map ? isStandardMap(delegate, context, property) : isStandardSet(delegate, context))) {
                return delegate;
            }
            return new CopyingDeserializer(delegate, map);
        }

        private static JsonDeserializer<?> resolveDelegate(
                DeserializationContext context, JavaType type, BeanProperty property) throws JsonMappingException {
            JsonDeserializer<?> delegate = context.findContextualValueDeserializer(type, property);
            if (!(delegate instanceof ContainerDeserializerBase<?> container)) {
                return delegate;
            }
            JsonDeserializer<?> content = container.getContentDeserializer();
            if (content == null || content.getObjectIdReader() == null) {
                return delegate;
            }
            if (hasTypeDeserializer(context, type)) {
                // Subtype dispatch can replace the container deserializer and bypass its guarded content handler.
                return context.reportBadDefinition(
                        type, "Object-id entries in polymorphic owned containers are not supported");
            }
            // Jackson's deferred callbacks would otherwise populate the discarded container after it is copied.
            return context.findContextualValueDeserializer(
                    type.withContentValueHandler(new ResolvedReferenceDeserializer(content)), property);
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

        private static final class ResolvedReferenceDeserializer extends DelegatingDeserializer {
            ResolvedReferenceDeserializer(JsonDeserializer<?> delegate) {
                super(delegate);
            }

            @Override
            protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> delegate) {
                return new ResolvedReferenceDeserializer(delegate);
            }

            @Override
            public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                try {
                    return super.deserialize(parser, context);
                } catch (UnresolvedForwardReference e) {
                    return context.reportInputMismatch(
                            this, "Forward object-id references are not supported in owned containers");
                }
            }

            @Override
            public Object deserializeWithType(
                    JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer)
                    throws IOException {
                try {
                    return super.deserializeWithType(parser, context, typeDeserializer);
                } catch (UnresolvedForwardReference e) {
                    return context.reportInputMismatch(
                            this, "Forward object-id references are not supported in owned containers");
                }
            }
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
            public Object deserialize(JsonParser parser, DeserializationContext context, Object intoValue)
                    throws IOException {
                return copy(super.deserialize(parser, context, copy(intoValue)));
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

            @Override
            public AccessPattern getNullAccessPattern() {
                return copyingAccessPattern(super.getNullAccessPattern());
            }

            @Override
            public AccessPattern getEmptyAccessPattern() {
                return copyingAccessPattern(super.getEmptyAccessPattern());
            }

            private static AccessPattern copyingAccessPattern(AccessPattern delegatePattern) {
                return delegatePattern == AccessPattern.ALWAYS_NULL ? AccessPattern.ALWAYS_NULL : AccessPattern.DYNAMIC;
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
     * instances for each mapper; do not share deserializer instances between mappers. Custom variant deserializers receive
     * an object starting at START_OBJECT with the discriminator removed.
     */
    public abstract static class UnionDeserializer<T> extends JsonDeserializer<T> implements ContextualDeserializer {
        private static final int MAX_CACHED_TYPE_LENGTH = 64;

        private final Class<T> unionClass;
        private final List<Class<? extends T>> variantTypes;
        private final AtomicReferenceArray<JsonDeserializer<? extends T>> variantDeserializers;
        private final ToIntFunction<String> variantIndex;
        private final BiFunction<String, Map<String, Object>, ? extends T> unknownFactory;
        private final BeanProperty property;
        private volatile String cachedType;
        private volatile JsonDeserializer<Object> unknownValueDeserializer;

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
            this.property = null;
        }

        private UnionDeserializer(UnionDeserializer<T> source, BeanProperty property) {
            this.unionClass = source.unionClass;
            this.variantTypes = source.variantTypes;
            this.variantDeserializers = new AtomicReferenceArray<>(variantTypes.size());
            this.variantIndex = source.variantIndex;
            this.unknownFactory = source.unknownFactory;
            this.property = property;
        }

        @Override
        @SuppressWarnings("ReferenceEquality") // Contextual caches belong to the exact Jackson property instance.
        public final JsonDeserializer<?> createContextual(DeserializationContext _context, BeanProperty beanProperty) {
            return property == beanProperty ? this : new ContextualUnionDeserializer<>(this, beanProperty);
        }

        @Override
        public final boolean isCachable() {
            return true;
        }

        @Override
        public final T deserialize(JsonParser inputParser, DeserializationContext context) throws IOException {
            JsonParser parser = ObjectStartParser.sourceParser(inputParser);
            if (parser instanceof BufferedUnionParser buffered) {
                return deserializeBufferedObject(buffered, context);
            }
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
                String type = readType(parser);
                parser.nextToken();
                return deserializeSelected(parser, context, type, true);
            }
            return deserializeBufferedPrefix(parser, context, acceptCaseInsensitiveProperties);
        }

        private String readType(JsonParser parser) throws IOException {
            String cached = cachedType;
            if (cached != null && parser.hasTextCharacters() && parser.getTextLength() == cached.length()) {
                char[] characters = parser.getTextCharacters();
                int offset = parser.getTextOffset();
                for (int i = 0; i < cached.length(); i++) {
                    if (characters[offset + i] != cached.charAt(i)) {
                        return parser.getText();
                    }
                }
                parser.streamReadConstraints().validateStringLength(cached.length());
                return cached;
            }
            return parser.getText();
        }

        private T deserializeBufferedPrefix(
                JsonParser parser, DeserializationContext context, boolean acceptCaseInsensitiveProperties)
                throws IOException {
            try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
                List<BufferedObject> objects = bufferPrefix(parser, context, buffer, acceptCaseInsensitiveProperties);
                try (BufferedUnionParser buffered = new BufferedUnionParser(buffer.asParser(parser), parser, objects)) {
                    buffered.nextToken();
                    return deserializeBufferedObject(buffered, context);
                }
            }
        }

        private List<BufferedObject> bufferPrefix(
                JsonParser parser,
                DeserializationContext context,
                TokenBuffer buffer,
                boolean acceptCaseInsensitiveProperties)
                throws IOException {
            buffer.writeStartObject();
            List<BufferedObject> objects = new ArrayList<>();
            BufferedObject object = new BufferedObject(null);
            objects.add(object);
            BufferedObject discriminatorValue = null;
            int tokenIndex = 1;
            JsonToken token = parser.currentToken();
            while (token != null) {
                if (discriminatorValue != null) {
                    if (bufferDiscriminatorValue(parser, context, buffer, discriminatorValue)) {
                        return objects;
                    }
                    discriminatorValue = null;
                }
                if (token == JsonToken.START_OBJECT) {
                    object = new BufferedObject(object);
                    objects.add(object);
                } else if (token == JsonToken.END_OBJECT) {
                    object = object.parent;
                } else if (token == JsonToken.FIELD_NAME
                        && object.typeFieldIndex < 0
                        && isTypeField(parser.currentName(), acceptCaseInsensitiveProperties)) {
                    object.typeFieldIndex = tokenIndex;
                    discriminatorValue = object;
                }
                copyBufferedEvent(parser, buffer);
                if (object == null) {
                    return objects;
                }
                token = parser.nextToken();
                tokenIndex++;
            }
            return context.reportInputMismatch(
                    unionClass, "Expected the end of a JSON object while deserializing a union");
        }

        private static void copyBufferedEvent(JsonParser parser, TokenBuffer buffer) throws IOException {
            if (parser.hasToken(JsonToken.VALUE_STRING)) {
                // TokenBuffer may copy character arrays without calling the parser's length-validating getText().
                parser.streamReadConstraints().validateStringLength(parser.getTextLength());
            }
            buffer.copyCurrentEvent(parser);
        }

        private boolean bufferDiscriminatorValue(
                JsonParser parser, DeserializationContext context, TokenBuffer buffer, BufferedObject object)
                throws IOException {
            object.readType(parser);
            if (object.parent != null) {
                return false;
            }
            if (object.typeToken != JsonToken.VALUE_STRING) {
                return context.reportInputMismatch(unionClass, "Union discriminator 'type' must be a string");
            }
            buffer.copyCurrentEvent(parser);
            parser.nextToken();
            return true;
        }

        private T deserializeBufferedObject(BufferedUnionParser parser, DeserializationContext context)
                throws IOException {
            JsonToken token = parser.currentToken();
            if (token != JsonToken.START_OBJECT && token != JsonToken.FIELD_NAME) {
                return context.reportInputMismatch(unionClass, "Union discriminator 'type' is required");
            }
            BufferedObject object = parser.object;
            if (object.typeFieldIndex < 0) {
                return context.reportInputMismatch(unionClass, "Union discriminator 'type' is required");
            }
            if (object.typeToken != JsonToken.VALUE_STRING) {
                return context.reportInputMismatch(unionClass, "Union discriminator 'type' must be a string");
            }
            object.skipDiscriminator = true;
            if (token == JsonToken.START_OBJECT) {
                return deserializeSelected(parser, context, object.type, false);
            }
            if (parser.tokenIndex == object.typeFieldIndex) {
                parser.nextToken();
                parser.nextToken();
            }
            return deserializeSelected(parser, context, object.type, true);
        }

        private static boolean isTypeField(String fieldName, boolean acceptCaseInsensitiveProperties) {
            return "type".equals(fieldName) || (acceptCaseInsensitiveProperties && "type".equalsIgnoreCase(fieldName));
        }

        private T deserializeSelected(
                JsonParser parser, DeserializationContext context, String type, boolean prefixObject)
                throws IOException {
            int index = variantIndex.applyAsInt(type);
            if (index < 0) {
                return unknownFactory.apply(type, deserializeUnknown(parser, context));
            }
            JsonDeserializer<? extends T> deserializer = variantDeserializers.get(index);
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, index);
                // Retain one bounded discriminator to avoid String allocation on repeated reads.
                if (cachedType == null && type.length() <= MAX_CACHED_TYPE_LENGTH) {
                    cachedType = type;
                }
            }
            // Jackson's standard bean deserializer accepts FIELD_NAME/END_OBJECT directly.
            if (prefixObject && deserializer.getClass() != BeanDeserializer.class) {
                try (JsonParser prefix = ObjectStartParser.OBJECT_START.asParser(parser)) {
                    JsonParser combined = new ObjectStartParser(prefix, parser);
                    combined.nextToken();
                    return deserializer.deserialize(combined, context);
                }
            }
            return deserializer.deserialize(parser, context);
        }

        @SuppressWarnings("unchecked") // The constructor restricts variant classes to subtypes of T.
        private JsonDeserializer<? extends T> resolveDeserializer(DeserializationContext context, int index)
                throws JsonMappingException {
            JsonDeserializer<? extends T> deserializer = (JsonDeserializer<? extends T>)
                    context.findContextualValueDeserializer(context.constructType(variantTypes.get(index)), property);
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
                    valueDeserializer = unknownValueDeserializer;
                    if (valueDeserializer == null) {
                        valueDeserializer = context.findRootValueDeserializer(context.constructType(Object.class));
                        unknownValueDeserializer = valueDeserializer;
                    }
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

    private static final class ContextualUnionDeserializer<T> extends UnionDeserializer<T> {
        ContextualUnionDeserializer(UnionDeserializer<T> source, BeanProperty property) {
            super(source, property);
        }
    }

    private static class ObjectStartParser extends JsonParserSequence {
        // Written once, then shared read-only. Each reader gets its own parser and parsing context.
        private static final TokenBuffer OBJECT_START = objectStart();

        ObjectStartParser(JsonParser prefix, JsonParser parser) {
            super(true, new JsonParser[] {prefix, sourceParser(parser)});
        }

        private static TokenBuffer objectStart() {
            TokenBuffer buffer = new TokenBuffer(null, false);
            try {
                buffer.writeStartObject();
            } catch (IOException e) {
                throw new SafeIllegalStateException("Failed to initialize union object prefix", e);
            }
            return buffer;
        }

        static JsonParser sourceParser(JsonParser parser) {
            JsonParser source = parser;
            // Drop exhausted prefixes so recursive streaming reads do not chain parser delegates.
            while (source instanceof ObjectStartParser prefix && prefix._nextParserIndex == prefix._parsers.length) {
                source = prefix.delegate();
            }
            return source;
        }

        @Override
        public JsonToken nextValue() throws IOException {
            JsonToken token = nextToken();
            return token == JsonToken.FIELD_NAME ? nextToken() : token;
        }
    }

    private static final class BufferedObject {
        private final BufferedObject parent;
        private int typeFieldIndex = -1;
        private JsonToken typeToken;
        private String type;
        private boolean skipDiscriminator;

        BufferedObject(BufferedObject parent) {
            this.parent = parent;
        }

        void readType(JsonParser parser) throws IOException {
            typeToken = parser.currentToken();
            if (typeToken == JsonToken.VALUE_STRING) {
                type = parser.getText();
            }
        }
    }

    /** Indexes objects in encounter order so recursive unions reuse the same token buffer. */
    private static final class BufferedUnionParser extends ObjectStartParser {
        private final List<BufferedObject> objects;
        private BufferedObject object;
        private int nextObject;
        private int tokenIndex = -1;

        BufferedUnionParser(JsonParser prefix, JsonParser parser, List<BufferedObject> objects) {
            super(prefix, parser);
            this.objects = objects;
        }

        @Override
        public void close() throws IOException {
            // The suffix parser is owned by the caller.
            _parsers[0].close();
        }

        @Override
        public JsonToken nextToken() throws IOException {
            JsonToken token = super.nextToken();
            // Only the buffered prefix has indexed object metadata.
            if (_nextParserIndex == _parsers.length) {
                return token;
            }
            tokenIndex++;
            if (token == JsonToken.START_OBJECT) {
                object = objects.get(nextObject++);
            } else if (token == JsonToken.END_OBJECT) {
                object = object.parent;
            } else if (token == JsonToken.FIELD_NAME
                    && object.skipDiscriminator
                    && tokenIndex == object.typeFieldIndex) {
                super.nextToken();
                tokenIndex++;
                return nextToken();
            }
            return token;
        }

        @Override
        public JsonToken nextValue() throws IOException {
            JsonToken token = nextToken();
            return token == JsonToken.FIELD_NAME ? nextToken() : token;
        }

        @Override
        public JsonParser skipChildren() throws IOException {
            JsonToken token = currentToken();
            if (token == JsonToken.START_OBJECT || token == JsonToken.START_ARRAY) {
                int depth = 1;
                while ((token = nextToken()) != null) {
                    if (token.isStructStart()) {
                        depth++;
                    } else if (token.isStructEnd() && --depth == 0) {
                        break;
                    }
                }
            }
            return this;
        }
    }

    /** Internal Jackson serializer that delegates from a sealed union to its concrete variant. */
    public static final class UnionSerializer extends JsonSerializer<Object> implements ContextualSerializer {
        private final BeanProperty property;
        private volatile PropertySerializerMap variants = PropertySerializerMap.emptyForProperties();
        private volatile ConcurrentMap<Class<?>, JsonSerializer<Object>> overflowVariants;

        public UnionSerializer() {
            this(null);
        }

        private UnionSerializer(BeanProperty property) {
            this.property = property;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider _provider, BeanProperty beanProperty) {
            return new UnionSerializer(beanProperty);
        }

        private JsonSerializer<Object> variantSerializer(Object value, SerializerProvider provider)
                throws JsonMappingException {
            Class<?> type = value.getClass();
            PropertySerializerMap snapshot = variants;
            JsonSerializer<Object> serializer = snapshot.serializerFor(type);
            return serializer == null ? resolveVariantSerializer(type, snapshot, provider) : serializer;
        }

        @SuppressWarnings("ReferenceEquality") // Jackson returns the same map when its bounded cache is full.
        private JsonSerializer<Object> resolveVariantSerializer(
                Class<?> type, PropertySerializerMap snapshot, SerializerProvider provider)
                throws JsonMappingException {
            ConcurrentMap<Class<?>, JsonSerializer<Object>> overflow = overflowVariants;
            if (overflow != null) {
                JsonSerializer<Object> serializer = overflow.get(type);
                if (serializer != null) {
                    return serializer;
                }
            }
            JsonSerializer<Object> serializer = provider.findValueSerializer(type, property);
            PropertySerializerMap updated = snapshot.newWith(type, serializer);
            if (updated == snapshot) {
                return cacheOverflow(type, serializer);
            }
            variants = updated;
            return serializer;
        }

        private synchronized JsonSerializer<Object> cacheOverflow(Class<?> type, JsonSerializer<Object> serializer) {
            if (overflowVariants == null) {
                overflowVariants = new ConcurrentHashMap<>();
            }
            JsonSerializer<Object> existing = overflowVariants.putIfAbsent(type, serializer);
            return existing == null ? serializer : existing;
        }

        @Override
        public void serialize(Object value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            variantSerializer(value, serializers).serialize(value, generator, serializers);
        }

        @Override
        public void serializeWithType(
                Object value, JsonGenerator generator, SerializerProvider serializers, TypeSerializer typeSerializer)
                throws IOException {
            variantSerializer(value, serializers).serializeWithType(value, generator, serializers, typeSerializer);
        }
    }
}
