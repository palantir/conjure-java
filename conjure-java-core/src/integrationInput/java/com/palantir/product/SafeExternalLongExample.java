package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = SafeExternalLongExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class SafeExternalLongExample {
    private final long safeExternalLongValue;

    private final Optional<Long> optionalSafeExternalLong;

    private final List<Long> safeExternalLongList;

    private final Set<Long> safeExternalLongSet;

    private int memoizedHashCode;

    private SafeExternalLongExample(
            long safeExternalLongValue,
            Optional<Long> optionalSafeExternalLong,
            List<Long> safeExternalLongList,
            Set<Long> safeExternalLongSet) {
        validateFields(optionalSafeExternalLong, safeExternalLongList, safeExternalLongSet);
        this.safeExternalLongValue = safeExternalLongValue;
        this.optionalSafeExternalLong = optionalSafeExternalLong;
        this.safeExternalLongList = Collections.unmodifiableList(safeExternalLongList);
        this.safeExternalLongSet = Collections.unmodifiableSet(safeExternalLongSet);
    }

    @JsonProperty("safeExternalLongValue")
    @Safe
    public long getSafeExternalLongValue() {
        return this.safeExternalLongValue;
    }

    @JsonProperty("optionalSafeExternalLong")
    @Safe
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Long> getOptionalSafeExternalLong() {
        return this.optionalSafeExternalLong;
    }

    @JsonProperty("safeExternalLongList")
    @Safe
    public List<Long> getSafeExternalLongList() {
        return this.safeExternalLongList;
    }

    @JsonProperty("safeExternalLongSet")
    @Safe
    public Set<Long> getSafeExternalLongSet() {
        return this.safeExternalLongSet;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeExternalLongExample && equalTo((SafeExternalLongExample) other));
    }

    private boolean equalTo(SafeExternalLongExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.safeExternalLongValue == other.safeExternalLongValue
                && this.optionalSafeExternalLong.equals(other.optionalSafeExternalLong)
                && this.safeExternalLongList.equals(other.safeExternalLongList)
                && this.safeExternalLongSet.equals(other.safeExternalLongSet);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + Long.hashCode(this.safeExternalLongValue);
            hash = 31 * hash + this.optionalSafeExternalLong.hashCode();
            hash = 31 * hash + this.safeExternalLongList.hashCode();
            hash = 31 * hash + this.safeExternalLongSet.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @Safe
    public String toString() {
        return "SafeExternalLongExample{safeExternalLongValue: " + safeExternalLongValue
                + ", optionalSafeExternalLong: " + optionalSafeExternalLong + ", safeExternalLongList: "
                + safeExternalLongList + ", safeExternalLongSet: " + safeExternalLongSet + '}';
    }

    private static void validateFields(
            Optional<Long> optionalSafeExternalLong, List<Long> safeExternalLongList, Set<Long> safeExternalLongSet) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalSafeExternalLong, "optionalSafeExternalLong");
        missingFields = addFieldIfMissing(missingFields, safeExternalLongList, "safeExternalLongList");
        missingFields = addFieldIfMissing(missingFields, safeExternalLongSet, "safeExternalLongSet");
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

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    public static final class Builder {
        boolean _buildInvoked;

        private long safeExternalLongValue;

        private Optional<@Safe Long> optionalSafeExternalLong = Optional.empty();

        private List<@Safe Long> safeExternalLongList = new ArrayList<>();

        private Set<@Safe Long> safeExternalLongSet = new LinkedHashSet<>();

        private boolean _safeExternalLongValueInitialized = false;

        private Builder() {}

        public Builder from(SafeExternalLongExample other) {
            checkNotBuilt();
            safeExternalLongValue(other.getSafeExternalLongValue());
            optionalSafeExternalLong(other.getOptionalSafeExternalLong());
            safeExternalLongList(other.getSafeExternalLongList());
            safeExternalLongSet(other.getSafeExternalLongSet());
            return this;
        }

        @JsonSetter("safeExternalLongValue")
        public Builder safeExternalLongValue(@Safe long safeExternalLongValue) {
            checkNotBuilt();
            this.safeExternalLongValue = safeExternalLongValue;
            this._safeExternalLongValueInitialized = true;
            return this;
        }

        @JsonSetter(value = "optionalSafeExternalLong", nulls = Nulls.SKIP)
        public Builder optionalSafeExternalLong(@Safe @Nonnull Optional<? extends Long> optionalSafeExternalLong) {
            checkNotBuilt();
            this.optionalSafeExternalLong = Preconditions.checkNotNull(
                            optionalSafeExternalLong, "optionalSafeExternalLong cannot be null")
                    .map(Function.identity());
            return this;
        }

        public Builder optionalSafeExternalLong(@Safe long optionalSafeExternalLong) {
            checkNotBuilt();
            this.optionalSafeExternalLong = Optional.of(
                    Preconditions.checkNotNull(optionalSafeExternalLong, "optionalSafeExternalLong cannot be null"));
            return this;
        }

        @JsonSetter(value = "safeExternalLongList", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder safeExternalLongList(@Safe @Nonnull Iterable<? extends Long> safeExternalLongList) {
            checkNotBuilt();
            this.safeExternalLongList = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(safeExternalLongList, "safeExternalLongList cannot be null"));
            return this;
        }

        public Builder addAllSafeExternalLongList(@Safe @Nonnull Iterable<? extends Long> safeExternalLongList) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.safeExternalLongList,
                    Preconditions.checkNotNull(safeExternalLongList, "safeExternalLongList cannot be null"));
            return this;
        }

        public Builder safeExternalLongList(@Safe long safeExternalLongList) {
            checkNotBuilt();
            this.safeExternalLongList.add(safeExternalLongList);
            return this;
        }

        @JsonSetter(value = "safeExternalLongSet", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder safeExternalLongSet(@Safe @Nonnull Iterable<? extends Long> safeExternalLongSet) {
            checkNotBuilt();
            this.safeExternalLongSet = ConjureCollections.newLinkedHashSet(
                    Preconditions.checkNotNull(safeExternalLongSet, "safeExternalLongSet cannot be null"));
            return this;
        }

        public Builder addAllSafeExternalLongSet(@Safe @Nonnull Iterable<? extends Long> safeExternalLongSet) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.safeExternalLongSet,
                    Preconditions.checkNotNull(safeExternalLongSet, "safeExternalLongSet cannot be null"));
            return this;
        }

        public Builder safeExternalLongSet(@Safe long safeExternalLongSet) {
            checkNotBuilt();
            this.safeExternalLongSet.add(safeExternalLongSet);
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields =
                    addFieldIfMissing(missingFields, _safeExternalLongValueInitialized, "safeExternalLongValue");
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

        public SafeExternalLongExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new SafeExternalLongExample(
                    safeExternalLongValue, optionalSafeExternalLong, safeExternalLongList, safeExternalLongSet);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
