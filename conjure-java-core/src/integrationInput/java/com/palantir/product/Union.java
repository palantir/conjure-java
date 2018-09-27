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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

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

    public static Union bar(int value) {
        return new Union(new BarWrapper(value));
    }

    public <T> T accept(Visitor<T> visitor) {
        if (value instanceof FooWrapper) {
            return visitor.visitFoo(((FooWrapper) value).value);
        } else if (value instanceof BarWrapper) {
            return visitor.visitBar(((BarWrapper) value).value);
        } else if (value instanceof UnknownWrapper) {
            return visitor.visitUnknown(((UnknownWrapper) value).getType());
        }
        throw new IllegalStateException(
                String.format("Could not identify type %s", value.getClass()));
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
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return new StringBuilder("Union")
                .append('{')
                .append("value")
                .append(": ")
                .append(value)
                .append('}')
                .toString();
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        T visitBar(int value);

        T visitUnknown(String unknownType);
    }

    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true,
        defaultImpl = UnknownWrapper.class
    )
    @JsonSubTypes({@JsonSubTypes.Type(FooWrapper.class), @JsonSubTypes.Type(BarWrapper.class)})
    @JsonIgnoreProperties(ignoreUnknown = true)
    private interface Base {}

    @JsonTypeName("foo")
    private static class FooWrapper implements Base {
        private final String value;

        @JsonCreator
        private FooWrapper(@JsonProperty("foo") String value) {
            Objects.requireNonNull(value, "foo cannot be null");
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

    @JsonTypeName("bar")
    private static class BarWrapper implements Base {
        private final int value;

        @JsonCreator
        private BarWrapper(@JsonProperty("bar") int value) {
            Objects.requireNonNull(value, "bar cannot be null");
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
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return new StringBuilder("BarWrapper")
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
        visible = true
    )
    private static class UnknownWrapper implements Base {
        private final String type;

        private final Map<String, Object> value;

        @JsonCreator
        private UnknownWrapper(@JsonProperty("type") String type) {
            this(type, new HashMap<String, Object>());
        }

        private UnknownWrapper(String type, Map<String, Object> value) {
            Objects.requireNonNull(type, "type cannot be null");
            Objects.requireNonNull(value, "value cannot be null");
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
