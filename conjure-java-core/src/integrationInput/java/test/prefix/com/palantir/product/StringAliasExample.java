package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasExample implements Comparable<StringAliasExample> {
    private final String value;

    private StringAliasExample(@Nonnull String value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    @Safe
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof StringAliasExample && this.value.equals(((StringAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(StringAliasExample other) {
        return value.compareTo(other.get());
    }

    public static StringAliasExample valueOf(@Safe String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StringAliasExample of(@Safe @Nonnull String value) {
        return new StringAliasExample(value);
    }
}
