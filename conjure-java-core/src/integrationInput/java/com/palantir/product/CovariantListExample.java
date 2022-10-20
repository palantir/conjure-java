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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;
import test.api.ExampleExternalReference;

@JsonDeserialize(builder = CovariantListExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CovariantListExample {
    private final List<Object> items;

    private final List<ExampleExternalReference> externalItems;

    private int memoizedHashCode;

    private CovariantListExample(List<Object> items, List<ExampleExternalReference> externalItems) {
        validateFields(items, externalItems);
        this.items = Collections.unmodifiableList(items);
        this.externalItems = Collections.unmodifiableList(externalItems);
    }

    @JsonProperty("items")
    public List<Object> getItems() {
        return this.items;
    }

    @JsonProperty("externalItems")
    public List<ExampleExternalReference> getExternalItems() {
        return this.externalItems;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof CovariantListExample && equalTo((CovariantListExample) other));
    }

    private boolean equalTo(CovariantListExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items) && this.externalItems.equals(other.externalItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.externalItems.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CovariantListExample{items: " + items + ", externalItems: " + externalItems + '}';
    }

    public static CovariantListExample of(List<Object> items, List<ExampleExternalReference> externalItems) {
        return builder().items(items).externalItems(externalItems).build();
    }

    private static void validateFields(List<Object> items, List<ExampleExternalReference> externalItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, externalItems, "externalItems");
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
    public static final class Builder {
        boolean _buildInvoked;

        private List<Object> items = new ArrayList<>();

        private List<ExampleExternalReference> externalItems = new ArrayList<>();

        private Builder() {}

        public Builder from(CovariantListExample other) {
            checkNotBuilt();
            items(other.getItems());
            externalItems(other.getExternalItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder items(@Nonnull Iterable<?> items) {
            checkNotBuilt();
            this.items = ConjureCollections.newArrayList(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(@Nonnull Iterable<?> items) {
            checkNotBuilt();
            ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(Object items) {
            checkNotBuilt();
            this.items.add(items);
            return this;
        }

        @JsonSetter(value = "externalItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder externalItems(@Nonnull Iterable<? extends ExampleExternalReference> externalItems) {
            checkNotBuilt();
            this.externalItems = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(externalItems, "externalItems cannot be null"));
            return this;
        }

        public Builder addAllExternalItems(@Nonnull Iterable<? extends ExampleExternalReference> externalItems) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.externalItems, Preconditions.checkNotNull(externalItems, "externalItems cannot be null"));
            return this;
        }

        public Builder externalItems(ExampleExternalReference externalItems) {
            checkNotBuilt();
            this.externalItems.add(externalItems);
            return this;
        }

        public CovariantListExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new CovariantListExample(items, externalItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
