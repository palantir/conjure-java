package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import java.nio.ByteBuffer;
import javax.annotation.Generated;
import javax.validation.constraints.NotNull;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class BinaryAliasOne {
    private final ByteBuffer value;

    private BinaryAliasOne(@NotNull ByteBuffer value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public ByteBuffer get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof BinaryAliasOne
                        && this.value.equals(((BinaryAliasOne) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @JsonCreator
    public static BinaryAliasOne of(@NotNull ByteBuffer value) {
        return new BinaryAliasOne(value);
    }
}
