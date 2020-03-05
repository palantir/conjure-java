package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class StringAliasThree {
    private final StringAliasTwo value;

    private StringAliasThree(@NotNull StringAliasTwo value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public StringAliasTwo get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof StringAliasThree
                        && this.value.equals(((StringAliasThree) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static StringAliasThree of(@NotNull StringAliasTwo value) {
        return new StringAliasThree(value);
    }
}
