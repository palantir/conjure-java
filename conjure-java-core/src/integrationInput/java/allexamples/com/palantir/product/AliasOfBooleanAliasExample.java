package allexamples.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasOfBooleanAliasExample {
    private final BooleanAliasExample value;

    private AliasOfBooleanAliasExample(@Nonnull BooleanAliasExample value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public BooleanAliasExample get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof AliasOfBooleanAliasExample && equalTo((AliasOfBooleanAliasExample) other));
    }

    private boolean equalTo(AliasOfBooleanAliasExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public static AliasOfBooleanAliasExample valueOf(String value) {
        return of(BooleanAliasExample.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasOfBooleanAliasExample of(@Nonnull BooleanAliasExample value) {
        return new AliasOfBooleanAliasExample(value);
    }
}
