package com.palantir.product;

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
import java.util.OptionalInt;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class Union {
    @JsonUnwrapped private final _Union_ union;

    @JsonCreator
    private Union(_Union_ union) {
        Objects.requireNonNull(union, "union must not be null");
        this.union = union;
    }

    public <T> T accept(Visitor<T> visitor) {
        if (union.getFoo().isPresent()) {
            return visitor.visitFoo(union.getFoo().get());
        } else if (union.getBar().isPresent()) {
            return visitor.visitBar(union.getBar().getAsInt());
        }
        return visitor.visitUnknown(union.getType());
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof Union && equalTo((Union) other));
    }

    private boolean equalTo(Union other) {
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

    public static Union foo(String value) {
        return new Union(_Union_.builder().type("foo").foo(value).build());
    }

    public static Union bar(int value) {
        return new Union(_Union_.builder().type("bar").bar(value).build());
    }

    public interface Visitor<T> {
        T visitFoo(String value);

        T visitBar(int value);

        T visitUnknown(String unknownType);
    }

    @JsonDeserialize(builder = _Union_.Builder.class)
    @Generated("com.palantir.conjure.java.types.BeanGenerator")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static final class _Union_ {
        private final String type;

        private final Optional<String> foo;

        private final OptionalInt bar;

        private final Map<String, Object> __unknownProperties;

        private volatile int memoizedHashCode;

        private _Union_(
                String type,
                Optional<String> foo,
                OptionalInt bar,
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
        public OptionalInt getBar() {
            return this.bar;
        }

        @JsonAnyGetter
        Map<String, Object> unknownProperties() {
            return __unknownProperties;
        }

        @Override
        public boolean equals(Object other) {
            return this == other || (other instanceof _Union_ && equalTo((_Union_) other));
        }

        private boolean equalTo(_Union_ other) {
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
            return new StringBuilder("_Union_")
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

        private static void validateFields(String type, Optional<String> foo, OptionalInt bar) {
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

            private OptionalInt bar = OptionalInt.empty();

            Map<String, Object> __unknownProperties = new LinkedHashMap<>();

            private Builder() {}

            public Builder from(_Union_ other) {
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
            public Builder bar(OptionalInt bar) {
                this.bar = Objects.requireNonNull(bar, "bar cannot be null");
                return this;
            }

            public Builder bar(int bar) {
                this.bar = OptionalInt.of(bar);
                return this;
            }

            public _Union_ build() {
                return new _Union_(type, foo, bar, __unknownProperties);
            }

            @JsonAnySetter
            private void setUnknownProperties(String key, Object value) {
                __unknownProperties.put(key, value);
            }
        }
    }
}
