package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Safe;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SafeLongAlias {
    private final @Safe long value;

    private SafeLongAlias(@Safe long value) {
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
        return this == other || (other instanceof SafeLongAlias && equalTo((SafeLongAlias) other));
    }

    private boolean equalTo(SafeLongAlias other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeLongAlias of(@Safe long value) {
        return new SafeLongAlias(value);
    }
}
