package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import test.api.ExampleExternalReference;

@JsonDeserialize(builder = CovariantListExampleNoStaticFactory.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CovariantListExampleNoStaticFactory {
    private final List<Object> items;

    private final List<ExampleExternalReference> externalItems;

    private final Optional<String> optionalField;

    private int memoizedHashCode;

    private CovariantListExampleNoStaticFactory(
            List<Object> items, List<ExampleExternalReference> externalItems, Optional<String> optionalField) {
        validateFields(items, externalItems, optionalField);
        this.items = Collections.unmodifiableList(items);
        this.externalItems = Collections.unmodifiableList(externalItems);
        this.optionalField = optionalField;
    }

    @JsonProperty("items")
    public List<Object> getItems() {
        return this.items;
    }

    @JsonProperty("externalItems")
    public List<ExampleExternalReference> getExternalItems() {
        return this.externalItems;
    }

    @JsonProperty("optionalField")
    public Optional<String> getOptionalField() {
        return this.optionalField;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof CovariantListExampleNoStaticFactory
                        && equalTo((CovariantListExampleNoStaticFactory) other));
    }

    private boolean equalTo(CovariantListExampleNoStaticFactory other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.items.equals(other.items)
                && this.externalItems.equals(other.externalItems)
                && this.optionalField.equals(other.optionalField);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.externalItems.hashCode();
            hash = 31 * hash + this.optionalField.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CovariantListExampleNoStaticFactory{items: " + items + ", externalItems: " + externalItems
                + ", optionalField: " + optionalField + '}';
    }

    private static void validateFields(
            List<Object> items, List<ExampleExternalReference> externalItems, Optional<String> optionalField) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, externalItems, "externalItems");
        missingFields = addFieldIfMissing(missingFields, optionalField, "optionalField");
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private List<Object> items = new ArrayList<>();

        private List<ExampleExternalReference> externalItems = new ArrayList<>();

        private Optional<String> optionalField = Optional.empty();

        private Builder() {}

        public Builder from(CovariantListExampleNoStaticFactory other) {
            checkNotBuilt();
            items(other.getItems());
            externalItems(other.getExternalItems());
            optionalField(other.getOptionalField());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP)
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

        @JsonSetter(value = "externalItems", nulls = Nulls.SKIP)
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

        @JsonSetter(value = "optionalField", nulls = Nulls.SKIP)
        public Builder optionalField(@Nonnull Optional<String> optionalField) {
            checkNotBuilt();
            this.optionalField = Preconditions.checkNotNull(optionalField, "optionalField cannot be null");
            return this;
        }

        public Builder optionalField(@Nonnull String optionalField) {
            checkNotBuilt();
            this.optionalField = Optional.of(Preconditions.checkNotNull(optionalField, "optionalField cannot be null"));
            return this;
        }

        public CovariantListExampleNoStaticFactory build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new CovariantListExampleNoStaticFactory(items, externalItems, optionalField);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
