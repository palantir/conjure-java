package jersey.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = CollectionExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CollectionExample {
    private final List<String> stringList;

    private final Set<String> stringSet;

    private final Map<String, String> stringMap;

    private int memoizedHashCode;

    private CollectionExample(List<String> stringList, Set<String> stringSet, Map<String, String> stringMap) {
        validateFields(stringList, stringSet, stringMap);
        this.stringList = ConjureCollections.unmodifiableList(stringList);
        this.stringSet = Collections.unmodifiableSet(stringSet);
        this.stringMap = Collections.unmodifiableMap(stringMap);
    }

    @JsonProperty("stringList")
    public List<String> getStringList() {
        return this.stringList;
    }

    @JsonProperty("stringSet")
    public Set<String> getStringSet() {
        return this.stringSet;
    }

    @JsonProperty("stringMap")
    public Map<String, String> getStringMap() {
        return this.stringMap;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof CollectionExample && equalTo((CollectionExample) other));
    }

    private boolean equalTo(CollectionExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.stringList.equals(other.stringList)
                && this.stringSet.equals(other.stringSet)
                && this.stringMap.equals(other.stringMap);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.stringList.hashCode();
            hash = 31 * hash + this.stringSet.hashCode();
            hash = 31 * hash + this.stringMap.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CollectionExample{stringList: " + stringList + ", stringSet: " + stringSet + ", stringMap: " + stringMap
                + '}';
    }

    public static CollectionExample of(List<String> stringList, Set<String> stringSet, Map<String, String> stringMap) {
        return builder()
                .stringList(stringList)
                .stringSet(stringSet)
                .stringMap(stringMap)
                .build();
    }

    private static void validateFields(List<String> stringList, Set<String> stringSet, Map<String, String> stringMap) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, stringList, "stringList");
        missingFields = addFieldIfMissing(missingFields, stringSet, "stringSet");
        missingFields = addFieldIfMissing(missingFields, stringMap, "stringMap");
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

        private List<String> stringList = ConjureCollections.newList();

        private Set<String> stringSet = ConjureCollections.newSet();

        private Map<String, String> stringMap = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(CollectionExample other) {
            checkNotBuilt();
            stringList(other.getStringList());
            stringSet(other.getStringSet());
            stringMap(other.getStringMap());
            return this;
        }

        @JsonSetter(value = "stringList", nulls = Nulls.SKIP)
        public Builder stringList(@Nonnull Iterable<String> stringList) {
            checkNotBuilt();
            this.stringList =
                    ConjureCollections.newList(Preconditions.checkNotNull(stringList, "stringList cannot be null"));
            return this;
        }

        public Builder addAllStringList(@Nonnull Iterable<String> stringList) {
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
        public Builder stringSet(@Nonnull Iterable<String> stringSet) {
            checkNotBuilt();
            this.stringSet =
                    ConjureCollections.newSet(Preconditions.checkNotNull(stringSet, "stringSet cannot be null"));
            return this;
        }

        public Builder addAllStringSet(@Nonnull Iterable<String> stringSet) {
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

        @JsonSetter(value = "stringMap", nulls = Nulls.SKIP)
        public Builder stringMap(@Nonnull Map<String, String> stringMap) {
            checkNotBuilt();
            this.stringMap = new LinkedHashMap<>(Preconditions.checkNotNull(stringMap, "stringMap cannot be null"));
            return this;
        }

        public Builder putAllStringMap(@Nonnull Map<String, String> stringMap) {
            checkNotBuilt();
            this.stringMap.putAll(Preconditions.checkNotNull(stringMap, "stringMap cannot be null"));
            return this;
        }

        public Builder stringMap(String key, String value) {
            checkNotBuilt();
            this.stringMap.put(key, value);
            return this;
        }

        @CheckReturnValue
        public CollectionExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new CollectionExample(stringList, stringSet, stringMap);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
