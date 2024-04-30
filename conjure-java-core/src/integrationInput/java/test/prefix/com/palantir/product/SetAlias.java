package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SetAlias {
    private static final SetAlias EMPTY = new SetAlias();

    private final Set<String> value;

    private int memoizedHashCode;

    private SetAlias(@Nonnull Set<String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private SetAlias() {
        this(Collections.emptySet());
    }

    @JsonValue
    public Set<String> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SetAlias && equalTo((SetAlias) other));
    }

    private boolean equalTo(SetAlias other) {
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
    public static SetAlias of(@Nonnull Set<String> value) {
        return new SetAlias(value);
    }

    public static SetAlias empty() {
        return EMPTY;
    }
}
