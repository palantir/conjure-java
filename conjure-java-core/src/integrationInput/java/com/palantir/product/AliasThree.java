package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasThree {
    private final AliasTwo value;

    private AliasThree(AliasTwo value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public AliasTwo get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof AliasThree && this.value.equals(((AliasThree) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static AliasThree valueOf(String value) {
        return new AliasThree(AliasTwo.valueOf(value));
    }

    @JsonCreator
    public static AliasThree of(AliasTwo value) {
        return new AliasThree(value);
    }
}
