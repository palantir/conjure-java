package allexamples.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

/** A union of a safe long. */
@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonDeserialize(using = ExternalLongUnionExample.Deserializer.class)
public final class ExternalLongUnionExample {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private ExternalLongUnionExample(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static ExternalLongUnionExample safeLong(@Safe long value) {
        return new ExternalLongUnionExample(new SafeLongWrapper(value));
    }

    public static ExternalLongUnionExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "safeLong":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: safeLong");
            default:
                return new ExternalLongUnionExample(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExternalLongUnionExample && equalTo((ExternalLongUnionExample) other));
    }

    private boolean equalTo(ExternalLongUnionExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "ExternalLongUnionExample{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitSafeLong(@Safe long value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> SafeLongStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements SafeLongStageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<@Safe Long, T> safeLongVisitor;

        private BiFunction<@Safe String, Object, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> safeLong(@Nonnull Function<@Safe Long, T> safeLongVisitor) {
            Preconditions.checkNotNull(safeLongVisitor, "safeLongVisitor cannot be null");
            this.safeLongVisitor = safeLongVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = (unknownType, _unknownValue) -> unknownVisitor.apply(unknownType);
            return this;
        }

        @Override
        public Completed_StageVisitorBuilder<T> throwOnUnknown() {
            this.unknownVisitor = (unknownType, _unknownValue) -> {
                throw new SafeIllegalArgumentException(
                        "Unknown variant of the 'ExternalLongUnionExample' union",
                        SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<@Safe Long, T> safeLongVisitor = this.safeLongVisitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitSafeLong(@Safe long value) {
                    return safeLongVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
                }
            };
        }
    }

    public interface SafeLongStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> safeLong(@Nonnull Function<@Safe Long, T> safeLongVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }

    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("safeLong")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class SafeLongWrapper implements Base {
        private final long value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SafeLongWrapper(@JsonSetter("safeLong") @Nonnull long value) {
            Preconditions.checkNotNull(value, "safeLong cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "safeLong";
        }

        @JsonProperty("safeLong")
        private long getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSafeLong(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof SafeLongWrapper && equalTo((SafeLongWrapper) other));
        }

        private boolean equalTo(SafeLongWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "SafeLongWrapper{value: " + value + '}';
        }
    }

    private static final class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownWrapper(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownWrapper(@Nonnull String type, @Nonnull Map<String, Object> value) {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "value cannot be null");
            this.type = type;
            this.value = value;
        }

        @JsonProperty
        private String getType() {
            return type;
        }

        @JsonAnyGetter
        private Map<String, Object> getValue() {
            return value;
        }

        @JsonAnySetter
        private void put(String key, Object val) {
            value.put(key, val);
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnknown(type, value.get(type));
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
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
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }

    static final class Deserializer extends JsonDeserializer<ExternalLongUnionExample>
            implements ResolvableDeserializer {
        private static final Class<?>[] VARIANT_TYPES = new Class<?>[] {SafeLongWrapper.class};

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
        public ExternalLongUnionExample deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {
            if (!parser.isExpectedStartObjectToken()) {
                return context.reportInputMismatch(
                        ExternalLongUnionExample.class, "Expected a JSON object for union deserialization");
            }
            JsonToken firstToken = parser.nextToken();
            if (firstToken == JsonToken.FIELD_NAME && isTypeField(parser.currentName(), context)) {
                if (parser.nextToken() != JsonToken.VALUE_STRING) {
                    return context.reportInputMismatch(
                            ExternalLongUnionExample.class, "Union discriminator 'type' must be a string");
                }
                String type = parser.getText();
                parser.nextToken();
                return deserializeSelected(parser, context, type);
            }
            return deserializeBuffered(parser, context);
        }

        private ExternalLongUnionExample deserializeBuffered(JsonParser parser, DeserializationContext context)
                throws IOException {
            try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
                buffer.writeStartObject();
                JsonToken token = parser.currentToken();
                while (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    JsonToken valueToken = parser.nextToken();
                    if (isTypeField(fieldName, context)) {
                        if (valueToken != JsonToken.VALUE_STRING) {
                            return context.reportInputMismatch(
                                    ExternalLongUnionExample.class, "Union discriminator 'type' must be a string");
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
                            ExternalLongUnionExample.class,
                            "Expected the end of a JSON object while deserializing a union");
                }
            }
            return context.reportInputMismatch(
                    ExternalLongUnionExample.class, "Union discriminator 'type' is required");
        }

        private static boolean isTypeField(String fieldName, DeserializationContext context) {
            return "type".equals(fieldName)
                    || (context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                            && "type".equalsIgnoreCase(fieldName));
        }

        private ExternalLongUnionExample deserializeSelected(
                JsonParser parser, DeserializationContext context, String type) throws IOException {
            int variantIndex =
                    switch (type) {
                        case "safeLong" -> 0;
                        default -> -1;
                    };
            if (variantIndex < 0) {
                return deserializeUnknown(parser, context, type);
            }
            JsonDeserializer<?> deserializer = deserializers[variantIndex];
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, variantIndex);
            }
            return new ExternalLongUnionExample((Base) deserializer.deserialize(parser, context));
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

        private static ExternalLongUnionExample deserializeUnknown(
                JsonParser parser, DeserializationContext context, String type) throws IOException {
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
                        ExternalLongUnionExample.class,
                        "Expected the end of a JSON object while deserializing a union");
            }
            return new ExternalLongUnionExample(new UnknownWrapper(type, values));
        }
    }
}
