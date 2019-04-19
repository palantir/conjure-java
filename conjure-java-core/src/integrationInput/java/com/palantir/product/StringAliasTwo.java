package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasTwo {
    private final Optional<StringAliasOne> value;

    private StringAliasTwo(Optional<StringAliasOne> value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public Optional<StringAliasOne> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof StringAliasTwo
                        && this.value.equals(((StringAliasTwo) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static StringAliasTwo of(Optional<StringAliasOne> value) {
        return new StringAliasTwo(value);
    }
}
