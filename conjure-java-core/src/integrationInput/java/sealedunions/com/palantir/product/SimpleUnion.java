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
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = SimpleUnion.UnknownVariant.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SimpleUnion.Foo.class, name = "foo"),
    @JsonSubTypes.Type(value = SimpleUnion.Bar.class, name = "bar"),
    @JsonSubTypes.Type(value = SimpleUnion.Baz.class, name = "baz")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public sealed interface SimpleUnion {
    static SimpleUnion foo(String value) {
        return new Foo(value);
    }

    static SimpleUnion bar(int value) {
        return new Bar(value);
    }

    static SimpleUnion baz(SafeLong value) {
        return new Baz(value);
    }

    static SimpleUnion unknown(@Safe String type, Object value) {
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
                return new UnknownVariant(type, Collections.singletonMap(type, value));
        }
    }

    default Known throwOnUnknown() {
        if (this instanceof UnknownVariant) {
            throw new SafeIllegalArgumentException(
                    "Unknown variant of the 'SimpleUnion' union",
                    SafeArg.of("unknownType", ((UnknownVariant) this).type()));
        } else {
            return (Known) this;
        }
    }

    <T> T accept(Visitor<T> visitor);

    sealed interface Known extends SimpleUnion permits Foo, Bar, Baz {}

    @JsonTypeName("Foo")
    record Foo(String value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Foo(@JsonSetter("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitFoo(value);
        }

        @Override
        public String toString() {
            return "SimpleUnion.Foo{value: " + value + '}';
        }
    }

    @JsonTypeName("Bar")
    record Bar(int value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Bar(@JsonSetter("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBar(value);
        }

        @Override
        public String toString() {
            return "SimpleUnion.Bar{value: " + value + '}';
        }
    }

    @JsonTypeName("Baz")
    record Baz(SafeLong value) implements Known {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public Baz(@JsonSetter("baz") @Nonnull SafeLong value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBaz(value);
        }

        @Override
        public String toString() {
            return "SimpleUnion.Baz{value: " + value + '}';
        }
    }

    record UnknownVariant(String type, Map<String, Object> value) implements SimpleUnion {
        public UnknownVariant {
            Preconditions.checkNotNull(type, "type cannot be null");
            Preconditions.checkNotNull(value, "type cannot be null");
        }

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        private UnknownVariant(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
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
        public String toString() {
            return "SimpleUnion.UnknownVariant{value: " + value + '}';
        }
    }

    interface Visitor<T> {
        T visitFoo(String value);

        T visitBar(int value);

        T visitBaz(SafeLong value);

        T visitUnknown(@Safe String unknownType, Object unknownValue);

        static <T> BarStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    final class VisitorBuilder<T>
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

    interface BarStageVisitorBuilder<T> {
        BazStageVisitorBuilder<T> bar(@Nonnull IntFunction<T> barVisitor);
    }

    interface BazStageVisitorBuilder<T> {
        FooStageVisitorBuilder<T> baz(@Nonnull Function<SafeLong, T> bazVisitor);
    }

    interface FooStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> foo(@Nonnull Function<String, T> fooVisitor);
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
