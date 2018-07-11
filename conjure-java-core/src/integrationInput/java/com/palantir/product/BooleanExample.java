package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = BooleanExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BooleanExample {
    private final boolean coin;

    private volatile int memoizedHashCode;

    private BooleanExample(boolean coin) {
        this.coin = coin;
    }

    @JsonProperty("coin")
    public boolean getCoin() {
        return this.coin;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BooleanExample && equalTo((BooleanExample) other));
    }

    private boolean equalTo(BooleanExample other) {
        return this.coin == other.coin;
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(coin);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("BooleanExample")
                .append("{")
                .append("coin")
                .append(": ")
                .append(coin)
                .append("}")
                .toString();
    }

    public static BooleanExample of(boolean coin) {
        return builder().coin(coin).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private boolean coin;

        private boolean _coinInitialized = false;

        private Builder() {}

        public Builder from(BooleanExample other) {
            coin(other.getCoin());
            return this;
        }

        @JsonSetter("coin")
        public Builder coin(boolean coin) {
            this.coin = coin;
            _coinInitialized = true;
            return this;
        }

        public BooleanExample build() {
            return new BooleanExample(coin);
        }
    }
}
