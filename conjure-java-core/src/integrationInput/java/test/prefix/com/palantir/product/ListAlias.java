package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ListAlias {
    private static final ListAlias EMPTY = new ListAlias();

    private final List<String> value;

    private int memoizedHashCode;

    private ListAlias(@Nonnull List<String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ListAlias() {
        this(Collections.emptyList());
    }

    @JsonValue
    public List<String> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ListAlias && equalTo((ListAlias) other));
    }

    private boolean equalTo(ListAlias other) {
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
    public static ListAlias of(@Nonnull List<String> value) {
        return new ListAlias(value);
    }

    public static ListAlias empty() {
        return EMPTY;
    }
}
