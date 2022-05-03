package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.time.OffsetDateTime;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class DateTimeAliasExample implements Comparable<DateTimeAliasExample> {
    private final OffsetDateTime value;

    private DateTimeAliasExample(@Nonnull OffsetDateTime value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public OffsetDateTime get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof DateTimeAliasExample && this.value.equals(((DateTimeAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(DateTimeAliasExample other) {
        return value.compareTo(other.get());
    }

    public static DateTimeAliasExample valueOf(String value) {
        return of(OffsetDateTime.parse(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static DateTimeAliasExample of(@Nonnull OffsetDateTime value) {
        return new DateTimeAliasExample(value);
    }
}
