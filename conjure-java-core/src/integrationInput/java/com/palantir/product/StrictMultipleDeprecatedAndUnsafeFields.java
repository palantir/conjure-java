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
import com.palantir.logsafe.Unsafe;
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
@JsonDeserialize(builder = StrictMultipleDeprecatedAndUnsafeFields.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StrictMultipleDeprecatedAndUnsafeFields {
    private final List<String> myList;

    private final BearerToken bearerTokenValue;

    private final SafeLong safeLongValue;

    private final int myInteger;

    private final Optional<String> optionalItem;

    private final Map<ResourceIdentifier, String> mappedRids;

    private final StrictFourFields strictFourFieldsObject;

    private int memoizedHashCode;

    private StrictMultipleDeprecatedAndUnsafeFields(
            List<String> myList,
            BearerToken bearerTokenValue,
            SafeLong safeLongValue,
            int myInteger,
            Optional<String> optionalItem,
            Map<ResourceIdentifier, String> mappedRids,
            StrictFourFields strictFourFieldsObject) {
        validateFields(myList, bearerTokenValue, safeLongValue, optionalItem, mappedRids, strictFourFieldsObject);
        this.myList = Collections.unmodifiableList(myList);
        this.bearerTokenValue = bearerTokenValue;
        this.safeLongValue = safeLongValue;
        this.myInteger = myInteger;
        this.optionalItem = optionalItem;
        this.mappedRids = Collections.unmodifiableMap(mappedRids);
        this.strictFourFieldsObject = strictFourFieldsObject;
    }

    /**
     * these are docs.
     *
     * @deprecated this is deprecated.
     */
    @JsonProperty("myList")
    @Unsafe
    @Deprecated
    public List<String> getMyList() {
        return this.myList;
    }

    /** @deprecated this is deprecated. */
    @JsonProperty("bearerTokenValue")
    @Deprecated
    public BearerToken getBearerTokenValue() {
        return this.bearerTokenValue;
    }

    @JsonProperty("safeLongValue")
    @DoNotLog
    public SafeLong getSafeLongValue() {
        return this.safeLongValue;
    }

    @JsonProperty("myInteger")
    public int getMyInteger() {
        return this.myInteger;
    }

    @JsonProperty("optionalItem")
    @Unsafe
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getOptionalItem() {
        return this.optionalItem;
    }

    @JsonProperty("mappedRids")
    public Map<ResourceIdentifier, String> getMappedRids() {
        return this.mappedRids;
    }

    @JsonProperty("strictFourFieldsObject")
    public StrictFourFields getStrictFourFieldsObject() {
        return this.strictFourFieldsObject;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof StrictMultipleDeprecatedAndUnsafeFields
                        && equalTo((StrictMultipleDeprecatedAndUnsafeFields) other));
    }

    private boolean equalTo(StrictMultipleDeprecatedAndUnsafeFields other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.myList.equals(other.myList)
                && this.bearerTokenValue.equals(other.bearerTokenValue)
                && this.safeLongValue.equals(other.safeLongValue)
                && this.myInteger == other.myInteger
                && this.optionalItem.equals(other.optionalItem)
                && this.mappedRids.equals(other.mappedRids)
                && this.strictFourFieldsObject.equals(other.strictFourFieldsObject);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.myList.hashCode();
            hash = 31 * hash + this.bearerTokenValue.hashCode();
            hash = 31 * hash + this.safeLongValue.hashCode();
            hash = 31 * hash + this.myInteger;
            hash = 31 * hash + this.optionalItem.hashCode();
            hash = 31 * hash + this.mappedRids.hashCode();
            hash = 31 * hash + this.strictFourFieldsObject.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @DoNotLog
    public String toString() {
        return "StrictMultipleDeprecatedAndUnsafeFields{myList: " + myList + ", bearerTokenValue: " + bearerTokenValue
                + ", safeLongValue: " + safeLongValue + ", myInteger: " + myInteger + ", optionalItem: " + optionalItem
                + ", mappedRids: " + mappedRids + ", strictFourFieldsObject: " + strictFourFieldsObject + '}';
    }

    private static void validateFields(
            List<String> myList,
            BearerToken bearerTokenValue,
            SafeLong safeLongValue,
            Optional<String> optionalItem,
            Map<ResourceIdentifier, String> mappedRids,
            StrictFourFields strictFourFieldsObject) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, myList, "myList");
        missingFields = addFieldIfMissing(missingFields, bearerTokenValue, "bearerTokenValue");
        missingFields = addFieldIfMissing(missingFields, safeLongValue, "safeLongValue");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        missingFields = addFieldIfMissing(missingFields, mappedRids, "mappedRids");
        missingFields = addFieldIfMissing(missingFields, strictFourFieldsObject, "strictFourFieldsObject");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(6);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static MyListStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface MyListStageBuilder {
        BearerTokenValueStageBuilder myList(@Nonnull Iterable<@Unsafe String> myList);

        Builder from(StrictMultipleDeprecatedAndUnsafeFields other);
    }

    public interface BearerTokenValueStageBuilder {
        SafeLongValueStageBuilder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);
    }

    public interface SafeLongValueStageBuilder {
        MyIntegerStageBuilder safeLongValue(@Nonnull @DoNotLog SafeLong safeLongValue);
    }

    public interface MyIntegerStageBuilder {
        OptionalItemStageBuilder myInteger(@Nonnull int myInteger);
    }

    public interface OptionalItemStageBuilder {
        MappedRidsStageBuilder optionalItem(@Nonnull Optional<@Unsafe String> optionalItem);

        MappedRidsStageBuilder optionalItem(@Nonnull @Unsafe String optionalItem);
    }

    public interface MappedRidsStageBuilder {
        StrictFourFieldsObjectStageBuilder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);
    }

    public interface StrictFourFieldsObjectStageBuilder {
        Completed_StageBuilder strictFourFieldsObject(@Nonnull StrictFourFields strictFourFieldsObject);
    }

    public interface Completed_StageBuilder {
        StrictMultipleDeprecatedAndUnsafeFields build();
    }

    public interface Builder
            extends MyListStageBuilder,
                    BearerTokenValueStageBuilder,
                    SafeLongValueStageBuilder,
                    MyIntegerStageBuilder,
                    OptionalItemStageBuilder,
                    MappedRidsStageBuilder,
                    StrictFourFieldsObjectStageBuilder,
                    Completed_StageBuilder {
        @Override
        Builder myList(@Nonnull Iterable<@Unsafe String> myList);

        @Override
        Builder from(StrictMultipleDeprecatedAndUnsafeFields other);

        @Override
        Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue);

        @Override
        Builder safeLongValue(@Nonnull @DoNotLog SafeLong safeLongValue);

        @Override
        Builder myInteger(@Nonnull int myInteger);

        @Override
        Builder optionalItem(@Nonnull Optional<@Unsafe String> optionalItem);

        @Override
        Builder optionalItem(@Nonnull @Unsafe String optionalItem);

        @Override
        Builder mappedRids(@Nonnull Map<ResourceIdentifier, String> mappedRids);

        @Override
        Builder strictFourFieldsObject(@Nonnull StrictFourFields strictFourFieldsObject);

        @Override
        StrictMultipleDeprecatedAndUnsafeFields build();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private List<@Unsafe String> myList = new ArrayList<>();

        private BearerToken bearerTokenValue;

        private @DoNotLog SafeLong safeLongValue;

        private int myInteger;

        private Optional<@Unsafe String> optionalItem = Optional.empty();

        private Map<ResourceIdentifier, String> mappedRids = new LinkedHashMap<>();

        private StrictFourFields strictFourFieldsObject;

        private boolean _myIntegerInitialized = false;

        private DefaultBuilder() {}

        @Override
        public Builder from(StrictMultipleDeprecatedAndUnsafeFields other) {
            checkNotBuilt();
            myList(other.getMyList());
            bearerTokenValue(other.getBearerTokenValue());
            safeLongValue(other.getSafeLongValue());
            myInteger(other.getMyInteger());
            optionalItem(other.getOptionalItem());
            mappedRids(other.getMappedRids());
            strictFourFieldsObject(other.getStrictFourFieldsObject());
            return this;
        }

        /**
         * these are docs.
         *
         * @deprecated this is deprecated.
         */
        @Deprecated
        @Override
        @JsonSetter(value = "myList", nulls = Nulls.SKIP)
        public Builder myList(@Nonnull Iterable<@Unsafe String> myList) {
            checkNotBuilt();
            this.myList = ConjureCollections.newArrayList(Preconditions.checkNotNull(myList, "myList cannot be null"));
            return this;
        }

        /** @deprecated this is deprecated. */
        @Deprecated
        @Override
        @JsonSetter("bearerTokenValue")
        public Builder bearerTokenValue(@Nonnull BearerToken bearerTokenValue) {
            checkNotBuilt();
            this.bearerTokenValue = Preconditions.checkNotNull(bearerTokenValue, "bearerTokenValue cannot be null");
            return this;
        }

        @Override
        @JsonSetter("safeLongValue")
        public Builder safeLongValue(@Nonnull @DoNotLog SafeLong safeLongValue) {
            checkNotBuilt();
            this.safeLongValue = Preconditions.checkNotNull(safeLongValue, "safeLongValue cannot be null");
            return this;
        }

        @Override
        @JsonSetter("myInteger")
        public Builder myInteger(int myInteger) {
            checkNotBuilt();
            this.myInteger = myInteger;
            this._myIntegerInitialized = true;
            return this;
        }

        @Override
        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(@Nonnull Optional<@Unsafe String> optionalItem) {
            checkNotBuilt();
            this.optionalItem = Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        @Override
        public Builder optionalItem(@Nonnull @Unsafe String optionalItem) {
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
        @JsonSetter("strictFourFieldsObject")
        public Builder strictFourFieldsObject(@Nonnull StrictFourFields strictFourFieldsObject) {
            checkNotBuilt();
            this.strictFourFieldsObject =
                    Preconditions.checkNotNull(strictFourFieldsObject, "strictFourFieldsObject cannot be null");
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _myIntegerInitialized, "myInteger");
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

        @Override
        public StrictMultipleDeprecatedAndUnsafeFields build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new StrictMultipleDeprecatedAndUnsafeFields(
                    myList,
                    bearerTokenValue,
                    safeLongValue,
                    myInteger,
                    optionalItem,
                    mappedRids,
                    strictFourFieldsObject);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
