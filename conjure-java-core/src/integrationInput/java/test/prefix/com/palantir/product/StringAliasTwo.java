package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasTwo {
    private static final StringAliasTwo EMPTY = new StringAliasTwo();

    private final Optional<StringAliasOne> value;

    private StringAliasTwo(@Nonnull Optional<StringAliasOne> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private StringAliasTwo() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<StringAliasOne> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof StringAliasTwo && equalTo((StringAliasTwo) other));
    }

    private boolean equalTo(StringAliasTwo other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StringAliasTwo of(@Nonnull Optional<StringAliasOne> value) {
        return new StringAliasTwo(value);
    }

    public static StringAliasTwo empty() {
        return EMPTY;
    }
}
