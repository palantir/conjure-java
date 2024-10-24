package com.company.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class RecipeName implements Comparable<RecipeName> {
    private final String value;

    private RecipeName(@Nonnull String value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof RecipeName && equalTo((RecipeName) other));
    }

    private boolean equalTo(RecipeName other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(RecipeName other) {
        return value.compareTo(other.get());
    }

    public static RecipeName valueOf(String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RecipeName of(@Nonnull String value) {
        return new RecipeName(value);
    }
}
