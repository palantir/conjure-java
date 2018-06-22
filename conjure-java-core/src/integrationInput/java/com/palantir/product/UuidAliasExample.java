package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class UuidAliasExample {
    private final UUID value;

    private UuidAliasExample(UUID value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public UUID get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof UuidAliasExample
                        && this.value.equals(((UuidAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static UuidAliasExample valueOf(String value) {
        return new UuidAliasExample(UUID.fromString(value));
    }

    @JsonCreator
    public static UuidAliasExample of(UUID value) {
        return new UuidAliasExample(value);
    }
}
