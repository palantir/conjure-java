package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class ExternalFloatAliasExample {
    private final float value;

    private ExternalFloatAliasExample(float value) {
        this.value = value;
    }

    @JsonValue
    public float get() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof ExternalFloatAliasExample && equalTo((ExternalFloatAliasExample) other));
    }

    private boolean equalTo(ExternalFloatAliasExample other) {
        return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExternalFloatAliasExample valueOf(String value) {
        return of(Float.valueOf(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExternalFloatAliasExample of(float value) {
        return new ExternalFloatAliasExample(value);
    }
}
