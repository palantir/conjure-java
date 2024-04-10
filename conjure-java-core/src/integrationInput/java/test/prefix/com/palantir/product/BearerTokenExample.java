package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
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
@JsonDeserialize(builder = BearerTokenExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BearerTokenExample {
    private final BearerToken bearerTokenValue;

    private BearerTokenExample(BearerToken bearerTokenValue) {
        validateFields(bearerTokenValue);
        this.bearerTokenValue = bearerTokenValue;
    }

    @JsonProperty("bearerTokenValue")
    public BearerToken getBearerTokenValue() {
        return this.bearerTokenValue;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof BearerTokenExample && equalTo((BearerTokenExample) other));
    }

    private boolean equalTo(BearerTokenExample other) {
        return this.bearerTokenValue.equals(other.bearerTokenValue);
    }

    @Override
    public int hashCode() {
        return this.bearerTokenValue.hashCode();
    }

    @Override
    @DoNotLog
    public String toString() {
        return "BearerTokenExample{bearerTokenValue: " + bearerTokenValue + '}';
    }

    public static BearerTokenExample of(BearerToken bearerTokenValue) {
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

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private BearerToken bearerTokenValue;

        private Builder() {}

        public Builder from(BearerTokenExample other) {
            checkNotBuilt();
            bearerTokenValue(other.getBearerTokenValue());
            return this;
        }

        @JsonSetter("bearerTokenValue")
        public Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue) {
            checkNotBuilt();
            this.bearerTokenValue = Preconditions.checkNotNull(bearerTokenValue, "bearerTokenValue cannot be null");
            return this;
        }

        @CheckReturnValue
        public BearerTokenExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new BearerTokenExample(bearerTokenValue);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
