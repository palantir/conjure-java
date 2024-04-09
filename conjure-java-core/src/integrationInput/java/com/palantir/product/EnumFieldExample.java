package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = EnumFieldExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class EnumFieldExample {
    private final EnumExample enum_;

    private EnumFieldExample(EnumExample enum_) {
        validateFields(enum_);
        this.enum_ = enum_;
    }

    @JsonProperty("enum")
    public EnumExample getEnum() {
        return this.enum_;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof EnumFieldExample && equalTo((EnumFieldExample) other));
    }

    private boolean equalTo(EnumFieldExample other) {
        return this.enum_.equals(other.enum_);
    }

    @Override
    public int hashCode() {
        return this.enum_.hashCode();
    }

    @Override
    @Safe
    public String toString() {
        return "EnumFieldExample{enum: " + enum_ + '}';
    }

    public static EnumFieldExample of(EnumExample enum_) {
        return builder().enum_(enum_).build();
    }

    private static void validateFields(EnumExample enum_) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, enum_, "enum");
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

    @CheckReturnValue
    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private EnumExample enum_;

        private Builder() {}

        public Builder from(EnumFieldExample other) {
            checkNotBuilt();
            enum_(other.getEnum());
            return this;
        }

        @JsonSetter("enum")
        public Builder enum_(@Nonnull EnumExample enum_) {
            checkNotBuilt();
            this.enum_ = Preconditions.checkNotNull(enum_, "enum cannot be null");
            return this;
        }

        @CheckReturnValue
        public EnumFieldExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new EnumFieldExample(enum_);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
