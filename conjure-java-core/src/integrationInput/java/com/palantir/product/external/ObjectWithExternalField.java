package com.palantir.product.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@JsonDeserialize(builder = ObjectWithExternalField.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ObjectWithExternalField {
    private final String field;

    private ObjectWithExternalField(String field) {
        validateFields(field);
        this.field = field;
    }

    @JsonProperty("field")
    @Safe
    public String getField() {
        return this.field;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ObjectWithExternalField && equalTo((ObjectWithExternalField) other));
    }

    private boolean equalTo(ObjectWithExternalField other) {
        return this.field.equals(other.field);
    }

    @Override
    public int hashCode() {
        return this.field.hashCode();
    }

    @Override
    @Safe
    public String toString() {
        return "ObjectWithExternalField{field: " + field + '}';
    }

    public static ObjectWithExternalField of(@Safe String field) {
        return builder().field(field).build();
    }

    private static void validateFields(String field) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, field, "field");
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

        private @Safe String field;

        private Builder() {}

        public Builder from(ObjectWithExternalField other) {
            checkNotBuilt();
            field(other.getField());
            return this;
        }

        @JsonSetter("field")
        public Builder field(@Nonnull @Safe String field) {
            checkNotBuilt();
            this.field = Preconditions.checkNotNull(field, "field cannot be null");
            return this;
        }

        public ObjectWithExternalField build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new ObjectWithExternalField(field);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
