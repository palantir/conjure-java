package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.api.errors.FieldMissingException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@JsonDeserialize(builder = DoubleExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class DoubleExample {
    private final double doubleValue;

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
        return Double.doubleToLongBits(this.doubleValue) == Double.doubleToLongBits(other.doubleValue);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.doubleValue);
    }

    @Override
    public String toString() {
        return "DoubleExample{doubleValue: " + doubleValue + '}';
    }

    public static DoubleExample of(double doubleValue) {
        return builder().doubleValue(doubleValue).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private double doubleValue;

        private boolean _doubleValueInitialized = false;

        private Builder() {}

        public Builder from(DoubleExample other) {
            checkNotBuilt();
            doubleValue(other.getDoubleValue());
            return this;
        }

        @JsonSetter("doubleValue")
        public Builder doubleValue(double doubleValue) {
            checkNotBuilt();
            this.doubleValue = doubleValue;
            this._doubleValueInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _doubleValueInitialized, "doubleValue");
            if (missingFields != null) {
                throw new FieldMissingException(SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(List<String> prev, boolean initialized, String fieldName) {
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
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new DoubleExample(doubleValue);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
