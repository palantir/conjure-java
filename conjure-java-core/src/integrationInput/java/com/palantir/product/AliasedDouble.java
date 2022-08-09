package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.SafeLong;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasedDouble implements Comparable<AliasedDouble> {
    private final double value;

    private AliasedDouble(double value) {
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
                || (other instanceof AliasedDouble
                        && Double.doubleToLongBits(this.value)
                                == Double.doubleToLongBits(((AliasedDouble) other).value));
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public int compareTo(AliasedDouble other) {
        return Double.compare(value, other.get());
    }

    public static AliasedDouble valueOf(String value) {
        return of(Double.parseDouble(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedDouble of(double value) {
        return new AliasedDouble(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedDouble of(long value) {
        long safeValue = SafeLong.of(value).longValue();
        return new AliasedDouble((double) safeValue);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedDouble of(int value) {
        return new AliasedDouble((double) value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    private static AliasedDouble of(BigDecimal value) {
        return new AliasedDouble(value.doubleValue());
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedDouble of(String value) {
        switch (value) {
            case "NaN":
                return AliasedDouble.of(Double.NaN);
            case "Infinity":
                return AliasedDouble.of(Double.POSITIVE_INFINITY);
            case "-Infinity":
                return AliasedDouble.of(Double.NEGATIVE_INFINITY);
            default:
                throw new IllegalArgumentException("Cannot deserialize string into double: " + value);
        }
    }
}
