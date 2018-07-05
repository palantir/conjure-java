package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Generated;

/**
 * This class is used instead of a native enum to support unknown values. Rather than throw an
 * exception, the {@link SimpleEnums#valueOf} method defaults to a new instantiation of {@link
 * SimpleEnums} where {@link SimpleEnums#get} will return {@link SimpleEnums.Value#UNKNOWN}.
 *
 * <p>For example, {@code SimpleEnums.valueOf("corrupted value").get()} will return {@link
 * SimpleEnums.Value#UNKNOWN}, but {@link SimpleEnums#toString} will return "corrupted value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at
 * compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
public final class SimpleEnums {
    public static final SimpleEnums FOO = new SimpleEnums(Value.FOO, "FOO");

    public static final SimpleEnums BAR = new SimpleEnums(Value.BAR, "BAR");

    public static final SimpleEnums BAZ = new SimpleEnums(Value.BAZ, "BAZ");

    private final Value value;

    private final String string;

    private SimpleEnums(Value value, String string) {
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
                || (other instanceof SimpleEnums
                        && this.string.equals(((SimpleEnums) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator
    public static SimpleEnums valueOf(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            case "FOO":
                return FOO;
            case "BAR":
                return BAR;
            case "BAZ":
                return BAZ;
            default:
                return new SimpleEnums(Value.UNKNOWN, upperCasedValue);
        }
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        FOO,

        BAR,

        BAZ,

        UNKNOWN
    }
}
