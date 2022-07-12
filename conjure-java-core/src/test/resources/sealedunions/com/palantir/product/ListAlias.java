package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ListAlias {
    private static final ListAlias EMPTY = new ListAlias();

    private final List<String> value;

    private ListAlias(@Nonnull List<String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ListAlias() {
        this(Collections.emptyList());
    }

    @JsonValue
    public List<String> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof ListAlias && this.value.equals(((ListAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ListAlias of(@Nonnull List<String> value) {
        return new ListAlias(value);
    }

    public static ListAlias empty() {
        return EMPTY;
    }
}
