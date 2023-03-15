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

@JsonDeserialize(builder = StrictOneOptionalField.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StrictOneOptionalField {
    private final Optional<String> optionalItem;

    private StrictOneOptionalField(Optional<String> optionalItem) {
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
        return this == other || (other instanceof StrictOneOptionalField && equalTo((StrictOneOptionalField) other));
    }

    private boolean equalTo(StrictOneOptionalField other) {
        return this.optionalItem.equals(other.optionalItem);
    }

    @Override
    public int hashCode() {
        return this.optionalItem.hashCode();
    }

    @Override
    public String toString() {
        return "StrictOneOptionalField{optionalItem: " + optionalItem + '}';
    }

    public static StrictOneOptionalField of(String optionalItem) {
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

    public static OptionalItemStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface OptionalItemStageBuilder {
        Completed_StageBuilder optionalItem(@Nonnull Optional<String> optionalItem);

        Builder from(StrictOneOptionalField other);
    }

    public interface Completed_StageBuilder {
        StrictOneOptionalField build();
    }

    public interface Builder extends OptionalItemStageBuilder, Completed_StageBuilder {
        @Override
        Builder optionalItem(@Nonnull Optional<String> optionalItem);

        @Override
        Builder from(StrictOneOptionalField other);

        @Override
        StrictOneOptionalField build();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private Optional<String> optionalItem = Optional.empty();

        private DefaultBuilder() {}

        @Override
        public Builder from(StrictOneOptionalField other) {
            checkNotBuilt();
            optionalItem(other.getOptionalItem());
            return this;
        }

        @Override
        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<String> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        @Override
        public StrictOneOptionalField build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new StrictOneOptionalField(optionalItem);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
