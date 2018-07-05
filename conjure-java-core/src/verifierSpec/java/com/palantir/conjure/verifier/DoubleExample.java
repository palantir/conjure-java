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
    private final double value;

    private volatile int memoizedHashCode;

    private DoubleExample(double value) {
        this.value = value;
    }

    @JsonProperty("value")
    public double getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof DoubleExample && equalTo((DoubleExample) other));
    }

    private boolean equalTo(DoubleExample other) {
        return this.value == other.value;
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
        return new StringBuilder("DoubleExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static DoubleExample of(double value) {
        return builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private double value;

        private Builder() {}

        public Builder from(DoubleExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(double value) {
            this.value = value;
            return this;
        }

        public DoubleExample build() {
            return new DoubleExample(value);
        }
    }
}
