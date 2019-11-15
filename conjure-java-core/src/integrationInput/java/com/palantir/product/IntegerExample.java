package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@JsonDeserialize(builder = IntegerExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class IntegerExample {
    private final int integer;

    private IntegerExample(int integer) {
        this.integer = integer;
    }

    @JsonProperty("integer")
    public int getInteger() {
        return this.integer;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof IntegerExample && equalTo((IntegerExample) other));
    }

    private boolean equalTo(IntegerExample other) {
        return this.integer == other.integer;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.integer);
    }

    @Override
    public String toString() {
        return "IntegerExample{integer: " + integer + '}';
    }

    public static IntegerExample of(int integer) {
        return builder().integer(integer).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        private int integer;

        private boolean _integerInitialized = false;

        private Builder() {}

        public Builder from(IntegerExample other) {
            integer(other.getInteger());
            return this;
        }

        @JsonSetter("integer")
        public Builder integer(int integer) {
            this.integer = integer;
            this._integerInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _integerInitialized, "integer");
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

        public IntegerExample build() {
            validatePrimitiveFieldsHaveBeenInitialized();
            return new IntegerExample(integer);
        }
    }
}
