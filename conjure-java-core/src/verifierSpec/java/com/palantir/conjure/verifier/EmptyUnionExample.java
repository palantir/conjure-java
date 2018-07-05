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
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.UnionGenerator")
public final class EmptyUnionExample {
    @JsonUnwrapped private final Union union;

    @JsonCreator
    private EmptyUnionExample(Union union) {
        Objects.requireNonNull(union, "union must not be null");
        this.union = union;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitUnknown(union.getType());
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof EmptyUnionExample && equalTo((EmptyUnionExample) other));
    }

    private boolean equalTo(EmptyUnionExample other) {
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

    public interface Visitor<T> {
        T visitUnknown(String unknownType);
    }

    @JsonDeserialize(builder = Union.Builder.class)
    @Generated("com.palantir.conjure.java.types.BeanGenerator")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    private static final class Union {
        private final String type;

        private final Map<String, Object> __unknownProperties;

        private volatile int memoizedHashCode;

        private Union(String type, Map<String, Object> __unknownProperties) {
            validateFields(type);
            this.type = type;
            this.__unknownProperties = Collections.unmodifiableMap(__unknownProperties);
        }

        @JsonProperty("type")
        public String getType() {
            return this.type;
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
                    && this.__unknownProperties.equals(other.__unknownProperties);
        }

        @Override
        public int hashCode() {
            if (memoizedHashCode == 0) {
                memoizedHashCode = Objects.hash(type, __unknownProperties);
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
                    .append("}")
                    .toString();
        }

        public static Union of(String type, Map<String, Object> __unknownProperties) {
            return builder().type(type).__unknownProperties(__unknownProperties).build();
        }

        private static void validateFields(String type) {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, type, "type");
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
                    missingFields = new ArrayList<>(1);
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

            Map<String, Object> __unknownProperties = new LinkedHashMap<>();

            private Builder() {}

            public Builder from(Union other) {
                type(other.getType());
                return this;
            }

            @JsonSetter("type")
            public Builder type(String type) {
                this.type = Objects.requireNonNull(type, "type cannot be null");
                return this;
            }

            public Union build() {
                return new Union(type, __unknownProperties);
            }

            @JsonAnySetter
            private void setUnknownProperties(String key, Object value) {
                __unknownProperties.put(key, value);
            }
        }
    }
}
