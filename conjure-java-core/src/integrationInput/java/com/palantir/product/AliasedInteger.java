package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasedInteger implements Comparable<AliasedInteger> {
    private final int value;

    private AliasedInteger(int value) {
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
        return this == other || (other instanceof AliasedInteger && this.value == ((AliasedInteger) other).value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public int compareTo(AliasedInteger other) {
        return Integer.compare(value, other.get());
    }

    public static AliasedInteger valueOf(String value) {
        return of(Integer.parseInt(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedInteger of(int value) {
        return new AliasedInteger(value);
    }
}
