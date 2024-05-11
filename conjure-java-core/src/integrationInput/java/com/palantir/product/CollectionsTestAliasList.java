package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class CollectionsTestAliasList {
    private static final CollectionsTestAliasList EMPTY = new CollectionsTestAliasList();

    private final List<Integer> value;

    private int memoizedHashCode;

    private CollectionsTestAliasList(@Nonnull List<Integer> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private CollectionsTestAliasList() {
        this(Collections.emptyList());
    }

    @JsonValue
    public List<Integer> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof CollectionsTestAliasList && equalTo((CollectionsTestAliasList) other));
    }

    private boolean equalTo(CollectionsTestAliasList other) {
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
    public static CollectionsTestAliasList of(@Nonnull List<Integer> value) {
        return new CollectionsTestAliasList(value);
    }

    public static CollectionsTestAliasList empty() {
        return EMPTY;
    }
}
