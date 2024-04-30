package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasedBoolean {
    private final boolean value;

    private AliasedBoolean(boolean value) {
        this.value = value;
    }

    @JsonValue
    public boolean get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof AliasedBoolean && equalTo((AliasedBoolean) other));
    }

    private boolean equalTo(AliasedBoolean other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this.value);
    }

    public static AliasedBoolean valueOf(String value) {
        return of(Boolean.parseBoolean(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedBoolean of(boolean value) {
        return new AliasedBoolean(value);
    }
}
