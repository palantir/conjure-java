package com.palantir.binary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = BinaryExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BinaryExample {
    private final ByteBuffer binary;

    private BinaryExample(ByteBuffer binary) {
        validateFields(binary);
        this.binary = binary;
    }

    @JsonProperty("binary")
    public ByteBuffer getBinary() {
        return this.binary.asReadOnlyBuffer();
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

    public static BinaryExample of(ByteBuffer binary) {
        return builder().binary(binary).build();
    }

    private static void validateFields(ByteBuffer binary) {
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private ByteBuffer binary;

        private Builder() {}

        public Builder from(BinaryExample other) {
            checkNotBuilt();
            binary(other.getBinary());
            return this;
        }

        @JsonSetter("binary")
        public Builder binary(@Nonnull ByteBuffer binary) {
            checkNotBuilt();
            Preconditions.checkNotNull(binary, "binary cannot be null");
            this.binary = ByteBuffer.allocate(binary.remaining()).put(binary.duplicate());
            ((Buffer) this.binary).rewind();
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
