package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class NestedStringAliasExample {
    private final StringAliasExample value;

    private NestedStringAliasExample(@Nonnull StringAliasExample value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public StringAliasExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof NestedStringAliasExample
                        && this.value.equals(((NestedStringAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static NestedStringAliasExample valueOf(String value) {
        return of(StringAliasExample.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NestedStringAliasExample of(@Nonnull StringAliasExample value) {
        return new NestedStringAliasExample(value);
    }
}
