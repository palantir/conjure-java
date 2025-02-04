package test.defensive.collections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedMap {
    private static final ExampleDefensiveAliasedMap EMPTY = new ExampleDefensiveAliasedMap();

    private final Map<String, Boolean> value;

    private int memoizedHashCode;

    private ExampleDefensiveAliasedMap(@Nonnull Map<String, Boolean> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ExampleDefensiveAliasedMap() {
        this(Collections.emptyMap());
    }

    @JsonValue
    public Map<String, Boolean> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveAliasedMap && equalTo((ExampleDefensiveAliasedMap) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedMap other) {
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
    public static ExampleDefensiveAliasedMap of(@Nonnull Map<String, Boolean> value) {
        return new ExampleDefensiveAliasedMap(
                new LinkedHashMap<>(Preconditions.checkNotNull(value, "value cannot be null")));
    }

    public static ExampleDefensiveAliasedMap empty() {
        return EMPTY;
    }
}
