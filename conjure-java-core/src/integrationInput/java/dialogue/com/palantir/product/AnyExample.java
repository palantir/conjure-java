package dialogue.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = AnyExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class AnyExample {
    private final Object anyValue;

    private final Map<String, Object> anyMap;

    private int memoizedHashCode;

    private AnyExample(Object anyValue, Map<String, Object> anyMap) {
        validateFields(anyValue, anyMap);
        this.anyValue = anyValue;
        this.anyMap = Collections.unmodifiableMap(anyMap);
    }

    @JsonProperty("anyValue")
    public Object getAnyValue() {
        return this.anyValue;
    }

    @JsonProperty("anyMap")
    public Map<String, Object> getAnyMap() {
        return this.anyMap;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof AnyExample && equalTo((AnyExample) other));
    }

    private boolean equalTo(AnyExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.anyValue.equals(other.anyValue) && this.anyMap.equals(other.anyMap);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.anyValue.hashCode();
            hash = 31 * hash + this.anyMap.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "AnyExample{anyValue: " + anyValue + ", anyMap: " + anyMap + '}';
    }

    public static AnyExample of(Object anyValue, Map<String, Object> anyMap) {
        return builder().anyValue(anyValue).anyMap(anyMap).build();
    }

    private static void validateFields(Object anyValue, Map<String, Object> anyMap) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, anyValue, "anyValue");
        missingFields = addFieldIfMissing(missingFields, anyMap, "anyMap");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(2);
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

        private Object anyValue;

        private Map<String, Object> anyMap = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(AnyExample other) {
            checkNotBuilt();
            anyValue(other.getAnyValue());
            anyMap(other.getAnyMap());
            return this;
        }

        @JsonSetter("anyValue")
        public Builder anyValue(@Nonnull Object anyValue) {
            checkNotBuilt();
            this.anyValue = Preconditions.checkNotNull(anyValue, "anyValue cannot be null");
            return this;
        }

        @JsonSetter(value = "anyMap", nulls = Nulls.SKIP)
        public Builder anyMap(@Nonnull Map<String, Object> anyMap) {
            checkNotBuilt();
            this.anyMap = new LinkedHashMap<>(Preconditions.checkNotNull(anyMap, "anyMap cannot be null"));
            return this;
        }

        public Builder putAllAnyMap(@Nonnull Map<String, Object> anyMap) {
            checkNotBuilt();
            this.anyMap.putAll(Preconditions.checkNotNull(anyMap, "anyMap cannot be null"));
            return this;
        }

        public Builder anyMap(String key, Object value) {
            checkNotBuilt();
            this.anyMap.put(key, value);
            return this;
        }

        @CheckReturnValue
        public AnyExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new AnyExample(anyValue, anyMap);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
