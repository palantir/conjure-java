package defensivenullablecollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ConjureGenerated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedList {
    private static final ExampleDefensiveAliasedList EMPTY = new ExampleDefensiveAliasedList();

    private final List<String> value;

    private int memoizedHashCode;

    private ExampleDefensiveAliasedList(@Nonnull List<String> value) {
        this.value = ConjureCollections.unmodifiableList(value);
    }

    private ExampleDefensiveAliasedList() {
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
        return this == other
                || (other instanceof ExampleDefensiveAliasedList && equalTo((ExampleDefensiveAliasedList) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedList other) {
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
    public static ExampleDefensiveAliasedList of(@Nonnull List<String> value) {
        return new ExampleDefensiveAliasedList(
                ConjureCollections.newList(Preconditions.checkNotNull(value, "value cannot be null")));
    }

    public static ExampleDefensiveAliasedList empty() {
        return EMPTY;
    }
}
