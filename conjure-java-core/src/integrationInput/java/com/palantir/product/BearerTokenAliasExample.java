package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.BearerToken;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class BearerTokenAliasExample {
    private final BearerToken value;

    private BearerTokenAliasExample(@Nonnull BearerToken value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public BearerToken get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof BearerTokenAliasExample && equalTo((BearerTokenAliasExample) other));
    }

    private boolean equalTo(BearerTokenAliasExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public static BearerTokenAliasExample valueOf(@DoNotLog String value) {
        return of(BearerToken.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BearerTokenAliasExample of(@Nonnull BearerToken value) {
        return new BearerTokenAliasExample(value);
    }
}
