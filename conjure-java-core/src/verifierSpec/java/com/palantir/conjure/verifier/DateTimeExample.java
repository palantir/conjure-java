package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = DateTimeExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class DateTimeExample {
    private final ZonedDateTime value;

    private volatile int memoizedHashCode;

    private DateTimeExample(ZonedDateTime value) {
        validateFields(value);
        this.value = value;
    }

    @JsonProperty("value")
    public ZonedDateTime getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DateTimeExample && equalTo((DateTimeExample) other));
    }

    private boolean equalTo(DateTimeExample other) {
        return this.value.isEqual(other.value);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(value.toInstant());
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("DateTimeExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static DateTimeExample of(ZonedDateTime value) {
        return builder().value(value).build();
    }

    private static void validateFields(ZonedDateTime value) {
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
        private ZonedDateTime value;

        private Builder() {}

        public Builder from(DateTimeExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(ZonedDateTime value) {
            this.value = Objects.requireNonNull(value, "value cannot be null");
            return this;
        }

        public DateTimeExample build() {
            return new DateTimeExample(value);
        }
    }
}
