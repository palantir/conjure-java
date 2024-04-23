package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

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
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof AliasedInteger && equalTo((AliasedInteger) other));
    }

    private boolean equalTo(AliasedInteger other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return this.value;
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
