package com.palantir.insensitive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Locale;
import javax.annotation.Generated;

/**
 * This enumerates the numbers 1:2 also 100.
 *
 * <p>This class is used instead of a native enum to support unknown values. Rather than throw an
 * exception, the {@link InsensitiveEnum#valueOf} method defaults to a new instantiation of {@link
 * InsensitiveEnum} where {@link InsensitiveEnum#get} will return {@link
 * InsensitiveEnum.Value#UNKNOWN}.
 *
 * <p>For example, {@code InsensitiveEnum.valueOf("corrupted value").get()} will return {@link
 * InsensitiveEnum.Value#UNKNOWN}, but {@link InsensitiveEnum#toString} will return "corrupted
 * value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at
 * compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
public final class InsensitiveEnum {
    public static final InsensitiveEnum ONE = new InsensitiveEnum(Value.ONE, "ONE");

    public static final InsensitiveEnum TWO = new InsensitiveEnum(Value.TWO, "TWO");

    /** Value of 100. */
    public static final InsensitiveEnum ONE_HUNDRED =
            new InsensitiveEnum(Value.ONE_HUNDRED, "ONE_HUNDRED");

    private final Value value;

    private final String string;

    private InsensitiveEnum(Value value, String string) {
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
                || (other instanceof InsensitiveEnum
                        && this.string.equals(((InsensitiveEnum) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator
    public static InsensitiveEnum valueOf(String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        value = value.toUpperCase(Locale.ENGLISH);
        switch (value) {
            case "ONE":
                return ONE;
            case "TWO":
                return TWO;
            case "ONE_HUNDRED":
                return ONE_HUNDRED;
            default:
                return new InsensitiveEnum(Value.UNKNOWN, value);
        }
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
