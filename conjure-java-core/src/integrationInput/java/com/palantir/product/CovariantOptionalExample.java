package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = CovariantOptionalExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CovariantOptionalExample {
    private final Optional<Object> item;

    private volatile int memoizedHashCode;

    private CovariantOptionalExample(Optional<Object> item) {
        validateFields(item);
        this.item = item;
    }

    @JsonProperty("item")
    public Optional<Object> getItem() {
        return this.item;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof CovariantOptionalExample
                        && equalTo((CovariantOptionalExample) other));
    }

    private boolean equalTo(CovariantOptionalExample other) {
        return this.item.equals(other.item);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(item);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("CovariantOptionalExample")
                .append('{')
                .append("item")
                .append(": ")
                .append(item)
                .append('}')
                .toString();
    }

    public static CovariantOptionalExample of(Object item) {
        return builder().item(Optional.of(item)).build();
    }

    private static void validateFields(Optional<Object> item) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, item, "item");
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
        private Optional<Object> item = Optional.empty();

        private Builder() {}

        public Builder from(CovariantOptionalExample other) {
            item(other.getItem());
            return this;
        }

        @JsonSetter("item")
        public Builder item(Optional<?> item) {
            this.item = (Optional<Object>) Objects.requireNonNull(item, "item cannot be null");
            return this;
        }

        public Builder item(Object item) {
            this.item = Optional.of(Objects.requireNonNull(item, "item cannot be null"));
            return this;
        }

        public CovariantOptionalExample build() {
            return new CovariantOptionalExample(item);
        }
    }
}
