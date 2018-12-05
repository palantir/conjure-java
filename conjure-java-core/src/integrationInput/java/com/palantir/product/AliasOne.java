package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasOne {
    private final String value;

    private AliasOne(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
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
        return this == other
                || (other instanceof AliasOne && this.value.equals(((AliasOne) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static AliasOne valueOf(String value) {
        return new AliasOne(value);
    }

    @JsonCreator
    public static AliasOne of(String value) {
        return new AliasOne(value);
    }
}
