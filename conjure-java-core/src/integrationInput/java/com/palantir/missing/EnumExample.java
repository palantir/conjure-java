package com.palantir.missing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import com.palantir.logsafe.Preconditions;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

/**
 * This enumerates the numbers 1:2 also 100.
 * <p>
 * This class is used instead of a native enum to support unknown values.
 * Rather than throw an exception, the {@link EnumExample#valueOf} method defaults to a new instantiation of
 * {@link EnumExample} where {@link EnumExample#get} will return {@link EnumExample.Value#UNKNOWN}.
 * <p>
 * For example, {@code EnumExample.valueOf("corrupted value").get()} will return {@link EnumExample.Value#UNKNOWN},
 * but {@link EnumExample#toString} will return "corrupted value".
 * <p>
 * There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Immutable
public final class EnumExample {
    public static final EnumExample ONE = new EnumExample(Value.ONE, "ONE");

    /**
     * @deprecated Prefer <code>ONE</code> where possible.
     */
    @Deprecated
    public static final EnumExample TWO = new EnumExample(Value.TWO, "TWO");

    /**
     * Value of 100.
     * @deprecated One is easier to manage.
     */
    @Deprecated
    public static final EnumExample ONE_HUNDRED = new EnumExample(Value.ONE_HUNDRED, "ONE_HUNDRED");

    @SuppressWarnings("deprecation")
    private static final List<EnumExample> values = Collections.unmodifiableList(Arrays.asList(ONE, TWO, ONE_HUNDRED));

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
        return (this == other) || (other instanceof EnumExample && this.string.equals(((EnumExample) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    @SuppressWarnings("deprecation")
    public static EnumExample valueOf(@Nonnull String value) {
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

    @SuppressWarnings("deprecation")
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

    public static List<EnumExample> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        ONE,

        /**
         * @deprecated Prefer <code>ONE</code> where possible.
         */
        @Deprecated
        TWO,

        /**
         * Value of 100.
         * @deprecated One is easier to manage.
         */
        @Deprecated
        ONE_HUNDRED,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitOne();

        /**
         * @deprecated Prefer <code>ONE</code> where possible.
         */
        @Deprecated
        T visitTwo();

        /**
         * Value of 100.
         * @deprecated One is easier to manage.
         */
        @Deprecated
        T visitOneHundred();

        T visitUnknown(String unknownValue);
    }
}
