package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalListAliasExample {
    private static final OptionalListAliasExample EMPTY = new OptionalListAliasExample();

    private final Optional<List<String>> value;

    private OptionalListAliasExample(@Nonnull Optional<List<String>> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalListAliasExample() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<List<String>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OptionalListAliasExample
                        && this.value.equals(((OptionalListAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalListAliasExample of(@Nonnull Optional<List<String>> value) {
        return new OptionalListAliasExample(value);
    }

    public static OptionalListAliasExample empty() {
        return EMPTY;
    }
}
