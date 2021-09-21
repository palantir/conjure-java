package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = MultipleFieldsOnlyFinalStage.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MultipleFieldsOnlyFinalStage {
    private final List<String> items;

    private final Map<String, Integer> itemsMap;

    private final Optional<String> optionalItem;

    private final Set<String> itemsSet;

    private int memoizedHashCode;

    private MultipleFieldsOnlyFinalStage(
            List<String> items, Map<String, Integer> itemsMap, Optional<String> optionalItem, Set<String> itemsSet) {
        validateFields(items, itemsMap, optionalItem, itemsSet);
        this.items = Collections.unmodifiableList(items);
        this.itemsMap = Collections.unmodifiableMap(itemsMap);
        this.optionalItem = optionalItem;
        this.itemsSet = Collections.unmodifiableSet(itemsSet);
    }

    @JsonProperty("items")
    public List<String> getItems() {
        return this.items;
    }

    @JsonProperty("itemsMap")
    public Map<String, Integer> getItemsMap() {
        return this.itemsMap;
    }

    @JsonProperty("optionalItem")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getOptionalItem() {
        return this.optionalItem;
    }

    @JsonProperty("itemsSet")
    public Set<String> getItemsSet() {
        return this.itemsSet;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof MultipleFieldsOnlyFinalStage && equalTo((MultipleFieldsOnlyFinalStage) other));
    }

    private boolean equalTo(MultipleFieldsOnlyFinalStage other) {
        return this.items.equals(other.items)
                && this.itemsMap.equals(other.itemsMap)
                && this.optionalItem.equals(other.optionalItem)
                && this.itemsSet.equals(other.itemsSet);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(this.items, this.itemsMap, this.optionalItem, this.itemsSet);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "MultipleFieldsOnlyFinalStage{items: " + items + ", itemsMap: " + itemsMap + ", optionalItem: "
                + optionalItem + ", itemsSet: " + itemsSet + '}';
    }

    private static void validateFields(
            List<String> items, Map<String, Integer> itemsMap, Optional<String> optionalItem, Set<String> itemsSet) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, itemsMap, "itemsMap");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        missingFields = addFieldIfMissing(missingFields, itemsSet, "itemsSet");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(4);
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

        private List<String> items = new ArrayList<>();

        private Map<String, Integer> itemsMap = new LinkedHashMap<>();

        private Optional<String> optionalItem = Optional.empty();

        private Set<String> itemsSet = new LinkedHashSet<>();

        private Builder() {}

        public Builder from(MultipleFieldsOnlyFinalStage other) {
            checkNotBuilt();
            items(other.getItems());
            itemsMap(other.getItemsMap());
            optionalItem(other.getOptionalItem());
            itemsSet(other.getItemsSet());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        public Builder items(@Nonnull Iterable<String> items) {
            checkNotBuilt();
            this.items.clear();
            ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(@Nonnull Iterable<String> items) {
            checkNotBuilt();
            ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String items) {
            checkNotBuilt();
            this.items.add(items);
            return this;
        }

        @JsonSetter(value = "itemsMap", nulls = Nulls.SKIP)
        public Builder itemsMap(@Nonnull Map<String, Integer> itemsMap) {
            checkNotBuilt();
            this.itemsMap.clear();
            this.itemsMap.putAll(Preconditions.checkNotNull(itemsMap, "itemsMap cannot be null"));
            return this;
        }

        public Builder putAllItemsMap(@Nonnull Map<String, Integer> itemsMap) {
            checkNotBuilt();
            this.itemsMap.putAll(Preconditions.checkNotNull(itemsMap, "itemsMap cannot be null"));
            return this;
        }

        public Builder itemsMap(String key, int value) {
            checkNotBuilt();
            this.itemsMap.put(key, value);
            return this;
        }

        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<String> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        public Builder optionalItem(@Nonnull String optionalItem) {
            checkNotBuilt();
            this.optionalItem = Optional.of(Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null"));
            return this;
        }

        @JsonSetter(value = "itemsSet", nulls = Nulls.SKIP)
        public Builder itemsSet(@Nonnull Iterable<String> itemsSet) {
            checkNotBuilt();
            this.itemsSet.clear();
            ConjureCollections.addAll(this.itemsSet, Preconditions.checkNotNull(itemsSet, "itemsSet cannot be null"));
            return this;
        }

        public Builder addAllItemsSet(@Nonnull Iterable<String> itemsSet) {
            checkNotBuilt();
            ConjureCollections.addAll(this.itemsSet, Preconditions.checkNotNull(itemsSet, "itemsSet cannot be null"));
            return this;
        }

        public Builder itemsSet(String itemsSet) {
            checkNotBuilt();
            this.itemsSet.add(itemsSet);
            return this;
        }

        public MultipleFieldsOnlyFinalStage build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new MultipleFieldsOnlyFinalStage(items, itemsMap, optionalItem, itemsSet);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
