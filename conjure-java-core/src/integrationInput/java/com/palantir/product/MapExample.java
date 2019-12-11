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
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = MapExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MapExample {
    private final Map<String, String> items;

    private final Map<String, Optional<String>> optionalItems;

    private volatile int memoizedHashCode;

    private MapExample(Map<String, String> items, Map<String, Optional<String>> optionalItems) {
        validateFields(items, optionalItems);
        this.items = Collections.unmodifiableMap(items);
        this.optionalItems = Collections.unmodifiableMap(optionalItems);
    }

    @JsonProperty("items")
    public Map<String, String> getItems() {
        return this.items;
    }

    @JsonProperty("optionalItems")
    public Map<String, Optional<String>> getOptionalItems() {
        return this.optionalItems;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof MapExample && equalTo((MapExample) other));
    }

    private boolean equalTo(MapExample other) {
        return this.items.equals(other.items) && this.optionalItems.equals(other.optionalItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(this.items, this.optionalItems);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "MapExample{items: " + items + ", optionalItems: " + optionalItems + '}';
    }

    public static MapExample of(
            Map<String, String> items, Map<String, Optional<String>> optionalItems) {
        return builder().items(items).optionalItems(optionalItems).build();
    }

    private static void validateFields(
            Map<String, String> items, Map<String, Optional<String>> optionalItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, optionalItems, "optionalItems");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set",
                    SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
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
    public static final class Builder {
        private Map<String, String> items = new LinkedHashMap<>();

        private Map<String, Optional<String>> optionalItems = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(MapExample other) {
            items(other.getItems());
            optionalItems(other.getOptionalItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder items(Map<String, String> items) {
            this.items.clear();
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder putAllItems(Map<String, String> items) {
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String key, String value) {
            this.items.put(key, value);
            return this;
        }

        @JsonSetter(value = "optionalItems", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder optionalItems(Map<String, Optional<String>> optionalItems) {
            this.optionalItems.clear();
            this.optionalItems.putAll(
                    Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder putAllOptionalItems(Map<String, Optional<String>> optionalItems) {
            this.optionalItems.putAll(
                    Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder optionalItems(String key, Optional<String> value) {
            this.optionalItems.put(key, value);
            return this;
        }

        public MapExample build() {
            return new MapExample(items, optionalItems);
        }
    }
}
