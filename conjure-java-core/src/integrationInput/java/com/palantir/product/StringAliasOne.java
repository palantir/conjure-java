package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasOne {
    private final String value;

    private StringAliasOne(@Nonnull String value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof StringAliasOne && this.value.equals(((StringAliasOne) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static StringAliasOne valueOf(String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StringAliasOne of(@Nonnull String value) {
        return new StringAliasOne(value);
    }
}
