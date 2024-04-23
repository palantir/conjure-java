package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.ri.ResourceIdentifier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class RidAliasExample {
    private final ResourceIdentifier value;

    private RidAliasExample(@Nonnull ResourceIdentifier value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public ResourceIdentifier get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof RidAliasExample && equalTo((RidAliasExample) other));
    }

    private boolean equalTo(RidAliasExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public static RidAliasExample valueOf(String value) {
        return of(ResourceIdentifier.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RidAliasExample of(@Nonnull ResourceIdentifier value) {
        return new RidAliasExample(value);
    }
}
