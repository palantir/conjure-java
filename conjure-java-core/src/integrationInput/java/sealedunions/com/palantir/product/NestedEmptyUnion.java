package sealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = NestedEmptyUnion.Unknown.class)
@JsonSubTypes(@JsonSubTypes.Type(value = NestedEmptyUnion.Empty.class, name = "empty"))
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract sealed class NestedEmptyUnion permits NestedEmptyUnion.Empty, NestedEmptyUnion.Unknown {
    public static NestedEmptyUnion empty(EmptyObject value) {
        return new Empty(value);
    }

    public static NestedEmptyUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "empty":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: empty");
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    public Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'NestedEmptyUnion' union",
                    SafeArg.of("unknownType", ((Unknown) this).type()));
        } else {
            return (Known) this;
        }
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public sealed interface Known permits Empty {}

    @JsonTypeName("empty")
    public static final class Empty extends NestedEmptyUnion implements Known {
        private final EmptyObject value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Empty(@JsonSetter("empty") @Nonnull EmptyObject value) {
            Preconditions.checkNotNull(value, "empty cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "empty";
        }

        @JsonProperty("empty")
        public EmptyObject value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitEmpty(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Empty && equalTo((Empty) other));
        }

        private boolean equalTo(Empty other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "NestedEmptyUnion{value: EmptyWrapper{value: " + value + "}}";
        }
    }

    public static final class Unknown extends NestedEmptyUnion {
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
            return visitor.visitUnknown(type, value.get(type));
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
            return "NestedEmptyUnion{value: UnknownWrapper{value: " + value + "}}";
        }
    }

    public interface Visitor<T> {
        T visitEmpty(EmptyObject value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> EmptyStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements EmptyStageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<EmptyObject, T> emptyVisitor;

        private BiFunction<@Safe String, Object, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> empty(@Nonnull Function<EmptyObject, T> emptyVisitor) {
            Preconditions.checkNotNull(emptyVisitor, "emptyVisitor cannot be null");
            this.emptyVisitor = emptyVisitor;
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
                        "Unknown variant of the 'NestedEmptyUnion' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<EmptyObject, T> emptyVisitor = this.emptyVisitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitEmpty(EmptyObject value) {
                    return emptyVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
                }
            };
        }
    }

    public interface EmptyStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> empty(@Nonnull Function<EmptyObject, T> emptyVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
