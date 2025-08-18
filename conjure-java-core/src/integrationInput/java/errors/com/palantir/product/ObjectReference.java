package errors.com.palantir.product;

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

@JsonDeserialize(builder = ObjectReference.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ObjectReference {
    private final String name;

    private final int value;

    private int memoizedHashCode;

    private ObjectReference(String name, int value) {
        validateFields(name);
        this.name = name;
        this.value = value;
    }

    @JsonProperty("name")
    public String getName() {
        return this.name;
    }

    @JsonProperty("value")
    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ObjectReference && equalTo((ObjectReference) other));
    }

    private boolean equalTo(ObjectReference other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.name.equals(other.name) && this.value == other.value;
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.name.hashCode();
            hash = 31 * hash + this.value;
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ObjectReference{name: " + name + ", value: " + value + '}';
    }

    public static ObjectReference of(String name, int value) {
        return builder().name(name).value(value).build();
    }

    private static void validateFields(String name) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, name, "name");
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

        private String name;

        private int value;

        private boolean _valueInitialized = false;

        private Builder() {}

        public Builder from(ObjectReference other) {
            checkNotBuilt();
            name(other.getName());
            value(other.getValue());
            return this;
        }

        @JsonSetter("name")
        public Builder name(@Nonnull String name) {
            checkNotBuilt();
            this.name = Preconditions.checkNotNull(name, "name cannot be null");
            return this;
        }

        @JsonSetter("value")
        public Builder value(int value) {
            checkNotBuilt();
            this.value = value;
            this._valueInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _valueInitialized, "value");
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
        public ObjectReference build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ObjectReference(name, value);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
