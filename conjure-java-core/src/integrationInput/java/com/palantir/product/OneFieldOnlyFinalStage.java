package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = OneFieldOnlyFinalStage.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class OneFieldOnlyFinalStage {
    private final Optional<String> optionalItem;

    private OneFieldOnlyFinalStage(Optional<String> optionalItem) {
        validateFields(optionalItem);
        this.optionalItem = optionalItem;
    }

    @JsonProperty("optionalItem")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getOptionalItem() {
        return this.optionalItem;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OneFieldOnlyFinalStage && equalTo((OneFieldOnlyFinalStage) other));
    }

    private boolean equalTo(OneFieldOnlyFinalStage other) {
        return this.optionalItem.equals(other.optionalItem);
    }

    @Override
    public int hashCode() {
        return this.optionalItem.hashCode();
    }

    @Override
    public String toString() {
        return "OneFieldOnlyFinalStage{optionalItem: " + optionalItem + '}';
    }

    public static OneFieldOnlyFinalStage of(String optionalItem) {
        return builder().optionalItem(Optional.of(optionalItem)).build();
    }

    private static void validateFields(Optional<String> optionalItem) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
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
        boolean _buildInvoked;

        private Optional<String> optionalItem = Optional.empty();

        private Builder() {}

        public Builder from(OneFieldOnlyFinalStage other) {
            checkNotBuilt();
            optionalItem(other.getOptionalItem());
            return this;
        }

        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<String> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        public Builder optionalItem(@Nonnull String optionalItem) {
            checkNotBuilt();
            this.optionalItem = Optional.of(Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null"));
            return this;
        }

        public OneFieldOnlyFinalStage build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new OneFieldOnlyFinalStage(optionalItem);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
