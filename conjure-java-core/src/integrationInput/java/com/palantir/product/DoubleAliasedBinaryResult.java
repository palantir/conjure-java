package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class DoubleAliasedBinaryResult {
    private final AliasedBinaryResult value;

    private DoubleAliasedBinaryResult(@Nonnull AliasedBinaryResult value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public AliasedBinaryResult get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DoubleAliasedBinaryResult
                        && this.value.equals(((DoubleAliasedBinaryResult) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DoubleAliasedBinaryResult of(@Nonnull AliasedBinaryResult value) {
        return new DoubleAliasedBinaryResult(value);
    }
}
