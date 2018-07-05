package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(doubleValue);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("DoubleExample")
                .append("{")
                .append("doubleValue")
                .append(": ")
                .append(doubleValue)
                .append("}")
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

        private Builder() {}

        public Builder from(DoubleExample other) {
            doubleValue(other.getDoubleValue());
            return this;
        }

        @JsonSetter("doubleValue")
        public Builder doubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
            return this;
        }

        public DoubleExample build() {
            return new DoubleExample(doubleValue);
        }
    }
}
