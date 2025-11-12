package template.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.conjure.java.lib.internal.ConjureGenerated;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JsonDeserialize(builder = ExternalLongExample.DefaultBuilder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class ExternalLongExample {
    private final long externalLong;

    private final Optional<Long> optionalExternalLong;

    private final List<Long> listExternalLong;

    private int memoizedHashCode;

    private ExternalLongExample(long externalLong, Optional<Long> optionalExternalLong, List<Long> listExternalLong) {
        validateFields(optionalExternalLong, listExternalLong);
        this.externalLong = externalLong;
        this.optionalExternalLong = optionalExternalLong;
        this.listExternalLong = ConjureCollections.unmodifiableList(listExternalLong);
    }

    @JsonProperty("externalLong")
    public long getExternalLong() {
        return this.externalLong;
    }

    @JsonProperty("optionalExternalLong")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Long> getOptionalExternalLong() {
        return this.optionalExternalLong;
    }

    @JsonProperty("listExternalLong")
    public List<Long> getListExternalLong() {
        return this.listExternalLong;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ExternalLongExample && equalTo((ExternalLongExample) other));
    }

    private boolean equalTo(ExternalLongExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.externalLong == other.externalLong
                && this.optionalExternalLong.equals(other.optionalExternalLong)
                && this.listExternalLong.equals(other.listExternalLong);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + Long.hashCode(this.externalLong);
            hash = 31 * hash + this.optionalExternalLong.hashCode();
            hash = 31 * hash + this.listExternalLong.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ExternalLongExample{externalLong: " + externalLong + ", optionalExternalLong: " + optionalExternalLong
                + ", listExternalLong: " + listExternalLong + '}';
    }

    private static void validateFields(Optional<Long> optionalExternalLong, List<Long> listExternalLong) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalExternalLong, "optionalExternalLong");
        missingFields = addFieldIfMissing(missingFields, listExternalLong, "listExternalLong");
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

    public static ExternalLongStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface ExternalLongStageBuilder {
        Completed_StageBuilder externalLong(@Nonnull long externalLong);

        Builder from(ExternalLongExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        ExternalLongExample build();

        Completed_StageBuilder optionalExternalLong(@Nonnull Optional<? extends Long> optionalExternalLong);

        Completed_StageBuilder optionalExternalLong(long optionalExternalLong);

        Completed_StageBuilder listExternalLong(@Nonnull Iterable<? extends Long> listExternalLong);

        Completed_StageBuilder addAllListExternalLong(@Nonnull Iterable<? extends Long> listExternalLong);

        Completed_StageBuilder listExternalLong(long listExternalLong);
    }

    public interface Builder extends ExternalLongStageBuilder, Completed_StageBuilder {
        @Override
        Builder externalLong(@Nonnull long externalLong);

        @Override
        Builder from(ExternalLongExample other);

        @CheckReturnValue
        @Override
        ExternalLongExample build();

        @Override
        Builder optionalExternalLong(@Nonnull Optional<? extends Long> optionalExternalLong);

        @Override
        Builder optionalExternalLong(long optionalExternalLong);

        @Override
        Builder listExternalLong(@Nonnull Iterable<? extends Long> listExternalLong);

        @Override
        Builder addAllListExternalLong(@Nonnull Iterable<? extends Long> listExternalLong);

        @Override
        Builder listExternalLong(long listExternalLong);
    }

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private long externalLong;

        private Optional<Long> optionalExternalLong = Optional.empty();

        private List<Long> listExternalLong = ConjureCollections.newNonNullList();

        private boolean _externalLongInitialized = false;

        private DefaultBuilder() {}

        @Override
        public Builder from(ExternalLongExample other) {
            checkNotBuilt();
            externalLong(other.getExternalLong());
            optionalExternalLong(other.getOptionalExternalLong());
            listExternalLong(other.getListExternalLong());
            return this;
        }

        @Override
        @JsonSetter("externalLong")
        public Builder externalLong(long externalLong) {
            checkNotBuilt();
            this.externalLong = externalLong;
            this._externalLongInitialized = true;
            return this;
        }

        @Override
        @JsonSetter(value = "optionalExternalLong", nulls = Nulls.SKIP)
        public Builder optionalExternalLong(@Nonnull Optional<? extends Long> optionalExternalLong) {
            checkNotBuilt();
            this.optionalExternalLong = Preconditions.checkNotNull(
                            optionalExternalLong, "optionalExternalLong cannot be null")
                    .map(Function.identity());
            return this;
        }

        @Override
        public Builder optionalExternalLong(long optionalExternalLong) {
            checkNotBuilt();
            this.optionalExternalLong = Optional.of(
                    Preconditions.checkNotNull(optionalExternalLong, "optionalExternalLong cannot be null"));
            return this;
        }

        @Override
        @JsonSetter(value = "listExternalLong", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder listExternalLong(@Nonnull Iterable<? extends Long> listExternalLong) {
            checkNotBuilt();
            this.listExternalLong = ConjureCollections.newNonNullList(
                    Preconditions.checkNotNull(listExternalLong, "listExternalLong cannot be null"));
            return this;
        }

        @Override
        public Builder addAllListExternalLong(@Nonnull Iterable<? extends Long> listExternalLong) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.listExternalLong,
                    Preconditions.checkNotNull(listExternalLong, "listExternalLong cannot be null"));
            return this;
        }

        @Override
        public Builder listExternalLong(long listExternalLong) {
            checkNotBuilt();
            Preconditions.checkNotNull(listExternalLong, "listExternalLong cannot be null");
            this.listExternalLong.add(listExternalLong);
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _externalLongInitialized, "externalLong");
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
        public ExternalLongExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ExternalLongExample(externalLong, optionalExternalLong, listExternalLong);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
