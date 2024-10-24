package com.company.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class Ingredient implements Comparable<Ingredient> {
    private final String value;

    private Ingredient(@Nonnull String value) {
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
        return this == other || (other instanceof Ingredient && equalTo((Ingredient) other));
    }

    private boolean equalTo(Ingredient other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(Ingredient other) {
        return value.compareTo(other.get());
    }

    public static Ingredient valueOf(String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Ingredient of(@Nonnull String value) {
        return new Ingredient(value);
    }
}
