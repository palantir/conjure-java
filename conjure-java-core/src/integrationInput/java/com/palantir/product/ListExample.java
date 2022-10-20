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
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = ListExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ListExample {
    private final List<String> items;

    private final List<Integer> primitiveItems;

    private final List<Double> doubleItems;

    private final List<Optional<String>> optionalItems;

    private final List<OptionalAlias> aliasOptionalItems;

    private final List<List<String>> nestedItems;

    private int memoizedHashCode;

    private ListExample(
            List<String> items,
            List<Integer> primitiveItems,
            List<Double> doubleItems,
            List<Optional<String>> optionalItems,
            List<OptionalAlias> aliasOptionalItems,
            List<List<String>> nestedItems) {
        validateFields(items, primitiveItems, doubleItems, optionalItems, aliasOptionalItems, nestedItems);
        this.items = Collections.unmodifiableList(items);
        this.primitiveItems = Collections.unmodifiableList(primitiveItems);
        this.doubleItems = Collections.unmodifiableList(doubleItems);
        this.optionalItems = Collections.unmodifiableList(optionalItems);
        this.aliasOptionalItems = Collections.unmodifiableList(aliasOptionalItems);
        this.nestedItems = Collections.unmodifiableList(nestedItems);
    }

    @JsonProperty("items")
    public List<String> getItems() {
        return this.items;
    }

    @JsonProperty("primitiveItems")
    public List<Integer> getPrimitiveItems() {
        return this.primitiveItems;
    }

    @JsonProperty("doubleItems")
    public List<Double> getDoubleItems() {
        return this.doubleItems;
    }

    @JsonProperty("optionalItems")
    public List<Optional<String>> getOptionalItems() {
        return this.optionalItems;
    }

    @JsonProperty("aliasOptionalItems")
    public List<OptionalAlias> getAliasOptionalItems() {
        return this.aliasOptionalItems;
    }

    @JsonProperty("nestedItems")
    public List<List<String>> getNestedItems() {
        return this.nestedItems;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ListExample && equalTo((ListExample) other));
    }

    private boolean equalTo(ListExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items)
                && this.primitiveItems.equals(other.primitiveItems)
                && this.doubleItems.equals(other.doubleItems)
                && this.optionalItems.equals(other.optionalItems)
                && this.aliasOptionalItems.equals(other.aliasOptionalItems)
                && this.nestedItems.equals(other.nestedItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.primitiveItems.hashCode();
            hash = 31 * hash + this.doubleItems.hashCode();
            hash = 31 * hash + this.optionalItems.hashCode();
            hash = 31 * hash + this.aliasOptionalItems.hashCode();
            hash = 31 * hash + this.nestedItems.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ListExample{items: " + items + ", primitiveItems: " + primitiveItems + ", doubleItems: " + doubleItems
                + ", optionalItems: " + optionalItems + ", aliasOptionalItems: " + aliasOptionalItems
                + ", nestedItems: " + nestedItems + '}';
    }

    private static void validateFields(
            List<String> items,
            List<Integer> primitiveItems,
            List<Double> doubleItems,
            List<Optional<String>> optionalItems,
            List<OptionalAlias> aliasOptionalItems,
            List<List<String>> nestedItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, primitiveItems, "primitiveItems");
        missingFields = addFieldIfMissing(missingFields, doubleItems, "doubleItems");
        missingFields = addFieldIfMissing(missingFields, optionalItems, "optionalItems");
        missingFields = addFieldIfMissing(missingFields, aliasOptionalItems, "aliasOptionalItems");
        missingFields = addFieldIfMissing(missingFields, nestedItems, "nestedItems");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(6);
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

        private List<String> items = new ArrayList<>();

        private List<Integer> primitiveItems = new ArrayList<>();

        private List<Double> doubleItems = new ArrayList<>();

        private List<Optional<String>> optionalItems = new ArrayList<>();

        private List<OptionalAlias> aliasOptionalItems = new ArrayList<>();

        private List<List<String>> nestedItems = new ArrayList<>();

        private Builder() {}

        public Builder from(ListExample other) {
            checkNotBuilt();
            items(other.getItems());
            primitiveItems(other.getPrimitiveItems());
            doubleItems(other.getDoubleItems());
            optionalItems(other.getOptionalItems());
            aliasOptionalItems(other.getAliasOptionalItems());
            nestedItems(other.getNestedItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
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

        @JsonSetter(value = "primitiveItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder primitiveItems(@Nonnull Iterable<Integer> primitiveItems) {
            checkNotBuilt();
            this.primitiveItems = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(primitiveItems, "primitiveItems cannot be null"));
            return this;
        }

        public Builder addAllPrimitiveItems(@Nonnull Iterable<Integer> primitiveItems) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.primitiveItems, Preconditions.checkNotNull(primitiveItems, "primitiveItems cannot be null"));
            return this;
        }

        public Builder primitiveItems(int primitiveItems) {
            checkNotBuilt();
            this.primitiveItems.add(primitiveItems);
            return this;
        }

        @JsonSetter(value = "doubleItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder doubleItems(@Nonnull Iterable<Double> doubleItems) {
            checkNotBuilt();
            this.doubleItems = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(doubleItems, "doubleItems cannot be null"));
            return this;
        }

        public Builder addAllDoubleItems(@Nonnull Iterable<Double> doubleItems) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.doubleItems, Preconditions.checkNotNull(doubleItems, "doubleItems cannot be null"));
            return this;
        }

        public Builder doubleItems(double doubleItems) {
            checkNotBuilt();
            this.doubleItems.add(doubleItems);
            return this;
        }

        @JsonSetter(value = "optionalItems", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder optionalItems(@Nonnull Iterable<Optional<String>> optionalItems) {
            checkNotBuilt();
            this.optionalItems = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder addAllOptionalItems(@Nonnull Iterable<Optional<String>> optionalItems) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.optionalItems, Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder optionalItems(Optional<String> optionalItems) {
            checkNotBuilt();
            this.optionalItems.add(optionalItems);
            return this;
        }

        @JsonSetter(value = "aliasOptionalItems", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder aliasOptionalItems(@Nonnull Iterable<OptionalAlias> aliasOptionalItems) {
            checkNotBuilt();
            this.aliasOptionalItems = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(aliasOptionalItems, "aliasOptionalItems cannot be null"));
            return this;
        }

        public Builder addAllAliasOptionalItems(@Nonnull Iterable<OptionalAlias> aliasOptionalItems) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.aliasOptionalItems,
                    Preconditions.checkNotNull(aliasOptionalItems, "aliasOptionalItems cannot be null"));
            return this;
        }

        public Builder aliasOptionalItems(OptionalAlias aliasOptionalItems) {
            checkNotBuilt();
            this.aliasOptionalItems.add(aliasOptionalItems);
            return this;
        }

        @JsonSetter(value = "nestedItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder nestedItems(@Nonnull Iterable<? extends List<String>> nestedItems) {
            checkNotBuilt();
            this.nestedItems = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(nestedItems, "nestedItems cannot be null"));
            return this;
        }

        public Builder addAllNestedItems(@Nonnull Iterable<? extends List<String>> nestedItems) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.nestedItems, Preconditions.checkNotNull(nestedItems, "nestedItems cannot be null"));
            return this;
        }

        public Builder nestedItems(List<String> nestedItems) {
            checkNotBuilt();
            this.nestedItems.add(nestedItems);
            return this;
        }

        public ListExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new ListExample(items, primitiveItems, doubleItems, optionalItems, aliasOptionalItems, nestedItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
