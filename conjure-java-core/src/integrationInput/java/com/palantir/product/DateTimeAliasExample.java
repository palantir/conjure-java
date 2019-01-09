package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.ParametersAreNonnullByDefault;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
@ParametersAreNonnullByDefault
public final class DateTimeAliasExample {
    private final OffsetDateTime value;

    private DateTimeAliasExample(OffsetDateTime value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
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
                || (other instanceof DateTimeAliasExample
                        && this.value.equals(((DateTimeAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static DateTimeAliasExample valueOf(String value) {
        return new DateTimeAliasExample(OffsetDateTime.parse(value));
    }

    @JsonCreator
    public static DateTimeAliasExample of(OffsetDateTime value) {
        return new DateTimeAliasExample(value);
    }
}
