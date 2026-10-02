package sealedunions.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SimpleUnionAlias {
    private final SimpleUnion value;

    private SimpleUnionAlias(@Nonnull SimpleUnion value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public SimpleUnion get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SimpleUnionAlias && equalTo((SimpleUnionAlias) other));
    }

    private boolean equalTo(SimpleUnionAlias other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SimpleUnionAlias of(@Nonnull SimpleUnion value) {
        return new SimpleUnionAlias(value);
    }
}
