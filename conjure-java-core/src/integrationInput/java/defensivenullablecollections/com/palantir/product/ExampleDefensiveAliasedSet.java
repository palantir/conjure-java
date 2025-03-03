package defensivenullablecollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedSet {
    private static final ExampleDefensiveAliasedSet EMPTY = new ExampleDefensiveAliasedSet();

    private final Set<Integer> value;

    private int memoizedHashCode;

    private ExampleDefensiveAliasedSet(@Nonnull Set<Integer> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ExampleDefensiveAliasedSet() {
        this(Collections.emptySet());
    }

    @JsonValue
    public Set<Integer> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveAliasedSet && equalTo((ExampleDefensiveAliasedSet) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedSet other) {
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
    public static ExampleDefensiveAliasedSet of(
            @Nonnull @JsonDeserialize(as = LinkedHashSet.class) Set<Integer> value) {
        return new ExampleDefensiveAliasedSet(
                ConjureCollections.newSet(Preconditions.checkNotNull(value, "value cannot be null")));
    }

    public static ExampleDefensiveAliasedSet empty() {
        return EMPTY;
    }
}
