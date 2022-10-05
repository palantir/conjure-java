package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalAlias {
    private static final OptionalAlias EMPTY = new OptionalAlias();

    private final Optional<@Safe String> value;

    private OptionalAlias(@Nonnull Optional<@Safe String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalAlias() {
        this(Optional.empty());
    }

    @JsonValue
    @Safe
    public Optional<@Safe String> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OptionalAlias && this.value.equals(((OptionalAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalAlias of(@Safe @Nonnull Optional<@Safe String> value) {
        return new OptionalAlias(value);
    }

    public static OptionalAlias empty() {
        return EMPTY;
    }
}
