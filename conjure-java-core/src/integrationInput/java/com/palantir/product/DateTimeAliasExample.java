package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.time.OffsetDateTime;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class DateTimeAliasExample implements Comparable<DateTimeAliasExample> {
    private final OffsetDateTime value;

    private int memoizedHashCode;

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
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof DateTimeAliasExample && equalTo((DateTimeAliasExample) other));
    }

    private boolean equalTo(DateTimeAliasExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.value.isEqual(other.value);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.value.toInstant().hashCode();
            memoizedHashCode = result;
        }
        return result;
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
