package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ReferenceAliasExample {
    private final AnyExample value;

    private ReferenceAliasExample(AnyExample value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public AnyExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ReferenceAliasExample
                        && this.value.equals(((ReferenceAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static ReferenceAliasExample of(AnyExample value) {
        return new ReferenceAliasExample(value);
    }
}
