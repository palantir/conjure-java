package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SafeLongAliasExample {
    private final SafeLong value;

    private SafeLongAliasExample(@NotNull SafeLong value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public SafeLong get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof SafeLongAliasExample
                        && this.value.equals(((SafeLongAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static SafeLongAliasExample valueOf(String value) {
        return of(SafeLong.valueOf(value));
    }

    @JsonCreator
    public static SafeLongAliasExample of(@NotNull SafeLong value) {
        return new SafeLongAliasExample(value);
    }
}
