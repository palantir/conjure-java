package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class Union {
    private final Base value;

    @JsonCreator
    private Union(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static Union foo(String value) {
        return new Union(new FooWrapper(value));
    }

    /**
     * @deprecated Int is deprecated.
     */
    @Deprecated
    public static Union bar(int value) {
        return new Union(new BarWrapper(value));
    }

    /**
     * 64-bit integer.
     * @deprecated Prefer <code>foo</code>.
     */
    @Deprecated
    public static Union baz(long value) {
        return new Union(new BazWrapper(value));
    }

    @SuppressWarnings("deprecation")
    public <T> T accept(Visitor<T> visitor) {
        if (value instanceof FooWrapper) {
            return visitor.visitFoo(((FooWrapper) value).value);
        } else if (value instanceof BarWrapper) {
            return visitor.visitBar(((BarWrapper) value).value);
        } else if (value instanceof BazWrapper) {
            return visitor.visitBaz(((BazWrapper) value).value);
        } else if (value instanceof UnknownWrapper) {
            return visitor.visitUnknown(((UnknownWrapper) value).getType());
        }
        throw new IllegalStateException(String.format("Could not identify type %s", value.getClass()));
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof Union && equalTo((Union) other));
    }

    private boolean equalTo(Union other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return "Union{value: " + value + '}';
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        /**
         * @deprecated Int is deprecated.
         */
        @Deprecated
        T visitBar(int value);

        /**
         * 64-bit integer.
         * @deprecated Prefer <code>foo</code>.
         */
        @Deprecated
        T visitBaz(long value);

        T visitUnknown(String unknownType);

        static <T> BarStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static final class VisitorBuilder<T>
            implements BarStageVisitorBuilder<T>,
                    BazStageVisitorBuilder<T>,
                    FooStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    CompletedStageVisitorBuilder<T> {
        private IntFunction<T> barVisitor;

        private Function<Long, T> bazVisitor;

        private Function<String, T> fooVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public BazStageVisitorBuilder<T> bar(@Nonnull IntFunction<T> barVisitor) {
            Preconditions.checkNotNull(barVisitor, "barVisitor cannot be null");
            this.barVisitor = barVisitor;
            return this;
        }

        @Override
        public FooStageVisitorBuilder<T> baz(@Nonnull Function<Long, T> bazVisitor) {
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
        public CompletedStageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Visitor<T> build() {
            final IntFunction<T> barVisitor = this.barVisitor;
            final Function<Long, T> bazVisitor = this.bazVisitor;
            final Function<String, T> fooVisitor = this.fooVisitor;
            final Function<String, T> unknownVisitor = this.unknownVisitor;
            return new Visitor<T>() {
                @Override
                public T visitBar(int value) {
                    return barVisitor.apply(value);
                }

                @Override
                public T visitBaz(long value) {
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
        FooStageVisitorBuilder<T> baz(@Nonnull Function<Long, T> bazVisitor);
    }

    public interface FooStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> foo(@Nonnull Function<String, T> fooVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        CompletedStageVisitorBuilder<T> unknown(@Nonnull Function<String, T> unknownVisitor);
    }

    public interface CompletedStageVisitorBuilder<T> {
        Visitor<T> build();
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true, defaultImpl = UnknownWrapper.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(FooWrapper.class),
        @JsonSubTypes.Type(BarWrapper.class),
        @JsonSubTypes.Type(BazWrapper.class)
    })
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {}

    @JsonTypeName("foo")
    private static final class FooWrapper implements Base {
        private final String value;

        @JsonCreator
        private FooWrapper(@JsonProperty("foo") @Nonnull String value) {
            Preconditions.checkNotNull(value, "foo cannot be null");
            this.value = value;
        }

        @JsonProperty("foo")
        private String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof FooWrapper && equalTo((FooWrapper) other));
        }

        private boolean equalTo(FooWrapper other) {
            return this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "FooWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("bar")
    private static final class BarWrapper implements Base {
        private final int value;

        @JsonCreator
        private BarWrapper(@JsonProperty("bar") @Nonnull int value) {
            Preconditions.checkNotNull(value, "bar cannot be null");
            this.value = value;
        }

        @JsonProperty("bar")
        private int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof BarWrapper && equalTo((BarWrapper) other));
        }

        private boolean equalTo(BarWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "BarWrapper{value: " + value + '}';
        }
    }

    @JsonTypeName("baz")
    private static final class BazWrapper implements Base {
        private final long value;

        @JsonCreator
        private BazWrapper(@JsonProperty("baz") @Nonnull long value) {
            Preconditions.checkNotNull(value, "baz cannot be null");
            this.value = value;
        }

        @JsonProperty("baz")
        private long getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof BazWrapper && equalTo((BazWrapper) other));
        }

        private boolean equalTo(BazWrapper other) {
            return this.value == other.value;
        }

        @Override
        public int hashCode() {
            return Long.hashCode(this.value);
        }

        @Override
        public String toString() {
            return "BazWrapper{value: " + value + '}';
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true)
    private static final class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator
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
        public boolean equals(Object other) {
            return this == other || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.type, this.value);
        }

        @Override
        public String toString() {
            return "UnknownWrapper{type: " + type + ", value: " + value + '}';
        }
    }
}
