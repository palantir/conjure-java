package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.SafeLong;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = SafeLongExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SafeLongExample {
    private final SafeLong value;

    private volatile int memoizedHashCode;

    private SafeLongExample(SafeLong value) {
        validateFields(value);
        this.value = value;
    }

    @JsonProperty("value")
    public SafeLong getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof SafeLongExample && equalTo((SafeLongExample) other));
    }

    private boolean equalTo(SafeLongExample other) {
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
        return new StringBuilder("SafeLongExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static SafeLongExample of(SafeLong value) {
        return builder().value(value).build();
    }

    private static void validateFields(SafeLong value) {
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
        private SafeLong value;

        private Builder() {}

        public Builder from(SafeLongExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(SafeLong value) {
            this.value = Objects.requireNonNull(value, "value cannot be null");
            return this;
        }

        public SafeLongExample build() {
            return new SafeLongExample(value);
        }
    }
}
