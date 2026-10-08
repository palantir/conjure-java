package dialogue.com.palantir.product;

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
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.JacksonSupportGenerator")
final class ConjureJacksonSupport {
    private ConjureJacksonSupport() {}

    static final class ContainerDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
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

    static final class UnionDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
        private final int unionIndex;

        private final Class<?> unionClass;

        private final Class<?>[] variantTypes;

        private final AtomicReferenceArray<JsonDeserializer<?>> variantDeserializers;

        UnionDeserializer() {
            this(Object.class, new Class<?>[0], -1);
        }

        private UnionDeserializer(Class<?> unionClass, Class<?>[] variantTypes, int unionIndex) {
            this.unionIndex = unionIndex;
            this.unionClass = unionClass;
            this.variantTypes = variantTypes;
            this.variantDeserializers = new AtomicReferenceArray<>(variantTypes.length);
        }

        @Override
        public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty _property)
                throws JsonMappingException {
            JavaType type = context.getContextualType();
            if (type == null) {
                return context.reportBadDefinition(Object.class, "Union deserializer requires a contextual type");
            }
            if (type.hasRawClass(EmptyUnion.class)) {
                return new UnionDeserializer(EmptyUnion.class, new Class<?>[] {}, 0);
            }
            if (type.hasRawClass(SimpleUnion.class)) {
                return new UnionDeserializer(
                        SimpleUnion.class,
                        new Class<?>[] {SimpleUnion.Foo.class, SimpleUnion.Bar.class, SimpleUnion.Baz.class},
                        1);
            }
            if (type.hasRawClass(UnionExample.class)) {
                return new UnionDeserializer(
                        UnionExample.class,
                        new Class<?>[] {
                            UnionExample.StringVariant.class,
                            UnionExample.IntVariant.class,
                            UnionExample.ObjectVariant.class,
                            UnionExample.CollectionVariant.class,
                            UnionExample.OptionalVariant.class
                        },
                        2);
            }
            return context.reportBadDefinition(type, "Unsupported union type");
        }

        @Override
        public boolean isCachable() {
            return true;
        }

        @Override
        public Object deserialize(JsonParser parser, DeserializationContext context) throws IOException {
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

        private Object deserializeBuffered(
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

        private Object deserializeSelected(JsonParser parser, DeserializationContext context, String type)
                throws IOException {
            return switch (unionIndex) {
                case 0 -> EmptyUnion.deserializeUnion(parser, context, type, this);
                case 1 -> SimpleUnion.deserializeUnion(parser, context, type, this);
                case 2 -> UnionExample.deserializeUnion(parser, context, type, this);
                default -> context.reportBadDefinition(unionClass, "Union deserializer must be contextualized");
            };
        }

        Object deserializeVariant(JsonParser parser, DeserializationContext context, int variantIndex)
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

        Map<String, Object> deserializeUnknown(JsonParser parser, DeserializationContext context) throws IOException {
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

    static final class UnionSerializer extends JsonSerializer<Object> {
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
