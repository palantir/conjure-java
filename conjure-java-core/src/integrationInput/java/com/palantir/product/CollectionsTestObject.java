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
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = CollectionsTestObject.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CollectionsTestObject {
    private final List<String> items;

    private final Map<String, Integer> itemsMap;

    private final Optional<String> optionalItem;

    private final Set<String> itemsSet;

    private final CollectionsTestAliasList alist;

    private final CollectionsTestAliasSet aset;

    private final CollectionsTestAliasMap amap;

    private int memoizedHashCode;

    private CollectionsTestObject(
            List<String> items,
            Map<String, Integer> itemsMap,
            Optional<String> optionalItem,
            Set<String> itemsSet,
            CollectionsTestAliasList alist,
            CollectionsTestAliasSet aset,
            CollectionsTestAliasMap amap) {
        validateFields(items, itemsMap, optionalItem, itemsSet, alist, aset, amap);
        this.items = Collections.unmodifiableList(items);
        this.itemsMap = Collections.unmodifiableMap(itemsMap);
        this.optionalItem = optionalItem;
        this.itemsSet = Collections.unmodifiableSet(itemsSet);
        this.alist = alist;
        this.aset = aset;
        this.amap = amap;
    }

    @JsonProperty("items")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<String> getItems() {
        return this.items;
    }

    @JsonProperty("itemsMap")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, Integer> getItemsMap() {
        return this.itemsMap;
    }

    @JsonProperty("optionalItem")
    public Optional<String> getOptionalItem() {
        return this.optionalItem;
    }

    @JsonProperty("itemsSet")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Set<String> getItemsSet() {
        return this.itemsSet;
    }

    @JsonProperty("alist")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public CollectionsTestAliasList getAlist() {
        return this.alist;
    }

    @JsonProperty("aset")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public CollectionsTestAliasSet getAset() {
        return this.aset;
    }

    @JsonProperty("amap")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public CollectionsTestAliasMap getAmap() {
        return this.amap;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof CollectionsTestObject && equalTo((CollectionsTestObject) other));
    }

    private boolean equalTo(CollectionsTestObject other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items)
                && this.itemsMap.equals(other.itemsMap)
                && this.optionalItem.equals(other.optionalItem)
                && this.itemsSet.equals(other.itemsSet)
                && this.alist.equals(other.alist)
                && this.aset.equals(other.aset)
                && this.amap.equals(other.amap);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.itemsMap.hashCode();
            hash = 31 * hash + this.optionalItem.hashCode();
            hash = 31 * hash + this.itemsSet.hashCode();
            hash = 31 * hash + this.alist.hashCode();
            hash = 31 * hash + this.aset.hashCode();
            hash = 31 * hash + this.amap.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CollectionsTestObject{items: " + items + ", itemsMap: " + itemsMap + ", optionalItem: " + optionalItem
                + ", itemsSet: " + itemsSet + ", alist: " + alist + ", aset: " + aset + ", amap: " + amap + '}';
    }

    private static void validateFields(
            List<String> items,
            Map<String, Integer> itemsMap,
            Optional<String> optionalItem,
            Set<String> itemsSet,
            CollectionsTestAliasList alist,
            CollectionsTestAliasSet aset,
            CollectionsTestAliasMap amap) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, itemsMap, "itemsMap");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        missingFields = addFieldIfMissing(missingFields, itemsSet, "itemsSet");
        missingFields = addFieldIfMissing(missingFields, alist, "alist");
        missingFields = addFieldIfMissing(missingFields, aset, "aset");
        missingFields = addFieldIfMissing(missingFields, amap, "amap");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(7);
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

        private CollectionsTestAliasList alist = CollectionsTestAliasList.empty();

        private CollectionsTestAliasSet aset = CollectionsTestAliasSet.empty();

        private CollectionsTestAliasMap amap = CollectionsTestAliasMap.empty();

        private Builder() {}

        public Builder from(CollectionsTestObject other) {
            checkNotBuilt();
            items(other.getItems());
            itemsMap(other.getItemsMap());
            optionalItem(other.getOptionalItem());
            itemsSet(other.getItemsSet());
            alist(other.getAlist());
            aset(other.getAset());
            amap(other.getAmap());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        public Builder items(@Nonnull Iterable<String> items) {
            checkNotBuilt();
            this.items = ConjureCollections.newArrayList(Preconditions.checkNotNull(items, "items cannot be null"));
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
            this.itemsMap = new LinkedHashMap<>(Preconditions.checkNotNull(itemsMap, "itemsMap cannot be null"));
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
            this.itemsSet = ConjureCollections.newLinkedHashSet(
                    Preconditions.checkNotNull(itemsSet, "itemsSet cannot be null"));
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

        @JsonSetter(value = "alist", nulls = Nulls.AS_EMPTY)
        public Builder alist(@Nonnull CollectionsTestAliasList alist) {
            checkNotBuilt();
            this.alist = Preconditions.checkNotNull(alist, "alist cannot be null");
            return this;
        }

        @JsonSetter(value = "aset", nulls = Nulls.AS_EMPTY)
        public Builder aset(@Nonnull CollectionsTestAliasSet aset) {
            checkNotBuilt();
            this.aset = Preconditions.checkNotNull(aset, "aset cannot be null");
            return this;
        }

        @JsonSetter(value = "amap", nulls = Nulls.AS_EMPTY)
        public Builder amap(@Nonnull CollectionsTestAliasMap amap) {
            checkNotBuilt();
            this.amap = Preconditions.checkNotNull(amap, "amap cannot be null");
            return this;
        }

        public CollectionsTestObject build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new CollectionsTestObject(items, itemsMap, optionalItem, itemsSet, alist, aset, amap);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
