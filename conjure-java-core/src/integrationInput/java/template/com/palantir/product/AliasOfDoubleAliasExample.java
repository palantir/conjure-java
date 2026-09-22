package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasOfDoubleAliasExample implements Comparable<AliasOfDoubleAliasExample> {
    private final DoubleAliasExample value;

    private AliasOfDoubleAliasExample(@Nonnull DoubleAliasExample value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public DoubleAliasExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof AliasOfDoubleAliasExample && equalTo((AliasOfDoubleAliasExample) other));
    }

    private boolean equalTo(AliasOfDoubleAliasExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(AliasOfDoubleAliasExample other) {
        return value.compareTo(other.get());
    }

    public static AliasOfDoubleAliasExample valueOf(String value) {
        return of(DoubleAliasExample.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasOfDoubleAliasExample of(@Nonnull DoubleAliasExample value) {
        return new AliasOfDoubleAliasExample(value);
    }
}
