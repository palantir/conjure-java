package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = SafeLongExample.DefaultBuilder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class SafeLongExample {
    private final SafeLong safeLongValue;

    private final List<SafeLong> safeLongList;

    private int memoizedHashCode;

    private SafeLongExample(SafeLong safeLongValue, List<SafeLong> safeLongList) {
        validateFields(safeLongValue, safeLongList);
        this.safeLongValue = safeLongValue;
        this.safeLongList = ConjureCollections.unmodifiableList(safeLongList);
    }

    @JsonProperty("safeLongValue")
    public SafeLong getSafeLongValue() {
        return this.safeLongValue;
    }

    @JsonProperty("safeLongList")
    public List<SafeLong> getSafeLongList() {
        return this.safeLongList;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof SafeLongExample && equalTo((SafeLongExample) other));
    }

    private boolean equalTo(SafeLongExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.safeLongValue.equals(other.safeLongValue) && this.safeLongList.equals(other.safeLongList);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.safeLongValue.hashCode();
            hash = 31 * hash + this.safeLongList.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "SafeLongExample{safeLongValue: " + safeLongValue + ", safeLongList: " + safeLongList + '}';
    }

    private static void validateFields(SafeLong safeLongValue, List<SafeLong> safeLongList) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, safeLongValue, "safeLongValue");
        missingFields = addFieldIfMissing(missingFields, safeLongList, "safeLongList");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(2);
            }
            missingFields.add(fieldName);
        }
        return missingFields;
    }

    public static SafeLongValueStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface SafeLongValueStageBuilder {
        Completed_StageBuilder safeLongValue(@Nonnull SafeLong safeLongValue);

        Builder from(SafeLongExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        SafeLongExample build();

        Completed_StageBuilder safeLongList(@Nonnull Iterable<SafeLong> safeLongList);

        Completed_StageBuilder addAllSafeLongList(@Nonnull Iterable<SafeLong> safeLongList);

        Completed_StageBuilder safeLongList(SafeLong safeLongList);
    }

    public interface Builder extends SafeLongValueStageBuilder, Completed_StageBuilder {
        @Override
        Builder safeLongValue(@Nonnull SafeLong safeLongValue);

        @Override
        Builder from(SafeLongExample other);

        @CheckReturnValue
        @Override
        SafeLongExample build();

        @Override
        Builder safeLongList(@Nonnull Iterable<SafeLong> safeLongList);

        @Override
        Builder addAllSafeLongList(@Nonnull Iterable<SafeLong> safeLongList);

        @Override
        Builder safeLongList(SafeLong safeLongList);
    }

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private SafeLong safeLongValue;

        private List<SafeLong> safeLongList = ConjureCollections.newNonNullSafeLongList();

        private DefaultBuilder() {}

        @Override
        public Builder from(SafeLongExample other) {
            checkNotBuilt();
            safeLongValue(other.getSafeLongValue());
            safeLongList(other.getSafeLongList());
            return this;
        }

        @Override
        @JsonSetter("safeLongValue")
        public Builder safeLongValue(@Nonnull SafeLong safeLongValue) {
            checkNotBuilt();
            this.safeLongValue = Preconditions.checkNotNull(safeLongValue, "safeLongValue cannot be null");
            return this;
        }

        @Override
        @JsonSetter(value = "safeLongList", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder safeLongList(@Nonnull Iterable<SafeLong> safeLongList) {
            checkNotBuilt();
            this.safeLongList = ConjureCollections.newNonNullSafeLongList(
                    Preconditions.checkNotNull(safeLongList, "safeLongList cannot be null"));
            return this;
        }

        @Override
        public Builder addAllSafeLongList(@Nonnull Iterable<SafeLong> safeLongList) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.safeLongList, Preconditions.checkNotNull(safeLongList, "safeLongList cannot be null"));
            return this;
        }

        @Override
        public Builder safeLongList(SafeLong safeLongList) {
            checkNotBuilt();
            Preconditions.checkNotNull(safeLongList, "safeLongList cannot be null");
            this.safeLongList.add(safeLongList);
            return this;
        }

        @Override
        @CheckReturnValue
        public SafeLongExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new SafeLongExample(safeLongValue, safeLongList);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
