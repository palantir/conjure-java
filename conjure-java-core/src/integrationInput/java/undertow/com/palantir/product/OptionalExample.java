package undertow.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.errorprone.annotations.CheckReturnValue;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = OptionalExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class OptionalExample {
    private final Optional<String> optionalString;

    private final Optional<ObjectReference> optionalObject;

    private final Optional<List<String>> optionalCollection;

    private int memoizedHashCode;

    private OptionalExample(
            Optional<String> optionalString,
            Optional<ObjectReference> optionalObject,
            Optional<List<String>> optionalCollection) {
        validateFields(optionalString, optionalObject, optionalCollection);
        this.optionalString = optionalString;
        this.optionalObject = optionalObject;
        this.optionalCollection = optionalCollection;
    }

    @JsonProperty("optionalString")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getOptionalString() {
        return this.optionalString;
    }

    @JsonProperty("optionalObject")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<ObjectReference> getOptionalObject() {
        return this.optionalObject;
    }

    @JsonProperty("optionalCollection")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<List<String>> getOptionalCollection() {
        return this.optionalCollection;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof OptionalExample && equalTo((OptionalExample) other));
    }

    private boolean equalTo(OptionalExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.optionalString.equals(other.optionalString)
                && this.optionalObject.equals(other.optionalObject)
                && this.optionalCollection.equals(other.optionalCollection);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.optionalString.hashCode();
            hash = 31 * hash + this.optionalObject.hashCode();
            hash = 31 * hash + this.optionalCollection.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "OptionalExample{optionalString: " + optionalString + ", optionalObject: " + optionalObject
                + ", optionalCollection: " + optionalCollection + '}';
    }

    public static OptionalExample of(
            String optionalString, ObjectReference optionalObject, List<String> optionalCollection) {
        return builder()
                .optionalString(Optional.of(optionalString))
                .optionalObject(Optional.of(optionalObject))
                .optionalCollection(Optional.of(optionalCollection))
                .build();
    }

    private static void validateFields(
            Optional<String> optionalString,
            Optional<ObjectReference> optionalObject,
            Optional<List<String>> optionalCollection) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalString, "optionalString");
        missingFields = addFieldIfMissing(missingFields, optionalObject, "optionalObject");
        missingFields = addFieldIfMissing(missingFields, optionalCollection, "optionalCollection");
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
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Builder {
        boolean _buildInvoked;

        private Optional<String> optionalString = Optional.empty();

        private Optional<ObjectReference> optionalObject = Optional.empty();

        private Optional<List<String>> optionalCollection = Optional.empty();

        private Builder() {}

        public Builder from(OptionalExample other) {
            checkNotBuilt();
            optionalString(other.getOptionalString());
            optionalObject(other.getOptionalObject());
            optionalCollection(other.getOptionalCollection());
            return this;
        }

        @JsonSetter(value = "optionalString", nulls = Nulls.SKIP)
        public Builder optionalString(@Nonnull Optional<String> optionalString) {
            checkNotBuilt();
            this.optionalString = Preconditions.checkNotNull(optionalString, "optionalString cannot be null");
            return this;
        }

        public Builder optionalString(@Nonnull String optionalString) {
            checkNotBuilt();
            this.optionalString =
                    Optional.of(Preconditions.checkNotNull(optionalString, "optionalString cannot be null"));
            return this;
        }

        @JsonSetter(value = "optionalObject", nulls = Nulls.SKIP)
        public Builder optionalObject(@Nonnull Optional<ObjectReference> optionalObject) {
            checkNotBuilt();
            this.optionalObject = Preconditions.checkNotNull(optionalObject, "optionalObject cannot be null");
            return this;
        }

        public Builder optionalObject(@Nonnull ObjectReference optionalObject) {
            checkNotBuilt();
            this.optionalObject =
                    Optional.of(Preconditions.checkNotNull(optionalObject, "optionalObject cannot be null"));
            return this;
        }

        @JsonSetter(value = "optionalCollection", nulls = Nulls.SKIP)
        public Builder optionalCollection(@Nonnull Optional<? extends List<String>> optionalCollection) {
            checkNotBuilt();
            this.optionalCollection = Preconditions.checkNotNull(
                            optionalCollection, "optionalCollection cannot be null")
                    .map(Function.identity());
            return this;
        }

        public Builder optionalCollection(@Nonnull List<String> optionalCollection) {
            checkNotBuilt();
            this.optionalCollection =
                    Optional.of(Preconditions.checkNotNull(optionalCollection, "optionalCollection cannot be null"));
            return this;
        }

        @CheckReturnValue
        public OptionalExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new OptionalExample(optionalString, optionalObject, optionalCollection);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
