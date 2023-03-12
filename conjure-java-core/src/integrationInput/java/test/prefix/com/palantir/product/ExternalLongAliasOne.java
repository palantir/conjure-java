package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.errorprone.annotations.Immutable;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Immutable
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExternalLongAliasOne {
    private final long value;

    private ExternalLongAliasOne(long value) {
        this.value = value;
    }

    @JsonValue
    public long get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExternalLongAliasOne && this.value == ((ExternalLongAliasOne) other).value);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExternalLongAliasOne valueOf(String value) {
        return of(Long.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExternalLongAliasOne of(long value) {
        return new ExternalLongAliasOne(value);
    }
}
