package errors.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.Nulls;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonDeserialize(using = UnionExample.Deserializer.class)
public final class UnionExample {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private UnionExample(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static UnionExample stringVariant(String value) {
        return new UnionExample(new StringVariantWrapper(value));
    }

    public static UnionExample intVariant(int value) {
        return new UnionExample(new IntVariantWrapper(value));
    }

    public static UnionExample objectVariant(ObjectReference value) {
        return new UnionExample(new ObjectVariantWrapper(value));
    }

    public static UnionExample collectionVariant(List<String> value) {
        return new UnionExample(new CollectionVariantWrapper(value));
    }

    public static UnionExample optionalVariant(Optional<String> value) {
        return new UnionExample(new OptionalVariantWrapper(value));
    }

    public static UnionExample unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "stringVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: stringVariant");
            case "intVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: intVariant");
            case "objectVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: objectVariant");
            case "collectionVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: collectionVariant");
            case "optionalVariant":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: optionalVariant");
            default:
                return new UnionExample(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof UnionExample && equalTo((UnionExample) other));
    }

    private boolean equalTo(UnionExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "UnionExample{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitStringVariant(String value);

        T visitIntVariant(int value);

        T visitObjectVariant(ObjectReference value);

        T visitCollectionVariant(List<String> value);

        T visitOptionalVariant(Optional<String> value);

        T visitUnknown(@Safe String unknownType);

        static <T> CollectionVariantStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements CollectionVariantStageVisitorBuilder<T>,
                    IntVariantStageVisitorBuilder<T>,
                    ObjectVariantStageVisitorBuilder<T>,
                    OptionalVariantStageVisitorBuilder<T>,
                    StringVariantStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private Function<List<String>, T> collectionVariantVisitor;

        private IntFunction<T> intVariantVisitor;

        private Function<ObjectReference, T> objectVariantVisitor;

        private Function<Optional<String>, T> optionalVariantVisitor;

        private Function<String, T> stringVariantVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public IntVariantStageVisitorBuilder<T> collectionVariant(
                @Nonnull Function<List<String>, T> collectionVariantVisitor) {
            Preconditions.checkNotNull(collectionVariantVisitor, "collectionVariantVisitor cannot be null");
            this.collectionVariantVisitor = collectionVariantVisitor;
            return this;
        }

        @Override
        public ObjectVariantStageVisitorBuilder<T> intVariant(@Nonnull IntFunction<T> intVariantVisitor) {
            Preconditions.checkNotNull(intVariantVisitor, "intVariantVisitor cannot be null");
            this.intVariantVisitor = intVariantVisitor;
            return this;
        }

        @Override
        public OptionalVariantStageVisitorBuilder<T> objectVariant(
                @Nonnull Function<ObjectReference, T> objectVariantVisitor) {
            Preconditions.checkNotNull(objectVariantVisitor, "objectVariantVisitor cannot be null");
            this.objectVariantVisitor = objectVariantVisitor;
            return this;
        }

        @Override
        public StringVariantStageVisitorBuilder<T> optionalVariant(
                @Nonnull Function<Optional<String>, T> optionalVariantVisitor) {
            Preconditions.checkNotNull(optionalVariantVisitor, "optionalVariantVisitor cannot be null");
            this.optionalVariantVisitor = optionalVariantVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> stringVariant(@Nonnull Function<String, T> stringVariantVisitor) {
            Preconditions.checkNotNull(stringVariantVisitor, "stringVariantVisitor cannot be null");
            this.stringVariantVisitor = stringVariantVisitor;
            return this;
        }

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
                        "Unknown variant of the 'UnionExample' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<List<String>, T> collectionVariantVisitor = this.collectionVariantVisitor;
            final IntFunction<T> intVariantVisitor = this.intVariantVisitor;
            final Function<ObjectReference, T> objectVariantVisitor = this.objectVariantVisitor;
            final Function<Optional<String>, T> optionalVariantVisitor = this.optionalVariantVisitor;
            final Function<String, T> stringVariantVisitor = this.stringVariantVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitCollectionVariant(List<String> value) {
                    return collectionVariantVisitor.apply(value);
                }

                @Override
                public T visitIntVariant(int value) {
                    return intVariantVisitor.apply(value);
                }

                @Override
                public T visitObjectVariant(ObjectReference value) {
                    return objectVariantVisitor.apply(value);
                }

                @Override
                public T visitOptionalVariant(Optional<String> value) {
                    return optionalVariantVisitor.apply(value);
                }

                @Override
                public T visitStringVariant(String value) {
                    return stringVariantVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface CollectionVariantStageVisitorBuilder<T> {
        IntVariantStageVisitorBuilder<T> collectionVariant(@Nonnull Function<List<String>, T> collectionVariantVisitor);
    }

    public interface IntVariantStageVisitorBuilder<T> {
        ObjectVariantStageVisitorBuilder<T> intVariant(@Nonnull IntFunction<T> intVariantVisitor);
    }

    public interface ObjectVariantStageVisitorBuilder<T> {
        OptionalVariantStageVisitorBuilder<T> objectVariant(@Nonnull Function<ObjectReference, T> objectVariantVisitor);
    }

    public interface OptionalVariantStageVisitorBuilder<T> {
        StringVariantStageVisitorBuilder<T> optionalVariant(
                @Nonnull Function<Optional<String>, T> optionalVariantVisitor);
    }

    public interface StringVariantStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> stringVariant(@Nonnull Function<String, T> stringVariantVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }

    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("stringVariant")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class StringVariantWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private StringVariantWrapper(@JsonSetter("stringVariant") @Nonnull String value) {
            Preconditions.checkNotNull(value, "stringVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "stringVariant";
        }

        @JsonProperty("stringVariant")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitStringVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof StringVariantWrapper && equalTo((StringVariantWrapper) other));
        }

        private boolean equalTo(StringVariantWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "StringVariantWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("intVariant")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class IntVariantWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private IntVariantWrapper(@JsonSetter("intVariant") @Nonnull int value) {
            Preconditions.checkNotNull(value, "intVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "intVariant";
        }

        @JsonProperty("intVariant")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitIntVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof IntVariantWrapper && equalTo((IntVariantWrapper) other));
        }

        private boolean equalTo(IntVariantWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "IntVariantWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("objectVariant")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class ObjectVariantWrapper implements Base {
        private final ObjectReference value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ObjectVariantWrapper(@JsonSetter("objectVariant") @Nonnull ObjectReference value) {
            Preconditions.checkNotNull(value, "objectVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "objectVariant";
        }

        @JsonProperty("objectVariant")
        private ObjectReference getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitObjectVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ObjectVariantWrapper && equalTo((ObjectVariantWrapper) other));
        }

        private boolean equalTo(ObjectVariantWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ObjectVariantWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("collectionVariant")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class CollectionVariantWrapper implements Base {
        private final List<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private CollectionVariantWrapper(
                @JsonSetter(value = "collectionVariant", nulls = Nulls.AS_EMPTY) @Nonnull List<String> value) {
            Preconditions.checkNotNull(value, "collectionVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "collectionVariant";
        }

        @JsonProperty("collectionVariant")
        private List<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitCollectionVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other
                    || (other instanceof CollectionVariantWrapper && equalTo((CollectionVariantWrapper) other));
        }

        private boolean equalTo(CollectionVariantWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "CollectionVariantWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("optionalVariant")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class OptionalVariantWrapper implements Base {
        private final Optional<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private OptionalVariantWrapper(
                @JsonSetter(value = "optionalVariant", nulls = Nulls.AS_EMPTY) @Nonnull Optional<String> value) {
            Preconditions.checkNotNull(value, "optionalVariant cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "optionalVariant";
        }

        @JsonProperty("optionalVariant")
        private Optional<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitOptionalVariant(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other
                    || (other instanceof OptionalVariantWrapper && equalTo((OptionalVariantWrapper) other));
        }

        private boolean equalTo(OptionalVariantWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "OptionalVariantWrapper{value: " + value + '}';
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
            return visitor.visitUnknown(type);
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

    static final class Deserializer extends JsonDeserializer<UnionExample> implements ResolvableDeserializer {
        private static final Class<?>[] VARIANT_TYPES = new Class<?>[] {
            StringVariantWrapper.class,
            IntVariantWrapper.class,
            ObjectVariantWrapper.class,
            CollectionVariantWrapper.class,
            OptionalVariantWrapper.class
        };

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
        public UnionExample deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (!parser.isExpectedStartObjectToken()) {
                return context.reportInputMismatch(
                        UnionExample.class, "Expected a JSON object for union deserialization");
            }
            JsonToken firstToken = parser.nextToken();
            if (firstToken == JsonToken.FIELD_NAME && isTypeField(parser.currentName(), context)) {
                if (parser.nextToken() != JsonToken.VALUE_STRING) {
                    return context.reportInputMismatch(
                            UnionExample.class, "Union discriminator 'type' must be a string");
                }
                String type = parser.getText();
                parser.nextToken();
                return deserializeSelected(parser, context, type);
            }
            return deserializeBuffered(parser, context);
        }

        private UnionExample deserializeBuffered(JsonParser parser, DeserializationContext context) throws IOException {
            try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
                buffer.writeStartObject();
                JsonToken token = parser.currentToken();
                while (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    JsonToken valueToken = parser.nextToken();
                    if (isTypeField(fieldName, context)) {
                        if (valueToken != JsonToken.VALUE_STRING) {
                            return context.reportInputMismatch(
                                    UnionExample.class, "Union discriminator 'type' must be a string");
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
                            UnionExample.class, "Expected the end of a JSON object while deserializing a union");
                }
            }
            return context.reportInputMismatch(UnionExample.class, "Union discriminator 'type' is required");
        }

        private static boolean isTypeField(String fieldName, DeserializationContext context) {
            return "type".equals(fieldName)
                    || (context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                            && "type".equalsIgnoreCase(fieldName));
        }

        private UnionExample deserializeSelected(JsonParser parser, DeserializationContext context, String type)
                throws IOException {
            int variantIndex =
                    switch (type) {
                        case "stringVariant" -> 0;
                        case "intVariant" -> 1;
                        case "objectVariant" -> 2;
                        case "collectionVariant" -> 3;
                        case "optionalVariant" -> 4;
                        default -> -1;
                    };
            if (variantIndex < 0) {
                return deserializeUnknown(parser, context, type);
            }
            JsonDeserializer<?> deserializer = deserializers[variantIndex];
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, variantIndex);
            }
            return new UnionExample((Base) deserializer.deserialize(parser, context));
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

        private static UnionExample deserializeUnknown(JsonParser parser, DeserializationContext context, String type)
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
                        UnionExample.class, "Expected the end of a JSON object while deserializing a union");
            }
            return new UnionExample(new UnknownWrapper(type, values));
        }
    }
}
