package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.Bytes;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = BinaryExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BinaryExample {
    private final Bytes binary;

    private BinaryExample(Bytes binary) {
        validateFields(binary);
        this.binary = binary;
    }

    @JsonProperty("binary")
    public Bytes getBinary() {
        return this.binary;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof BinaryExample && equalTo((BinaryExample) other));
    }

    private boolean equalTo(BinaryExample other) {
        return this.binary.equals(other.binary);
    }

    @Override
    public int hashCode() {
        return this.binary.hashCode();
    }

    @Override
    public String toString() {
        return "BinaryExample{binary: " + binary + '}';
    }

    public static BinaryExample of(Bytes binary) {
        return builder().binary(binary).build();
    }

    private static void validateFields(Bytes binary) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, binary, "binary");
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

        private Bytes binary;

        private Builder() {}

        public Builder from(BinaryExample other) {
            checkNotBuilt();
            binary(other.getBinary());
            return this;
        }

        @JsonSetter("binary")
        public Builder binary(@Nonnull Bytes binary) {
            checkNotBuilt();
            this.binary = Preconditions.checkNotNull(binary, "binary cannot be null");
            return this;
        }

        public BinaryExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new BinaryExample(binary);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
