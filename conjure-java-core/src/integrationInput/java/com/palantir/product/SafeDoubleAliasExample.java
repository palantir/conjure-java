package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Safe;
import java.math.BigDecimal;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SafeDoubleAliasExample implements Comparable<SafeDoubleAliasExample> {
    private final @Safe double value;

    private SafeDoubleAliasExample(@Safe double value) {
        this.value = value;
    }

    @JsonValue
    public @Safe double get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeDoubleAliasExample && equalTo((SafeDoubleAliasExample) other));
    }

    private boolean equalTo(SafeDoubleAliasExample other) {
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.value);
    }

    @Override
    public int compareTo(SafeDoubleAliasExample other) {
        return Double.compare(value, other.get());
    }

    public static SafeDoubleAliasExample valueOf(@Safe String value) {
        return of(Double.parseDouble(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeDoubleAliasExample of(@Safe double value) {
        return new SafeDoubleAliasExample(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeDoubleAliasExample of(@Safe long value) {
        long safeValue = SafeLong.of(value).longValue();
        return new SafeDoubleAliasExample((double) safeValue);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeDoubleAliasExample of(@Safe int value) {
        return new SafeDoubleAliasExample((double) value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private static SafeDoubleAliasExample of(@Safe BigDecimal value) {
        return new SafeDoubleAliasExample(value.doubleValue());
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeDoubleAliasExample of(@Safe String value) {
        switch (value) {
            case "NaN":
                return SafeDoubleAliasExample.of(Double.NaN);
            case "Infinity":
                return SafeDoubleAliasExample.of(Double.POSITIVE_INFINITY);
            case "-Infinity":
                return SafeDoubleAliasExample.of(Double.NEGATIVE_INFINITY);
            default:
                throw new IllegalArgumentException("Cannot deserialize string into double: " + value);
        }
    }
}
