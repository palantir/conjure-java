package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class IntegerAliasExample {
    private final int value;

    private IntegerAliasExample(int value) {
        this.value = value;
    }

    @JsonValue
    public int get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof IntegerAliasExample && this.value == ((IntegerAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    public static IntegerAliasExample valueOf(String value) {
        return of(Integer.parseInt(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static IntegerAliasExample of(int value) {
        return new IntegerAliasExample(value);
    }
}
