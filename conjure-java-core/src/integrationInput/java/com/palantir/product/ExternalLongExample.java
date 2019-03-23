package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = ExternalLongExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ExternalLongExample {
    private final long externalLong;

    private final Optional<Long> optionalExternalLong;

    private volatile int memoizedHashCode;

    private ExternalLongExample(long externalLong, Optional<Long> optionalExternalLong) {
        validateFields(optionalExternalLong);
        this.externalLong = externalLong;
        this.optionalExternalLong = optionalExternalLong;
    }

    @JsonProperty("externalLong")
    public long getExternalLong() {
        return this.externalLong;
    }

    @JsonProperty("optionalExternalLong")
    public Optional<Long> getOptionalExternalLong() {
        return this.optionalExternalLong;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ExternalLongExample && equalTo((ExternalLongExample) other));
    }

    private boolean equalTo(ExternalLongExample other) {
        return this.externalLong == other.externalLong
                && this.optionalExternalLong.equals(other.optionalExternalLong);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(externalLong, optionalExternalLong);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("ExternalLongExample")
                .append('{')
                .append("externalLong")
                .append(": ")
                .append(externalLong)
                .append(", ")
                .append("optionalExternalLong")
                .append(": ")
                .append(optionalExternalLong)
                .append('}')
                .toString();
    }

    public static ExternalLongExample of(long externalLong, Long optionalExternalLong) {
        return builder()
                .externalLong(externalLong)
                .optionalExternalLong(Optional.of(optionalExternalLong))
                .build();
    }

    private static void validateFields(Optional<Long> optionalExternalLong) {
        List<String> missingFields = null;
        missingFields =
                addFieldIfMissing(missingFields, optionalExternalLong, "optionalExternalLong");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set",
                    SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        private long externalLong;

        private Optional<Long> optionalExternalLong = Optional.empty();

        private boolean _externalLongInitialized = false;

        private Builder() {}

        public Builder from(ExternalLongExample other) {
            externalLong(other.getExternalLong());
            optionalExternalLong(other.getOptionalExternalLong());
            return this;
        }

        @JsonSetter("externalLong")
        public Builder externalLong(long externalLong) {
            this.externalLong = externalLong;
            this._externalLongInitialized = true;
            return this;
        }

        @JsonSetter("optionalExternalLong")
        public Builder optionalExternalLong(Optional<? extends Long> optionalExternalLong) {
            this.optionalExternalLong =
                    (Optional<Long>)
                            Preconditions.checkNotNull(
                                    optionalExternalLong, "optionalExternalLong cannot be null");
            return this;
        }

        public Builder optionalExternalLong(long optionalExternalLong) {
            this.optionalExternalLong =
                    Optional.of(
                            Preconditions.checkNotNull(
                                    optionalExternalLong, "optionalExternalLong cannot be null"));
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields =
                    addFieldIfMissing(missingFields, _externalLongInitialized, "externalLong");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set",
                        SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(
                List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(1);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public ExternalLongExample build() {
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ExternalLongExample(externalLong, optionalExternalLong);
        }
    }
}
