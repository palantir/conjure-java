package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

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
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof LongAlias && equalTo((LongAlias) other));
    }

    private boolean equalTo(LongAlias other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LongAlias valueOf(String value) {
        return of(Long.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static LongAlias of(long value) {
        return new LongAlias(value);
    }
}
