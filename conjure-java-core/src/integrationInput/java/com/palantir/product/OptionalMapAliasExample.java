package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalMapAliasExample {
    private static final OptionalMapAliasExample EMPTY = new OptionalMapAliasExample();

    private final Optional<Map<String, Object>> value;

    private int memoizedHashCode;

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
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OptionalMapAliasExample && equalTo((OptionalMapAliasExample) other));
    }

    private boolean equalTo(OptionalMapAliasExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.value.hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalMapAliasExample of(@Nonnull Optional<Map<String, Object>> value) {
        return new OptionalMapAliasExample(value);
    }

    public static OptionalMapAliasExample empty() {
        return EMPTY;
    }
}
