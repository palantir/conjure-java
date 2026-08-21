package defensivenullablecollections.com.palantir.product;

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
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonDeserialize(using = ExampleDefensiveCollectionSetsUnion.Deserializer.class)
public final class ExampleDefensiveCollectionSetsUnion {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private ExampleDefensiveCollectionSetsUnion(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static ExampleDefensiveCollectionSetsUnion set(Set<String> value) {
        return new ExampleDefensiveCollectionSetsUnion(new SetWrapper(value));
    }

    public static ExampleDefensiveCollectionSetsUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "set":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: set");
            default:
                return new ExampleDefensiveCollectionSetsUnion(
                        new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveCollectionSetsUnion
                        && equalTo((ExampleDefensiveCollectionSetsUnion) other));
    }

    private boolean equalTo(ExampleDefensiveCollectionSetsUnion other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "ExampleDefensiveCollectionSetsUnion{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitSet(Set<String> value);

        T visitUnknown(@Safe String unknownType);

        static <T> SetStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements SetStageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<Set<String>, T> setVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor) {
            Preconditions.checkNotNull(setVisitor, "setVisitor cannot be null");
            this.setVisitor = setVisitor;
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
                        "Unknown variant of the 'ExampleDefensiveCollectionSetsUnion' union",
                        SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<Set<String>, T> setVisitor = this.setVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitSet(Set<String> value) {
                    return setVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface SetStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> set(@Nonnull Function<Set<String>, T> setVisitor);
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

    @JsonTypeName("set")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class SetWrapper implements Base {
        private final Set<String> value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private SetWrapper(
                @JsonSetter(value = "set", nulls = Nulls.AS_EMPTY) @JsonDeserialize(as = LinkedHashSet.class) @Nonnull
                        Set<String> value) {
            Preconditions.checkNotNull(value, "set cannot be null");
            this.value = Collections.unmodifiableSet(ConjureCollections.newSet(value));
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "set";
        }

        @JsonProperty("set")
        private Set<String> getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitSet(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof SetWrapper && equalTo((SetWrapper) other));
        }

        private boolean equalTo(SetWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SetWrapper{value: " + value + '}';
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

    static final class Deserializer extends JsonDeserializer<ExampleDefensiveCollectionSetsUnion>
            implements ResolvableDeserializer {
        private static final Class<?>[] VARIANT_TYPES = new Class<?>[] {SetWrapper.class};

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
        public ExampleDefensiveCollectionSetsUnion deserialize(JsonParser parser, DeserializationContext context)
                throws IOException {
            if (!parser.isExpectedStartObjectToken()) {
                return context.reportInputMismatch(
                        ExampleDefensiveCollectionSetsUnion.class, "Expected a JSON object for union deserialization");
            }
            JsonToken firstToken = parser.nextToken();
            if (firstToken == JsonToken.FIELD_NAME && isTypeField(parser.currentName(), context)) {
                if (parser.nextToken() != JsonToken.VALUE_STRING) {
                    return context.reportInputMismatch(
                            ExampleDefensiveCollectionSetsUnion.class, "Union discriminator 'type' must be a string");
                }
                String type = parser.getText();
                parser.nextToken();
                return deserializeSelected(parser, context, type);
            }
            return deserializeBuffered(parser, context);
        }

        private ExampleDefensiveCollectionSetsUnion deserializeBuffered(
                JsonParser parser, DeserializationContext context) throws IOException {
            try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
                buffer.writeStartObject();
                JsonToken token = parser.currentToken();
                while (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    JsonToken valueToken = parser.nextToken();
                    if (isTypeField(fieldName, context)) {
                        if (valueToken != JsonToken.VALUE_STRING) {
                            return context.reportInputMismatch(
                                    ExampleDefensiveCollectionSetsUnion.class,
                                    "Union discriminator 'type' must be a string");
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
                            ExampleDefensiveCollectionSetsUnion.class,
                            "Expected the end of a JSON object while deserializing a union");
                }
            }
            return context.reportInputMismatch(
                    ExampleDefensiveCollectionSetsUnion.class, "Union discriminator 'type' is required");
        }

        private static boolean isTypeField(String fieldName, DeserializationContext context) {
            return "type".equals(fieldName)
                    || (context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                            && "type".equalsIgnoreCase(fieldName));
        }

        private ExampleDefensiveCollectionSetsUnion deserializeSelected(
                JsonParser parser, DeserializationContext context, String type) throws IOException {
            int variantIndex =
                    switch (type) {
                        case "set" -> 0;
                        default -> -1;
                    };
            if (variantIndex < 0) {
                return deserializeUnknown(parser, context, type);
            }
            JsonDeserializer<?> deserializer = deserializers[variantIndex];
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, variantIndex);
            }
            return new ExampleDefensiveCollectionSetsUnion((Base) deserializer.deserialize(parser, context));
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

        private static ExampleDefensiveCollectionSetsUnion deserializeUnknown(
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
                        ExampleDefensiveCollectionSetsUnion.class,
                        "Expected the end of a JSON object while deserializing a union");
            }
            return new ExampleDefensiveCollectionSetsUnion(new UnknownWrapper(type, values));
        }
    }
}
