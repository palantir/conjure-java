package dialogue.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.Unsafe;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Unsafe
@JsonDeserialize(builder = SafetyExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SafetyExample {
    private final String safeString;

    private final double unsafeDouble;

    private int memoizedHashCode;

    private SafetyExample(String safeString, double unsafeDouble) {
        validateFields(safeString);
        this.safeString = safeString;
        this.unsafeDouble = unsafeDouble;
    }

    @JsonProperty("safeString")
    @Safe
    public String getSafeString() {
        return this.safeString;
    }

    @JsonProperty("unsafeDouble")
    @Unsafe
    public double getUnsafeDouble() {
        return this.unsafeDouble;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafetyExample && equalTo((SafetyExample) other));
    }

    private boolean equalTo(SafetyExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.safeString.equals(other.safeString)
                && Double.doubleToLongBits(this.unsafeDouble) == Double.doubleToLongBits(other.unsafeDouble);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.safeString.hashCode();
            hash = 31 * hash + Double.hashCode(this.unsafeDouble);
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @Unsafe
    public String toString() {
        return "SafetyExample{safeString: " + safeString + ", unsafeDouble: " + unsafeDouble + '}';
    }

    public static SafetyExample of(@Safe String safeString, @Unsafe double unsafeDouble) {
        return builder().safeString(safeString).unsafeDouble(unsafeDouble).build();
    }

    private static void validateFields(String safeString) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, safeString, "safeString");
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private @Safe String safeString;

        private @Unsafe double unsafeDouble;

        private boolean _unsafeDoubleInitialized = false;

        private Builder() {}

        public Builder from(SafetyExample other) {
            checkNotBuilt();
            safeString(other.getSafeString());
            unsafeDouble(other.getUnsafeDouble());
            return this;
        }

        @JsonSetter("safeString")
        public Builder safeString(@Nonnull @Safe String safeString) {
            checkNotBuilt();
            this.safeString = Preconditions.checkNotNull(safeString, "safeString cannot be null");
            return this;
        }

        @JsonSetter("unsafeDouble")
        public Builder unsafeDouble(@Unsafe double unsafeDouble) {
            checkNotBuilt();
            this.unsafeDouble = unsafeDouble;
            this._unsafeDoubleInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _unsafeDoubleInitialized, "unsafeDouble");
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
        public SafetyExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new SafetyExample(safeString, unsafeDouble);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
