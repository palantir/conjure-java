package defensivenullablecollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedMapOptionalValue {
    private static final ExampleDefensiveAliasedMapOptionalValue EMPTY = new ExampleDefensiveAliasedMapOptionalValue();

    private final Map<String, Optional<String>> value;

    private int memoizedHashCode;

    private ExampleDefensiveAliasedMapOptionalValue(@Nonnull Map<String, Optional<String>> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ExampleDefensiveAliasedMapOptionalValue() {
        this(Collections.emptyMap());
    }

    @JsonValue
    public Map<String, Optional<String>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveAliasedMapOptionalValue
                        && equalTo((ExampleDefensiveAliasedMapOptionalValue) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedMapOptionalValue other) {
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
    public static ExampleDefensiveAliasedMapOptionalValue of(@Nonnull Map<String, Optional<String>> value) {
        return new ExampleDefensiveAliasedMapOptionalValue(value);
    }

    public static ExampleDefensiveAliasedMapOptionalValue empty() {
        return EMPTY;
    }
}
