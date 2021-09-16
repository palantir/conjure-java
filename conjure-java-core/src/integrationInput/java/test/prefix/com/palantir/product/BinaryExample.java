package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

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
        return Objects.hashCode(this.binary);
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
            throw new ServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("missingFields", missingFields));
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
        private ByteBuffer binary;

        private Builder() {}

        public Builder from(BinaryExample other) {
            binary(other.getBinary());
            return this;
        }

        @JsonSetter("binary")
        public Builder binary(@Nonnull ByteBuffer binary) {
            Preconditions.checkNotNull(binary, "binary cannot be null");
            this.binary = ByteBuffer.allocate(binary.remaining()).put(binary.duplicate());
            ((Buffer) this.binary).rewind();
            return this;
        }

        public BinaryExample build() {
            return new BinaryExample(binary);
        }
    }
}
