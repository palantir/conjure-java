package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = FloatExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class FloatExample {
    private final float floatValue;

    private FloatExample(float floatValue) {
        this.floatValue = floatValue;
    }

    @JsonProperty("floatValue")
    public float getFloatValue() {
        return this.floatValue;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof FloatExample && equalTo((FloatExample) other));
    }

    private boolean equalTo(FloatExample other) {
        return this.floatValue == other.floatValue;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.floatValue);
    }

    @Override
    public String toString() {
        return "FloatExample{floatValue: " + floatValue + '}';
    }

    public static FloatExample of(float floatValue) {
        return builder().floatValue(floatValue).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private float floatValue;

        private boolean _floatValueInitialized = false;

        private Builder() {}

        public Builder from(FloatExample other) {
            checkNotBuilt();
            floatValue(other.getFloatValue());
            return this;
        }

        @JsonSetter("floatValue")
        public Builder floatValue(float floatValue) {
            checkNotBuilt();
            this.floatValue = floatValue;
            this._floatValueInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _floatValueInitialized, "floatValue");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(1);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public FloatExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new FloatExample(floatValue);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
