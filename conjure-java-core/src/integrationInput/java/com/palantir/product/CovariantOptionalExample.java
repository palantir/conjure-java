package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = CovariantOptionalExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CovariantOptionalExample {
    private final Optional<Object> item;

    private final Optional<Set<StringAliasExample>> setItem;

    private volatile int memoizedHashCode;

    private CovariantOptionalExample(
            Optional<Object> item, Optional<Set<StringAliasExample>> setItem) {
        validateFields(item, setItem);
        this.item = item;
        this.setItem = setItem;
    }

    @JsonProperty("item")
    public Optional<Object> getItem() {
        return this.item;
    }

    @JsonProperty("setItem")
    public Optional<Set<StringAliasExample>> getSetItem() {
        return this.setItem;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof CovariantOptionalExample
                        && equalTo((CovariantOptionalExample) other));
    }

    private boolean equalTo(CovariantOptionalExample other) {
        return this.item.equals(other.item) && this.setItem.equals(other.setItem);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(this.item, this.setItem);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CovariantOptionalExample{item: " + item + ", setItem: " + setItem + '}';
    }

    public static CovariantOptionalExample of(Object item, Set<StringAliasExample> setItem) {
        return builder().item(Optional.of(item)).setItem(Optional.of(setItem)).build();
    }

    private static void validateFields(
            Optional<Object> item, Optional<Set<StringAliasExample>> setItem) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, item, "item");
        missingFields = addFieldIfMissing(missingFields, setItem, "setItem");
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
    public static final class Builder {
        private Optional<Object> item = Optional.empty();

        private Optional<Set<StringAliasExample>> setItem = Optional.empty();

        private Builder() {}

        public Builder from(CovariantOptionalExample other) {
            item(other.getItem());
            setItem(other.getSetItem());
            return this;
        }

        @JsonSetter(value = "item", nulls = Nulls.SKIP)
        public Builder item(@Nonnull Optional<?> item) {
            this.item =
                    Preconditions.checkNotNull(item, "item cannot be null")
                            .map(Function.identity());
            return this;
        }

        public Builder item(@Nonnull Object item) {
            this.item = Optional.of(Preconditions.checkNotNull(item, "item cannot be null"));
            return this;
        }

        @JsonSetter(value = "setItem", nulls = Nulls.SKIP)
        public Builder setItem(@Nonnull Optional<? extends Set<StringAliasExample>> setItem) {
            this.setItem =
                    Preconditions.checkNotNull(setItem, "setItem cannot be null")
                            .map(Function.identity());
            return this;
        }

        public Builder setItem(@Nonnull Set<StringAliasExample> setItem) {
            this.setItem =
                    Optional.of(Preconditions.checkNotNull(setItem, "setItem cannot be null"));
            return this;
        }

        public CovariantOptionalExample build() {
            return new CovariantOptionalExample(item, setItem);
        }
    }
}
