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

@JsonDeserialize(builder = PrimitiveExample.DefaultBuilder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class PrimitiveExample {
    private final int field;

    private final List<Integer> ints;

    private final List<Double> doubles;

    private int memoizedHashCode;

    private PrimitiveExample(int field, List<Integer> ints, List<Double> doubles) {
        validateFields(ints, doubles);
        this.field = field;
        this.ints = ConjureCollections.unmodifiableList(ints);
        this.doubles = ConjureCollections.unmodifiableList(doubles);
    }

    @JsonProperty("field")
    public int getField() {
        return this.field;
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
        return this == other || (other instanceof PrimitiveExample && equalTo((PrimitiveExample) other));
    }

    private boolean equalTo(PrimitiveExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.field == other.field && this.ints.equals(other.ints) && this.doubles.equals(other.doubles);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.field;
            hash = 31 * hash + this.ints.hashCode();
            hash = 31 * hash + this.doubles.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimitiveExample{field: " + field + ", ints: " + ints + ", doubles: " + doubles + '}';
    }

    public static PrimitiveExample of(int field, List<Integer> ints, List<Double> doubles) {
        return builder().field(field).ints(ints).doubles(doubles).build();
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

    public static FieldStageBuilder builder() {
        return new DefaultBuilder();
    }

    public interface FieldStageBuilder {
        Completed_StageBuilder field(@Nonnull int field);

        Builder from(PrimitiveExample other);
    }

    public interface Completed_StageBuilder {
        @CheckReturnValue
        PrimitiveExample build();

        Completed_StageBuilder ints(@Nonnull Iterable<Integer> ints);

        Completed_StageBuilder addAllInts(@Nonnull Iterable<Integer> ints);

        Completed_StageBuilder addAllInts(@Nonnull int... ints);

        Completed_StageBuilder ints(int ints);

        Completed_StageBuilder doubles(@Nonnull Iterable<Double> doubles);

        Completed_StageBuilder addAllDoubles(@Nonnull Iterable<Double> doubles);

        Completed_StageBuilder addAllDoubles(@Nonnull double... doubles);

        Completed_StageBuilder doubles(double doubles);
    }

    public interface Builder extends FieldStageBuilder, Completed_StageBuilder {
        @Override
        Builder field(@Nonnull int field);

        @Override
        Builder from(PrimitiveExample other);

        @CheckReturnValue
        @Override
        PrimitiveExample build();

        @Override
        Builder ints(@Nonnull Iterable<Integer> ints);

        @Override
        Builder addAllInts(@Nonnull Iterable<Integer> ints);

        @Override
        Builder addAllInts(@Nonnull int... ints);

        @Override
        Builder ints(int ints);

        @Override
        Builder doubles(@Nonnull Iterable<Double> doubles);

        @Override
        Builder addAllDoubles(@Nonnull Iterable<Double> doubles);

        @Override
        Builder addAllDoubles(@Nonnull double... doubles);

        @Override
        Builder doubles(double doubles);
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    static final class DefaultBuilder implements Builder {
        boolean _buildInvoked;

        private int field;

        private List<Integer> ints = ConjureCollections.newNonNullIntegerList();

        private List<Double> doubles = ConjureCollections.newNonNullDoubleList();

        private boolean _fieldInitialized = false;

        private DefaultBuilder() {}

        @Override
        public Builder from(PrimitiveExample other) {
            checkNotBuilt();
            field(other.getField());
            ints(other.getInts());
            doubles(other.getDoubles());
            return this;
        }

        @Override
        @JsonSetter("field")
        public Builder field(int field) {
            checkNotBuilt();
            this.field = field;
            this._fieldInitialized = true;
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
        public Builder addAllInts(@Nonnull int... ints) {
            checkNotBuilt();
            ConjureCollections.addAllToIntegerList(this.ints, Preconditions.checkNotNull(ints, "ints cannot be null"));
            return this;
        }

        @Override
        public Builder addAllInts(@Nonnull Iterable<Integer> ints) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.ints, Preconditions.checkNotNull(ints, "ints cannot be null"));
            return this;
        }

        @Override
        public Builder ints(int ints) {
            checkNotBuilt();
            Preconditions.checkNotNull(ints, "ints cannot be null");
            this.ints.add(ints);
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
        public Builder addAllDoubles(@Nonnull double... doubles) {
            checkNotBuilt();
            ConjureCollections.addAllToDoubleList(
                    this.doubles, Preconditions.checkNotNull(doubles, "doubles cannot be null"));
            return this;
        }

        @Override
        public Builder addAllDoubles(@Nonnull Iterable<Double> doubles) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.doubles, Preconditions.checkNotNull(doubles, "doubles cannot be null"));
            return this;
        }

        @Override
        public Builder doubles(double doubles) {
            checkNotBuilt();
            Preconditions.checkNotNull(doubles, "doubles cannot be null");
            this.doubles.add(doubles);
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _fieldInitialized, "field");
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
        public PrimitiveExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            validatePrimitiveFieldsHaveBeenInitialized();
            return new PrimitiveExample(field, ints, doubles);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
