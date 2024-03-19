package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Generated("com.palantir.conjure.java.types.AliasGenerator")
public final class FloatAliasExample implements Comparable<FloatAliasExample> {
    private final float value;

    private FloatAliasExample(float value) {
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
        return this == other || (other instanceof FloatAliasExample && this.value == ((FloatAliasExample) other).value);
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public int compareTo(FloatAliasExample other) {
        return Float.compare(value, other.get());
    }

    public static FloatAliasExample valueOf(String value) {
        return of(Float.parseFloat(value));
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FloatAliasExample of(float value) {
        return new FloatAliasExample(value);
    }
}