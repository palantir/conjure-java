package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasExample implements Comparable<StringAliasExample> {
    private final @Safe String value;

    private StringAliasExample(@Nonnull @Safe String value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public @Safe String get() {
        return value;
    }

    @Override
    @Safe
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof StringAliasExample && equalTo((StringAliasExample) other));
    }

    private boolean equalTo(StringAliasExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(StringAliasExample other) {
        return value.compareTo(other.get());
    }

    public static StringAliasExample valueOf(@Safe String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StringAliasExample of(@Nonnull @Safe String value) {
        return new StringAliasExample(value);
    }
}
