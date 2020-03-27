package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExternalLongAliasTwo {
    private final ExternalLongAliasOne value;

    private ExternalLongAliasTwo(@Nonnull ExternalLongAliasOne value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public ExternalLongAliasOne get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ExternalLongAliasTwo && this.value.equals(((ExternalLongAliasTwo) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static ExternalLongAliasTwo valueOf(String value) {
        return of(ExternalLongAliasOne.valueOf(value));
    }

    @JsonCreator
    public static ExternalLongAliasTwo of(@Nonnull ExternalLongAliasOne value) {
        return new ExternalLongAliasTwo(value);
    }
}
