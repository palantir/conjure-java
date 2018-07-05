package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = BinaryExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BinaryExample {
    private final ByteBuffer value;

    private volatile int memoizedHashCode;

    private BinaryExample(ByteBuffer value) {
        validateFields(value);
        this.value = value;
    }

    @JsonProperty("value")
    public ByteBuffer getValue() {
        return this.value.asReadOnlyBuffer();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof BinaryExample && equalTo((BinaryExample) other));
    }

    private boolean equalTo(BinaryExample other) {
        return this.value.equals(other.value);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(value);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("BinaryExample")
                .append("{")
                .append("value")
                .append(": ")
                .append(value)
                .append("}")
                .toString();
    }

    public static BinaryExample of(ByteBuffer value) {
        return builder().value(value).build();
    }

    private static void validateFields(ByteBuffer value) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, value, "value");
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
        private ByteBuffer value;

        private Builder() {}

        public Builder from(BinaryExample other) {
            value(other.getValue());
            return this;
        }

        @JsonSetter("value")
        public Builder value(ByteBuffer value) {
            Objects.requireNonNull(value, "value cannot be null");
            this.value = ByteBuffer.allocate(value.remaining()).put(value.duplicate());
            this.value.rewind();
            return this;
        }

        public BinaryExample build() {
            return new BinaryExample(value);
        }
    }
}
