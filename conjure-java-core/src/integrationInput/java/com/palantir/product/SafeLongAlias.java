package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Safe;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SafeLongAlias {
    private final long value;

    private SafeLongAlias(long value) {
        this.value = value;
    }

    @JsonValue
    @Safe
    public long get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeLongAlias && this.value == ((SafeLongAlias) other).value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeLongAlias of(@Safe long value) {
        return new SafeLongAlias(value);
    }
}
