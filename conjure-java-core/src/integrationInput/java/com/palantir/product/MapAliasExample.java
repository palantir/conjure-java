package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class MapAliasExample {
    private static final MapAliasExample EMPTY = new MapAliasExample();

    private final Map<String, Object> value;

    private int memoizedHashCode;

    private MapAliasExample(@Nonnull Map<String, Object> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private MapAliasExample() {
        this(Collections.emptyMap());
    }

    @JsonValue
    public Map<String, Object> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof MapAliasExample && equalTo((MapAliasExample) other));
    }

    private boolean equalTo(MapAliasExample other) {
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
    public static MapAliasExample of(@Nonnull Map<String, Object> value) {
        return new MapAliasExample(value);
    }

    public static MapAliasExample empty() {
        return EMPTY;
    }
}
