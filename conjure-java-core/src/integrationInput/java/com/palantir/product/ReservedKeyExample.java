package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = ReservedKeyExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ReservedKeyExample {
    private final String package_;

    private final String interface_;

    private final String fieldNameWithDashes;

    private final int primitveFieldNameWithDashes;

    private final int memoizedHashCode_;

    private volatile int memoizedHashCode;

    private ReservedKeyExample(
            String package_,
            String interface_,
            String fieldNameWithDashes,
            int primitveFieldNameWithDashes,
            int memoizedHashCode_) {
        validateFields(package_, interface_, fieldNameWithDashes);
        this.package_ = package_;
        this.interface_ = interface_;
        this.fieldNameWithDashes = fieldNameWithDashes;
        this.primitveFieldNameWithDashes = primitveFieldNameWithDashes;
        this.memoizedHashCode_ = memoizedHashCode_;
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

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ReservedKeyExample && equalTo((ReservedKeyExample) other));
    }

    private boolean equalTo(ReservedKeyExample other) {
        return this.package_.equals(other.package_)
                && this.interface_.equals(other.interface_)
                && this.fieldNameWithDashes.equals(other.fieldNameWithDashes)
                && this.primitveFieldNameWithDashes == other.primitveFieldNameWithDashes
                && this.memoizedHashCode_ == other.memoizedHashCode_;
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode =
                    Objects.hash(
                            package_,
                            interface_,
                            fieldNameWithDashes,
                            primitveFieldNameWithDashes,
                            memoizedHashCode_);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("ReservedKeyExample")
                .append('{')
                .append("package")
                .append(": ")
                .append(package_)
                .append(", ")
                .append("interface")
                .append(": ")
                .append(interface_)
                .append(", ")
                .append("field-name-with-dashes")
                .append(": ")
                .append(fieldNameWithDashes)
                .append(", ")
                .append("primitve-field-name-with-dashes")
                .append(": ")
                .append(primitveFieldNameWithDashes)
                .append(", ")
                .append("memoizedHashCode")
                .append(": ")
                .append(memoizedHashCode_)
                .append('}')
                .toString();
    }

    private static void validateFields(
            String package_, String interface_, String fieldNameWithDashes) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, package_, "package");
        missingFields = addFieldIfMissing(missingFields, interface_, "interface");
        missingFields =
                addFieldIfMissing(missingFields, fieldNameWithDashes, "field-name-with-dashes");
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
        private String package_;

        private String interface_;

        private String fieldNameWithDashes;

        private int primitveFieldNameWithDashes;

        private int memoizedHashCode_;

        private boolean _primitveFieldNameWithDashesInitialized = false;

        private boolean _memoizedHashCode_Initialized = false;

        private Builder() {}

        public Builder from(ReservedKeyExample other) {
            package_(other.getPackage());
            interface_(other.getInterface());
            fieldNameWithDashes(other.getFieldNameWithDashes());
            primitveFieldNameWithDashes(other.getPrimitveFieldNameWithDashes());
            memoizedHashCode_(other.getMemoizedHashCode());
            return this;
        }

        @JsonSetter("package")
        public Builder package_(String package_) {
            this.package_ = Objects.requireNonNull(package_, "package cannot be null");
            return this;
        }

        @JsonSetter("interface")
        public Builder interface_(String interface_) {
            this.interface_ = Objects.requireNonNull(interface_, "interface cannot be null");
            return this;
        }

        @JsonSetter("field-name-with-dashes")
        public Builder fieldNameWithDashes(String fieldNameWithDashes) {
            this.fieldNameWithDashes =
                    Objects.requireNonNull(
                            fieldNameWithDashes, "field-name-with-dashes cannot be null");
            return this;
        }

        @JsonSetter("primitve-field-name-with-dashes")
        public Builder primitveFieldNameWithDashes(int primitveFieldNameWithDashes) {
            this.primitveFieldNameWithDashes = primitveFieldNameWithDashes;
            this._primitveFieldNameWithDashesInitialized = true;
            return this;
        }

        @JsonSetter("memoizedHashCode")
        public Builder memoizedHashCode_(int memoizedHashCode_) {
            this.memoizedHashCode_ = memoizedHashCode_;
            this._memoizedHashCode_Initialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields =
                    addFieldIfMissing(
                            missingFields,
                            _primitveFieldNameWithDashesInitialized,
                            "primitve-field-name-with-dashes");
            missingFields =
                    addFieldIfMissing(
                            missingFields, _memoizedHashCode_Initialized, "memoizedHashCode");
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
                    missingFields = new ArrayList<>(2);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public ReservedKeyExample build() {
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ReservedKeyExample(
                    package_,
                    interface_,
                    fieldNameWithDashes,
                    primitveFieldNameWithDashes,
                    memoizedHashCode_);
        }
    }
}
