package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class LongAlias {
    private final long value;

    private LongAlias(long value) {
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
        return this == other || (other instanceof LongAlias && this.value == ((LongAlias) other).value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @JsonCreator
    public static LongAlias valueOf(String value) {
        return of(Long.valueOf(value));
    }

    @JsonCreator
    public static LongAlias of(long value) {
        return new LongAlias(value);
    }
}
