package com.palantir.binary;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.nio.ByteBuffer;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class BinaryAliasExample {
    private final ByteBuffer value;

    private BinaryAliasExample(ByteBuffer value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public ByteBuffer get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BinaryAliasExample
                        && this.value.equals(((BinaryAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static BinaryAliasExample of(ByteBuffer value) {
        return new BinaryAliasExample(value);
    }
}
