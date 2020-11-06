package com.palantir.product;

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
 * This class is used instead of a native enum to support unknown values.
 * Rather than throw an exception, the {@link SimpleEnum#valueOf} method defaults to a new instantiation of
 * {@link SimpleEnum} where {@link SimpleEnum#get} will return {@link SimpleEnum.Value#UNKNOWN}.
 * <p>
 * For example, {@code SimpleEnum.valueOf("corrupted value").get()} will return {@link SimpleEnum.Value#UNKNOWN},
 * but {@link SimpleEnum#toString} will return "corrupted value".
 * <p>
 * There is no method to access all instantiations of this class, since they cannot be known at compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
@Immutable
public final class SimpleEnum {
    public static final SimpleEnum VALUE = new SimpleEnum(Value.VALUE, "VALUE");

    private static final List<SimpleEnum> values = Collections.unmodifiableList(Arrays.asList(VALUE));

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
        return (this == other) || (other instanceof SimpleEnum && this.string.equals(((SimpleEnum) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SimpleEnum valueOf(@Nonnull String value) {
        Preconditions.checkNotNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "VALUE":
                return VALUE;
            default:
                return new SimpleEnum(Value.UNKNOWN, upperCasedValue);
        }
    }

    public <T> T accept(Visitor<T> visitor) {
        switch (value) {
            case VALUE:
                return visitor.visitValue();
            default:
                return visitor.visitUnknown(string);
        }
    }

    public static List<SimpleEnum> values() {
        return values;
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        VALUE,

        UNKNOWN
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public interface Visitor<T> {
        T visitValue();

        T visitUnknown(String unknownValue);
    }
}
