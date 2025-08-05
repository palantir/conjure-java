package jersey.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class NestedAlias {
    private final StringAliasEx value;

    private NestedAlias(@Nonnull StringAliasEx value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public StringAliasEx get() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof NestedAlias && equalTo((NestedAlias) other));
    }

    private boolean equalTo(NestedAlias other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    public static NestedAlias valueOf(@Safe String value) {
        return of(StringAliasEx.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static NestedAlias of(@Nonnull StringAliasEx value) {
        return new NestedAlias(value);
    }
}
