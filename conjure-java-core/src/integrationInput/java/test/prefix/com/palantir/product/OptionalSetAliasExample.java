package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalSetAliasExample {
    private static final OptionalSetAliasExample EMPTY = new OptionalSetAliasExample();

    private final Optional<Set<String>> value;

    private int memoizedHashCode;

    private OptionalSetAliasExample(@Nonnull Optional<Set<String>> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalSetAliasExample() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<Set<String>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OptionalSetAliasExample && equalTo((OptionalSetAliasExample) other));
    }

    private boolean equalTo(OptionalSetAliasExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.value.hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalSetAliasExample of(@Nonnull Optional<Set<String>> value) {
        return new OptionalSetAliasExample(value);
    }

    public static OptionalSetAliasExample empty() {
        return EMPTY;
    }
}
