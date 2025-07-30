package errors.com.palantir.product;

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
public final class OptionalExample {
    private static final OptionalExample EMPTY = new OptionalExample();

    private final Optional<@Safe String> value;

    private OptionalExample(@Nonnull Optional<@Safe String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalExample() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<@Safe String> get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OptionalExample && equalTo((OptionalExample) other));
    }

    private boolean equalTo(OptionalExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalExample of(@Nonnull Optional<@Safe String> value) {
        return new OptionalExample(value);
    }

    public static OptionalExample empty() {
        return EMPTY;
    }
}
