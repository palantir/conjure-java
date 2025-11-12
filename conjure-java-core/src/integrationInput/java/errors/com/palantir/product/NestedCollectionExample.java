package errors.com.palantir.product;

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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = NestedCollectionExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class NestedCollectionExample {
    private final List<List<String>> nestedList;

    private final Map<String, Map<String, String>> nestedMap;

    private final Map<String, List<ObjectReference>> mixedCollection;

    private int memoizedHashCode;

    private NestedCollectionExample(
            List<List<String>> nestedList,
            Map<String, Map<String, String>> nestedMap,
            Map<String, List<ObjectReference>> mixedCollection) {
        validateFields(nestedList, nestedMap, mixedCollection);
        this.nestedList = ConjureCollections.unmodifiableList(nestedList);
        this.nestedMap = Collections.unmodifiableMap(nestedMap);
        this.mixedCollection = Collections.unmodifiableMap(mixedCollection);
    }

    @JsonProperty("nestedList")
    public List<List<String>> getNestedList() {
        return this.nestedList;
    }

    @JsonProperty("nestedMap")
    public Map<String, Map<String, String>> getNestedMap() {
        return this.nestedMap;
    }

    @JsonProperty("mixedCollection")
    public Map<String, List<ObjectReference>> getMixedCollection() {
        return this.mixedCollection;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return this == other || (other instanceof NestedCollectionExample && equalTo((NestedCollectionExample) other));
    }

    private boolean equalTo(NestedCollectionExample other) {
        if (this.memoizedHashCode != 0
                && other.memoizedHashCode != 0
                && this.memoizedHashCode != other.memoizedHashCode) {
            return false;
        }
        return this.nestedList.equals(other.nestedList)
                && this.nestedMap.equals(other.nestedMap)
                && this.mixedCollection.equals(other.mixedCollection);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.nestedList.hashCode();
            hash = 31 * hash + this.nestedMap.hashCode();
            hash = 31 * hash + this.mixedCollection.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "NestedCollectionExample{nestedList: " + nestedList + ", nestedMap: " + nestedMap + ", mixedCollection: "
                + mixedCollection + '}';
    }

    public static NestedCollectionExample of(
            List<List<String>> nestedList,
            Map<String, Map<String, String>> nestedMap,
            Map<String, List<ObjectReference>> mixedCollection) {
        return builder()
                .nestedList(nestedList)
                .nestedMap(nestedMap)
                .mixedCollection(mixedCollection)
                .build();
    }

    private static void validateFields(
            List<List<String>> nestedList,
            Map<String, Map<String, String>> nestedMap,
            Map<String, List<ObjectReference>> mixedCollection) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, nestedList, "nestedList");
        missingFields = addFieldIfMissing(missingFields, nestedMap, "nestedMap");
        missingFields = addFieldIfMissing(missingFields, mixedCollection, "mixedCollection");
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

        private List<List<String>> nestedList = ConjureCollections.newList();

        private Map<String, Map<String, String>> nestedMap = new LinkedHashMap<>();

        private Map<String, List<ObjectReference>> mixedCollection = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(NestedCollectionExample other) {
            checkNotBuilt();
            nestedList(other.getNestedList());
            nestedMap(other.getNestedMap());
            mixedCollection(other.getMixedCollection());
            return this;
        }

        @JsonSetter(value = "nestedList", nulls = Nulls.SKIP)
        public Builder nestedList(@Nonnull Iterable<? extends List<String>> nestedList) {
            checkNotBuilt();
            this.nestedList =
                    ConjureCollections.newList(Preconditions.checkNotNull(nestedList, "nestedList cannot be null"));
            return this;
        }

        public Builder addAllNestedList(@Nonnull Iterable<? extends List<String>> nestedList) {
            checkNotBuilt();
            ConjureCollections.addAll(
                    this.nestedList, Preconditions.checkNotNull(nestedList, "nestedList cannot be null"));
            return this;
        }

        public Builder nestedList(List<String> nestedList) {
            checkNotBuilt();
            this.nestedList.add(nestedList);
            return this;
        }

        @JsonSetter(value = "nestedMap", nulls = Nulls.SKIP)
        public Builder nestedMap(@Nonnull Map<String, Map<String, String>> nestedMap) {
            checkNotBuilt();
            this.nestedMap = new LinkedHashMap<>(Preconditions.checkNotNull(nestedMap, "nestedMap cannot be null"));
            return this;
        }

        public Builder putAllNestedMap(@Nonnull Map<String, Map<String, String>> nestedMap) {
            checkNotBuilt();
            this.nestedMap.putAll(Preconditions.checkNotNull(nestedMap, "nestedMap cannot be null"));
            return this;
        }

        public Builder nestedMap(String key, Map<String, String> value) {
            checkNotBuilt();
            this.nestedMap.put(key, value);
            return this;
        }

        @JsonSetter(value = "mixedCollection", nulls = Nulls.SKIP)
        public Builder mixedCollection(@Nonnull Map<String, List<ObjectReference>> mixedCollection) {
            checkNotBuilt();
            this.mixedCollection =
                    new LinkedHashMap<>(Preconditions.checkNotNull(mixedCollection, "mixedCollection cannot be null"));
            return this;
        }

        public Builder putAllMixedCollection(@Nonnull Map<String, List<ObjectReference>> mixedCollection) {
            checkNotBuilt();
            this.mixedCollection.putAll(Preconditions.checkNotNull(mixedCollection, "mixedCollection cannot be null"));
            return this;
        }

        public Builder mixedCollection(String key, List<ObjectReference> value) {
            checkNotBuilt();
            this.mixedCollection.put(key, value);
            return this;
        }

        @CheckReturnValue
        public NestedCollectionExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new NestedCollectionExample(nestedList, nestedMap, mixedCollection);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
