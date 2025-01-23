package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Safe;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SafeExternalLongAlias {
    private final @Safe long value;

    private SafeExternalLongAlias(@Safe long value) {
        this.value = value;
    }

    @JsonValue
    public @Safe long get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeExternalLongAlias && equalTo((SafeExternalLongAlias) other));
    }

    private boolean equalTo(SafeExternalLongAlias other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeExternalLongAlias valueOf(@Safe String value) {
        return of(Long.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeExternalLongAlias of(@Safe long value) {
        return new SafeExternalLongAlias(value);
    }
}
