package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.internal.ConjureEnums;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;

/**
 * This class is used instead of a native enum to support unknown values. Rather than throw an
 * exception, the {@link SimpleEnum#valueOf} method defaults to a new instantiation of {@link
 * SimpleEnum} where {@link SimpleEnum#get} will return {@link SimpleEnum.Value#UNKNOWN}.
 *
 * <p>For example, {@code SimpleEnum.valueOf("corrupted value").get()} will return {@link
 * SimpleEnum.Value#UNKNOWN}, but {@link SimpleEnum#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at
 * compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
public final class SimpleEnum {
    public static final SimpleEnum VALUE = new SimpleEnum(Value.VALUE, "VALUE");

    public static final SimpleEnum VALUE2 = new SimpleEnum(Value.VALUE2, "VALUE2");

    private final Value value;

    private final String string;

    private SimpleEnum(Value value, String string) {
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
                || (other instanceof SimpleEnum && this.string.equals(((SimpleEnum) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator
    public static SimpleEnum valueOf(String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        switch (value) {
            case "VALUE":
                return VALUE;
            case "VALUE2":
                return VALUE2;
            default:
                ConjureEnums.validate(value);
                return new SimpleEnum(Value.UNKNOWN, value);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case VALUE:
                return visitor.visitValue();
            case VALUE2:
                return visitor.visitValue2();
            default:
                return visitor.visitUnknown(string);
        }
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        VALUE,

        VALUE2,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitValue();

        T visitValue2();

        T visitUnknown(String unknownValue);
    }
}
