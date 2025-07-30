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
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.processing.Generated;

@JsonDeserialize(builder = CollectionExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class CollectionExample {
    private final List<String> strings;

    private final Map<String, StringExample> map;

    private final Set<String> set;

    private int memoizedHashCode;

    private CollectionExample(List<String> strings, Map<String, StringExample> map, Set<String> set) {
        validateFields(strings, map, set);
        this.strings = ConjureCollections.unmodifiableList(strings);
        this.map = Collections.unmodifiableMap(map);
        this.set = Collections.unmodifiableSet(set);
    }

    @JsonProperty("strings")
    public List<String> getStrings() {
        return this.strings;
    }

    @JsonProperty("map")
    public Map<String, StringExample> getMap() {
        return this.map;
    }

    @JsonProperty("set")
    public Set<String> getSet() {
        return this.set;
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
        return this.strings.equals(other.strings) && this.map.equals(other.map) && this.set.equals(other.set);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.strings.hashCode();
            hash = 31 * hash + this.map.hashCode();
            hash = 31 * hash + this.set.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "CollectionExample{strings: " + strings + ", map: " + map + ", set: " + set + '}';
    }

    public static CollectionExample of(List<String> strings, Map<String, StringExample> map, Set<String> set) {
        return builder().strings(strings).map(map).set(set).build();
    }

    private static void validateFields(List<String> strings, Map<String, StringExample> map, Set<String> set) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, strings, "strings");
        missingFields = addFieldIfMissing(missingFields, map, "map");
        missingFields = addFieldIfMissing(missingFields, set, "set");
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

        private List<String> strings = ConjureCollections.newList();

        private Map<String, StringExample> map = new LinkedHashMap<>();

        private Set<String> set = ConjureCollections.newSet();

        private Builder() {}

        public Builder from(CollectionExample other) {
            checkNotBuilt();
            strings(other.getStrings());
            map(other.getMap());
            set(other.getSet());
            return this;
        }

        @JsonSetter(value = "strings", nulls = Nulls.SKIP)
        public Builder strings(@Nonnull Iterable<String> strings) {
            checkNotBuilt();
            this.strings = ConjureCollections.newList(Preconditions.checkNotNull(strings, "strings cannot be null"));
            return this;
        }

        public Builder addAllStrings(@Nonnull Iterable<String> strings) {
            checkNotBuilt();
            ConjureCollections.addAll(this.strings, Preconditions.checkNotNull(strings, "strings cannot be null"));
            return this;
        }

        public Builder strings(String strings) {
            checkNotBuilt();
            this.strings.add(strings);
            return this;
        }

        @JsonSetter(value = "map", nulls = Nulls.SKIP)
        public Builder map(@Nonnull Map<String, StringExample> map) {
            checkNotBuilt();
            this.map = new LinkedHashMap<>(Preconditions.checkNotNull(map, "map cannot be null"));
            return this;
        }

        public Builder putAllMap(@Nonnull Map<String, StringExample> map) {
            checkNotBuilt();
            this.map.putAll(Preconditions.checkNotNull(map, "map cannot be null"));
            return this;
        }

        public Builder map(String key, StringExample value) {
            checkNotBuilt();
            this.map.put(key, value);
            return this;
        }

        @JsonSetter(value = "set", nulls = Nulls.SKIP)
        public Builder set(@Nonnull Iterable<String> set) {
            checkNotBuilt();
            this.set = ConjureCollections.newSet(Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        public Builder addAllSet(@Nonnull Iterable<String> set) {
            checkNotBuilt();
            ConjureCollections.addAll(this.set, Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        public Builder set(String set) {
            checkNotBuilt();
            this.set.add(set);
            return this;
        }

        @CheckReturnValue
        public CollectionExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new CollectionExample(strings, map, set);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
