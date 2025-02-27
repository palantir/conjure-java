package defensivenonnullcollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedPrimitiveList {
    private static final ExampleDefensiveAliasedPrimitiveList EMPTY = new ExampleDefensiveAliasedPrimitiveList();

    private final List<Double> value;

    private int memoizedHashCode;

    private ExampleDefensiveAliasedPrimitiveList(@Nonnull List<Double> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private ExampleDefensiveAliasedPrimitiveList() {
        this(Collections.emptyList());
    }

    @JsonValue
    public List<Double> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveAliasedPrimitiveList
                        && equalTo((ExampleDefensiveAliasedPrimitiveList) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedPrimitiveList other) {
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
    public static ExampleDefensiveAliasedPrimitiveList of(@Nonnull List<Double> value) {
        return new ExampleDefensiveAliasedPrimitiveList(value);
    }

    public static ExampleDefensiveAliasedPrimitiveList empty() {
        return EMPTY;
    }
}
