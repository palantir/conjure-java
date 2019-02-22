package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import test.api.ExampleExternalReference;

@JsonDeserialize(builder = CovariantListExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CovariantListExample {
    private final List<Object> items;

    private final List<ExampleExternalReference> externalItems;

    private volatile int memoizedHashCode;

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
    public boolean equals(Object other) {
        return this == other
                || (other instanceof CovariantListExample && equalTo((CovariantListExample) other));
    }

    private boolean equalTo(CovariantListExample other) {
        return this.items.equals(other.items) && this.externalItems.equals(other.externalItems);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(items, externalItems);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("CovariantListExample")
                .append('{')
                .append("items")
                .append(": ")
                .append(items)
                .append(", ")
                .append("externalItems")
                .append(": ")
                .append(externalItems)
                .append('}')
                .toString();
    }

    public static CovariantListExample of(
            List<Object> items, List<ExampleExternalReference> externalItems) {
        return builder().items(items).externalItems(externalItems).build();
    }

    private static void validateFields(
            List<Object> items, List<ExampleExternalReference> externalItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, externalItems, "externalItems");
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    @SuppressWarnings("unchecked")
    public static final class Builder {
        private List<Object> items = new ArrayList<>();

        private List<ExampleExternalReference> externalItems = new ArrayList<>();

        private Builder() {}

        public Builder from(CovariantListExample other) {
            items(other.getItems());
            externalItems(other.getExternalItems());
            return this;
        }

        @JsonSetter("items")
        public Builder items(Iterable<?> items) {
            this.items.clear();
            ConjureCollections.addAll(
                    this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(Iterable<?> items) {
            ConjureCollections.addAll(
                    this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(Object items) {
            this.items.add(items);
            return this;
        }

        @JsonSetter("externalItems")
        public Builder externalItems(Iterable<? extends ExampleExternalReference> externalItems) {
            this.externalItems.clear();
            ConjureCollections.addAll(
                    this.externalItems,
                    Preconditions.checkNotNull(externalItems, "externalItems cannot be null"));
            return this;
        }

        public Builder addAllExternalItems(
                Iterable<? extends ExampleExternalReference> externalItems) {
            ConjureCollections.addAll(
                    this.externalItems,
                    Preconditions.checkNotNull(externalItems, "externalItems cannot be null"));
            return this;
        }

        public Builder externalItems(ExampleExternalReference externalItems) {
            this.externalItems.add(externalItems);
            return this;
        }

        public CovariantListExample build() {
            return new CovariantListExample(items, externalItems);
        }
    }
}
