package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = SafeExternalLongExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SafeExternalLongExample {
    private final long safeExternalLongValue;

    private SafeExternalLongExample(long safeExternalLongValue) {
        this.safeExternalLongValue = safeExternalLongValue;
    }

    @JsonProperty("safeExternalLongValue")
    @Safe
    public long getSafeExternalLongValue() {
        return this.safeExternalLongValue;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeExternalLongExample && equalTo((SafeExternalLongExample) other));
    }

    private boolean equalTo(SafeExternalLongExample other) {
        return this.safeExternalLongValue == other.safeExternalLongValue;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.safeExternalLongValue);
    }

    @Override
    public String toString() {
        return "SafeExternalLongExample{safeExternalLongValue: " + safeExternalLongValue + '}';
    }

    public static SafeExternalLongExample of(@Safe long safeExternalLongValue) {
        return builder().safeExternalLongValue(safeExternalLongValue).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private long safeExternalLongValue;

        private boolean _safeExternalLongValueInitialized = false;

        private Builder() {}

        public Builder from(SafeExternalLongExample other) {
            checkNotBuilt();
            safeExternalLongValue(other.getSafeExternalLongValue());
            return this;
        }

        @JsonSetter("safeExternalLongValue")
        public Builder safeExternalLongValue(@Safe long safeExternalLongValue) {
            checkNotBuilt();
            this.safeExternalLongValue = safeExternalLongValue;
            this._safeExternalLongValueInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields =
                    addFieldIfMissing(missingFields, _safeExternalLongValueInitialized, "safeExternalLongValue");
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

        public SafeExternalLongExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new SafeExternalLongExample(safeExternalLongValue);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
