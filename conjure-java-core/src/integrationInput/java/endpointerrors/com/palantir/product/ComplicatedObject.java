package endpointerrors.com.palantir.product;

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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = ComplicatedObject.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ComplicatedObject {
    private final List<String> fieldOne;

    private final Optional<StringAlias> fieldTwo;

    private int memoizedHashCode;

    private ComplicatedObject(List<String> fieldOne, Optional<StringAlias> fieldTwo) {
        validateFields(fieldOne, fieldTwo);
        this.fieldOne = ConjureCollections.unmodifiableList(fieldOne);
        this.fieldTwo = fieldTwo;
    }

    @JsonProperty("fieldOne")
    public List<String> getFieldOne() {
        return this.fieldOne;
    }

    @JsonProperty("fieldTwo")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<StringAlias> getFieldTwo() {
        return this.fieldTwo;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof ComplicatedObject && equalTo((ComplicatedObject) other));
    }

    private boolean equalTo(ComplicatedObject other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.fieldOne.equals(other.fieldOne) && this.fieldTwo.equals(other.fieldTwo);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.fieldOne.hashCode();
            hash = 31 * hash + this.fieldTwo.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ComplicatedObject{fieldOne: " + fieldOne + ", fieldTwo: " + fieldTwo + '}';
    }

    public static ComplicatedObject of(List<String> fieldOne, StringAlias fieldTwo) {
        return builder().fieldOne(fieldOne).fieldTwo(Optional.of(fieldTwo)).build();
    }

    private static void validateFields(List<String> fieldOne, Optional<StringAlias> fieldTwo) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, fieldOne, "fieldOne");
        missingFields = addFieldIfMissing(missingFields, fieldTwo, "fieldTwo");
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

    public static Builder builder() {
        return new Builder();
    }

    @Generated("com.palantir.conjure.java.types.BeanBuilderGenerator")
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private List<String> fieldOne = ConjureCollections.newNonNullList();

        private Optional<StringAlias> fieldTwo = Optional.empty();

        private Builder() {}

        public Builder from(ComplicatedObject other) {
            checkNotBuilt();
            fieldOne(other.getFieldOne());
            fieldTwo(other.getFieldTwo());
            return this;
        }

        @JsonSetter(value = "fieldOne", nulls = Nulls.SKIP, contentNulls = Nulls.FAIL)
        public Builder fieldOne(@Nonnull Iterable<String> fieldOne) {
            checkNotBuilt();
            this.fieldOne =
                    ConjureCollections.newNonNullList(Preconditions.checkNotNull(fieldOne, "fieldOne cannot be null"));
            return this;
        }

        public Builder addAllFieldOne(@Nonnull Iterable<String> fieldOne) {
            checkNotBuilt();
            ConjureCollections.addAllAndCheckNonNull(
                    this.fieldOne, Preconditions.checkNotNull(fieldOne, "fieldOne cannot be null"));
            return this;
        }

        public Builder fieldOne(String fieldOne) {
            checkNotBuilt();
            Preconditions.checkNotNull(fieldOne, "fieldOne cannot be null");
            this.fieldOne.add(fieldOne);
            return this;
        }

        @JsonSetter(value = "fieldTwo", nulls = Nulls.SKIP)
        public Builder fieldTwo(@Nonnull Optional<StringAlias> fieldTwo) {
            checkNotBuilt();
            this.fieldTwo = Preconditions.checkNotNull(fieldTwo, "fieldTwo cannot be null");
            return this;
        }

        public Builder fieldTwo(@Nonnull StringAlias fieldTwo) {
            checkNotBuilt();
            this.fieldTwo = Optional.of(Preconditions.checkNotNull(fieldTwo, "fieldTwo cannot be null"));
            return this;
        }

        @CheckReturnValue
        public ComplicatedObject build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new ComplicatedObject(fieldOne, fieldTwo);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
