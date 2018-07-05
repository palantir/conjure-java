package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = IntegerExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class IntegerExample {
    private final int value;

    private volatile int memoizedHashCode;

    private IntegerExample(int value) {
        this.value = value;
    }

    @JsonProperty("value")
    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof IntegerExample && equalTo((IntegerExample) other));
    }

    private boolean equalTo(IntegerExample other) {
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
        return new StringBuilder("IntegerExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static IntegerExample of(int value) {
        return builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private int value;

        private Builder() {}

        public Builder from(IntegerExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public IntegerExample build() {
            return new IntegerExample(value);
        }
    }
}
