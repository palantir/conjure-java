package allexamples.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureJacksonSupport;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = WithPrefixOnlyMapExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class WithPrefixOnlyMapExample {
    private final Map<String, String> withItems;

    private int memoizedHashCode;

    private WithPrefixOnlyMapExample(Map<String, String> withItems) {
        validateFields(withItems);
        this.withItems = Collections.unmodifiableMap(withItems);
    }

    @JsonProperty("withItems")
    public Map<String, String> getWithItems() {
        return this.withItems;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof WithPrefixOnlyMapExample && equalTo((WithPrefixOnlyMapExample) other));
    }

    private boolean equalTo(WithPrefixOnlyMapExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.withItems.equals(other.withItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.withItems.hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "WithPrefixOnlyMapExample{withItems: " + withItems + '}';
    }

    public static WithPrefixOnlyMapExample of(Map<String, String> withItems) {
        return builder().withItems(withItems).build();
    }

    private static void validateFields(Map<String, String> withItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, withItems, "withItems");
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

        @JsonSetter(value = "withItems", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        private Map<String, String> withItems = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(WithPrefixOnlyMapExample other) {
            checkNotBuilt();
            withItems(other.getWithItems());
            return this;
        }

        public Builder withItems(@Nonnull Map<String, String> withItems) {
            checkNotBuilt();
            this.withItems = new LinkedHashMap<>(Preconditions.checkNotNull(withItems, "withItems cannot be null"));
            return this;
        }

        public Builder putAllWithItems(@Nonnull Map<String, String> withItems) {
            checkNotBuilt();
            this.withItems.putAll(Preconditions.checkNotNull(withItems, "withItems cannot be null"));
            return this;
        }

        public Builder withItems(String key, String value) {
            checkNotBuilt();
            this.withItems.put(key, value);
            return this;
        }

        @CheckReturnValue
        public WithPrefixOnlyMapExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new WithPrefixOnlyMapExample(withItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
