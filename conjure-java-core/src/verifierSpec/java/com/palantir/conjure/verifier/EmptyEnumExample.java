package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Generated;

/**
 * This class is used instead of a native enum to support unknown values. Rather than throw an
 * exception, the {@link EmptyEnumExample#valueOf} method defaults to a new instantiation of {@link
 * EmptyEnumExample} where {@link EmptyEnumExample#get} will return {@link
 * EmptyEnumExample.Value#UNKNOWN}.
 *
 * <p>For example, {@code EmptyEnumExample.valueOf("corrupted value").get()} will return {@link
 * EmptyEnumExample.Value#UNKNOWN}, but {@link EmptyEnumExample#toString} will return "corrupted
 * value".
 *
 * <p>There is no method to access all instantiations of this class, since they cannot be known at
 * compile time.
 */
@Generated("com.palantir.conjure.java.types.EnumGenerator")
public final class EmptyEnumExample {
    private final Value value;

    private final String string;

    private EmptyEnumExample(Value value, String string) {
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
                || (other instanceof EmptyEnumExample
                        && this.string.equals(((EmptyEnumExample) other).string));
    }

    @Override
    public int hashCode() {
        return this.string.hashCode();
    }

    @JsonCreator
    public static EmptyEnumExample valueOf(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        String upperCasedValue = value.toUpperCase(Locale.ROOT);
        switch (upperCasedValue) {
            default:
                return new EmptyEnumExample(Value.UNKNOWN, upperCasedValue);
        }
    }

    @Generated("com.palantir.conjure.java.types.EnumGenerator")
    public enum Value {
        UNKNOWN
    }
}
