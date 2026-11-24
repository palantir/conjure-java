package stagedbuilder.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonDeserialize(builder = WithPrefixedMapFields.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class WithPrefixedMapFields {
    private final String required;

    private final Map<String, String> items;

    private final Map<String, String> withItems;

    private int memoizedHashCode;

    private WithPrefixedMapFields(String required, Map<String, String> items, Map<String, String> withItems) {
        validateFields(required, items, withItems);
        this.required = required;
        this.items = Collections.unmodifiableMap(items);
        this.withItems = Collections.unmodifiableMap(withItems);
    }

    @JsonProperty("required")
    public String getRequired() {
        return this.required;
    }

    @JsonProperty("items")
    public Map<String, String> getItems() {
        return this.items;
    }

    @JsonProperty("withItems")
    public Map<String, String> getWithItems() {
        return this.withItems;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof WithPrefixedMapFields && equalTo((WithPrefixedMapFields) other));
    }

    private boolean equalTo(WithPrefixedMapFields other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.required.equals(other.required)
                && this.items.equals(other.items)
                && this.withItems.equals(other.withItems);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.required.hashCode();
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.withItems.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "WithPrefixedMapFields{required: " + required + ", items: " + items + ", withItems: " + withItems + '}';
    }

    public static WithPrefixedMapFields of(String required, Map<String, String> items, Map<String, String> withItems) {
        return builder().required(required).items(items).withItems(withItems).build();
    }

    private static void validateFields(String required, Map<String, String> items, Map<String, String> withItems) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, required, "required");
        missingFields = addFieldIfMissing(missingFields, items, "items");
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
                missingFields = new ArrayList<>(3);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static RequiredStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface RequiredStageBuilder {
        Completed_StageBuilder required(@Nonnull String required);

        Builder from(WithPrefixedMapFields other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        WithPrefixedMapFields build();

        Completed_StageBuilder items(@Nonnull Map<String, String> items);

        Completed_StageBuilder putAllItems(@Nonnull Map<String, String> items);

        Completed_StageBuilder items(String key, String value);

        Completed_StageBuilder withItems(@Nonnull Map<String, String> withItems);

        Completed_StageBuilder putAllWithItems(@Nonnull Map<String, String> withItems);

        Completed_StageBuilder withItems(String key, String value);
    }

    public interface Builder extends RequiredStageBuilder, Completed_StageBuilder {
        @Override
        Builder required(@Nonnull String required);

        @Override
        Builder from(WithPrefixedMapFields other);

        @CheckReturnValue
        @Override
        WithPrefixedMapFields build();

        @Override
        Builder items(@Nonnull Map<String, String> items);

        @Override
        Builder putAllItems(@Nonnull Map<String, String> items);

        @Override
        Builder items(String key, String value);

        @Override
        Builder withItems(@Nonnull Map<String, String> withItems);

        @Override
        Builder putAllWithItems(@Nonnull Map<String, String> withItems);

        @Override
        Builder withItems(String key, String value);
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonAutoDetect(setterVisibility = JsonAutoDetect.Visibility.NONE)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private String required;

        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        private Map<String, String> items = new LinkedHashMap<>();

        @JsonSetter(value = "withItems", nulls = Nulls.SKIP)
        @JsonDeserialize(using = ConjureJacksonSupport.ContainerDeserializer.class)
        private Map<String, String> withItems = new LinkedHashMap<>();

        private DefaultBuilder() {}

        @Override
        public Builder from(WithPrefixedMapFields other) {
            checkNotBuilt();
            required(other.getRequired());
            items(other.getItems());
            withItems(other.getWithItems());
            return this;
        }

        @Override
        @JsonSetter("required")
        public Builder required(@Nonnull String required) {
            checkNotBuilt();
            this.required = Preconditions.checkNotNull(required, "required cannot be null");
            return this;
        }

        @Override
        public Builder items(@Nonnull Map<String, String> items) {
            checkNotBuilt();
            this.items = new LinkedHashMap<>(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        @Override
        public Builder putAllItems(@Nonnull Map<String, String> items) {
            checkNotBuilt();
            this.items.putAll(Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        @Override
        public Builder items(String key, String value) {
            checkNotBuilt();
            this.items.put(key, value);
            return this;
        }

        @Override
        public Builder withItems(@Nonnull Map<String, String> withItems) {
            checkNotBuilt();
            this.withItems = new LinkedHashMap<>(Preconditions.checkNotNull(withItems, "withItems cannot be null"));
            return this;
        }

        @Override
        public Builder putAllWithItems(@Nonnull Map<String, String> withItems) {
            checkNotBuilt();
            this.withItems.putAll(Preconditions.checkNotNull(withItems, "withItems cannot be null"));
            return this;
        }

        @Override
        public Builder withItems(String key, String value) {
            checkNotBuilt();
            this.withItems.put(key, value);
            return this;
        }

        @Override
        @CheckReturnValue
        public WithPrefixedMapFields build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new WithPrefixedMapFields(required, items, withItems);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
