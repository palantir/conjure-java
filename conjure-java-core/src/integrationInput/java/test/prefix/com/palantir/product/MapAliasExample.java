package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class MapAliasExample {
    private static final MapAliasExample EMPTY = new MapAliasExample();

    private final Map<String, Object> value;

    private MapAliasExample(@Nonnull Map<String, Object> value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    private MapAliasExample() {
        this(Collections.emptyMap());
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
                || (other instanceof MapAliasExample && this.value.equals(((MapAliasExample) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static MapAliasExample of(@Nonnull Map<String, Object> value) {
        return new MapAliasExample(value);
    }

    public static MapAliasExample empty() {
        return EMPTY;
    }
}
