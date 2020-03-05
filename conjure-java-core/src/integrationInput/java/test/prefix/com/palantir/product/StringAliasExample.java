package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasExample {
    private final String value;

    private StringAliasExample(@NotNull String value) {
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
    public boolean equals(Object other) {
        return this == other
                || (other instanceof StringAliasExample
                        && this.value.equals(((StringAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    public static StringAliasExample valueOf(String value) {
        return of(value);
    }

    @JsonCreator
    public static StringAliasExample of(@NotNull String value) {
        return new StringAliasExample(value);
    }
}
