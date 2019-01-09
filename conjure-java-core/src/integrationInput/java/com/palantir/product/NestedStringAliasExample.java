package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.ParametersAreNonnullByDefault;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@ParametersAreNonnullByDefault
public final class NestedStringAliasExample {
    private final StringAliasExample value;

    private NestedStringAliasExample(StringAliasExample value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public StringAliasExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof NestedStringAliasExample
                        && this.value.equals(((NestedStringAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static NestedStringAliasExample valueOf(String value) {
        return new NestedStringAliasExample(StringAliasExample.valueOf(value));
    }

    @JsonCreator
    public static NestedStringAliasExample of(StringAliasExample value) {
        return new NestedStringAliasExample(value);
    }
}
