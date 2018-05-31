package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;

@JsonDeserialize(builder = CovariantListExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CovariantListExample {
    private final List<Object> items;

    private volatile int memoizedHashCode;

    private CovariantListExample(List<Object> items) {
        validateFields(items);
        this.items = Collections.unmodifiableList(items);
    }

    @JsonProperty("items")
    public List<Object> getItems() {
        return this.items;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof CovariantListExample && equalTo((CovariantListExample) other));
    }

    private boolean equalTo(CovariantListExample other) {
        return this.items.equals(other.items);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(items);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("CovariantListExample")
                .append("{")
                .append("items")
                .append(": ")
                .append(items)
                .append("}")
                .toString();
    }

    public static CovariantListExample of(List<Object> items) {
        return builder().items(items).build();
    }

    private static void validateFields(List<Object> items) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, items, "items");
        if (missingFields != null) {
            throw new IllegalArgumentException(
                    "Some required fields have not been set: " + missingFields);
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(1);
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
        private List<Object> items = new ArrayList<>();

        private Builder() {}

        public Builder from(CovariantListExample other) {
            items(other.getItems());
            return this;
        }

        @JsonSetter("items")
        public Builder items(Iterable<?> items) {
            this.items.clear();
            ConjureCollections.addAll(
                    this.items, Objects.requireNonNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(Iterable<?> items) {
            ConjureCollections.addAll(
                    this.items, Objects.requireNonNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(Object items) {
            this.items.add(items);
            return this;
        }

        public CovariantListExample build() {
            return new CovariantListExample(items);
        }
    }
}
