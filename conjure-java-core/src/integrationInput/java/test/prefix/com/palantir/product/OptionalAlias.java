package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Optional;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class OptionalAlias {
    private final Optional<String> value;

    private OptionalAlias(@Nonnull Optional<String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private OptionalAlias() {
        this(Optional.empty());
    }

    @JsonValue
    public Optional<String> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof OptionalAlias && this.value.equals(((OptionalAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static OptionalAlias of(@Nonnull Optional<String> value) {
        return new OptionalAlias(value);
    }
}
