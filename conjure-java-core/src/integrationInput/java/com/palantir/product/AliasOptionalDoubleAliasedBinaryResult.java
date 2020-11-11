package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Optional;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasOptionalDoubleAliasedBinaryResult {
    private final Optional<DoubleAliasedBinaryResult> value;

    private AliasOptionalDoubleAliasedBinaryResult(@Nonnull Optional<DoubleAliasedBinaryResult> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private AliasOptionalDoubleAliasedBinaryResult() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<DoubleAliasedBinaryResult> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof AliasOptionalDoubleAliasedBinaryResult
                        && this.value.equals(((AliasOptionalDoubleAliasedBinaryResult) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasOptionalDoubleAliasedBinaryResult of(@Nonnull Optional<DoubleAliasedBinaryResult> value) {
        return new AliasOptionalDoubleAliasedBinaryResult(value);
    }
}
