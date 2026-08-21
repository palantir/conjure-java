package jersey.com.palantir.product;

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
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonDeserialize(using = SimpleUnion.Deserializer.class)
public final class SimpleUnion {
    private final Base value;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private SimpleUnion(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static SimpleUnion foo(String value) {
        return new SimpleUnion(new FooWrapper(value));
    }

    public static SimpleUnion bar(int value) {
        return new SimpleUnion(new BarWrapper(value));
    }

    public static SimpleUnion baz(SafeLong value) {
        return new SimpleUnion(new BazWrapper(value));
    }

    public static SimpleUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: foo");
            case "bar":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: bar");
            case "baz":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: baz");
            default:
                return new SimpleUnion(new UnknownWrapper(type, Collections.singletonMap(type, value)));
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        return value.accept(visitor);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SimpleUnion && equalTo((SimpleUnion) other));
    }

    private boolean equalTo(SimpleUnion other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return "SimpleUnion{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        T visitBar(int value);

        T visitBaz(SafeLong value);

        T visitUnknown(@Safe String unknownType);

        static <T> BarStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements BarStageVisitorBuilder<T>,
                    BazStageVisitorBuilder<T>,
                    FooStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    Completed_StageVisitorBuilder<T> {
        private IntFunction<T> barVisitor;

        private Function<SafeLong, T> bazVisitor;

        private Function<String, T> fooVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public BazStageVisitorBuilder<T> bar(@Nonnull IntFunction<T> barVisitor) {
            Preconditions.checkNotNull(barVisitor, "barVisitor cannot be null");
            this.barVisitor = barVisitor;
            return this;
        }

        @Override
        public FooStageVisitorBuilder<T> baz(@Nonnull Function<SafeLong, T> bazVisitor) {
            Preconditions.checkNotNull(bazVisitor, "bazVisitor cannot be null");
            this.bazVisitor = bazVisitor;
            return this;
        }

        @Override
        public UnknownStageVisitorBuilder<T> foo(@Nonnull Function<String, T> fooVisitor) {
            Preconditions.checkNotNull(fooVisitor, "fooVisitor cannot be null");
            this.fooVisitor = fooVisitor;
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
                        "Unknown variant of the 'SimpleUnion' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final IntFunction<T> barVisitor = this.barVisitor;
            final Function<SafeLong, T> bazVisitor = this.bazVisitor;
            final Function<String, T> fooVisitor = this.fooVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitBar(int value) {
                    return barVisitor.apply(value);
                }

                @Override
                public T visitBaz(SafeLong value) {
                    return bazVisitor.apply(value);
                }

                @Override
                public T visitFoo(String value) {
                    return fooVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface BarStageVisitorBuilder<T> {
        BazStageVisitorBuilder<T> bar(@Nonnull IntFunction<T> barVisitor);
    }

    public interface BazStageVisitorBuilder<T> {
        FooStageVisitorBuilder<T> baz(@Nonnull Function<SafeLong, T> bazVisitor);
    }

    public interface FooStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> foo(@Nonnull Function<String, T> fooVisitor);
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

    @JsonTypeName("foo")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class FooWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private FooWrapper(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "foo";
        }

        @JsonProperty("foo")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFoo(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof FooWrapper && equalTo((FooWrapper) other));
        }

        private boolean equalTo(FooWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "FooWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class BarWrapper implements Base {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private BarWrapper(@JsonSetter("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "bar";
        }

        @JsonProperty("bar")
        private int getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBar(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof BarWrapper && equalTo((BarWrapper) other));
        }

        private boolean equalTo(BarWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "BarWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class BazWrapper implements Base {
        private final SafeLong value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private BazWrapper(@JsonSetter("baz") @Nonnull SafeLong value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "baz";
        }

        @JsonProperty("baz")
        private SafeLong getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBaz(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof BazWrapper && equalTo((BazWrapper) other));
        }

        private boolean equalTo(BazWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "BazWrapper{value: " + value + '}';
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

    static final class Deserializer extends JsonDeserializer<SimpleUnion> implements ResolvableDeserializer {
        private static final Class<?>[] VARIANT_TYPES =
                new Class<?>[] {FooWrapper.class, BarWrapper.class, BazWrapper.class};

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
        public SimpleUnion deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            if (!parser.isExpectedStartObjectToken()) {
                return context.reportInputMismatch(
                        SimpleUnion.class, "Expected a JSON object for union deserialization");
            }
            JsonToken firstToken = parser.nextToken();
            if (firstToken == JsonToken.FIELD_NAME && isTypeField(parser.currentName(), context)) {
                if (parser.nextToken() != JsonToken.VALUE_STRING) {
                    return context.reportInputMismatch(
                            SimpleUnion.class, "Union discriminator 'type' must be a string");
                }
                String type = parser.getText();
                parser.nextToken();
                return deserializeSelected(parser, context, type);
            }
            return deserializeBuffered(parser, context);
        }

        private SimpleUnion deserializeBuffered(JsonParser parser, DeserializationContext context) throws IOException {
            try (TokenBuffer buffer = context.bufferForInputBuffering(parser)) {
                buffer.writeStartObject();
                JsonToken token = parser.currentToken();
                while (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.currentName();
                    JsonToken valueToken = parser.nextToken();
                    if (isTypeField(fieldName, context)) {
                        if (valueToken != JsonToken.VALUE_STRING) {
                            return context.reportInputMismatch(
                                    SimpleUnion.class, "Union discriminator 'type' must be a string");
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
                            SimpleUnion.class, "Expected the end of a JSON object while deserializing a union");
                }
            }
            return context.reportInputMismatch(SimpleUnion.class, "Union discriminator 'type' is required");
        }

        private static boolean isTypeField(String fieldName, DeserializationContext context) {
            return "type".equals(fieldName)
                    || (context.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
                            && "type".equalsIgnoreCase(fieldName));
        }

        private SimpleUnion deserializeSelected(JsonParser parser, DeserializationContext context, String type)
                throws IOException {
            int variantIndex =
                    switch (type) {
                        case "foo" -> 0;
                        case "bar" -> 1;
                        case "baz" -> 2;
                        default -> -1;
                    };
            if (variantIndex < 0) {
                return deserializeUnknown(parser, context, type);
            }
            JsonDeserializer<?> deserializer = deserializers[variantIndex];
            if (deserializer == null) {
                deserializer = resolveDeserializer(context, variantIndex);
            }
            return new SimpleUnion((Base) deserializer.deserialize(parser, context));
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

        private static SimpleUnion deserializeUnknown(JsonParser parser, DeserializationContext context, String type)
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
                        SimpleUnion.class, "Expected the end of a JSON object while deserializing a union");
            }
            return new SimpleUnion(new UnknownWrapper(type, values));
        }
    }
}
