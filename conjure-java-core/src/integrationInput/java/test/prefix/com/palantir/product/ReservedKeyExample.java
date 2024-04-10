package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = ReservedKeyExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ReservedKeyExample {
    private final String package_;

    private final String interface_;

    private final String fieldNameWithDashes;

    private final int primitveFieldNameWithDashes;

    private final int memoizedHashCode_;

    private final int result;

    private int memoizedHashCode;

    private ReservedKeyExample(
            String package_,
            String interface_,
            String fieldNameWithDashes,
            int primitveFieldNameWithDashes,
            int memoizedHashCode_,
            int result) {
        validateFields(package_, interface_, fieldNameWithDashes);
        this.package_ = package_;
        this.interface_ = interface_;
        this.fieldNameWithDashes = fieldNameWithDashes;
        this.primitveFieldNameWithDashes = primitveFieldNameWithDashes;
        this.memoizedHashCode_ = memoizedHashCode_;
        this.result = result;
    }

    @JsonProperty("package")
    public String getPackage() {
        return this.package_;
    }

    @JsonProperty("interface")
    public String getInterface() {
        return this.interface_;
    }

    @JsonProperty("field-name-with-dashes")
    public String getFieldNameWithDashes() {
        return this.fieldNameWithDashes;
    }

    @JsonProperty("primitve-field-name-with-dashes")
    public int getPrimitveFieldNameWithDashes() {
        return this.primitveFieldNameWithDashes;
    }

    @JsonProperty("memoizedHashCode")
    public int getMemoizedHashCode() {
        return this.memoizedHashCode_;
    }

    @JsonProperty("result")
    public int getResult() {
        return this.result;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ReservedKeyExample && equalTo((ReservedKeyExample) other));
    }

    private boolean equalTo(ReservedKeyExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.package_.equals(other.package_)
                && this.interface_.equals(other.interface_)
                && this.fieldNameWithDashes.equals(other.fieldNameWithDashes)
                && this.primitveFieldNameWithDashes == other.primitveFieldNameWithDashes
                && this.memoizedHashCode_ == other.memoizedHashCode_
                && this.result == other.result;
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.package_.hashCode();
            hash = 31 * hash + this.interface_.hashCode();
            hash = 31 * hash + this.fieldNameWithDashes.hashCode();
            hash = 31 * hash + this.primitveFieldNameWithDashes;
            hash = 31 * hash + this.memoizedHashCode_;
            hash = 31 * hash + this.result;
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ReservedKeyExample{package: " + package_ + ", interface: " + interface_ + ", field-name-with-dashes: "
                + fieldNameWithDashes + ", primitve-field-name-with-dashes: " + primitveFieldNameWithDashes
                + ", memoizedHashCode: " + memoizedHashCode_ + ", result: " + result + '}';
    }

    private static void validateFields(String package_, String interface_, String fieldNameWithDashes) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, package_, "package");
        missingFields = addFieldIfMissing(missingFields, interface_, "interface");
        missingFields = addFieldIfMissing(missingFields, fieldNameWithDashes, "field-name-with-dashes");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(3);
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

        private String package_;

        private String interface_;

        private String fieldNameWithDashes;

        private int primitveFieldNameWithDashes;

        private int memoizedHashCode_;

        private int result;

        private boolean _primitveFieldNameWithDashesInitialized = false;

        private boolean _memoizedHashCode_Initialized = false;

        private boolean _resultInitialized = false;

        private Builder() {}

        public Builder from(ReservedKeyExample other) {
            checkNotBuilt();
            package_(other.getPackage());
            interface_(other.getInterface());
            fieldNameWithDashes(other.getFieldNameWithDashes());
            primitveFieldNameWithDashes(other.getPrimitveFieldNameWithDashes());
            memoizedHashCode_(other.getMemoizedHashCode());
            result(other.getResult());
            return this;
        }

        @JsonSetter("package")
        public Builder package_(@Nonnull String package_) {
            checkNotBuilt();
            this.package_ = Preconditions.checkNotNull(package_, "package cannot be null");
            return this;
        }

        @JsonSetter("interface")
        public Builder interface_(@Nonnull String interface_) {
            checkNotBuilt();
            this.interface_ = Preconditions.checkNotNull(interface_, "interface cannot be null");
            return this;
        }

        @JsonSetter("field-name-with-dashes")
        public Builder fieldNameWithDashes(@Nonnull String fieldNameWithDashes) {
            checkNotBuilt();
            this.fieldNameWithDashes =
                    Preconditions.checkNotNull(fieldNameWithDashes, "field-name-with-dashes cannot be null");
            return this;
        }

        @JsonSetter("primitve-field-name-with-dashes")
        public Builder primitveFieldNameWithDashes(int primitveFieldNameWithDashes) {
            checkNotBuilt();
            this.primitveFieldNameWithDashes = primitveFieldNameWithDashes;
            this._primitveFieldNameWithDashesInitialized = true;
            return this;
        }

        @JsonSetter("memoizedHashCode")
        public Builder memoizedHashCode_(int memoizedHashCode_) {
            checkNotBuilt();
            this.memoizedHashCode_ = memoizedHashCode_;
            this._memoizedHashCode_Initialized = true;
            return this;
        }

        @JsonSetter("result")
        public Builder result(int result) {
            checkNotBuilt();
            this.result = result;
            this._resultInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(
                    missingFields, _primitveFieldNameWithDashesInitialized, "primitve-field-name-with-dashes");
            missingFields = addFieldIfMissing(missingFields, _memoizedHashCode_Initialized, "memoizedHashCode");
            missingFields = addFieldIfMissing(missingFields, _resultInitialized, "result");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(3);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        @CheckReturnValue
        public ReservedKeyExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ReservedKeyExample(
                    package_, interface_, fieldNameWithDashes, primitveFieldNameWithDashes, memoizedHashCode_, result);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
