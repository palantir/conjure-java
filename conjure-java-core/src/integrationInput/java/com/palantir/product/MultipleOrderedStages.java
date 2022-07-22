package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@DoNotLog
@JsonDeserialize(builder = MultipleOrderedStages.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MultipleOrderedStages {
    private final OneField token;

    private final String item;

    private final Set<SafeLong> items;

    private final Map<ResourceIdentifier, String> mappedRids;

    private final Optional<OneField> optionalItem;

    private int memoizedHashCode;

    private MultipleOrderedStages(
            OneField token,
            String item,
            Set<SafeLong> items,
            Map<ResourceIdentifier, String> mappedRids,
            Optional<OneField> optionalItem) {
        validateFields(token, item, items, mappedRids, optionalItem);
        this.token = token;
        this.item = item;
        this.items = Collections.unmodifiableSet(items);
        this.mappedRids = Collections.unmodifiableMap(mappedRids);
        this.optionalItem = optionalItem;
    }

    @JsonProperty("token")
    public OneField getToken() {
        return this.token;
    }

    @JsonProperty("item")
    public String getItem() {
        return this.item;
    }

    @JsonProperty("items")
    public Set<SafeLong> getItems() {
        return this.items;
    }

    @JsonProperty("mappedRids")
    public Map<ResourceIdentifier, String> getMappedRids() {
        return this.mappedRids;
    }

    /**
     * @deprecated this optional is deprecated
     */
    @JsonProperty("optionalItem")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    @Deprecated
    public Optional<OneField> getOptionalItem() {
        return this.optionalItem;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof MultipleOrderedStages && equalTo((MultipleOrderedStages) other));
    }

    private boolean equalTo(MultipleOrderedStages other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.token.equals(other.token)
                && this.item.equals(other.item)
                && this.items.equals(other.items)
                && this.mappedRids.equals(other.mappedRids)
                && this.optionalItem.equals(other.optionalItem);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.token.hashCode();
            hash = 31 * hash + this.item.hashCode();
            hash = 31 * hash + this.items.hashCode();
            hash = 31 * hash + this.mappedRids.hashCode();
            hash = 31 * hash + this.optionalItem.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "MultipleOrderedStages{token: " + token + ", item: " + item + ", items: " + items + ", mappedRids: "
                + mappedRids + ", optionalItem: " + optionalItem + '}';
    }

    private static void validateFields(
            OneField token,
            String item,
            Set<SafeLong> items,
            Map<ResourceIdentifier, String> mappedRids,
            Optional<OneField> optionalItem) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, token, "token");
        missingFields = addFieldIfMissing(missingFields, item, "item");
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, mappedRids, "mappedRids");
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
                missingFields = new ArrayList<>(5);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static TokenStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface TokenStageBuilder {
        ItemStageBuilder token(@Nonnull OneField token);

        Builder from(MultipleOrderedStages other);
    }

    public interface ItemStageBuilder {
        Completed_StageBuilder item(@Nonnull String item);
    }

    public interface Completed_StageBuilder {
        MultipleOrderedStages build();

        Completed_StageBuilder items(@Nonnull Iterable<SafeLong> items);

        Completed_StageBuilder addAllItems(@Nonnull Iterable<SafeLong> items);

        Completed_StageBuilder items(SafeLong items);

        Completed_StageBuilder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        Completed_StageBuilder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        Completed_StageBuilder mappedRids(ResourceIdentifier key, String value);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        Completed_StageBuilder optionalItem(@Nonnull Optional<OneField> optionalItem);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        Completed_StageBuilder optionalItem(@Nonnull OneField optionalItem);
    }

    public interface Builder extends TokenStageBuilder, ItemStageBuilder, Completed_StageBuilder {
        @Override
        MultipleOrderedStages build();

        @Override
        Builder items(@Nonnull Iterable<SafeLong> items);

        @Override
        Builder items(SafeLong items);

        @Override
        Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        @Override
        Builder from(MultipleOrderedStages other);

        @Override
        Builder addAllItems(@Nonnull Iterable<SafeLong> items);

        @Override
        Builder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        Builder optionalItem(@Nonnull Optional<OneField> optionalItem);

        @Override
        Builder token(@Nonnull OneField token);

        @Override
        Builder mappedRids(ResourceIdentifier key, String value);

        @Override
        Builder item(@Nonnull String item);

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        Builder optionalItem(@Nonnull OneField optionalItem);
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private OneField token;

        private String item;

        private Set<SafeLong> items = new LinkedHashSet<>();

        private Map<ResourceIdentifier, String> mappedRids = new LinkedHashMap<>();

        private Optional<OneField> optionalItem = Optional.empty();

        private DefaultBuilder() {}

        @Override
        public Builder from(MultipleOrderedStages other) {
            checkNotBuilt();
            token(other.getToken());
            item(other.getItem());
            items(other.getItems());
            mappedRids(other.getMappedRids());
            optionalItem(other.getOptionalItem());
            return this;
        }

        @Override
        @JsonSetter("token")
        public Builder token(@Nonnull OneField token) {
            checkNotBuilt();
            this.token = Preconditions.checkNotNull(token, "token cannot be null");
            return this;
        }

        @Override
        @JsonSetter("item")
        public Builder item(@Nonnull String item) {
            checkNotBuilt();
            this.item = Preconditions.checkNotNull(item, "item cannot be null");
            return this;
        }

        @Override
        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        public Builder items(@Nonnull Iterable<SafeLong> items) {
            checkNotBuilt();
            if (items instanceof Collection) {
                this.items =
                        new LinkedHashSet<>((Collection) Preconditions.checkNotNull(items, "items cannot be null"));
            } else {
                this.items.clear();
                ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            }
            return this;
        }

        @Override
        public Builder addAllItems(@Nonnull Iterable<SafeLong> items) {
            checkNotBuilt();
            ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        @Override
        public Builder items(SafeLong items) {
            checkNotBuilt();
            this.items.add(items);
            return this;
        }

        @Override
        @JsonSetter(value = "mappedRids", nulls = Nulls.SKIP)
        public Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids) {
            checkNotBuilt();
            this.mappedRids = new LinkedHashMap<>(Preconditions.checkNotNull(mappedRids, "mappedRids cannot be null"));
            return this;
        }

        @Override
        public Builder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids) {
            checkNotBuilt();
            this.mappedRids.putAll(Preconditions.checkNotNull(mappedRids, "mappedRids cannot be null"));
            return this;
        }

        @Override
        public Builder mappedRids(ResourceIdentifier key, String value) {
            checkNotBuilt();
            this.mappedRids.put(key, value);
            return this;
        }

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<OneField> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        /**
         * @deprecated this optional is deprecated
         */
        @Deprecated
        @Override
        public Builder optionalItem(@Nonnull OneField optionalItem) {
            checkNotBuilt();
            this.optionalItem = Optional.of(Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null"));
            return this;
        }

        @Override
        public MultipleOrderedStages build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new MultipleOrderedStages(token, item, items, mappedRids, optionalItem);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
