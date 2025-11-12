package exceptionthrowingdialogueinterfaces.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ConjureGenerated("com.palantir.conjure.java.types.UnionGenerator")
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

    public static SimpleUnion value(@Safe String value) {
        return new SimpleUnion(new ValueWrapper(value));
    }

    public static SimpleUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "value":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: value");
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
        T visitValue(@Safe String value);

        T visitUnknown(@Safe String unknownType);

        static <T> ValueStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements ValueStageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<@Safe String, T> valueVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> value(@Nonnull Function<@Safe String, T> valueVisitor) {
            Preconditions.checkNotNull(valueVisitor, "valueVisitor cannot be null");
            this.valueVisitor = valueVisitor;
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
            final Function<@Safe String, T> valueVisitor = this.valueVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitValue(@Safe String value) {
                    return valueVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String value) {
                    return unknownVisitor.apply(value);
                }
            };
        }
    }

    public interface ValueStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> value(@Nonnull Function<@Safe String, T> valueVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true,
            defaultImpl = UnknownWrapper.class)
    @JsonSubTypes(@JsonSubTypes.Type(ValueWrapper.class))
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {
        <T> T accept(Visitor<T> visitor);
    }

    @JsonTypeName("value")
    private static final class ValueWrapper implements Base {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private ValueWrapper(@JsonSetter("value") @Nonnull String value) {
            Preconditions.checkNotNull(value, "value cannot be null");
            this.value = value;
        }

        @JsonProperty(value = "type", index = 0)
        private String getType() {
            return "value";
        }

        @JsonProperty("value")
        private String getValue() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitValue(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof ValueWrapper && equalTo((ValueWrapper) other));
        }

        private boolean equalTo(ValueWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "ValueWrapper{value: " + value + '}';
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
}
