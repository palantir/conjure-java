package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.ri.ResourceIdentifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = MultipleOrderedStages.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class MultipleOrderedStages {
    private final OneField token;

    private final String item;

    private final Set<SafeLong> items;

    private final Map<ResourceIdentifier, String> mappedRids;

    private int memoizedHashCode;

    private MultipleOrderedStages(
            OneField token, String item, Set<SafeLong> items, Map<ResourceIdentifier, String> mappedRids) {
        validateFields(token, item, items, mappedRids);
        this.token = token;
        this.item = item;
        this.items = Collections.unmodifiableSet(items);
        this.mappedRids = Collections.unmodifiableMap(mappedRids);
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

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof MultipleOrderedStages && equalTo((MultipleOrderedStages) other));
    }

    private boolean equalTo(MultipleOrderedStages other) {
        return this.token.equals(other.token)
                && this.item.equals(other.item)
                && this.items.equals(other.items)
                && this.mappedRids.equals(other.mappedRids);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(this.token, this.item, this.items, this.mappedRids);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "MultipleOrderedStages{token: " + token + ", item: " + item + ", items: " + items + ", mappedRids: "
                + mappedRids + '}';
    }

    private static void validateFields(
            OneField token, String item, Set<SafeLong> items, Map<ResourceIdentifier, String> mappedRids) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, token, "token");
        missingFields = addFieldIfMissing(missingFields, item, "item");
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, mappedRids, "mappedRids");
        if (missingFields != null) {
            throw new ServiceException(
                    ErrorType.create(ErrorType.Code.INVALID_ARGUMENT, "Error:MissingField"),
                    SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(4);
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

        @Override
        Builder token(@Nonnull OneField token);

        @Override
        Builder mappedRids(ResourceIdentifier key, String value);

        @Override
        Builder item(@Nonnull String item);
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        private OneField token;

        private String item;

        private Set<SafeLong> items = new LinkedHashSet<>();

        private Map<ResourceIdentifier, String> mappedRids = new LinkedHashMap<>();

        private DefaultBuilder() {}

        public Builder from(MultipleOrderedStages other) {
            token(other.getToken());
            item(other.getItem());
            items(other.getItems());
            mappedRids(other.getMappedRids());
            return this;
        }

        @JsonSetter("token")
        public Builder token(@Nonnull OneField token) {
            this.token = Preconditions.checkNotNull(token, "token cannot be null");
            return this;
        }

        @JsonSetter("item")
        public Builder item(@Nonnull String item) {
            this.item = Preconditions.checkNotNull(item, "item cannot be null");
            return this;
        }

        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        public Builder items(@Nonnull Iterable<SafeLong> items) {
            this.items.clear();
            ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder addAllItems(@Nonnull Iterable<SafeLong> items) {
            ConjureCollections.addAll(this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        public Builder items(SafeLong items) {
            this.items.add(items);
            return this;
        }

        @JsonSetter(value = "mappedRids", nulls = Nulls.SKIP)
        public Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids) {
            this.mappedRids.clear();
            this.mappedRids.putAll(Preconditions.checkNotNull(mappedRids, "mappedRids cannot be null"));
            return this;
        }

        public Builder putAllMappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids) {
            this.mappedRids.putAll(Preconditions.checkNotNull(mappedRids, "mappedRids cannot be null"));
            return this;
        }

        public Builder mappedRids(ResourceIdentifier key, String value) {
            this.mappedRids.put(key, value);
            return this;
        }

        public MultipleOrderedStages build() {
            return new MultipleOrderedStages(token, item, items, mappedRids);
        }
    }
}
