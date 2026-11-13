package redactdonotlogobjects.com.palantir.product;

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
@JsonDeserialize(builder = ObjectNoDoNotLog.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ObjectNoDoNotLog {
    private final String safeString;

    private final int unsafeInt;

    private int memoizedHashCode;

    private ObjectNoDoNotLog(String safeString, int unsafeInt) {
        validateFields(safeString);
        this.safeString = safeString;
        this.unsafeInt = unsafeInt;
    }

    @JsonProperty("safeString")
    @Safe
    public String getSafeString() {
        return this.safeString;
    }

    @JsonProperty("unsafeInt")
    @Unsafe
    public int getUnsafeInt() {
        return this.unsafeInt;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ObjectNoDoNotLog && equalTo((ObjectNoDoNotLog) other));
    }

    private boolean equalTo(ObjectNoDoNotLog other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.safeString.equals(other.safeString) && this.unsafeInt == other.unsafeInt;
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.safeString.hashCode();
            hash = 31 * hash + this.unsafeInt;
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @Unsafe
    public String toString() {
        return "ObjectNoDoNotLog{safeString: " + safeString + ", unsafeInt: " + unsafeInt + '}';
    }

    @Unsafe
    public String dangerousToString() {
        return "ObjectNoDoNotLog{safeString: " + safeString + ", unsafeInt: " + unsafeInt + '}';
    }

    public static ObjectNoDoNotLog of(@Safe String safeString, @Unsafe int unsafeInt) {
        return builder().safeString(safeString).unsafeInt(unsafeInt).build();
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

        private @Unsafe int unsafeInt;

        private boolean _unsafeIntInitialized = false;

        private Builder() {}

        public Builder from(ObjectNoDoNotLog other) {
            checkNotBuilt();
            safeString(other.getSafeString());
            unsafeInt(other.getUnsafeInt());
            return this;
        }

        @JsonSetter("safeString")
        public Builder safeString(@Nonnull @Safe String safeString) {
            checkNotBuilt();
            this.safeString = Preconditions.checkNotNull(safeString, "safeString cannot be null");
            return this;
        }

        @JsonSetter("unsafeInt")
        public Builder unsafeInt(@Unsafe int unsafeInt) {
            checkNotBuilt();
            this.unsafeInt = unsafeInt;
            this._unsafeIntInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _unsafeIntInitialized, "unsafeInt");
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
        public ObjectNoDoNotLog build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ObjectNoDoNotLog(safeString, unsafeInt);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
