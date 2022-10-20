package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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

@JsonDeserialize(builder = AnyMapExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class AnyMapExample {
    private final Map<String, Object> items;

    private int memoizedHashCode;

    private AnyMapExample(Map<String, Object> items) {
        validateFields(items);
        this.items = Collections.unmodifiableMap(items);
    }

    @JsonProperty("items")
    public Map<String, Object> getItems() {
        return this.items;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof AnyMapExample && equalTo((AnyMapExample) other));
    }

    private boolean equalTo(AnyMapExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.items.hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "AnyMapExample{items: " + items + '}';
    }

    public static AnyMapExample of(Map<String, Object> items) {
        return builder().items(items).build();
    }

    private static void validateFields(Map<String, Object> items) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
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

        private Map<String, Object> items = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(AnyMapExample other) {
            checkNotBuilt();
            items(other.getItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        public Builder items(@Nonnull Map<String, Object> items) {
            checkNotBuilt();
            this.items = new LinkedHashMap<>(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder putAllItems(@Nonnull Map<String, Object> items) {
            checkNotBuilt();
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String key, Object value) {
            checkNotBuilt();
            this.items.put(key, value);
            return this;
        }

        public AnyMapExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new AnyMapExample(items);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
