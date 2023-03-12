package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Immutable
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SafeLongAliasExample implements Comparable<SafeLongAliasExample> {
    private final @Safe SafeLong value;

    private SafeLongAliasExample(@Nonnull @Safe SafeLong value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public @Safe SafeLong get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof SafeLongAliasExample && this.value.equals(((SafeLongAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(SafeLongAliasExample other) {
        return value.compareTo(other.get());
    }

    public static SafeLongAliasExample valueOf(@Safe String value) {
        return of(SafeLong.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeLongAliasExample of(@Nonnull @Safe SafeLong value) {
        return new SafeLongAliasExample(value);
    }
}
