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

@JsonDeserialize(builder = MultipleFieldsOnlyFinalStage.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MultipleFieldsOnlyFinalStage {
    private final List<String> items;

    private final Map<String, Integer> itemsMap;

    private final Optional<String> optionalItem;

    private final Set<String> itemsSet;

    private final List<String> itemsOld;

    private final Map<String, Integer> itemsMapOld;

    private final Optional<String> optionalItemOld;

    private final Set<String> itemsSetOld;

    private int memoizedHashCode;

    private MultipleFieldsOnlyFinalStage(
            List<String> items,
            Map<String, Integer> itemsMap,
            Optional<String> optionalItem,
            Set<String> itemsSet,
            List<String> itemsOld,
            Map<String, Integer> itemsMapOld,
            Optional<String> optionalItemOld,
            Set<String> itemsSetOld) {
        validateFields(items, itemsMap, optionalItem, itemsSet, itemsOld, itemsMapOld, optionalItemOld, itemsSetOld);
        this.items = Collections.unmodifiableList(items);
        this.itemsMap = Collections.unmodifiableMap(itemsMap);
        this.optionalItem = optionalItem;
        this.itemsSet = Collections.unmodifiableSet(itemsSet);
        this.itemsOld = Collections.unmodifiableList(itemsOld);
        this.itemsMapOld = Collections.unmodifiableMap(itemsMapOld);
        this.optionalItemOld = optionalItemOld;
        this.itemsSetOld = Collections.unmodifiableSet(itemsSetOld);
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

    /**
     * @deprecated this list is deprecated
     */
    @JsonProperty("itemsOld")
    @Deprecated
    public List<String> getItemsOld() {
        return this.itemsOld;
    }

    /**
     * @deprecated this map is deprecated
     */
    @JsonProperty("itemsMapOld")
    @Deprecated
    public Map<String, Integer> getItemsMapOld() {
        return this.itemsMapOld;
    }

    /**
     * @deprecated this optional is deprecated
     */
    @JsonProperty("optionalItemOld")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @Deprecated
    public Optional<String> getOptionalItemOld() {
        return this.optionalItemOld;
    }

    /**
     * @deprecated this set is deprecated
     */
    @JsonProperty("itemsSetOld")
    @Deprecated
    public Set<String> getItemsSetOld() {
        return this.itemsSetOld;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof MultipleFieldsOnlyFinalStage && equalTo((MultipleFieldsOnlyFinalStage) other));
    }

    private boolean equalTo(MultipleFieldsOnlyFinalStage other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items)
                && this.itemsMap.equals(other.itemsMap)
                && this.optionalItem.equals(other.optionalItem)
                && this.itemsSet.equals(other.itemsSet)
                && this.itemsOld.equals(other.itemsOld)
                && this.itemsMapOld.equals(other.itemsMapOld)
                && this.optionalItemOld.equals(other.optionalItemOld)
                && this.itemsSetOld.equals(other.itemsSetOld);
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
            hash = 31 * hash + this.itemsOld.hashCode();
            hash = 31 * hash + this.itemsMapOld.hashCode();
            hash = 31 * hash + this.optionalItemOld.hashCode();
            hash = 31 * hash + this.itemsSetOld.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "MultipleFieldsOnlyFinalStage{items: " + items + ", itemsMap: " + itemsMap + ", optionalItem: "
                + optionalItem + ", itemsSet: " + itemsSet + ", itemsOld: " + itemsOld + ", itemsMapOld: " + itemsMapOld
                + ", optionalItemOld: " + optionalItemOld + ", itemsSetOld: " + itemsSetOld + '}';
    }

    private static void validateFields(
            List<String> items,
            Map<String, Integer> itemsMap,
            Optional<String> optionalItem,
            Set<String> itemsSet,
            List<String> itemsOld,
            Map<String, Integer> itemsMapOld,
            Optional<String> optionalItemOld,
            Set<String> itemsSetOld) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, itemsMap, "itemsMap");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        missingFields = addFieldIfMissing(missingFields, itemsSet, "itemsSet");
        missingFields = addFieldIfMissing(missingFields, itemsOld, "itemsOld");
        missingFields = addFieldIfMissing(missingFields, itemsMapOld, "itemsMapOld");
        missingFields = addFieldIfMissing(missingFields, optionalItemOld, "optionalItemOld");
        missingFields = addFieldIfMissing(missingFields, itemsSetOld, "itemsSetOld");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(8);
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

        private List<String> itemsOld = new ArrayList<>();

        private Map<String, Integer> itemsMapOld = new LinkedHashMap<>();

        private Optional<String> optionalItemOld = Optional.empty();

        private Set<String> itemsSetOld = new LinkedHashSet<>();

        private Builder() {}

        public Builder from(MultipleFieldsOnlyFinalStage other) {
            checkNotBuilt();
            items(other.getItems());
            itemsMap(other.getItemsMap());
            optionalItem(other.getOptionalItem());
            itemsSet(other.getItemsSet());
            itemsOld(other.getItemsOld());
            itemsMapOld(other.getItemsMapOld());
            optionalItemOld(other.getOptionalItemOld());
            itemsSetOld(other.getItemsSetOld());
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

        /**
         * @deprecated this list is deprecated
         */
        @Deprecated
        @JsonSetter(value = "itemsOld", nulls = Nulls.SKIP)
        public Builder itemsOld(@Nonnull Iterable<String> itemsOld) {
            checkNotBuilt();
            this.itemsOld =
                    ConjureCollections.newArrayList(Preconditions.checkNotNull(itemsOld, "itemsOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this list is deprecated
         */
        @Deprecated
        public Builder addAllItemsOld(@Nonnull Iterable<String> itemsOld) {
            checkNotBuilt();
            ConjureCollections.addAll(this.itemsOld, Preconditions.checkNotNull(itemsOld, "itemsOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this list is deprecated
         */
        @Deprecated
        public Builder itemsOld(String itemsOld) {
            checkNotBuilt();
            this.itemsOld.add(itemsOld);
            return this;
        }

        /**
         * @deprecated this map is deprecated
         */
        @Deprecated
        @JsonSetter(value = "itemsMapOld", nulls = Nulls.SKIP)
        public Builder itemsMapOld(@Nonnull Map<String, Integer> itemsMapOld) {
            checkNotBuilt();
            this.itemsMapOld =
                    new LinkedHashMap<>(Preconditions.checkNotNull(itemsMapOld, "itemsMapOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this map is deprecated
         */
        @Deprecated
        public Builder putAllItemsMapOld(@Nonnull Map<String, Integer> itemsMapOld) {
            checkNotBuilt();
            this.itemsMapOld.putAll(Preconditions.checkNotNull(itemsMapOld, "itemsMapOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this map is deprecated
         */
        @Deprecated
        public Builder itemsMapOld(String key, int value) {
            checkNotBuilt();
            this.itemsMapOld.put(key, value);
            return this;
        }

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @JsonSetter(value = "optionalItemOld", nulls = Nulls.SKIP)
        public Builder optionalItemOld(@Nonnull Optional<String> optionalItemOld) {
            checkNotBuilt();
            this.optionalItemOld = Preconditions.checkNotNull(optionalItemOld, "optionalItemOld cannot be null");
            return this;
        }

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        public Builder optionalItemOld(@Nonnull String optionalItemOld) {
            checkNotBuilt();
            this.optionalItemOld =
                    Optional.of(Preconditions.checkNotNull(optionalItemOld, "optionalItemOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this set is deprecated
         */
        @Deprecated
        @JsonSetter(value = "itemsSetOld", nulls = Nulls.SKIP)
        public Builder itemsSetOld(@Nonnull Iterable<String> itemsSetOld) {
            checkNotBuilt();
            this.itemsSetOld = ConjureCollections.newLinkedHashSet(
                    Preconditions.checkNotNull(itemsSetOld, "itemsSetOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this set is deprecated
         */
        @Deprecated
        public Builder addAllItemsSetOld(@Nonnull Iterable<String> itemsSetOld) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.itemsSetOld, Preconditions.checkNotNull(itemsSetOld, "itemsSetOld cannot be null"));
            return this;
        }

        /**
         * @deprecated this set is deprecated
         */
        @Deprecated
        public Builder itemsSetOld(String itemsSetOld) {
            checkNotBuilt();
            this.itemsSetOld.add(itemsSetOld);
            return this;
        }

        public MultipleFieldsOnlyFinalStage build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new MultipleFieldsOnlyFinalStage(
                    items, itemsMap, optionalItem, itemsSet, itemsOld, itemsMapOld, optionalItemOld, itemsSetOld);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
