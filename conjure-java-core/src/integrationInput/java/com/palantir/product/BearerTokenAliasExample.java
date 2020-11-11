package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.BearerToken;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

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
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BearerTokenAliasExample
                        && this.value.equals(((BearerTokenAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static BearerTokenAliasExample valueOf(String value) {
        return of(BearerToken.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static BearerTokenAliasExample of(@Nonnull BearerToken value) {
        return new BearerTokenAliasExample(value);
    }
}
