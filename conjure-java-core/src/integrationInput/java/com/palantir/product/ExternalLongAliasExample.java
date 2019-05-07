package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExternalLongAliasExample {
    private final long value;

    private ExternalLongAliasExample(long value) {
        this.value = value;
    }

    @JsonValue
    public long get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ExternalLongAliasExample
                        && this.value == ((ExternalLongAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @JsonCreator
    public static ExternalLongAliasExample valueOf(String value) {
        return of(Long.valueOf(value));
    }

    @JsonCreator
    public static ExternalLongAliasExample of(long value) {
        return new ExternalLongAliasExample(value);
    }
}
