package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = SafeExternalLongExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SafeExternalLongExample {
    private final long safeExternalLongValue;

    private final Optional<Long> optionalSafeExternalLong;

    private int memoizedHashCode;

    private SafeExternalLongExample(long safeExternalLongValue, Optional<Long> optionalSafeExternalLong) {
        validateFields(optionalSafeExternalLong);
        this.safeExternalLongValue = safeExternalLongValue;
        this.optionalSafeExternalLong = optionalSafeExternalLong;
    }

    @JsonProperty("safeExternalLongValue")
    @Safe
    public long getSafeExternalLongValue() {
        return this.safeExternalLongValue;
    }

    @JsonProperty("optionalSafeExternalLong")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Long> getOptionalSafeExternalLong() {
        return this.optionalSafeExternalLong;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeExternalLongExample && equalTo((SafeExternalLongExample) other));
    }

    private boolean equalTo(SafeExternalLongExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.safeExternalLongValue == other.safeExternalLongValue
                && this.optionalSafeExternalLong.equals(other.optionalSafeExternalLong);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + Long.hashCode(this.safeExternalLongValue);
            hash = 31 * hash + this.optionalSafeExternalLong.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @Safe
    public String toString() {
        return "SafeExternalLongExample{safeExternalLongValue: " + safeExternalLongValue
                + ", optionalSafeExternalLong: " + optionalSafeExternalLong + '}';
    }

    public static SafeExternalLongExample of(@Safe long safeExternalLongValue, long optionalSafeExternalLong) {
        return builder()
                .safeExternalLongValue(safeExternalLongValue)
                .optionalSafeExternalLong(Optional.of(optionalSafeExternalLong))
                .build();
    }

    private static void validateFields(Optional<Long> optionalSafeExternalLong) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalSafeExternalLong, "optionalSafeExternalLong");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(1);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private long safeExternalLongValue;

        private Optional<Long> optionalSafeExternalLong = Optional.empty();

        private boolean _safeExternalLongValueInitialized = false;

        private Builder() {}

        public Builder from(SafeExternalLongExample other) {
            checkNotBuilt();
            safeExternalLongValue(other.getSafeExternalLongValue());
            optionalSafeExternalLong(other.getOptionalSafeExternalLong());
            return this;
        }

        @JsonSetter("safeExternalLongValue")
        public Builder safeExternalLongValue(@Safe long safeExternalLongValue) {
            checkNotBuilt();
            this.safeExternalLongValue = safeExternalLongValue;
            this._safeExternalLongValueInitialized = true;
            return this;
        }

        @JsonSetter(value = "optionalSafeExternalLong", nulls = Nulls.SKIP)
        public Builder optionalSafeExternalLong(@Nonnull Optional<? extends Long> optionalSafeExternalLong) {
            checkNotBuilt();
            this.optionalSafeExternalLong = Preconditions.checkNotNull(
                            optionalSafeExternalLong, "optionalSafeExternalLong cannot be null")
                    .map(Function.identity());
            return this;
        }

        public Builder optionalSafeExternalLong(long optionalSafeExternalLong) {
            checkNotBuilt();
            this.optionalSafeExternalLong = Optional.of(
                    Preconditions.checkNotNull(optionalSafeExternalLong, "optionalSafeExternalLong cannot be null"));
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
            return new SafeExternalLongExample(safeExternalLongValue, optionalSafeExternalLong);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
