package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = OptionalExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class OptionalExample {
    private final Optional<String> item;

    private volatile int memoizedHashCode;

    private OptionalExample(Optional<String> item) {
        validateFields(item);
        this.item = item;
    }

    @JsonProperty("item")
    public Optional<String> getItem() {
        return this.item;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OptionalExample && equalTo((OptionalExample) other));
    }

    private boolean equalTo(OptionalExample other) {
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
        return new StringBuilder("OptionalExample")
                .append('{')
                .append("item")
                .append(": ")
                .append(item)
                .append('}')
                .toString();
    }

    public static OptionalExample of(String item) {
        return builder().item(Optional.of(item)).build();
    }

    private static void validateFields(Optional<String> item) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, item, "item");
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
        private Optional<String> item = Optional.empty();

        private Builder() {}

        public Builder from(OptionalExample other) {
            item(other.getItem());
            return this;
        }

        @JsonSetter("item")
        public Builder item(Optional<String> item) {
            this.item = Objects.requireNonNull(item, "item cannot be null");
            return this;
        }

        public Builder item(String item) {
            this.item = Optional.of(Objects.requireNonNull(item, "item cannot be null"));
            return this;
        }

        public OptionalExample build() {
            return new OptionalExample(item);
        }
    }
}
