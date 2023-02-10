package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@Safe
@JsonDeserialize(builder = NestedTypesExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class NestedTypesExample {
    private final Optional<String> optionalString;

    private final List<String> stringList;

    private final Set<String> stringSet;

    private int memoizedHashCode;

    private NestedTypesExample(Optional<String> optionalString, List<String> stringList, Set<String> stringSet) {
        validateFields(optionalString, stringList, stringSet);
        this.optionalString = optionalString;
        this.stringList = Collections.unmodifiableList(stringList);
        this.stringSet = Collections.unmodifiableSet(stringSet);
    }

    @JsonProperty("optionalString")
    @Safe
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<String> getOptionalString() {
        return this.optionalString;
    }

    @JsonProperty("stringList")
    @Safe
    public List<String> getStringList() {
        return this.stringList;
    }

    @JsonProperty("stringSet")
    @Safe
    public Set<String> getStringSet() {
        return this.stringSet;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof NestedTypesExample && equalTo((NestedTypesExample) other));
    }

    private boolean equalTo(NestedTypesExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.optionalString.equals(other.optionalString)
                && this.stringList.equals(other.stringList)
                && this.stringSet.equals(other.stringSet);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.optionalString.hashCode();
            hash = 31 * hash + this.stringList.hashCode();
            hash = 31 * hash + this.stringSet.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    @Safe
    public String toString() {
        return "NestedTypesExample{optionalString: " + optionalString + ", stringList: " + stringList + ", stringSet: "
                + stringSet + '}';
    }

    public static NestedTypesExample of(
            @Safe String optionalString, @Safe List<String> stringList, @Safe Set<String> stringSet) {
        return builder()
                .optionalString(Optional.of(optionalString))
                .stringList(stringList)
                .stringSet(stringSet)
                .build();
    }

    private static void validateFields(
            Optional<String> optionalString, List<String> stringList, Set<String> stringSet) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, optionalString, "optionalString");
        missingFields = addFieldIfMissing(missingFields, stringList, "stringList");
        missingFields = addFieldIfMissing(missingFields, stringSet, "stringSet");
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

        private Optional<@Safe String> optionalString = Optional.empty();

        private List<@Safe String> stringList = new ArrayList<>();

        private Set<@Safe String> stringSet = new LinkedHashSet<>();

        private Builder() {}

        public Builder from(NestedTypesExample other) {
            checkNotBuilt();
            optionalString(other.getOptionalString());
            stringList(other.getStringList());
            stringSet(other.getStringSet());
            return this;
        }

        @JsonSetter(value = "optionalString", nulls = Nulls.SKIP)
        public Builder optionalString(@Safe @Nonnull Optional<@Safe String> optionalString) {
            checkNotBuilt();
            this.optionalString = Preconditions.checkNotNull(optionalString, "optionalString cannot be null");
            return this;
        }

        public Builder optionalString(@Safe @Nonnull String optionalString) {
            checkNotBuilt();
            this.optionalString =
                    Optional.of(Preconditions.checkNotNull(optionalString, "optionalString cannot be null"));
            return this;
        }

        @JsonSetter(value = "stringList", nulls = Nulls.SKIP)
        public Builder stringList(@Safe @Nonnull Iterable<String> stringList) {
            checkNotBuilt();
            this.stringList = ConjureCollections.newArrayList(
                    Preconditions.checkNotNull(stringList, "stringList cannot be null"));
            return this;
        }

        public Builder addAllStringList(@Safe @Nonnull Iterable<String> stringList) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.stringList, Preconditions.checkNotNull(stringList, "stringList cannot be null"));
            return this;
        }

        public Builder stringList(String stringList) {
            checkNotBuilt();
            this.stringList.add(stringList);
            return this;
        }

        @JsonSetter(value = "stringSet", nulls = Nulls.SKIP)
        public Builder stringSet(@Safe @Nonnull Iterable<String> stringSet) {
            checkNotBuilt();
            this.stringSet = ConjureCollections.newLinkedHashSet(
                    Preconditions.checkNotNull(stringSet, "stringSet cannot be null"));
            return this;
        }

        public Builder addAllStringSet(@Safe @Nonnull Iterable<String> stringSet) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.stringSet, Preconditions.checkNotNull(stringSet, "stringSet cannot be null"));
            return this;
        }

        public Builder stringSet(String stringSet) {
            checkNotBuilt();
            this.stringSet.add(stringSet);
            return this;
        }

        public NestedTypesExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new NestedTypesExample(optionalString, stringList, stringSet);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
