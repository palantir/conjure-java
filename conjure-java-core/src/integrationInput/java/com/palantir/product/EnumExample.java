package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.Generated;

/**
 * This enumerates the numbers 1:2 also 100.
 *
 * <p>This class is used instead of a native enum to support unknown values. Rather than throw an
 * exception, the {@link EnumExample#valueOf} method defaults to a new instantiation of {@link
 * EnumExample} where {@link EnumExample#get} will return {@link EnumExample.Value#UNKNOWN}.
 *
 * <p>For example, {@code EnumExample.valueOf("corrupted value").get()} will return {@link
 * EnumExample.Value#UNKNOWN}, but {@link EnumExample#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at
 * compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
public final class EnumExample {
    public static final EnumExample ONE = new EnumExample(Value.ONE, "ONE");

    public static final EnumExample TWO = new EnumExample(Value.TWO, "TWO");

    /** Value of 100. */
    public static final EnumExample ONE_HUNDRED = new EnumExample(Value.ONE_HUNDRED, "ONE_HUNDRED");

    public static final List<EnumExample> VALUES =
            Collections.unmodifiableList(Arrays.asList(ONE, TWO, ONE_HUNDRED));

    private final Value value;

    private final String string;

    private EnumExample(Value value, String string) {
        this.value = value;
        this.string = string;
    }

    public Value get() {
        return this.value;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.string;
    }

    @Override
    public boolean equals(Object other) {
        return (this == other)
                || (other instanceof EnumExample
                        && this.string.equals(((EnumExample) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator
    public static EnumExample valueOf(String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "ONE":
                return ONE;
            case "TWO":
                return TWO;
            case "ONE_HUNDRED":
                return ONE_HUNDRED;
            default:
                return new EnumExample(Value.UNKNOWN, upperCasedValue);
        }
    }

    public static List<EnumExample> values() {
        return VALUES;
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case ONE:
                return visitor.visitOne();
            case TWO:
                return visitor.visitTwo();
            case ONE_HUNDRED:
                return visitor.visitOneHundred();
            default:
                return visitor.visitUnknown(string);
        }
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        ONE,

        TWO,

        /** Value of 100. */
        ONE_HUNDRED,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitOne();

        T visitTwo();

        /** Value of 100. */
        T visitOneHundred();

        T visitUnknown(String unknownValue);
    }
}
