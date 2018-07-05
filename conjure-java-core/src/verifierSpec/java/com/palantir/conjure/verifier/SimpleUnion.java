package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class SimpleUnion {
    @JsonUnwrapped private final Union union;

    @JsonCreator
    private SimpleUnion(Union union) {
        Objects.requireNonNull(union, "union must not be null");
        this.union = union;
    }

    public <T> T accept(Visitor<T> visitor) {
        if (union.getFoo().isPresent()) {
            return visitor.visitFoo(union.getFoo().get());
        } else if (union.getBar().isPresent()) {
            return visitor.visitBar(union.getBar().get());
        }
        return visitor.visitUnknown(union.getType());
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof SimpleUnion && equalTo((SimpleUnion) other));
    }

    private boolean equalTo(SimpleUnion other) {
        return this.union.equals(other.union);
    }

    @Override
    public int hashCode() {
        return union.hashCode();
    }

    @Override
    public String toString() {
        return union.toString();
    }

    public static SimpleUnion foo(String value) {
        return new SimpleUnion(Union.builder().type("foo").foo(value).build());
    }

    public static SimpleUnion bar(String value) {
        return new SimpleUnion(Union.builder().type("bar").bar(value).build());
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        T visitBar(String value);

        T visitUnknown(String unknownType);
    }

    @JsonDeserialize(builder = Union.Builder.class)
    @Generated("com.palantir.conjure.java.types.BeanGenerator")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static final class Union {
        private final String type;

        private final Optional<String> foo;

        private final Optional<String> bar;

        private final Map<String, Object> __unknownProperties;

        private volatile int memoizedHashCode;

        private Union(
                String type,
                Optional<String> foo,
                Optional<String> bar,
                Map<String, Object> __unknownProperties) {
            validateFields(type, foo, bar);
            this.type = type;
            this.foo = foo;
            this.bar = bar;
            this.__unknownProperties = Collections.unmodifiableMap(__unknownProperties);
        }

        @JsonProperty("type")
        public String getType() {
            return this.type;
        }

        @JsonProperty("foo")
        public Optional<String> getFoo() {
            return this.foo;
        }

        @JsonProperty("bar")
        public Optional<String> getBar() {
            return this.bar;
        }

        @JsonAnyGetter
        Map<String, Object> unknownProperties() {
            return __unknownProperties;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof Union && equalTo((Union) other));
        }

        private boolean equalTo(Union other) {
            return this.type.equals(other.type)
                    && this.foo.equals(other.foo)
                    && this.bar.equals(other.bar)
                    && this.__unknownProperties.equals(other.__unknownProperties);
        }

        @Override
        public int hashCode() {
            if (memoizedHashCode == 0) {
                memoizedHashCode = Objects.hash(type, foo, bar, __unknownProperties);
            }
            return memoizedHashCode;
        }

        @Override
        public String toString() {
            return new StringBuilder("Union")
                    .append("{")
                    .append("type")
                    .append(": ")
                    .append(type)
                    .append(", ")
                    .append("foo")
                    .append(": ")
                    .append(foo)
                    .append(", ")
                    .append("bar")
                    .append(": ")
                    .append(bar)
                    .append("}")
                    .toString();
        }

        private static void validateFields(
                String type, Optional<String> foo, Optional<String> bar) {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, type, "type");
            missingFields = addFieldIfMissing(missingFields, foo, "foo");
            missingFields = addFieldIfMissing(missingFields, bar, "bar");
            if (missingFields != null) {
                throw new IllegalArgumentException(
                        "Some required fields have not been set: " + missingFields);
            }
        }

        private static List<String> addFieldIfMissing(
                List<String> prev, Object fieldValue, String fieldName) {
            List<String> missingFields = prev;
            if (fieldValue == null) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(3);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public static Builder builder() {
            return new Builder();
        }

        @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Builder {
            private String type;

            private Optional<String> foo = Optional.empty();

            private Optional<String> bar = Optional.empty();

            Map<String, Object> __unknownProperties = new LinkedHashMap<>();

            private Builder() {}

            public Builder from(Union other) {
                type(other.getType());
                foo(other.getFoo());
                bar(other.getBar());
                return this;
            }

            @JsonSetter("type")
            public Builder type(String type) {
                this.type = Objects.requireNonNull(type, "type cannot be null");
                return this;
            }

            @JsonSetter("foo")
            public Builder foo(Optional<String> foo) {
                this.foo = Objects.requireNonNull(foo, "foo cannot be null");
                return this;
            }

            public Builder foo(String foo) {
                this.foo = Optional.of(Objects.requireNonNull(foo, "foo cannot be null"));
                return this;
            }

            @JsonSetter("bar")
            public Builder bar(Optional<String> bar) {
                this.bar = Objects.requireNonNull(bar, "bar cannot be null");
                return this;
            }

            public Builder bar(String bar) {
                this.bar = Optional.of(Objects.requireNonNull(bar, "bar cannot be null"));
                return this;
            }

            public Union build() {
                return new Union(type, foo, bar, __unknownProperties);
            }

            @JsonAnySetter
            private void setUnknownProperties(String key, Object value) {
                __unknownProperties.put(key, value);
            }
        }
    }
}
