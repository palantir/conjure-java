package errors.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.Bytes;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = PrimitiveExample.Builder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class PrimitiveExample {
    private final String stringVal;

    private final int intVal;

    private final SafeLong longVal;

    private final double doubleVal;

    private final boolean boolVal;

    private final ResourceIdentifier ridVal;

    private final UUID uuidVal;

    private final OffsetDateTime datetimeVal;

    private final Bytes binaryVal;

    private int memoizedHashCode;

    private PrimitiveExample(
            String stringVal,
            int intVal,
            SafeLong longVal,
            double doubleVal,
            boolean boolVal,
            ResourceIdentifier ridVal,
            UUID uuidVal,
            OffsetDateTime datetimeVal,
            Bytes binaryVal) {
        validateFields(stringVal, longVal, ridVal, uuidVal, datetimeVal, binaryVal);
        this.stringVal = stringVal;
        this.intVal = intVal;
        this.longVal = longVal;
        this.doubleVal = doubleVal;
        this.boolVal = boolVal;
        this.ridVal = ridVal;
        this.uuidVal = uuidVal;
        this.datetimeVal = datetimeVal;
        this.binaryVal = binaryVal;
    }

    @JsonProperty("stringVal")
    public String getStringVal() {
        return this.stringVal;
    }

    @JsonProperty("intVal")
    public int getIntVal() {
        return this.intVal;
    }

    @JsonProperty("longVal")
    public SafeLong getLongVal() {
        return this.longVal;
    }

    @JsonProperty("doubleVal")
    public double getDoubleVal() {
        return this.doubleVal;
    }

    @JsonProperty("boolVal")
    public boolean getBoolVal() {
        return this.boolVal;
    }

    @JsonProperty("ridVal")
    public ResourceIdentifier getRidVal() {
        return this.ridVal;
    }

    @JsonProperty("uuidVal")
    public UUID getUuidVal() {
        return this.uuidVal;
    }

    @JsonProperty("datetimeVal")
    public OffsetDateTime getDatetimeVal() {
        return this.datetimeVal;
    }

    @JsonProperty("binaryVal")
    public Bytes getBinaryVal() {
        return this.binaryVal;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof PrimitiveExample && equalTo((PrimitiveExample) other));
    }

    private boolean equalTo(PrimitiveExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.stringVal.equals(other.stringVal)
                && this.intVal == other.intVal
                && this.longVal.equals(other.longVal)
                && Double.doubleToLongBits(this.doubleVal) == Double.doubleToLongBits(other.doubleVal)
                && this.boolVal == other.boolVal
                && this.ridVal.equals(other.ridVal)
                && this.uuidVal.equals(other.uuidVal)
                && this.datetimeVal.isEqual(other.datetimeVal)
                && this.binaryVal.equals(other.binaryVal);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.stringVal.hashCode();
            hash = 31 * hash + this.intVal;
            hash = 31 * hash + this.longVal.hashCode();
            hash = 31 * hash + Double.hashCode(this.doubleVal);
            hash = 31 * hash + Boolean.hashCode(this.boolVal);
            hash = 31 * hash + this.ridVal.hashCode();
            hash = 31 * hash + this.uuidVal.hashCode();
            hash = 31 * hash + this.datetimeVal.toInstant().hashCode();
            hash = 31 * hash + this.binaryVal.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimitiveExample{stringVal: " + stringVal + ", intVal: " + intVal + ", longVal: " + longVal
                + ", doubleVal: " + doubleVal + ", boolVal: " + boolVal + ", ridVal: " + ridVal + ", uuidVal: "
                + uuidVal + ", datetimeVal: " + datetimeVal + ", binaryVal: " + binaryVal + '}';
    }

    private static void validateFields(
            String stringVal,
            SafeLong longVal,
            ResourceIdentifier ridVal,
            UUID uuidVal,
            OffsetDateTime datetimeVal,
            Bytes binaryVal) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, stringVal, "stringVal");
        missingFields = addFieldIfMissing(missingFields, longVal, "longVal");
        missingFields = addFieldIfMissing(missingFields, ridVal, "ridVal");
        missingFields = addFieldIfMissing(missingFields, uuidVal, "uuidVal");
        missingFields = addFieldIfMissing(missingFields, datetimeVal, "datetimeVal");
        missingFields = addFieldIfMissing(missingFields, binaryVal, "binaryVal");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(6);
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

        private String stringVal;

        private int intVal;

        private SafeLong longVal;

        private double doubleVal;

        private boolean boolVal;

        private ResourceIdentifier ridVal;

        private UUID uuidVal;

        private OffsetDateTime datetimeVal;

        private Bytes binaryVal;

        private boolean _intValInitialized = false;

        private boolean _doubleValInitialized = false;

        private boolean _boolValInitialized = false;

        private Builder() {}

        public Builder from(PrimitiveExample other) {
            checkNotBuilt();
            stringVal(other.getStringVal());
            intVal(other.getIntVal());
            longVal(other.getLongVal());
            doubleVal(other.getDoubleVal());
            boolVal(other.getBoolVal());
            ridVal(other.getRidVal());
            uuidVal(other.getUuidVal());
            datetimeVal(other.getDatetimeVal());
            binaryVal(other.getBinaryVal());
            return this;
        }

        @JsonSetter("stringVal")
        public Builder stringVal(@Nonnull String stringVal) {
            checkNotBuilt();
            this.stringVal = Preconditions.checkNotNull(stringVal, "stringVal cannot be null");
            return this;
        }

        @JsonSetter("intVal")
        public Builder intVal(int intVal) {
            checkNotBuilt();
            this.intVal = intVal;
            this._intValInitialized = true;
            return this;
        }

        @JsonSetter("longVal")
        public Builder longVal(@Nonnull SafeLong longVal) {
            checkNotBuilt();
            this.longVal = Preconditions.checkNotNull(longVal, "longVal cannot be null");
            return this;
        }

        @JsonSetter("doubleVal")
        public Builder doubleVal(double doubleVal) {
            checkNotBuilt();
            this.doubleVal = doubleVal;
            this._doubleValInitialized = true;
            return this;
        }

        @JsonSetter("boolVal")
        public Builder boolVal(boolean boolVal) {
            checkNotBuilt();
            this.boolVal = boolVal;
            this._boolValInitialized = true;
            return this;
        }

        @JsonSetter("ridVal")
        public Builder ridVal(@Nonnull ResourceIdentifier ridVal) {
            checkNotBuilt();
            this.ridVal = Preconditions.checkNotNull(ridVal, "ridVal cannot be null");
            return this;
        }

        @JsonSetter("uuidVal")
        public Builder uuidVal(@Nonnull UUID uuidVal) {
            checkNotBuilt();
            this.uuidVal = Preconditions.checkNotNull(uuidVal, "uuidVal cannot be null");
            return this;
        }

        @JsonSetter("datetimeVal")
        public Builder datetimeVal(@Nonnull OffsetDateTime datetimeVal) {
            checkNotBuilt();
            this.datetimeVal = Preconditions.checkNotNull(datetimeVal, "datetimeVal cannot be null");
            return this;
        }

        @JsonSetter("binaryVal")
        public Builder binaryVal(@Nonnull Bytes binaryVal) {
            checkNotBuilt();
            this.binaryVal = Preconditions.checkNotNull(binaryVal, "binaryVal cannot be null");
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _intValInitialized, "intVal");
            missingFields = addFieldIfMissing(missingFields, _doubleValInitialized, "doubleVal");
            missingFields = addFieldIfMissing(missingFields, _boolValInitialized, "boolVal");
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
        public PrimitiveExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new PrimitiveExample(
                    stringVal, intVal, longVal, doubleVal, boolVal, ridVal, uuidVal, datetimeVal, binaryVal);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
