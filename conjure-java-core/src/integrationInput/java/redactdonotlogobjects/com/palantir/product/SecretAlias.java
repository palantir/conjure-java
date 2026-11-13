package redactdonotlogobjects.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class SecretAlias implements Comparable<SecretAlias> {
    private final @DoNotLog String value;

    private SecretAlias(@Nonnull @DoNotLog String value) {
        this.value = Preconditions.checkNotNull(value, "value cannot be null");
    }

    @JsonValue
    public @DoNotLog String get() {
        return value;
    }

    @Override
    @DoNotLog
    public String toString() {
        return "REDACTED";
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SecretAlias && equalTo((SecretAlias) other));
    }

    private boolean equalTo(SecretAlias other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public int compareTo(SecretAlias other) {
        return value.compareTo(other.get());
    }

    public static SecretAlias valueOf(@DoNotLog String value) {
        return of(value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SecretAlias of(@Nonnull @DoNotLog String value) {
        return new SecretAlias(value);
    }
}
