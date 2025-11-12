package exceptionthrowingdialogueinterfaces.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = ExternalExample.Builder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class ExternalExample {
    private final long externalLong;

    private final Optional<Long> optionalExternal;

    private int memoizedHashCode;

    private ExternalExample(long externalLong, Optional<Long> optionalExternal) {
        validateFields(optionalExternal);
        this.externalLong = externalLong;
        this.optionalExternal = optionalExternal;
    }

    @JsonProperty("externalLong")
    public long getExternalLong() {
        return this.externalLong;
    }

    @JsonProperty("optionalExternal")
    public Optional<Long> getOptionalExternal() {
        return this.optionalExternal;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ExternalExample && equalTo((ExternalExample) other));
    }

    private boolean equalTo(ExternalExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.externalLong == other.externalLong && this.optionalExternal.equals(other.optionalExternal);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + Long.hashCode(this.externalLong);
            hash = 31 * hash + this.optionalExternal.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ExternalExample{externalLong: " + externalLong + ", optionalExternal: " + optionalExternal + '}';
    }

    public static ExternalExample of(long externalLong, long optionalExternal) {
        return builder()
                .externalLong(externalLong)
                .optionalExternal(Optional.of(optionalExternal))
                .build();
    }

    private static void validateFields(Optional<Long> optionalExternal) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalExternal, "optionalExternal");
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

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private long externalLong;

        private Optional<Long> optionalExternal = Optional.empty();

        private boolean _externalLongInitialized = false;

        private Builder() {}

        public Builder from(ExternalExample other) {
            checkNotBuilt();
            externalLong(other.getExternalLong());
            optionalExternal(other.getOptionalExternal());
            return this;
        }

        @JsonSetter("externalLong")
        public Builder externalLong(long externalLong) {
            checkNotBuilt();
            this.externalLong = externalLong;
            this._externalLongInitialized = true;
            return this;
        }

        @JsonSetter(value = "optionalExternal", nulls = Nulls.SKIP)
        public Builder optionalExternal(@Nonnull Optional<? extends Long> optionalExternal) {
            checkNotBuilt();
            this.optionalExternal = Preconditions.checkNotNull(optionalExternal, "optionalExternal cannot be null")
                    .map(Function.identity());
            return this;
        }

        public Builder optionalExternal(long optionalExternal) {
            checkNotBuilt();
            this.optionalExternal =
                    Optional.of(Preconditions.checkNotNull(optionalExternal, "optionalExternal cannot be null"));
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _externalLongInitialized, "externalLong");
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

        @CheckReturnValue
        public ExternalExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ExternalExample(externalLong, optionalExternal);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
