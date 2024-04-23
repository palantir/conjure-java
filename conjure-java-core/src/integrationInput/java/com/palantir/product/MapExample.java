package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = MapExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MapExample {
    private final Map<String, String> items;

    private final Map<String, Optional<String>> optionalItems;

    private final Map<String, OptionalAlias> aliasOptionalItems;

    private int memoizedHashCode;

    private MapExample(
            Map<String, String> items,
            Map<String, Optional<String>> optionalItems,
            Map<String, OptionalAlias> aliasOptionalItems) {
        validateFields(items, optionalItems, aliasOptionalItems);
        this.items = Collections.unmodifiableMap(items);
        this.optionalItems = Collections.unmodifiableMap(optionalItems);
        this.aliasOptionalItems = Collections.unmodifiableMap(aliasOptionalItems);
    }

    @JsonProperty("items")
    public Map<String, String> getItems() {
        return this.items;
    }

    @JsonProperty("optionalItems")
    public Map<String, Optional<String>> getOptionalItems() {
        return this.optionalItems;
    }

    @JsonProperty("aliasOptionalItems")
    public Map<String, OptionalAlias> getAliasOptionalItems() {
        return this.aliasOptionalItems;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof MapExample && equalTo((MapExample) other));
    }

    private boolean equalTo(MapExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items)
                && this.optionalItems.equals(other.optionalItems)
                && this.aliasOptionalItems.equals(other.aliasOptionalItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.optionalItems.hashCode();
            hash = 31 * hash + this.aliasOptionalItems.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "MapExample{items: " + items + ", optionalItems: " + optionalItems + ", aliasOptionalItems: "
                + aliasOptionalItems + '}';
    }

    public static MapExample of(
            Map<String, String> items,
            Map<String, Optional<String>> optionalItems,
            Map<String, OptionalAlias> aliasOptionalItems) {
        return builder()
                .items(items)
                .optionalItems(optionalItems)
                .aliasOptionalItems(aliasOptionalItems)
                .build();
    }

    private static void validateFields(
            Map<String, String> items,
            Map<String, Optional<String>> optionalItems,
            Map<String, OptionalAlias> aliasOptionalItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, optionalItems, "optionalItems");
        missingFields = addFieldIfMissing(missingFields, aliasOptionalItems, "aliasOptionalItems");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(3);
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

        private Map<String, String> items = new LinkedHashMap<>();

        private Map<String, Optional<String>> optionalItems = new LinkedHashMap<>();

        private Map<String, OptionalAlias> aliasOptionalItems = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(MapExample other) {
            checkNotBuilt();
            items(other.getItems());
            optionalItems(other.getOptionalItems());
            aliasOptionalItems(other.getAliasOptionalItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder items(@Nonnull Map<String, String> items) {
            checkNotBuilt();
            this.items = ConjureCollections.newNullCheckedLinkedHashMap(items);
            return this;
        }

        public Builder putAllItems(@Nonnull Map<String, String> items) {
            checkNotBuilt();
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String key, String value) {
            checkNotBuilt();
            this.items.put(
                    Preconditions.checkNotNull(key, "items cannot have a null key"),
                    Preconditions.checkNotNull(value, "items cannot have a null value"));
            return this;
        }

        @JsonSetter(value = "optionalItems", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder optionalItems(@Nonnull Map<String, Optional<String>> optionalItems) {
            checkNotBuilt();
            this.optionalItems = ConjureCollections.newNullCheckedLinkedHashMap(optionalItems);
            return this;
        }

        public Builder putAllOptionalItems(@Nonnull Map<String, Optional<String>> optionalItems) {
            checkNotBuilt();
            this.optionalItems.putAll(Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder optionalItems(String key, Optional<String> value) {
            checkNotBuilt();
            this.optionalItems.put(
                    Preconditions.checkNotNull(key, "optionalItems cannot have a null key"),
                    Preconditions.checkNotNull(value, "optionalItems cannot have a null value"));
            return this;
        }

        @JsonSetter(value = "aliasOptionalItems", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder aliasOptionalItems(@Nonnull Map<String, OptionalAlias> aliasOptionalItems) {
            checkNotBuilt();
            this.aliasOptionalItems = ConjureCollections.newNullCheckedLinkedHashMap(aliasOptionalItems);
            return this;
        }

        public Builder putAllAliasOptionalItems(@Nonnull Map<String, OptionalAlias> aliasOptionalItems) {
            checkNotBuilt();
            this.aliasOptionalItems.putAll(
                    Preconditions.checkNotNull(aliasOptionalItems, "aliasOptionalItems cannot be null"));
            return this;
        }

        public Builder aliasOptionalItems(String key, OptionalAlias value) {
            checkNotBuilt();
            this.aliasOptionalItems.put(
                    Preconditions.checkNotNull(key, "aliasOptionalItems cannot have a null key"),
                    Preconditions.checkNotNull(value, "aliasOptionalItems cannot have a null value"));
            return this;
        }

        public MapExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new MapExample(items, optionalItems, aliasOptionalItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
