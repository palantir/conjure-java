package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = BooleanExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BooleanExample {
    private final boolean value;

    private volatile int memoizedHashCode;

    private BooleanExample(boolean value) {
        this.value = value;
    }

    @JsonProperty("value")
    public boolean getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BooleanExample && equalTo((BooleanExample) other));
    }

    private boolean equalTo(BooleanExample other) {
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
        return new StringBuilder("BooleanExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static BooleanExample of(boolean value) {
        return builder().value(value).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private boolean value;

        private Builder() {}

        public Builder from(BooleanExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(boolean value) {
            this.value = value;
            return this;
        }

        public BooleanExample build() {
            return new BooleanExample(value);
        }
    }
}
