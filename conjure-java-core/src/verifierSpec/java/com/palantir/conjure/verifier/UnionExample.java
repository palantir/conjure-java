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
import java.util.OptionalInt;
import java.util.Set;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class UnionExample {
    @JsonUnwrapped private final Union union;

    @JsonCreator
    private UnionExample(Union union) {
        Objects.requireNonNull(union, "union must not be null");
        this.union = union;
    }

    public <T> T accept(Visitor<T> visitor) {
        if (union.getStringExample().isPresent()) {
            return visitor.visitStringExample(union.getStringExample().get());
        } else if (union.getSet().isPresent()) {
            return visitor.visitSet(union.getSet().get());
        } else if (union.getInteger().isPresent()) {
            return visitor.visitInteger(union.getInteger().getAsInt());
        }
        return visitor.visitUnknown(union.getType());
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof UnionExample && equalTo((UnionExample) other));
    }

    private boolean equalTo(UnionExample other) {
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

    public static UnionExample stringExample(StringExample value) {
        return new UnionExample(Union.builder().type("stringExample").stringExample(value).build());
    }

    public static UnionExample set(Set<String> value) {
        return new UnionExample(Union.builder().type("set").set(value).build());
    }

    public static UnionExample integer(int value) {
        return new UnionExample(Union.builder().type("integer").integer(value).build());
    }

    public interface Visitor<T> {
        T visitStringExample(StringExample value);

        T visitSet(Set<String> value);

        T visitInteger(int value);

        T visitUnknown(String unknownType);
    }

    @JsonDeserialize(builder = Union.Builder.class)
    @Generated("com.palantir.conjure.java.types.BeanGenerator")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static final class Union {
        private final String type;

        private final Optional<StringExample> stringExample;

        private final Optional<Set<String>> set;

        private final OptionalInt integer;

        private final Map<String, Object> __unknownProperties;

        private volatile int memoizedHashCode;

        private Union(
                String type,
                Optional<StringExample> stringExample,
                Optional<Set<String>> set,
                OptionalInt integer,
                Map<String, Object> __unknownProperties) {
            validateFields(type, stringExample, set, integer);
            this.type = type;
            this.stringExample = stringExample;
            this.set = set;
            this.integer = integer;
            this.__unknownProperties = Collections.unmodifiableMap(__unknownProperties);
        }

        @JsonProperty("type")
        public String getType() {
            return this.type;
        }

        @JsonProperty("stringExample")
        public Optional<StringExample> getStringExample() {
            return this.stringExample;
        }

        @JsonProperty("set")
        public Optional<Set<String>> getSet() {
            return this.set;
        }

        @JsonProperty("integer")
        public OptionalInt getInteger() {
            return this.integer;
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
                    && this.stringExample.equals(other.stringExample)
                    && this.set.equals(other.set)
                    && this.integer.equals(other.integer)
                    && this.__unknownProperties.equals(other.__unknownProperties);
        }

        @Override
        public int hashCode() {
            if (memoizedHashCode == 0) {
                memoizedHashCode =
                        Objects.hash(type, stringExample, set, integer, __unknownProperties);
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
                    .append("stringExample")
                    .append(": ")
                    .append(stringExample)
                    .append(", ")
                    .append("set")
                    .append(": ")
                    .append(set)
                    .append(", ")
                    .append("integer")
                    .append(": ")
                    .append(integer)
                    .append("}")
                    .toString();
        }

        private static void validateFields(
                String type,
                Optional<StringExample> stringExample,
                Optional<Set<String>> set,
                OptionalInt integer) {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, type, "type");
            missingFields = addFieldIfMissing(missingFields, stringExample, "stringExample");
            missingFields = addFieldIfMissing(missingFields, set, "set");
            missingFields = addFieldIfMissing(missingFields, integer, "integer");
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
                    missingFields = new ArrayList<>(4);
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

            private Optional<StringExample> stringExample = Optional.empty();

            private Optional<Set<String>> set = Optional.empty();

            private OptionalInt integer = OptionalInt.empty();

            Map<String, Object> __unknownProperties = new LinkedHashMap<>();

            private Builder() {}

            public Builder from(Union other) {
                type(other.getType());
                stringExample(other.getStringExample());
                set(other.getSet());
                integer(other.getInteger());
                return this;
            }

            @JsonSetter("type")
            public Builder type(String type) {
                this.type = Objects.requireNonNull(type, "type cannot be null");
                return this;
            }

            @JsonSetter("stringExample")
            public Builder stringExample(Optional<StringExample> stringExample) {
                this.stringExample =
                        Objects.requireNonNull(stringExample, "stringExample cannot be null");
                return this;
            }

            public Builder stringExample(StringExample stringExample) {
                this.stringExample =
                        Optional.of(
                                Objects.requireNonNull(
                                        stringExample, "stringExample cannot be null"));
                return this;
            }

            @JsonSetter("set")
            public Builder set(Optional<Set<String>> set) {
                this.set = Objects.requireNonNull(set, "set cannot be null");
                return this;
            }

            public Builder set(Set<String> set) {
                this.set = Optional.of(Objects.requireNonNull(set, "set cannot be null"));
                return this;
            }

            @JsonSetter("integer")
            public Builder integer(OptionalInt integer) {
                this.integer = Objects.requireNonNull(integer, "integer cannot be null");
                return this;
            }

            public Builder integer(int integer) {
                this.integer = OptionalInt.of(integer);
                return this;
            }

            public Union build() {
                return new Union(type, stringExample, set, integer, __unknownProperties);
            }

            @JsonAnySetter
            private void setUnknownProperties(String key, Object value) {
                __unknownProperties.put(key, value);
            }
        }
    }
}
