package jakartaservice.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@ConjureGenerated("com.palantir.conjure.java.types.AliasGenerator")
public final class AliasedBinary {
    private final ByteBuffer value;

    private AliasedBinary(@Nonnull ByteBuffer value) {
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
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof AliasedBinary && equalTo((AliasedBinary) other));
    }

    private boolean equalTo(AliasedBinary other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AliasedBinary of(@Nonnull ByteBuffer value) {
        return new AliasedBinary(value);
    }
}
