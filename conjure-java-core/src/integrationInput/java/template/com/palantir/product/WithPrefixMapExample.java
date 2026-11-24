package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureJacksonSupport;
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

@JsonDeserialize(builder = WithPrefixMapExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class WithPrefixMapExample {
    private final Map<String, String> items;

    private final Map<String, String> withItems;

    private int memoizedHashCode;

    private WithPrefixMapExample(Map<String, String> items, Map<String, String> withItems) {
        validateFields(items, withItems);
        this.items = Collections.unmodifiableMap(items);
        this.withItems = Collections.unmodifiableMap(withItems);
    }

    @JsonProperty("items")
    public Map<String, String> getItems() {
        return this.items;
    }

    @JsonProperty("withItems")
    public Map<String, String> getWithItems() {
        return this.withItems;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof WithPrefixMapExample && equalTo((WithPrefixMapExample) other));
    }

    private boolean equalTo(WithPrefixMapExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items) && this.withItems.equals(other.withItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.withItems.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "WithPrefixMapExample{items: " + items + ", withItems: " + withItems + '}';
    }

    private static void validateFields(Map<String, String> items, Map<String, String> withItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, withItems, "withItems");
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
    @JsonAutoDetect(setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class Builder {
        boolean _buildInvoked;

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        private Map<String, String> items = new LinkedHashMap<>();

        @JsonSetter(value = "withItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        private Map<String, String> withItems = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(WithPrefixMapExample other) {
            checkNotBuilt();
            items(other.getItems());
            withItems(other.getWithItems());
            return this;
        }

        public Builder items(@Nonnull Map<String, String> items) {
            checkNotBuilt();
            this.items = new LinkedHashMap<>(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder putAllItems(@Nonnull Map<String, String> items) {
            checkNotBuilt();
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String key, String value) {
            checkNotBuilt();
            this.items.put(key, value);
            return this;
        }

        public Builder withItems(@Nonnull Map<String, String> withItems) {
            checkNotBuilt();
            this.withItems = new LinkedHashMap<>(Preconditions.checkNotNull(withItems, "withItems cannot be null"));
            return this;
        }

        public Builder putAllWithItems(@Nonnull Map<String, String> withItems) {
            checkNotBuilt();
            this.withItems.putAll(Preconditions.checkNotNull(withItems, "withItems cannot be null"));
            return this;
        }

        public Builder withItems(String key, String value) {
            checkNotBuilt();
            this.withItems.put(key, value);
            return this;
        }

        @CheckReturnValue
        public WithPrefixMapExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new WithPrefixMapExample(items, withItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
