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
import com.palantir.tokens.auth.BearerToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@DoNotLog
@JsonDeserialize(builder = StrictThreeFields.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StrictThreeFields {
    private final List<String> myList;

    private final BearerToken bearerTokenValue;

    private final Optional<String> optionalItem;

    private int memoizedHashCode;

    private StrictThreeFields(List<String> myList, BearerToken bearerTokenValue, Optional<String> optionalItem) {
        validateFields(myList, bearerTokenValue, optionalItem);
        this.myList = Collections.unmodifiableList(myList);
        this.bearerTokenValue = bearerTokenValue;
        this.optionalItem = optionalItem;
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

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof StrictThreeFields && equalTo((StrictThreeFields) other));
    }

    private boolean equalTo(StrictThreeFields other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.myList.equals(other.myList)
                && this.bearerTokenValue.equals(other.bearerTokenValue)
                && this.optionalItem.equals(other.optionalItem);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.myList.hashCode();
            hash = 31 * hash + this.bearerTokenValue.hashCode();
            hash = 31 * hash + this.optionalItem.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @DoNotLog
    public String toString() {
        return "StrictThreeFields{myList: " + myList + ", bearerTokenValue: " + bearerTokenValue + ", optionalItem: "
                + optionalItem + '}';
    }

    public static StrictThreeFields of(List<String> myList, BearerToken bearerTokenValue, String optionalItem) {
        return builder()
                .myList(myList)
                .bearerTokenValue(bearerTokenValue)
                .optionalItem(Optional.of(optionalItem))
                .build();
    }

    private static void validateFields(
            List<String> myList, BearerToken bearerTokenValue, Optional<String> optionalItem) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, myList, "myList");
        missingFields = addFieldIfMissing(missingFields, bearerTokenValue, "bearerTokenValue");
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
                missingFields = new ArrayList<>(3);
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

        Builder from(StrictThreeFields other);
    }

    public interface BearerTokenValueStageBuilder {
        OptionalItemStageBuilder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);
    }

    public interface OptionalItemStageBuilder {
        Completed_StageBuilder optionalItem(@Nonnull Optional<String> optionalItem);

        Completed_StageBuilder optionalItem(@Nonnull String optionalItem);
    }

    public interface Completed_StageBuilder {
        StrictThreeFields build();

        Completed_StageBuilder addAllMyList(@Nonnull Iterable<String> myList);

        Completed_StageBuilder myList(String myList);
    }

    public interface Builder
            extends MyListStageBuilder, BearerTokenValueStageBuilder, OptionalItemStageBuilder, Completed_StageBuilder {
        @Override
        Builder myList(@Nonnull Iterable<String> myList);

        @Override
        Builder from(StrictThreeFields other);

        @Override
        Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);

        @Override
        Builder optionalItem(@Nonnull Optional<String> optionalItem);

        @Override
        Builder optionalItem(@Nonnull String optionalItem);

        @Override
        StrictThreeFields build();

        @Override
        Builder addAllMyList(@Nonnull Iterable<String> myList);

        @Override
        Builder myList(String myList);
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private List<String> myList = new ArrayList<>();

        private BearerToken bearerTokenValue;

        private Optional<String> optionalItem = Optional.empty();

        private DefaultBuilder() {}

        @Override
        public Builder from(StrictThreeFields other) {
            checkNotBuilt();
            myList(other.getMyList());
            bearerTokenValue(other.getBearerTokenValue());
            optionalItem(other.getOptionalItem());
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
        public Builder addAllMyList(@Nonnull Iterable<String> myList) {
            checkNotBuilt();
            ConjureCollections.addAll(this.myList, Preconditions.checkNotNull(myList, "myList cannot be null"));
            return this;
        }

        @Override
        public Builder myList(String myList) {
            checkNotBuilt();
            this.myList.add(myList);
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
        public StrictThreeFields build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new StrictThreeFields(myList, bearerTokenValue, optionalItem);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
