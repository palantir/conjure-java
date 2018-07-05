package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAlias {
    private final String value;

    private StringAlias(String value) {
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
                || (other instanceof StringAlias && this.value.equals(((StringAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static StringAlias valueOf(String value) {
        return new StringAlias(value);
    }

    @JsonCreator
    public static StringAlias of(String value) {
        return new StringAlias(value);
    }
}
