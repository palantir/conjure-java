package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class DateTimeAliasExample {
    private final ZonedDateTime value;

    private DateTimeAliasExample(ZonedDateTime value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    @JsonValue
    public ZonedDateTime get() {
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
        return new DateTimeAliasExample(ZonedDateTime.parse(value));
    }

    @JsonCreator
    public static DateTimeAliasExample of(ZonedDateTime value) {
        return new DateTimeAliasExample(value);
    }
}
