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
public final class SafeExternalAliasExample {
    private final SafeExternalLongAlias value;

    private SafeExternalAliasExample(@Nonnull SafeExternalLongAlias value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public SafeExternalLongAlias get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof SafeExternalAliasExample
                        && this.value.equals(((SafeExternalAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static SafeExternalAliasExample valueOf(String value) {
        return of(SafeExternalLongAlias.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SafeExternalAliasExample of(@Nonnull SafeExternalLongAlias value) {
        return new SafeExternalAliasExample(value);
    }
}
