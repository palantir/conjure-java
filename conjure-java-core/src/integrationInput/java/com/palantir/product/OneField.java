package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.Immutable;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.tokens.auth.BearerToken;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@Immutable
@JsonDeserialize(builder = OneField.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class OneField {
    private final BearerToken bearerTokenValue;

    private OneField(BearerToken bearerTokenValue) {
        validateFields(bearerTokenValue);
        this.bearerTokenValue = bearerTokenValue;
    }

    @JsonProperty("bearerTokenValue")
    public BearerToken getBearerTokenValue() {
        return this.bearerTokenValue;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OneField && equalTo((OneField) other));
    }

    private boolean equalTo(OneField other) {
        return this.bearerTokenValue.equals(other.bearerTokenValue);
    }

    @Override
    public int hashCode() {
        return this.bearerTokenValue.hashCode();
    }

    @Override
    @DoNotLog
    public String toString() {
        return "OneField{bearerTokenValue: " + bearerTokenValue + '}';
    }

    public static OneField of(BearerToken bearerTokenValue) {
        return builder().bearerTokenValue(bearerTokenValue).build();
    }

    private static void validateFields(BearerToken bearerTokenValue) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, bearerTokenValue, "bearerTokenValue");
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

    public static BearerTokenValueStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface BearerTokenValueStageBuilder {
        Completed_StageBuilder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);

        Builder from(OneField other);
    }

    public interface Completed_StageBuilder {
        OneField build();
    }

    public interface Builder extends BearerTokenValueStageBuilder, Completed_StageBuilder {
        @Override
        Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);

        @Override
        Builder from(OneField other);

        @Override
        OneField build();
    }

    @Immutable
    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private BearerToken bearerTokenValue;

        private DefaultBuilder() {}

        @Override
        public Builder from(OneField other) {
            checkNotBuilt();
            bearerTokenValue(other.getBearerTokenValue());
            return this;
        }

        @Override
        @JsonSetter("bearerTokenValue")
        public Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue) {
            checkNotBuilt();
            this.bearerTokenValue = Preconditions.checkNotNull(bearerTokenValue, "bearerTokenValue cannot be null");
            return this;
        }

        @Override
        public OneField build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new OneField(bearerTokenValue);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
