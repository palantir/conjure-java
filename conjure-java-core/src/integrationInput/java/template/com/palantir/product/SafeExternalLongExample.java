package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = SafeExternalLongExample.DefaultBuilder.class)
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
        this.safeExternalLongList = ConjureCollections.unmodifiableList(safeExternalLongList);
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

    public static SafeExternalLongValueStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface SafeExternalLongValueStageBuilder {
        Completed_StageBuilder safeExternalLongValue(@Nonnull @Safe long safeExternalLongValue);

        Builder from(SafeExternalLongExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        SafeExternalLongExample build();

        Completed_StageBuilder optionalSafeExternalLong(
                @Nonnull Optional<? extends @Safe Long> optionalSafeExternalLong);

        Completed_StageBuilder optionalSafeExternalLong(@Safe long optionalSafeExternalLong);

        Completed_StageBuilder safeExternalLongList(@Nonnull Iterable<? extends @Safe Long> safeExternalLongList);

        Completed_StageBuilder addAllSafeExternalLongList(@Nonnull Iterable<? extends @Safe Long> safeExternalLongList);

        Completed_StageBuilder safeExternalLongList(@Safe long safeExternalLongList);

        Completed_StageBuilder safeExternalLongSet(@Nonnull Iterable<? extends @Safe Long> safeExternalLongSet);

        Completed_StageBuilder addAllSafeExternalLongSet(@Nonnull Iterable<? extends @Safe Long> safeExternalLongSet);

        Completed_StageBuilder safeExternalLongSet(@Safe long safeExternalLongSet);
    }

    public interface Builder extends SafeExternalLongValueStageBuilder, Completed_StageBuilder {
        @Override
        Builder safeExternalLongValue(@Nonnull @Safe long safeExternalLongValue);

        @Override
        Builder from(SafeExternalLongExample other);

        @CheckReturnValue
        @Override
        SafeExternalLongExample build();

        @Override
        Builder optionalSafeExternalLong(@Nonnull Optional<? extends @Safe Long> optionalSafeExternalLong);

        @Override
        Builder optionalSafeExternalLong(@Safe long optionalSafeExternalLong);

        @Override
        Builder safeExternalLongList(@Nonnull Iterable<? extends @Safe Long> safeExternalLongList);

        @Override
        Builder addAllSafeExternalLongList(@Nonnull Iterable<? extends @Safe Long> safeExternalLongList);

        @Override
        Builder safeExternalLongList(@Safe long safeExternalLongList);

        @Override
        Builder safeExternalLongSet(@Nonnull Iterable<? extends @Safe Long> safeExternalLongSet);

        @Override
        Builder addAllSafeExternalLongSet(@Nonnull Iterable<? extends @Safe Long> safeExternalLongSet);

        @Override
        Builder safeExternalLongSet(@Safe long safeExternalLongSet);
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private @Safe long safeExternalLongValue;

        private Optional<@Safe Long> optionalSafeExternalLong = Optional.empty();

        private List<@Safe Long> safeExternalLongList = ConjureCollections.newNonNullList();

        private Set<@Safe Long> safeExternalLongSet = ConjureCollections.newNonNullSet();

        private boolean _safeExternalLongValueInitialized = false;

        private DefaultBuilder() {}

        @Override
        public Builder from(SafeExternalLongExample other) {
            checkNotBuilt();
            safeExternalLongValue(other.getSafeExternalLongValue());
            optionalSafeExternalLong(other.getOptionalSafeExternalLong());
            safeExternalLongList(other.getSafeExternalLongList());
            safeExternalLongSet(other.getSafeExternalLongSet());
            return this;
        }

        @Override
        @JsonSetter("safeExternalLongValue")
        public Builder safeExternalLongValue(@Safe long safeExternalLongValue) {
            checkNotBuilt();
            this.safeExternalLongValue = safeExternalLongValue;
            this._safeExternalLongValueInitialized = true;
            return this;
        }

        @Override
        @JsonSetter(value = "optionalSafeExternalLong", nulls = Nulls.SKIP)
        public Builder optionalSafeExternalLong(@Nonnull Optional<? extends @Safe Long> optionalSafeExternalLong) {
            checkNotBuilt();
            this.optionalSafeExternalLong = Preconditions.checkNotNull(
                            optionalSafeExternalLong, "optionalSafeExternalLong cannot be null")
                    .map(Function.identity());
            return this;
        }

        @Override
        public Builder optionalSafeExternalLong(@Safe long optionalSafeExternalLong) {
            checkNotBuilt();
            this.optionalSafeExternalLong = Optional.of(
                    Preconditions.checkNotNull(optionalSafeExternalLong, "optionalSafeExternalLong cannot be null"));
            return this;
        }

        @Override
        @JsonSetter(value = "safeExternalLongList", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder safeExternalLongList(@Nonnull Iterable<? extends @Safe Long> safeExternalLongList) {
            checkNotBuilt();
            this.safeExternalLongList = ConjureCollections.newNonNullList(
                    Preconditions.checkNotNull(safeExternalLongList, "safeExternalLongList cannot be null"));
            return this;
        }

        @Override
        public Builder addAllSafeExternalLongList(@Nonnull Iterable<? extends @Safe Long> safeExternalLongList) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.safeExternalLongList,
                    Preconditions.checkNotNull(safeExternalLongList, "safeExternalLongList cannot be null"));
            return this;
        }

        @Override
        public Builder safeExternalLongList(@Safe long safeExternalLongList) {
            checkNotBuilt();
            Preconditions.checkNotNull(safeExternalLongList, "safeExternalLongList cannot be null");
            this.safeExternalLongList.add(safeExternalLongList);
            return this;
        }

        @Override
        @JsonSetter(value = "safeExternalLongSet", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder safeExternalLongSet(@Nonnull Iterable<? extends @Safe Long> safeExternalLongSet) {
            checkNotBuilt();
            this.safeExternalLongSet = ConjureCollections.newNonNullSet(
                    Preconditions.checkNotNull(safeExternalLongSet, "safeExternalLongSet cannot be null"));
            return this;
        }

        @Override
        public Builder addAllSafeExternalLongSet(@Nonnull Iterable<? extends @Safe Long> safeExternalLongSet) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.safeExternalLongSet,
                    Preconditions.checkNotNull(safeExternalLongSet, "safeExternalLongSet cannot be null"));
            return this;
        }

        @Override
        public Builder safeExternalLongSet(@Safe long safeExternalLongSet) {
            checkNotBuilt();
            Preconditions.checkNotNull(safeExternalLongSet, "safeExternalLongSet cannot be null");
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

        @Override
        @CheckReturnValue
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
