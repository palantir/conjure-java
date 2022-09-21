package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = OptionalExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class OptionalExample {
    private final Optional<String> item;

    private OptionalExample(Optional<String> item) {
        validateFields(item);
        this.item = item;
    }

    @JsonProperty("item")
    @Safe
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getItem() {
        return this.item;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OptionalExample && equalTo((OptionalExample) other));
    }

    private boolean equalTo(OptionalExample other) {
        return this.item.equals(other.item);
    }

    @Override
    public int hashCode() {
        return this.item.hashCode();
    }

    @Override
    public String toString() {
        return "OptionalExample{item: " + item + '}';
    }

    public static OptionalExample of(@Safe String item) {
        return builder().item(Optional.of(item)).build();
    }

    private static void validateFields(Optional<String> item) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, item, "item");
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
    public static final class Builder {
        boolean _buildInvoked;

        private Optional<@Safe String> item = Optional.empty();

        private Builder() {}

        public Builder from(OptionalExample other) {
            checkNotBuilt();
            item(other.getItem());
            return this;
        }

        @JsonSetter(value = "item", nulls = Nulls.SKIP)
        public Builder item(@Safe @Nonnull Optional<@Safe String> item) {
            checkNotBuilt();
            this.item = Preconditions.checkNotNull(item, "item cannot be null");
            return this;
        }

        public Builder item(@Safe @Nonnull String item) {
            checkNotBuilt();
            this.item = Optional.of(Preconditions.checkNotNull(item, "item cannot be null"));
            return this;
        }

        public OptionalExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new OptionalExample(item);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
