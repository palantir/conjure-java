package withvisitors.com.palantir.product;

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
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = SingleUnion.Unknown.class)
@JsonSubTypes(@JsonSubTypes.Type(Foo.class))
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface SingleUnion {
    static SingleUnion foo(@Safe String value) {
        return new Foo(value);
    }

    static SingleUnion unknown(@Safe String type, Object value) {
        switch (Preconditions.checkNotNull(type, "Type is required")) {
            case "foo":
                throw new SafeIllegalArgumentException(
                        "Unknown type cannot be created as the provided type is known: foo");
            default:
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    default Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'Union' union", SafeArg.of("type", ((Unknown) this).getType()));
        } else {
            return (Known) this;
        }
    }

    <T> void accept(Visitor<T> visitor);

    sealed interface Known permits Foo {}

    @JsonTypeName("foo")
    record Foo(String value) implements SingleUnion, Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        Foo(@JsonSetter("foo") @Nonnull String value) {
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
        public String toString() {
            return "Foo{value: " + value + '}';
        }
    }

    final class Unknown implements SingleUnion {
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
        public boolean equals(Object other) {
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
            return "Unknown{type: " + type + ", value: " + value + '}';
        }
    }

    interface Visitor<T> {
        T visitFoo(@Safe String value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        /**
         * @Deprecated - prefer Java 17 pattern matching switch expressions.
         */
        @Deprecated
        static <T> FooStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    final class VisitorBuilder<T>
            implements FooStageVisitorBuilder<T>, UnknownStageVisitorBuilder<T>, Completed_StageVisitorBuilder<T> {
        private Function<@Safe String, T> fooVisitor;

        private BiFunction<@Safe String, Object, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> foo(@Nonnull Function<@Safe String, T> fooVisitor) {
            Preconditions.checkNotNull(fooVisitor, "fooVisitor cannot be null");
            this.fooVisitor = fooVisitor;
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
                        "Unknown variant of the 'SingleUnion' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final Function<@Safe String, T> fooVisitor = this.fooVisitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitFoo(String value) {
                    return fooVisitor.apply(value);
                }

                @Override
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
                }
            };
        }
    }

    interface FooStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> foo(@Nonnull Function<@Safe String, T> fooVisitor);
    }

    interface UnknownStageVisitorBuilder<T> {
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
