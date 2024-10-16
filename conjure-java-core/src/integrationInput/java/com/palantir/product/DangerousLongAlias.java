package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.DoNotLog;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class DangerousLongAlias {
    private final @DoNotLog long value;

    private DangerousLongAlias(@DoNotLog long value) {
        this.value = value;
    }

    @JsonValue
    public @DoNotLog long get() {
        return value;
    }

    @Override
    @DoNotLog
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof DangerousLongAlias && equalTo((DangerousLongAlias) other));
    }

    private boolean equalTo(DangerousLongAlias other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DangerousLongAlias of(@DoNotLog long value) {
        return new DangerousLongAlias(value);
    }
}
