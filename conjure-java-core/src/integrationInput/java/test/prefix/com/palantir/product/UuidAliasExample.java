package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.UnsafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

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
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof UuidAliasExample && this.value.equals(((UuidAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static UuidAliasExample valueOf(String value) {
        try {
            return of(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new SafeIllegalArgumentException("Unable to parse as UUID", e, UnsafeArg.of("input", value));
        }
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UuidAliasExample of(@Nonnull UUID value) {
        return new UuidAliasExample(value);
    }
}
