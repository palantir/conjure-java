package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = StrictOneCollectionField.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class StrictOneCollectionField {
    private final List<String> myList;

    private int memoizedHashCode;

    private StrictOneCollectionField(List<String> myList) {
        validateFields(myList);
        this.myList = Collections.unmodifiableList(myList);
    }

    @JsonProperty("myList")
    public List<String> getMyList() {
        return this.myList;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other
                || (other instanceof StrictOneCollectionField && equalTo((StrictOneCollectionField) other));
    }

    private boolean equalTo(StrictOneCollectionField other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.myList.equals(other.myList);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = this.myList.hashCode();
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "StrictOneCollectionField{myList: " + myList + '}';
    }

    public static StrictOneCollectionField of(List<String> myList) {
        return builder().myList(myList).build();
    }

    private static void validateFields(List<String> myList) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, myList, "myList");
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

    public static MyListStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface MyListStageBuilder {
        Completed_StageBuilder myList(@Nonnull Iterable<String> myList);

        Builder from(StrictOneCollectionField other);
    }

    public interface Completed_StageBuilder {
        StrictOneCollectionField build();
    }

    public interface Builder extends MyListStageBuilder, Completed_StageBuilder {
        @Override
        Builder myList(@Nonnull Iterable<String> myList);

        @Override
        Builder from(StrictOneCollectionField other);

        @Override
        StrictOneCollectionField build();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private List<String> myList = ConjureCollections.newList();

        private DefaultBuilder() {}

        @Override
        public Builder from(StrictOneCollectionField other) {
            checkNotBuilt();
            myList(other.getMyList());
            return this;
        }

        @Override
        @JsonSetter(value = "myList", nulls = Nulls.SKIP)
        public Builder myList(@Nonnull Iterable<String> myList) {
            checkNotBuilt();
            this.myList = ConjureCollections.newList(Preconditions.checkNotNull(myList, "myList cannot be null"));
            return this;
        }

        @Override
        public StrictOneCollectionField build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new StrictOneCollectionField(myList);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
