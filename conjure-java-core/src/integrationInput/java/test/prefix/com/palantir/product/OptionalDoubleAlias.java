package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Unsafe;
import java.util.OptionalDouble;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Unsafe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalDoubleAlias {
    private static final OptionalDoubleAlias EMPTY = new OptionalDoubleAlias();

    private final @Unsafe OptionalDouble value;

    private OptionalDoubleAlias(@Nonnull @Unsafe OptionalDouble value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalDoubleAlias() {
        this(OptionalDouble.empty());
    }

    @JsonValue
    public @Unsafe OptionalDouble get() {
        return value;
    }

    @Override
    @Unsafe
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof OptionalDoubleAlias && this.value.equals(((OptionalDoubleAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalDoubleAlias of(@Nonnull @Unsafe OptionalDouble value) {
        return new OptionalDoubleAlias(value);
    }

    public static OptionalDoubleAlias empty() {
        return EMPTY;
    }
}
