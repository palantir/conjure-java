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
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ConjureGenerated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = SimpleUnion.Unknown.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleUnion.Foo.class, name = "foo"),
    @JsonSubTypes.Type(value = SimpleUnion.Bar.class, name = "bar"),
    @JsonSubTypes.Type(value = SimpleUnion.Baz.class, name = "baz")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract sealed class SimpleUnion
        permits SimpleUnion.Foo, SimpleUnion.Bar, SimpleUnion.Baz, SimpleUnion.Unknown {
    public static SimpleUnion foo(String value) {
        return new Foo(value);
    }

    public static SimpleUnion bar(int value) {
        return new Bar(value);
    }

    public static SimpleUnion baz(SafeLong value) {
        return new Baz(value);
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
                return new Unknown(type, Collections.singletonMap(type, value));
        }
    }

    public Known throwOnUnknown() {
        if (this instanceof Unknown) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'SimpleUnion' union", SafeArg.of("unknownType", ((Unknown) this).type()));
        } else {
            return (Known) this;
        }
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public sealed interface Known permits Foo, Bar, Baz {}

    @JsonTypeName("foo")
    public static final class Foo extends SimpleUnion implements Known {
        private final String value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Foo(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "foo";
        }

        @JsonProperty("foo")
        public String value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFoo(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Foo && equalTo((Foo) other));
        }

        private boolean equalTo(Foo other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SimpleUnion.Foo{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    public static final class Bar extends SimpleUnion implements Known {
        private final int value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Bar(@JsonSetter("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "bar";
        }

        @JsonProperty("bar")
        public int value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBar(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Bar && equalTo((Bar) other));
        }

        private boolean equalTo(Bar other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return this.value;
        }

        @Override
        public String toString() {
            return "SimpleUnion.Bar{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    public static final class Baz extends SimpleUnion implements Known {
        private final SafeLong value;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private Baz(@JsonSetter("baz") @Nonnull SafeLong value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty(index = 0)
        private String type() {
            return "baz";
        }

        @JsonProperty("baz")
        public SafeLong value() {
            return value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBaz(value);
        }

        @Override
        public boolean equals(@Nullable Object other) {
            return this == other || (other instanceof Baz && equalTo((Baz) other));
        }

        private boolean equalTo(Baz other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return this.value.hashCode();
        }

        @Override
        public String toString() {
            return "SimpleUnion.Baz{value: " + value + '}';
        }
    }

    public static final class Unknown extends SimpleUnion {
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
            return "SimpleUnion.Unknown{type: " + type + ", value: " + value + '}';
        }
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        T visitBar(int value);

        T visitBaz(SafeLong value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

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

        private BiFunction<@Safe String, Object, T> unknownVisitor;

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
                        "Unknown variant of the 'SimpleUnion' union", SafeArg.of("unknownType", unknownType));
            };
            return this;
        }

        @Override
        public Visitor<T> build() {
            final IntFunction<T> barVisitor = this.barVisitor;
            final Function<SafeLong, T> bazVisitor = this.bazVisitor;
            final Function<String, T> fooVisitor = this.fooVisitor;
            final BiFunction<@Safe String, Object, T> unknownVisitor = this.unknownVisitor;
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
                public T visitUnknown(String unknownType, Object unknownValue) {
                    return unknownVisitor.apply(unknownType, unknownValue);
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
        Completed_StageVisitorBuilder<T> unknown(@Nonnull BiFunction<@Safe String, Object, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> unknown(@Nonnull Function<@Safe String, T> unknownVisitor);

        Completed_StageVisitorBuilder<T> throwOnUnknown();
    }

    public interface Completed_StageVisitorBuilder<T> {
        Visitor<T> build();
    }
}
