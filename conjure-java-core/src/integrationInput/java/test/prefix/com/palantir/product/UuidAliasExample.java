package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.UUID;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class UuidAliasExample {
    private final UUID value;

    private UuidAliasExample(@Nonnull UUID value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public UUID get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof UuidAliasExample
                        && this.value.equals(((UuidAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static UuidAliasExample valueOf(String value) {
        return of(UUID.fromString(value));
    }

    @JsonCreator
    public static UuidAliasExample of(@Nonnull UUID value) {
        return new UuidAliasExample(value);
    }
}
