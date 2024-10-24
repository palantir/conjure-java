package com.company.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = Recipe.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class Recipe {
    private final RecipeName name;

    private final List<RecipeStep> steps;

    private int memoizedHashCode;

    private Recipe(RecipeName name, List<RecipeStep> steps) {
        validateFields(name, steps);
        this.name = name;
        this.steps = Collections.unmodifiableList(steps);
    }

    @JsonProperty("name")
    public RecipeName getName() {
        return this.name;
    }

    @JsonProperty("steps")
    public List<RecipeStep> getSteps() {
        return this.steps;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof Recipe && equalTo((Recipe) other));
    }

    private boolean equalTo(Recipe other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.name.equals(other.name) && this.steps.equals(other.steps);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.name.hashCode();
            hash = 31 * hash + this.steps.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{name: " + name + ", steps: " + steps + '}';
    }

    public static Recipe of(RecipeName name, List<RecipeStep> steps) {
        return builder().name(name).steps(steps).build();
    }

    private static void validateFields(RecipeName name, List<RecipeStep> steps) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, name, "name");
        missingFields = addFieldIfMissing(missingFields, steps, "steps");
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private RecipeName name;

        private List<RecipeStep> steps = ConjureCollections.newList();

        private Builder() {}

        public Builder from(Recipe other) {
            checkNotBuilt();
            name(other.getName());
            steps(other.getSteps());
            return this;
        }

        @JsonSetter("name")
        public Builder name(@Nonnull RecipeName name) {
            checkNotBuilt();
            this.name = Preconditions.checkNotNull(name, "name cannot be null");
            return this;
        }

        @JsonSetter(value = "steps", nulls = Nulls.SKIP)
        public Builder steps(@Nonnull Iterable<RecipeStep> steps) {
            checkNotBuilt();
            this.steps = ConjureCollections.newList(Preconditions.checkNotNull(steps, "steps cannot be null"));
            return this;
        }

        public Builder addAllSteps(@Nonnull Iterable<RecipeStep> steps) {
            checkNotBuilt();
            ConjureCollections.addAll(this.steps, Preconditions.checkNotNull(steps, "steps cannot be null"));
            return this;
        }

        public Builder steps(RecipeStep steps) {
            checkNotBuilt();
            this.steps.add(steps);
            return this;
        }

        @CheckReturnValue
        public Recipe build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new Recipe(name, steps);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
