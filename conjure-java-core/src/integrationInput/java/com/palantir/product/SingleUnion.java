package com.palantir.product;

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
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class SingleUnion {
    private final Base value;

    @JsonCreator
    private SingleUnion(Base value) {
        this.value = value;
    }

    @JsonValue
    private Base getValue() {
        return value;
    }

    public static SingleUnion foo(String value) {
        return new SingleUnion(new FooWrapper(value));
    }

    public <T> T accept(Visitor<T> visitor) {
        if (value instanceof FooWrapper) {
            return visitor.visitFoo(((FooWrapper) value).value);
        } else if (value instanceof UnknownWrapper) {
            return visitor.visitUnknown(((UnknownWrapper) value).getType());
        }
        throw new IllegalStateException(
                String.format("Could not identify type %s", value.getClass()));
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof SingleUnion && equalTo((SingleUnion) other));
    }

    private boolean equalTo(SingleUnion other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return new StringBuilder("SingleUnion")
                .append('{')
                .append("value")
                .append(": ")
                .append(value)
                .append('}')
                .toString();
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        T visitUnknown(String unknownType);

        static <T> FooStageVisitorBuilder<T> builder() {
            return new VisitorBuilder<T>();
        }
    }

    private static class VisitorBuilder<T>
            implements FooStageVisitorBuilder<T>,
                    UnknownStageVisitorBuilder<T>,
                    CompletedStageVisitorBuilder<T> {
        private Function<String, T> fooVisitor;

        private Function<String, T> unknownVisitor;

        @Override
        public UnknownStageVisitorBuilder<T> foo(Function<String, T> fooVisitor) {
            Preconditions.checkNotNull(fooVisitor, "fooVisitor cannot be null");
            this.fooVisitor = fooVisitor;
            return this;
        }

        @Override
        public CompletedStageVisitorBuilder<T> unknown(Function<String, T> unknownVisitor) {
            Preconditions.checkNotNull(unknownVisitor, "unknownVisitor cannot be null");
            this.unknownVisitor = unknownVisitor;
            return this;
        }

        @Override
        public Visitor<T> build() {
            return new Visitor<T>() {
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

    public interface FooStageVisitorBuilder<T> {
        UnknownStageVisitorBuilder<T> foo(Function<String, T> fooVisitor);
    }

    public interface UnknownStageVisitorBuilder<T> {
        CompletedStageVisitorBuilder<T> unknown(Function<String, T> unknownVisitor);
    }

    public interface CompletedStageVisitorBuilder<T> {
        Visitor<T> build();
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            property = "type",
            visible = true,
            defaultImpl = UnknownWrapper.class)
    @JsonSubTypes(@JsonSubTypes.Type(FooWrapper.class))
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {}

    @JsonTypeName("foo")
    private static class FooWrapper implements Base {
        private final String value;

        @JsonCreator
        private FooWrapper(@JsonProperty("foo") String value) {
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
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("FooWrapper")
                    .append('{')
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXISTING_PROPERTY,
            property = "type",
            visible = true)
    private static class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator
        private UnknownWrapper(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownWrapper(String type, Map<String, Object> value) {
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
            return this == other
                    || (other instanceof UnknownWrapper && equalTo((UnknownWrapper) other));
        }

        private boolean equalTo(UnknownWrapper other) {
            return this.type.equals(other.type) && this.value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, value);
        }

        @Override
        public String toString() {
            return new StringBuilder("UnknownWrapper")
                    .append('{')
                    .append("type")
                    .append(": ")
                    .append(type)
                    .append(", ")
                    .append("value")
                    .append(": ")
                    .append(value)
                    .append('}')
                    .toString();
        }
    }
}
