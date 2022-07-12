package sealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SetAlias {
    private static final SetAlias EMPTY = new SetAlias();

    private final Set<String> value;

    private SetAlias(@Nonnull Set<String> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private SetAlias() {
        this(Collections.emptySet());
    }

    @JsonValue
    public Set<String> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof SetAlias && this.value.equals(((SetAlias) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SetAlias of(@Nonnull Set<String> value) {
        return new SetAlias(value);
    }

    public static SetAlias empty() {
        return EMPTY;
    }
}
