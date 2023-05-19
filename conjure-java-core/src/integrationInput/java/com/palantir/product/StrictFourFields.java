package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.DoNotLog;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@JsonDeserialize(builder = StrictFourFields.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StrictFourFields {
    private final List<String> myList;

    private final BearerToken bearerTokenValue;

    private final Optional<String> optionalItem;

    private final Map<ResourceIdentifier, String> mappedRids;

    private volatile int memoizedHashCode;

    private StrictFourFields(
            List<String> myList,
            BearerToken bearerTokenValue,
            Optional<String> optionalItem,
            Map<ResourceIdentifier, String> mappedRids) {
        validateFields(myList, bearerTokenValue, optionalItem, mappedRids);
        this.myList = Collections.unmodifiableList(myList);
        this.bearerTokenValue = bearerTokenValue;
        this.optionalItem = optionalItem;
        this.mappedRids = Collections.unmodifiableMap(mappedRids);
    }

    @JsonProperty("myList")
    public List<String> getMyList() {
        return this.myList;
    }

    @JsonProperty("bearerTokenValue")
    public BearerToken getBearerTokenValue() {
        return this.bearerTokenValue;
    }

    @JsonProperty("optionalItem")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getOptionalItem() {
        return this.optionalItem;
    }

    @JsonProperty("mappedRids")
    public Map<ResourceIdentifier, String> getMappedRids() {
        return this.mappedRids;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof StrictFourFields && equalTo((StrictFourFields) other));
    }

    private boolean equalTo(StrictFourFields other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.myList.equals(other.myList)
                && this.bearerTokenValue.equals(other.bearerTokenValue)
                && this.optionalItem.equals(other.optionalItem)
                && this.mappedRids.equals(other.mappedRids);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.myList.hashCode();
            hash = 31 * hash + this.bearerTokenValue.hashCode();
            hash = 31 * hash + this.optionalItem.hashCode();
            hash = 31 * hash + this.mappedRids.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @DoNotLog
    public String toString() {
        return "StrictFourFields{myList: " + myList + ", bearerTokenValue: " + bearerTokenValue + ", optionalItem: "
                + optionalItem + ", mappedRids: " + mappedRids + '}';
    }

    private static void validateFields(
            List<String> myList,
            BearerToken bearerTokenValue,
            Optional<String> optionalItem,
            Map<ResourceIdentifier, String> mappedRids) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, myList, "myList");
        missingFields = addFieldIfMissing(missingFields, bearerTokenValue, "bearerTokenValue");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        missingFields = addFieldIfMissing(missingFields, mappedRids, "mappedRids");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
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

    public static MyListStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface MyListStageBuilder {
        BearerTokenValueStageBuilder myList(@Nonnull Iterable<String> myList);

        Builder from(StrictFourFields other);
    }

    public interface BearerTokenValueStageBuilder {
        OptionalItemStageBuilder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);
    }

    public interface OptionalItemStageBuilder {
        MappedRidsStageBuilder optionalItem(@Nonnull Optional<String> optionalItem);

        MappedRidsStageBuilder optionalItem(@Nonnull String optionalItem);
    }

    public interface MappedRidsStageBuilder {
        Completed_StageBuilder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);
    }

    public interface Completed_StageBuilder {
        StrictFourFields build();
    }

    public interface Builder
            extends MyListStageBuilder,
                    BearerTokenValueStageBuilder,
                    OptionalItemStageBuilder,
                    MappedRidsStageBuilder,
                    Completed_StageBuilder {
        @Override
        Builder myList(@Nonnull Iterable<String> myList);

        @Override
        Builder from(StrictFourFields other);

        @Override
        Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);

        @Override
        Builder optionalItem(@Nonnull Optional<String> optionalItem);

        @Override
        Builder optionalItem(@Nonnull String optionalItem);

        @Override
        Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        @Override
        StrictFourFields build();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private List<String> myList = new ArrayList<>();

        private BearerToken bearerTokenValue;

        private Optional<String> optionalItem = Optional.empty();

        private Map<ResourceIdentifier, String> mappedRids = new LinkedHashMap<>();

        private DefaultBuilder() {}

        @Override
        public Builder from(StrictFourFields other) {
            checkNotBuilt();
            myList(other.getMyList());
            bearerTokenValue(other.getBearerTokenValue());
            optionalItem(other.getOptionalItem());
            mappedRids(other.getMappedRids());
            return this;
        }

        @Override
        @JsonSetter(value = "myList", nulls = Nulls.SKIP)
        public Builder myList(@Nonnull Iterable<String> myList) {
            checkNotBuilt();
            this.myList = ConjureCollections.newArrayList(Preconditions.checkNotNull(myList, "myList cannot be null"));
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
        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<String> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        @Override
        public Builder optionalItem(@Nonnull String optionalItem) {
            checkNotBuilt();
            this.optionalItem = Optional.of(Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null"));
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
        public StrictFourFields build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new StrictFourFields(myList, bearerTokenValue, optionalItem, mappedRids);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
