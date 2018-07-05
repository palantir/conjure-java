package com.palantir.conjure.verifier;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Generated;

@JsonDeserialize(builder = OptionalObject.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class OptionalObject {
    private final Optional<String> name;

    private volatile int memoizedHashCode;

    private OptionalObject(Optional<String> name) {
        validateFields(name);
        this.name = name;
    }

    @JsonProperty("name")
    public Optional<String> getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof OptionalObject && equalTo((OptionalObject) other));
    }

    private boolean equalTo(OptionalObject other) {
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(name);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("OptionalObject")
                .append("{")
                .append("name")
                .append(": ")
                .append(name)
                .append("}")
                .toString();
    }

    public static OptionalObject of(String name) {
        return builder().name(Optional.of(name)).build();
    }

    private static void validateFields(Optional<String> name) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, name, "name");
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
        private Optional<String> name = Optional.empty();

        private Builder() {}

        public Builder from(OptionalObject other) {
            name(other.getName());
            return this;
        }

        @JsonSetter("name")
        public Builder name(Optional<String> name) {
            this.name = Objects.requireNonNull(name, "name cannot be null");
            return this;
        }

        public Builder name(String name) {
            this.name = Optional.of(Objects.requireNonNull(name, "name cannot be null"));
            return this;
        }

        public OptionalObject build() {
            return new OptionalObject(name);
        }
    }
}
