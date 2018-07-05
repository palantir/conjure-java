package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = StringExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StringExample {
    private final String value;

    private volatile int memoizedHashCode;

    private StringExample(String value) {
        validateFields(value);
        this.value = value;
    }

    @JsonProperty("value")
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof StringExample && equalTo((StringExample) other));
    }

    private boolean equalTo(StringExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(value);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("StringExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static StringExample of(String value) {
        return builder().value(value).build();
    }

    private static void validateFields(String value) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, value, "value");
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
        private String value;

        private Builder() {}

        public Builder from(StringExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(String value) {
            this.value = Objects.requireNonNull(value, "value cannot be null");
            return this;
        }

        public StringExample build() {
            return new StringExample(value);
        }
    }
}
