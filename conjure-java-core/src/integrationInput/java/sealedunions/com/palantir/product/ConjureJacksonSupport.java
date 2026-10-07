package sealedunions.com.palantir.product;

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
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReferenceArray;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.JacksonSupportGenerator")
final class ConjureJacksonSupport {
    private ConjureJacksonSupport() {}

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
            if (type.hasRawClass(CamelCaseUnion.class)) {
                return new UnionDeserializer(
                        CamelCaseUnion.class, new Class<?>[] {CamelCaseUnion.CamelCasedField.class}, 0);
            }
            if (type.hasRawClass(EmptyUnion.class)) {
                return new UnionDeserializer(EmptyUnion.class, new Class<?>[] {}, 1);
            }
            if (type.hasRawClass(NestedEmptyUnion.class)) {
                return new UnionDeserializer(NestedEmptyUnion.class, new Class<?>[] {NestedEmptyUnion.Empty.class}, 2);
            }
            if (type.hasRawClass(SimpleUnion.class)) {
                return new UnionDeserializer(
                        SimpleUnion.class,
                        new Class<?>[] {SimpleUnion.Foo.class, SimpleUnion.Bar.class, SimpleUnion.Baz.class},
                        3);
            }
            if (type.hasRawClass(UnionReservedNames.class)) {
                return new UnionDeserializer(
                        UnionReservedNames.class,
                        new Class<?>[] {
                            UnionReservedNames.Known_.class,
                            UnionReservedNames.Unknown_.class,
                            UnionReservedNames.If.class,
                            UnionReservedNames.New.class,
                            UnionReservedNames.Interface.class,
                            UnionReservedNames.Void.class,
                            UnionReservedNames.Return.class,
                            UnionReservedNames.Private.class,
                            UnionReservedNames.Public.class,
                            UnionReservedNames.Int.class,
                            UnionReservedNames.Import.class,
                            UnionReservedNames.Final.class,
                            UnionReservedNames.Throws.class,
                            UnionReservedNames.Static.class,
                            UnionReservedNames.UnionReservedNames_.class
                        },
                        4);
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
                case 0 -> CamelCaseUnion.deserializeUnion(parser, context, type, this);
                case 1 -> EmptyUnion.deserializeUnion(parser, context, type, this);
                case 2 -> NestedEmptyUnion.deserializeUnion(parser, context, type, this);
                case 3 -> SimpleUnion.deserializeUnion(parser, context, type, this);
                case 4 -> UnionReservedNames.deserializeUnion(parser, context, type, this);
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
