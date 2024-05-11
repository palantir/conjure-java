package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class CollectionsTestAliasSet {
    private static final CollectionsTestAliasSet EMPTY = new CollectionsTestAliasSet();

    private final Set<Integer> value;

    private int memoizedHashCode;

    private CollectionsTestAliasSet(@Nonnull Set<Integer> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private CollectionsTestAliasSet() {
        this(Collections.emptySet());
    }

    @JsonValue
    public Set<Integer> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof CollectionsTestAliasSet && equalTo((CollectionsTestAliasSet) other));
    }

    private boolean equalTo(CollectionsTestAliasSet other) {
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
    public static CollectionsTestAliasSet of(@Nonnull Set<Integer> value) {
        return new CollectionsTestAliasSet(value);
    }

    public static CollectionsTestAliasSet empty() {
        return EMPTY;
    }
}
