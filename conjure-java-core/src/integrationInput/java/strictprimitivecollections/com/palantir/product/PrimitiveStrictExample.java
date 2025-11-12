package strictprimitivecollections.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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

@JsonDeserialize(builder = PrimitiveStrictExample.DefaultBuilder.class)
@ConjureGenerated("com.palantir.conjure.java.types.BeanGenerator")
public final class PrimitiveStrictExample {
    private final List<Integer> ints;

    private final List<Double> doubles;

    private final List<SafeLong> longs;

    private final List<Boolean> bools;

    private int memoizedHashCode;

    private PrimitiveStrictExample(
            List<Integer> ints, List<Double> doubles, List<SafeLong> longs, List<Boolean> bools) {
        validateFields(ints, doubles, longs, bools);
        this.ints = ConjureCollections.unmodifiableList(ints);
        this.doubles = ConjureCollections.unmodifiableList(doubles);
        this.longs = ConjureCollections.unmodifiableList(longs);
        this.bools = ConjureCollections.unmodifiableList(bools);
    }

    @JsonProperty("ints")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Integer> getInts() {
        return this.ints;
    }

    @JsonProperty("doubles")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Double> getDoubles() {
        return this.doubles;
    }

    /**
     * This primitive type is stored optimally, but does not have boxing optimizations.
     *
     * <p>This type was added in case we choose to change that later we have a comparison.
     */
    @JsonProperty("longs")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<SafeLong> getLongs() {
        return this.longs;
    }

    /**
     * This primitive type is intentionally not optimized
     *
     * <p>This type was added in case we choose to change that later we have a comparison.
     */
    @JsonProperty("bools")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public List<Boolean> getBools() {
        return this.bools;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof PrimitiveStrictExample && equalTo((PrimitiveStrictExample) other));
    }

    private boolean equalTo(PrimitiveStrictExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.ints.equals(other.ints)
                && this.doubles.equals(other.doubles)
                && this.longs.equals(other.longs)
                && this.bools.equals(other.bools);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.ints.hashCode();
            hash = 31 * hash + this.doubles.hashCode();
            hash = 31 * hash + this.longs.hashCode();
            hash = 31 * hash + this.bools.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimitiveStrictExample{ints: " + ints + ", doubles: " + doubles + ", longs: " + longs + ", bools: "
                + bools + '}';
    }

    private static void validateFields(
            List<Integer> ints, List<Double> doubles, List<SafeLong> longs, List<Boolean> bools) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, ints, "ints");
        missingFields = addFieldIfMissing(missingFields, doubles, "doubles");
        missingFields = addFieldIfMissing(missingFields, longs, "longs");
        missingFields = addFieldIfMissing(missingFields, bools, "bools");
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

    public static IntsStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface IntsStageBuilder {
        DoublesStageBuilder ints(@Nonnull Iterable<Integer> ints);

        DoublesStageBuilder ints(@Nonnull int... ints);

        Builder from(PrimitiveStrictExample other);
    }

    public interface DoublesStageBuilder {
        LongsStageBuilder doubles(@Nonnull Iterable<Double> doubles);

        LongsStageBuilder doubles(@Nonnull double... doubles);
    }

    public interface LongsStageBuilder {
        BoolsStageBuilder longs(@Nonnull Iterable<SafeLong> longs);
    }

    public interface BoolsStageBuilder {
        Completed_StageBuilder bools(@Nonnull Iterable<Boolean> bools);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        PrimitiveStrictExample build();
    }

    public interface Builder
            extends IntsStageBuilder,
                    DoublesStageBuilder,
                    LongsStageBuilder,
                    BoolsStageBuilder,
                    Completed_StageBuilder {
        @Override
        Builder ints(@Nonnull Iterable<Integer> ints);

        @Override
        Builder ints(@Nonnull int... ints);

        @Override
        Builder from(PrimitiveStrictExample other);

        @Override
        Builder doubles(@Nonnull Iterable<Double> doubles);

        @Override
        Builder doubles(@Nonnull double... doubles);

        @Override
        Builder longs(@Nonnull Iterable<SafeLong> longs);

        @Override
        Builder bools(@Nonnull Iterable<Boolean> bools);

        @CheckReturnValue
        @Override
        PrimitiveStrictExample build();
    }

    @ConjureGenerated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private List<Integer> ints = ConjureCollections.newNonNullIntegerList();

        private List<Double> doubles = ConjureCollections.newNonNullDoubleList();

        private List<SafeLong> longs = ConjureCollections.newNonNullSafeLongList();

        private List<Boolean> bools = ConjureCollections.newNonNullList();

        private DefaultBuilder() {}

        @Override
        public Builder from(PrimitiveStrictExample other) {
            checkNotBuilt();
            ints(other.getInts());
            doubles(other.getDoubles());
            longs(other.getLongs());
            bools(other.getBools());
            return this;
        }

        @Override
        public Builder ints(@Nonnull Iterable<Integer> ints) {
            checkNotBuilt();
            this.ints =
                    ConjureCollections.newNonNullIntegerList(Preconditions.checkNotNull(ints, "ints cannot be null"));
            return this;
        }

        @Override
        @JsonSetter(value = "ints", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder ints(@Nonnull int... ints) {
            checkNotBuilt();
            this.ints =
                    ConjureCollections.newNonNullIntegerList(Preconditions.checkNotNull(ints, "ints cannot be null"));
            return this;
        }

        @Override
        public Builder doubles(@Nonnull Iterable<Double> doubles) {
            checkNotBuilt();
            this.doubles = ConjureCollections.newNonNullDoubleList(
                    Preconditions.checkNotNull(doubles, "doubles cannot be null"));
            return this;
        }

        @Override
        @JsonSetter(value = "doubles", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder doubles(@Nonnull double... doubles) {
            checkNotBuilt();
            this.doubles = ConjureCollections.newNonNullDoubleList(
                    Preconditions.checkNotNull(doubles, "doubles cannot be null"));
            return this;
        }

        /**
         * This primitive type is stored optimally, but does not have boxing optimizations.
         *
         * <p>This type was added in case we choose to change that later we have a comparison.
         */
        @Override
        @JsonSetter(value = "longs", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder longs(@Nonnull Iterable<SafeLong> longs) {
            checkNotBuilt();
            this.longs = ConjureCollections.newNonNullSafeLongList(
                    Preconditions.checkNotNull(longs, "longs cannot be null"));
            return this;
        }

        /**
         * This primitive type is intentionally not optimized
         *
         * <p>This type was added in case we choose to change that later we have a comparison.
         */
        @Override
        @JsonSetter(value = "bools", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder bools(@Nonnull Iterable<Boolean> bools) {
            checkNotBuilt();
            this.bools = ConjureCollections.newNonNullList(Preconditions.checkNotNull(bools, "bools cannot be null"));
            return this;
        }

        @Override
        @CheckReturnValue
        public PrimitiveStrictExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new PrimitiveStrictExample(ints, doubles, longs, bools);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
