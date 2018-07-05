package com.palantir.conjure.verifier;

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

@JsonDeserialize(builder = PositiveAndNegativeTestCases.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class PositiveAndNegativeTestCases {
    private final List<String> positive;

    private final List<String> negative;

    private volatile int memoizedHashCode;

    private PositiveAndNegativeTestCases(List<String> positive, List<String> negative) {
        validateFields(positive, negative);
        this.positive = Collections.unmodifiableList(positive);
        this.negative = Collections.unmodifiableList(negative);
    }

    @JsonProperty("positive")
    public List<String> getPositive() {
        return this.positive;
    }

    @JsonProperty("negative")
    public List<String> getNegative() {
        return this.negative;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof PositiveAndNegativeTestCases
                        && equalTo((PositiveAndNegativeTestCases) other));
    }

    private boolean equalTo(PositiveAndNegativeTestCases other) {
        return this.positive.equals(other.positive) && this.negative.equals(other.negative);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode == 0) {
            memoizedHashCode = Objects.hash(positive, negative);
        }
        return memoizedHashCode;
    }

    @Override
    public String toString() {
        return new StringBuilder("PositiveAndNegativeTestCases")
                .append("{")
                .append("positive")
                .append(": ")
                .append(positive)
                .append(", ")
                .append("negative")
                .append(": ")
                .append(negative)
                .append("}")
                .toString();
    }

    public static PositiveAndNegativeTestCases of(List<String> positive, List<String> negative) {
        return builder().positive(positive).negative(negative).build();
    }

    private static void validateFields(List<String> positive, List<String> negative) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, positive, "positive");
        missingFields = addFieldIfMissing(missingFields, negative, "negative");
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
    public static final class Builder {
        private List<String> positive = new ArrayList<>();

        private List<String> negative = new ArrayList<>();

        private Builder() {}

        public Builder from(PositiveAndNegativeTestCases other) {
            positive(other.getPositive());
            negative(other.getNegative());
            return this;
        }

        @JsonSetter("positive")
        public Builder positive(Iterable<String> positive) {
            this.positive.clear();
            ConjureCollections.addAll(
                    this.positive, Objects.requireNonNull(positive, "positive cannot be null"));
            return this;
        }

        public Builder addAllPositive(Iterable<String> positive) {
            ConjureCollections.addAll(
                    this.positive, Objects.requireNonNull(positive, "positive cannot be null"));
            return this;
        }

        public Builder positive(String positive) {
            this.positive.add(positive);
            return this;
        }

        @JsonSetter("negative")
        public Builder negative(Iterable<String> negative) {
            this.negative.clear();
            ConjureCollections.addAll(
                    this.negative, Objects.requireNonNull(negative, "negative cannot be null"));
            return this;
        }

        public Builder addAllNegative(Iterable<String> negative) {
            ConjureCollections.addAll(
                    this.negative, Objects.requireNonNull(negative, "negative cannot be null"));
            return this;
        }

        public Builder negative(String negative) {
            this.negative.add(negative);
            return this;
        }

        public PositiveAndNegativeTestCases build() {
            return new PositiveAndNegativeTestCases(positive, negative);
        }
    }
}
