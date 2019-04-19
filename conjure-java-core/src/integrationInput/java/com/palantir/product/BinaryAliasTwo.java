package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class BinaryAliasTwo {
    private final BinaryAliasOne value;

    private BinaryAliasTwo(BinaryAliasOne value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public BinaryAliasOne get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BinaryAliasTwo
                        && this.value.equals(((BinaryAliasTwo) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static BinaryAliasTwo of(BinaryAliasOne value) {
        return new BinaryAliasTwo(value);
    }
}
