package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Generated;

@JsonDeserialize(builder = PrimitivesObject.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class PrimitivesObject {
    private final BearerToken bearertokenField;

    private final ByteBuffer binaryField;

    private final ZonedDateTime datetimeField;

    private final double doubleField;

    private final int integerField;

    private final ResourceIdentifier ridField;

    private final SafeLong safelongField;

    private final String stringField;

    private final UUID uuidField;

    private volatile int memoizedHashCode;

    private PrimitivesObject(
            BearerToken bearertokenField,
            ByteBuffer binaryField,
            ZonedDateTime datetimeField,
            double doubleField,
            int integerField,
            ResourceIdentifier ridField,
            SafeLong safelongField,
            String stringField,
            UUID uuidField) {
        validateFields(
                bearertokenField,
                binaryField,
                datetimeField,
                ridField,
                safelongField,
                stringField,
                uuidField);
        this.bearertokenField = bearertokenField;
        this.binaryField = binaryField;
        this.datetimeField = datetimeField;
        this.doubleField = doubleField;
        this.integerField = integerField;
        this.ridField = ridField;
        this.safelongField = safelongField;
        this.stringField = stringField;
        this.uuidField = uuidField;
    }

    @JsonProperty("bearertokenField")
    public BearerToken getBearertokenField() {
        return this.bearertokenField;
    }

    @JsonProperty("binaryField")
    public ByteBuffer getBinaryField() {
        return this.binaryField.asReadOnlyBuffer();
    }

    @JsonProperty("datetimeField")
    public ZonedDateTime getDatetimeField() {
        return this.datetimeField;
    }

    @JsonProperty("doubleField")
    public double getDoubleField() {
        return this.doubleField;
    }

    @JsonProperty("integerField")
    public int getIntegerField() {
        return this.integerField;
    }

    @JsonProperty("ridField")
    public ResourceIdentifier getRidField() {
        return this.ridField;
    }

    @JsonProperty("safelongField")
    public SafeLong getSafelongField() {
        return this.safelongField;
    }

    @JsonProperty("stringField")
    public String getStringField() {
        return this.stringField;
    }

    @JsonProperty("uuidField")
    public UUID getUuidField() {
        return this.uuidField;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof PrimitivesObject && equalTo((PrimitivesObject) other));
    }

    private boolean equalTo(PrimitivesObject other) {
        return this.bearertokenField.equals(other.bearertokenField)
                && this.binaryField.equals(other.binaryField)
                && this.datetimeField.isEqual(other.datetimeField)
                && this.doubleField == other.doubleField
                && this.integerField == other.integerField
                && this.ridField.equals(other.ridField)
                && this.safelongField.equals(other.safelongField)
                && this.stringField.equals(other.stringField)
                && this.uuidField.equals(other.uuidField);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode =
                    Objects.hash(
                            bearertokenField,
                            binaryField,
                            datetimeField.toInstant(),
                            doubleField,
                            integerField,
                            ridField,
                            safelongField,
                            stringField,
                            uuidField);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("PrimitivesObject")
                .append("{")
                .append("bearertokenField")
                .append(": ")
                .append(bearertokenField)
                .append(", ")
                .append("binaryField")
                .append(": ")
                .append(binaryField)
                .append(", ")
                .append("datetimeField")
                .append(": ")
                .append(datetimeField)
                .append(", ")
                .append("doubleField")
                .append(": ")
                .append(doubleField)
                .append(", ")
                .append("integerField")
                .append(": ")
                .append(integerField)
                .append(", ")
                .append("ridField")
                .append(": ")
                .append(ridField)
                .append(", ")
                .append("safelongField")
                .append(": ")
                .append(safelongField)
                .append(", ")
                .append("stringField")
                .append(": ")
                .append(stringField)
                .append(", ")
                .append("uuidField")
                .append(": ")
                .append(uuidField)
                .append("}")
                .toString();
    }

    private static void validateFields(
            BearerToken bearertokenField,
            ByteBuffer binaryField,
            ZonedDateTime datetimeField,
            ResourceIdentifier ridField,
            SafeLong safelongField,
            String stringField,
            UUID uuidField) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, bearertokenField, "bearertokenField");
        missingFields = addFieldIfMissing(missingFields, binaryField, "binaryField");
        missingFields = addFieldIfMissing(missingFields, datetimeField, "datetimeField");
        missingFields = addFieldIfMissing(missingFields, ridField, "ridField");
        missingFields = addFieldIfMissing(missingFields, safelongField, "safelongField");
        missingFields = addFieldIfMissing(missingFields, stringField, "stringField");
        missingFields = addFieldIfMissing(missingFields, uuidField, "uuidField");
        if (missingFields != null) {
            throw new IllegalArgumentException(
                    "Some required fields have not been set: " + missingFields);
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(7);
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
        private BearerToken bearertokenField;

        private ByteBuffer binaryField;

        private ZonedDateTime datetimeField;

        private double doubleField;

        private int integerField;

        private ResourceIdentifier ridField;

        private SafeLong safelongField;

        private String stringField;

        private UUID uuidField;

        private Builder() {}

        public Builder from(PrimitivesObject other) {
            bearertokenField(other.getBearertokenField());
            binaryField(other.getBinaryField());
            datetimeField(other.getDatetimeField());
            doubleField(other.getDoubleField());
            integerField(other.getIntegerField());
            ridField(other.getRidField());
            safelongField(other.getSafelongField());
            stringField(other.getStringField());
            uuidField(other.getUuidField());
            return this;
        }

        @JsonSetter("bearertokenField")
        public Builder bearertokenField(BearerToken bearertokenField) {
            this.bearertokenField =
                    Objects.requireNonNull(bearertokenField, "bearertokenField cannot be null");
            return this;
        }

        @JsonSetter("binaryField")
        public Builder binaryField(ByteBuffer binaryField) {
            Objects.requireNonNull(binaryField, "binaryField cannot be null");
            this.binaryField =
                    ByteBuffer.allocate(binaryField.remaining()).put(binaryField.duplicate());
            this.binaryField.rewind();
            return this;
        }

        @JsonSetter("datetimeField")
        public Builder datetimeField(ZonedDateTime datetimeField) {
            this.datetimeField =
                    Objects.requireNonNull(datetimeField, "datetimeField cannot be null");
            return this;
        }

        @JsonSetter("doubleField")
        public Builder doubleField(double doubleField) {
            this.doubleField = doubleField;
            return this;
        }

        @JsonSetter("integerField")
        public Builder integerField(int integerField) {
            this.integerField = integerField;
            return this;
        }

        @JsonSetter("ridField")
        public Builder ridField(ResourceIdentifier ridField) {
            this.ridField = Objects.requireNonNull(ridField, "ridField cannot be null");
            return this;
        }

        @JsonSetter("safelongField")
        public Builder safelongField(SafeLong safelongField) {
            this.safelongField =
                    Objects.requireNonNull(safelongField, "safelongField cannot be null");
            return this;
        }

        @JsonSetter("stringField")
        public Builder stringField(String stringField) {
            this.stringField = Objects.requireNonNull(stringField, "stringField cannot be null");
            return this;
        }

        @JsonSetter("uuidField")
        public Builder uuidField(UUID uuidField) {
            this.uuidField = Objects.requireNonNull(uuidField, "uuidField cannot be null");
            return this;
        }

        public PrimitivesObject build() {
            return new PrimitivesObject(
                    bearertokenField,
                    binaryField,
                    datetimeField,
                    doubleField,
                    integerField,
                    ridField,
                    safelongField,
                    stringField,
                    uuidField);
        }
    }
}
