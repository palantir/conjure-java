package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = DoubleExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class DoubleExample {
    private final double doubleValue;

    private volatile int memoizedHashCode;

    private DoubleExample(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    @JsonProperty("doubleValue")
    public double getDoubleValue() {
        return this.doubleValue;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof DoubleExample && equalTo((DoubleExample) other));
    }

    private boolean equalTo(DoubleExample other) {
        return this.doubleValue == other.doubleValue;
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(this.doubleValue);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder("DoubleExample")
                .append('{')
                .append("doubleValue")
                .append(": ")
                .append(doubleValue)
                .append('}')
                .toString();
    }

    public static DoubleExample of(double doubleValue) {
        return builder().doubleValue(doubleValue).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private double doubleValue;

        private boolean _doubleValueInitialized = false;

        private Builder() {}

        public Builder from(DoubleExample other) {
            doubleValue(other.getDoubleValue());
            return this;
        }

        @JsonSetter("doubleValue")
        public Builder doubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
            this._doubleValueInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields =
                    addFieldIfMissing(missingFields, _doubleValueInitialized, "doubleValue");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set",
                        SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(
                List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(1);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public DoubleExample build() {
            validatePrimitiveFieldsHaveBeenInitialized();
            return new DoubleExample(doubleValue);
        }
    }
}
