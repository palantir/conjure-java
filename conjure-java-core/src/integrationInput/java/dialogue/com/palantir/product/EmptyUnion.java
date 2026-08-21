package dialogue.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonDeserialize(using = EmptyUnion.Deserializer.class)
@JsonSerialize(using = EmptyUnion.Serializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract sealed class EmptyUnion permits EmptyUnion.Unknown {
    public static EmptyUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    public abstract <T> T accept(Visitor<T> visitor);

    @JsonDeserialize
    @JsonSerialize
    public static final class Unknown extends EmptyUnion {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Unknown(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private Unknown(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        public String type() {
            return type;
        }

        @JsonAnyGetter
        public Map<String, Object> value() {
            return value;
        }

        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Unknown && equalTo((Unknown) other));
        }

        private boolean equalTo(Unknown other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            int hash = 1;
            hash = 31 * hash + this.type.hashCode();
            hash = 31 * hash + this.value.hashCode();
            return hash;
        }

        @Override
        public String toString() {
            return "EmptyUnion{value: UnknownWrapper{value: " + value + "}}";
        }
    }

    static final class Serializer extends JsonSerializer<EmptyUnion> {
        @Override
        public void serialize(EmptyUnion value, JsonGenerator generator, SerializerProvider serializers)
                throws IOException {
            serializers.findValueSerializer(value.getClass()).serialize(value, generator, serializers);
        }
    }

    static final class Deserializer extends JsonDeserializer<EmptyUnion> implements ResolvableDeserializer {
        private static final Class<?>[] VARIANT_TYPES = new Class<?>[] {};

        private volatile JsonDeserializer<?>[] deserializers;

        @Override
        public void resolve(DeserializationContext context) throws JsonMappingException {
            deserializers = new JsonDeserializer<?>[VARIANT_TYPES.length];
        }

        @Override
        public boolean isCachable() {
            return true;
        }

        @Override
        public EmptyUnion deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (!parser.isExpectedStartObjectToken()) {
                return context.reportInputMismatch(
                        EmptyUnion.class, "Expected a JSON object for union deserialization");
            }
            JsonToken firstToken = parser.nextToken();
            if (firstToken == JsonToken.FIELD_NAME && isTypeField(parser.currentName(), context)) {
                if (parser.nextToken() != JsonToken.VALUE_STRING) {
                    return context.reportInputMismatch(EmptyUnion.class, "Union discriminator 'type' must be a string");
                }
                String type = parser.getText();
                parser.nextToken();
                return deserializeSelected(parser, context, type);
            }
            return deserializeBuffered(parser, context);
        }

        private EmptyUnion deserializeBuffered(JsonParser parser, DeserializationContext context) throws IOException {
            try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
                buffer.writeStartObject();
                JsonToken token = parser.currentToken();
                while (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    JsonToken valueToken = parser.nextToken();
                    if (isTypeField(fieldName, context)) {
                        if (valueToken != JsonToken.VALUE_STRING) {
                            return context.reportInputMismatch(
                                    EmptyUnion.class, "Union discriminator 'type' must be a string");
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
                            EmptyUnion.class, "Expected the end of a JSON object while deserializing a union");
                }
            }
            return context.reportInputMismatch(EmptyUnion.class, "Union discriminator 'type' is required");
        }

        private static boolean isTypeField(String fieldName, DeserializationContext context) {
            return "type".equals(fieldName)
                    || (context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                            && "type".equalsIgnoreCase(fieldName));
        }

        private EmptyUnion deserializeSelected(JsonParser parser, DeserializationContext context, String type)
                throws IOException {
            int variantIndex =
                    switch (type) {
                        default -> -1;
                    };
            if (variantIndex < 0) {
                return deserializeUnknown(parser, context, type);
            }
            JsonDeserializer<?> deserializer = deserializers[variantIndex];
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, variantIndex);
            }
            return (EmptyUnion) deserializer.deserialize(parser, context);
        }

        private synchronized JsonDeserializer<?> resolveDeserializer(DeserializationContext context, int variantIndex)
                throws JsonMappingException {
            JsonDeserializer<?> deserializer = deserializers[variantIndex];
            if (deserializer == null) {
                deserializer = context.findRootValueDeserializer(context.constructType(VARIANT_TYPES[variantIndex]));
                JsonDeserializer<?>[] updated = deserializers.clone();
                updated[variantIndex] = deserializer;
                deserializers = updated;
            }
            return deserializer;
        }

        private static EmptyUnion deserializeUnknown(JsonParser parser, DeserializationContext context, String type)
                throws IOException {
            Map<String, Object> values = new HashMap<>();
            if (parser.currentToken() == JsonToken.START_OBJECT) {
                parser.nextToken();
            }
            while (parser.currentToken() == JsonToken.FIELD_NAME) {
                String fieldName = parser.currentName();
                parser.nextToken();
                values.put(fieldName, context.readValue(parser, Object.class));
                parser.nextToken();
            }
            if (parser.currentToken() != JsonToken.END_OBJECT) {
                return context.reportInputMismatch(
                        EmptyUnion.class, "Expected the end of a JSON object while deserializing a union");
            }
            return new Unknown(type, values);
        }
    }

    public interface Visitor<T> {
        T visitUnknown(@Safe String unknownType);

        static <T> UnknownStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<String, T> unknownVisitor;

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> throwOnUnknown() {
            this.unknownVisitor = unknownType -> {
                throw new SafeIllegalArgumentException(
                        "Unknown variant of the 'EmptyUnion' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
