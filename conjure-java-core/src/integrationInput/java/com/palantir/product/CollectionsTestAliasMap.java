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
public final class CollectionsTestAliasMap {
    private static final CollectionsTestAliasMap EMPTY = new CollectionsTestAliasMap();

    private final Map<String, Integer> value;

    private int memoizedHashCode;

    private CollectionsTestAliasMap(@Nonnull Map<String, Integer> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private CollectionsTestAliasMap() {
        this(Collections.emptyMap());
    }

    @JsonValue
    public Map<String, Integer> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof CollectionsTestAliasMap && equalTo((CollectionsTestAliasMap) other));
    }

    private boolean equalTo(CollectionsTestAliasMap other) {
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
    public static CollectionsTestAliasMap of(@Nonnull Map<String, Integer> value) {
        return new CollectionsTestAliasMap(value);
    }

    public static CollectionsTestAliasMap empty() {
        return EMPTY;
    }
}
