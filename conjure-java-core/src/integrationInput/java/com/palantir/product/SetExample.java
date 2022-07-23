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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = SetExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SetExample {
    private final Set<String> items;

    private final Set<Double> doubleItems;

    private int memoizedHashCode;

    private SetExample(Set<String> items, Set<Double> doubleItems) {
        validateFields(items, doubleItems);
        this.items = Collections.unmodifiableSet(items);
        this.doubleItems = Collections.unmodifiableSet(doubleItems);
    }

    @JsonProperty("items")
    public Set<String> getItems() {
        return this.items;
    }

    @JsonProperty("doubleItems")
    public Set<Double> getDoubleItems() {
        return this.doubleItems;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof SetExample && equalTo((SetExample) other));
    }

    private boolean equalTo(SetExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items) && this.doubleItems.equals(other.doubleItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.doubleItems.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "SetExample{items: " + items + ", doubleItems: " + doubleItems + '}';
    }

    public static SetExample of(Set<String> items, Set<Double> doubleItems) {
        return builder().items(items).doubleItems(doubleItems).build();
    }

    private static void validateFields(Set<String> items, Set<Double> doubleItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, doubleItems, "doubleItems");
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

        private Set<String> items = new LinkedHashSet<>();

        private Set<Double> doubleItems = new LinkedHashSet<>();

        private Builder() {}

        public Builder from(SetExample other) {
            checkNotBuilt();
            items(other.getItems());
            doubleItems(other.getDoubleItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder items(@Nonnull Iterable<String> items) {
            checkNotBuilt();
            this.items = ConjureCollections.newLinkedHashSet(Preconditions.checkNotNull(items, "items cannot be null"));
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

        @JsonSetter(value = "doubleItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder doubleItems(@Nonnull Iterable<Double> doubleItems) {
            checkNotBuilt();
            this.doubleItems = ConjureCollections.newLinkedHashSet(
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

        public SetExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new SetExample(items, doubleItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
