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
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = ListExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ListExample {
    private final List<String> items;

    private final List<Integer> primitiveItems;

    private final List<Double> doubleItems;

    private final List<Optional<String>> optionalItems;

    private volatile int memoizedHashCode;

    private ListExample(
            List<String> items,
            List<Integer> primitiveItems,
            List<Double> doubleItems,
            List<Optional<String>> optionalItems) {
        validateFields(items, primitiveItems, doubleItems, optionalItems);
        this.items = Collections.unmodifiableList(items);
        this.primitiveItems = Collections.unmodifiableList(primitiveItems);
        this.doubleItems = Collections.unmodifiableList(doubleItems);
        this.optionalItems = Collections.unmodifiableList(optionalItems);
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

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof ListExample && equalTo((ListExample) other));
    }

    private boolean equalTo(ListExample other) {
        return this.items.equals(other.items)
                && this.primitiveItems.equals(other.primitiveItems)
                && this.doubleItems.equals(other.doubleItems)
                && this.optionalItems.equals(other.optionalItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result =
                    Objects.hash(
                            this.items, this.primitiveItems, this.doubleItems, this.optionalItems);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ListExample{items: "
                + items
                + ", primitiveItems: "
                + primitiveItems
                + ", doubleItems: "
                + doubleItems
                + ", optionalItems: "
                + optionalItems
                + '}';
    }

    private static void validateFields(
            List<String> items,
            List<Integer> primitiveItems,
            List<Double> doubleItems,
            List<Optional<String>> optionalItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, primitiveItems, "primitiveItems");
        missingFields = addFieldIfMissing(missingFields, doubleItems, "doubleItems");
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
    public static final class Builder {
        private List<String> items = new ArrayList<>();

        private List<Integer> primitiveItems = new ArrayList<>();

        private List<Double> doubleItems = new ArrayList<>();

        private List<Optional<String>> optionalItems = new ArrayList<>();

        private Builder() {}

        public Builder from(ListExample other) {
            items(other.getItems());
            primitiveItems(other.getPrimitiveItems());
            doubleItems(other.getDoubleItems());
            optionalItems(other.getOptionalItems());
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder items(Iterable<String> items) {
            this.items.clear();
            ConjureCollections.addAll(
                    this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(Iterable<String> items) {
            ConjureCollections.addAll(
                    this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(String items) {
            this.items.add(items);
            return this;
        }

        @JsonSetter(value = "primitiveItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder primitiveItems(Iterable<Integer> primitiveItems) {
            this.primitiveItems.clear();
            ConjureCollections.addAll(
                    this.primitiveItems,
                    Preconditions.checkNotNull(primitiveItems, "primitiveItems cannot be null"));
            return this;
        }

        public Builder addAllPrimitiveItems(Iterable<Integer> primitiveItems) {
            ConjureCollections.addAll(
                    this.primitiveItems,
                    Preconditions.checkNotNull(primitiveItems, "primitiveItems cannot be null"));
            return this;
        }

        public Builder primitiveItems(int primitiveItems) {
            this.primitiveItems.add(primitiveItems);
            return this;
        }

        @JsonSetter(value = "doubleItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder doubleItems(Iterable<Double> doubleItems) {
            this.doubleItems.clear();
            ConjureCollections.addAll(
                    this.doubleItems,
                    Preconditions.checkNotNull(doubleItems, "doubleItems cannot be null"));
            return this;
        }

        public Builder addAllDoubleItems(Iterable<Double> doubleItems) {
            ConjureCollections.addAll(
                    this.doubleItems,
                    Preconditions.checkNotNull(doubleItems, "doubleItems cannot be null"));
            return this;
        }

        public Builder doubleItems(double doubleItems) {
            this.doubleItems.add(doubleItems);
            return this;
        }

        @JsonSetter(value = "optionalItems", nulls = Nulls.SKIP, contentNulls = Nulls.AS_EMPTY)
        public Builder optionalItems(Iterable<Optional<String>> optionalItems) {
            this.optionalItems.clear();
            ConjureCollections.addAll(
                    this.optionalItems,
                    Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder addAllOptionalItems(Iterable<Optional<String>> optionalItems) {
            ConjureCollections.addAll(
                    this.optionalItems,
                    Preconditions.checkNotNull(optionalItems, "optionalItems cannot be null"));
            return this;
        }

        public Builder optionalItems(Optional<String> optionalItems) {
            this.optionalItems.add(optionalItems);
            return this;
        }

        public ListExample build() {
            return new ListExample(items, primitiveItems, doubleItems, optionalItems);
        }
    }
}
