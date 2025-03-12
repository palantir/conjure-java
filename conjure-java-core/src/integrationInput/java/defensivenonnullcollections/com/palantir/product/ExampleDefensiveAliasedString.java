package defensivenonnullcollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

/** This class should remain unchanged with the defensiveCollections flag. */
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExampleDefensiveAliasedString implements Comparable<ExampleDefensiveAliasedString> {
    private final String value;

    private ExampleDefensiveAliasedString(@Nonnull String value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExampleDefensiveAliasedString && equalTo((ExampleDefensiveAliasedString) other));
    }

    private boolean equalTo(ExampleDefensiveAliasedString other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(ExampleDefensiveAliasedString other) {
        return value.compareTo(other.get());
    }

    public static ExampleDefensiveAliasedString valueOf(String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExampleDefensiveAliasedString of(@Nonnull String value) {
        return new ExampleDefensiveAliasedString(value);
    }
}
