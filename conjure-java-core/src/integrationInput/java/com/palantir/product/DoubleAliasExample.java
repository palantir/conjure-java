package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.SafeLong;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class DoubleAliasExample {
    private final double value;

    private DoubleAliasExample(double value) {
        this.value = value;
    }

    @JsonValue
    public double get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DoubleAliasExample && this.value == ((DoubleAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    public static DoubleAliasExample valueOf(String value) {
        return of(Double.parseDouble(value));
    }

    @JsonCreator
    public static DoubleAliasExample of(double value) {
        return new DoubleAliasExample(value);
    }

    @JsonCreator
    public static DoubleAliasExample of(long value) {
        long safeValue = SafeLong.of(value).longValue();
        return new DoubleAliasExample((double) safeValue);
    }

    @JsonCreator
    public static DoubleAliasExample of(int value) {
        return new DoubleAliasExample((double) value);
    }

    @JsonCreator
    public static DoubleAliasExample of(String value) {
        switch (value) {
            case "NaN":
                return DoubleAliasExample.of(Double.NaN);
            case "Infinity":
                return DoubleAliasExample.of(Double.POSITIVE_INFINITY);
            case "-Infinity":
                return DoubleAliasExample.of(Double.NEGATIVE_INFINITY);
            default:
                throw new IllegalArgumentException("Cannot deserialize string into double: " + value);
        }
    }
}
