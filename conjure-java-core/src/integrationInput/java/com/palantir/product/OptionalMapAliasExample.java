package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalMapAliasExample {
    private static final OptionalMapAliasExample EMPTY = new OptionalMapAliasExample();

    private final Optional<Map<String, Object>> value;

    private OptionalMapAliasExample(@Nonnull Optional<Map<String, Object>> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalMapAliasExample() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<Map<String, Object>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OptionalMapAliasExample
                        && this.value.equals(((OptionalMapAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalMapAliasExample of(@Nonnull Optional<Map<String, Object>> value) {
        return new OptionalMapAliasExample(value);
    }

    public static OptionalMapAliasExample empty() {
        return EMPTY;
    }
}
