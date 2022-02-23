package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalSetAliasExample {
    private static final OptionalSetAliasExample EMPTY = new OptionalSetAliasExample();

    private final Optional<Set<String>> value;

    private OptionalSetAliasExample(@Nonnull Optional<Set<String>> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalSetAliasExample() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<Set<String>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OptionalSetAliasExample
                        && this.value.equals(((OptionalSetAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalSetAliasExample of(@Nonnull Optional<Set<String>> value) {
        return new OptionalSetAliasExample(value);
    }

    public static OptionalSetAliasExample empty() {
        return EMPTY;
    }
}
