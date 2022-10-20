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
        return this == other
                || (other instanceof CollectionsTestAliasSet
                        && this.value.equals(((CollectionsTestAliasSet) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CollectionsTestAliasSet of(@Nonnull Set<Integer> value) {
        return new CollectionsTestAliasSet(value);
    }

    public static CollectionsTestAliasSet empty() {
        return EMPTY;
    }
}
