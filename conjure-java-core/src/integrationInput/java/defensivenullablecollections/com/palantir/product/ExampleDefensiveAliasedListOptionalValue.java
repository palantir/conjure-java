package defensivenullablecollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedListOptionalValue {
    private static final ExampleDefensiveAliasedListOptionalValue EMPTY =
            new ExampleDefensiveAliasedListOptionalValue();

    private final List<Optional<String>> value;

    private int memoizedHashCode;

    private ExampleDefensiveAliasedListOptionalValue(@Nonnull List<Optional<String>> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ExampleDefensiveAliasedListOptionalValue() {
        this(Collections.emptyList());
    }

    @JsonValue
    public List<Optional<String>> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveAliasedListOptionalValue
                        && equalTo((ExampleDefensiveAliasedListOptionalValue) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedListOptionalValue other) {
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
    public static ExampleDefensiveAliasedListOptionalValue of(@Nonnull List<Optional<String>> value) {
        return new ExampleDefensiveAliasedListOptionalValue(value);
    }

    public static ExampleDefensiveAliasedListOptionalValue empty() {
        return EMPTY;
    }
}
