package com.palantir.product;

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
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

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
    public boolean equals(Object other) {
        return this == other || (other instanceof AnyMapExample && equalTo((AnyMapExample) other));
    }

    private boolean equalTo(AnyMapExample other) {
        return this.items.equals(other.items);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hashCode(this.items);
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
    public static final class Builder {
        private Map<String, Object> items = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(AnyMapExample other) {
            items(other.getItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder items(@Nonnull Map<String, Object> items) {
            this.items.clear();
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder putAllItems(@Nonnull Map<String, Object> items) {
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String key, Object value) {
            this.items.put(key, value);
            return this;
        }

        public AnyMapExample build() {
            return new AnyMapExample(items);
        }
    }
}
