package allexamples.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasOfAliasOfDoubleAliasExample implements Comparable<AliasOfAliasOfDoubleAliasExample> {
    private final AliasOfDoubleAliasExample value;

    private AliasOfAliasOfDoubleAliasExample(@Nonnull AliasOfDoubleAliasExample value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public AliasOfDoubleAliasExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof AliasOfAliasOfDoubleAliasExample
                        && equalTo((AliasOfAliasOfDoubleAliasExample) other));
    }

    private boolean equalTo(AliasOfAliasOfDoubleAliasExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(AliasOfAliasOfDoubleAliasExample other) {
        return value.compareTo(other.get());
    }

    public static AliasOfAliasOfDoubleAliasExample valueOf(String value) {
        return of(AliasOfDoubleAliasExample.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasOfAliasOfDoubleAliasExample of(@Nonnull AliasOfDoubleAliasExample value) {
        return new AliasOfAliasOfDoubleAliasExample(value);
    }
}
