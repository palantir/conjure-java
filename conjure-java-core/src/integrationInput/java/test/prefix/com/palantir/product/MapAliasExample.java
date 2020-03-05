package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class MapAliasExample {
    private final Map<String, Object> value;

    private MapAliasExample(@Nonnull Map<String, Object> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public Map<String, Object> get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof MapAliasExample
                        && this.value.equals(((MapAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static MapAliasExample of(@Nonnull Map<String, Object> value) {
        return new MapAliasExample(value);
    }
}
