package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@JsonDeserialize(builder = BooleanExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class BooleanExample {
    private final boolean coin;

    private BooleanExample(boolean coin) {
        this.coin = coin;
    }

    @JsonProperty("coin")
    public boolean getCoin() {
        return this.coin;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof BooleanExample && equalTo((BooleanExample) other));
    }

    private boolean equalTo(BooleanExample other) {
        return this.coin == other.coin;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(this.coin);
    }

    @Override
    public String toString() {
        return "BooleanExample{coin: " + coin + '}';
    }

    public static BooleanExample of(boolean coin) {
        return builder().coin(coin).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private boolean coin;

        private boolean _coinInitialized = false;

        private Builder() {}

        public Builder from(BooleanExample other) {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
            coin(other.getCoin());
            return this;
        }

        @JsonSetter("coin")
        public Builder coin(boolean coin) {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
            this.coin = coin;
            this._coinInitialized = true;
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _coinInitialized, "coin");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(1);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public BooleanExample build() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new BooleanExample(coin);
        }
    }
}
