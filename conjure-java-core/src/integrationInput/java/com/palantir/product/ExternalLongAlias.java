package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Immutable
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExternalLongAlias {
    private final long value;

    private ExternalLongAlias(long value) {
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
        return this == other || (other instanceof ExternalLongAlias && this.value == ((ExternalLongAlias) other).value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExternalLongAlias of(long value) {
        return new ExternalLongAlias(value);
    }
}
