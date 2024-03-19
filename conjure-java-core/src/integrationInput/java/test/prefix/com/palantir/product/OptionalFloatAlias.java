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
public final class OptionalFloatAlias {
    private static final OptionalFloatAlias EMPTY = new OptionalFloatAlias();

    private final Optional<@Safe Float> value;

    private OptionalFloatAlias(@Nonnull Optional<@Safe Float> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalFloatAlias() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<@Safe Float> get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof OptionalFloatAlias && this.value.equals(((OptionalFloatAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalFloatAlias of(@Nonnull Optional<@Safe Float> value) {
        return new OptionalFloatAlias(value);
    }

    public static OptionalFloatAlias empty() {
        return EMPTY;
    }
}
