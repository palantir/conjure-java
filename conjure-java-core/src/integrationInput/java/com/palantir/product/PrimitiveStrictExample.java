package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = PrimitiveStrictExample.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class PrimitiveStrictExample {
    private final List<Integer> ints;

    private final List<Double> doubles;

    private int memoizedHashCode;

    private PrimitiveStrictExample(List<Integer> ints, List<Double> doubles) {
        validateFields(ints, doubles);
        this.ints = ConjureCollections.unmodifiableList(ints);
        this.doubles = ConjureCollections.unmodifiableList(doubles);
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
        return this.ints.equals(other.ints) && this.doubles.equals(other.doubles);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.ints.hashCode();
            hash = 31 * hash + this.doubles.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimitiveStrictExample{ints: " + ints + ", doubles: " + doubles + '}';
    }

    public static PrimitiveStrictExample of(List<Integer> ints, List<Double> doubles) {
        return builder().ints(ints).doubles(doubles).build();
    }

    private static void validateFields(List<Integer> ints, List<Double> doubles) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, ints, "ints");
        missingFields = addFieldIfMissing(missingFields, doubles, "doubles");
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

    public static IntsStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface IntsStageBuilder {
        DoublesStageBuilder ints(@Nonnull Iterable<Integer> ints);

        Builder from(PrimitiveStrictExample other);
    }

    public interface DoublesStageBuilder {
        Completed_StageBuilder doubles(@Nonnull Iterable<Double> doubles);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        PrimitiveStrictExample build();
    }

    public interface Builder extends IntsStageBuilder, DoublesStageBuilder, Completed_StageBuilder {
        @Override
        Builder ints(@Nonnull Iterable<Integer> ints);

        @Override
        Builder from(PrimitiveStrictExample other);

        @Override
        Builder doubles(@Nonnull Iterable<Double> doubles);

        @CheckReturnValue
        @Override
        PrimitiveStrictExample build();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private List<Integer> ints = ConjureCollections.newNonNullIntegerList();

        private List<Double> doubles = ConjureCollections.newNonNullDoubleList();

        private DefaultBuilder() {}

        @Override
        public Builder from(PrimitiveStrictExample other) {
            checkNotBuilt();
            ints(other.getInts());
            doubles(other.getDoubles());
            return this;
        }

        @Override
        @JsonSetter(value = "ints", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder ints(@Nonnull Iterable<Integer> ints) {
            checkNotBuilt();
            this.ints =
                    ConjureCollections.newNonNullIntegerList(Preconditions.checkNotNull(ints, "ints cannot be null"));
            return this;
        }

        @Override
        @JsonSetter(value = "doubles", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder doubles(@Nonnull Iterable<Double> doubles) {
            checkNotBuilt();
            this.doubles = ConjureCollections.newNonNullDoubleList(
                    Preconditions.checkNotNull(doubles, "doubles cannot be null"));
            return this;
        }

        @Override
        @CheckReturnValue
        public PrimitiveStrictExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new PrimitiveStrictExample(ints, doubles);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
