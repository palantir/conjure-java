package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.internal.ConjureCollections;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Generated;

@JsonDeserialize(builder = ManyFieldExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class ManyFieldExample {
    private final String string;

    private final int integer;

    private final double doubleValue;

    private final Optional<String> optionalItem;

    private final List<String> items;

    private final Set<String> set;

    private final Map<String, String> map;

    private final StringAliasExample alias;

    private volatile int memoizedHashCode;

    private ManyFieldExample(
            String string,
            int integer,
            double doubleValue,
            Optional<String> optionalItem,
            List<String> items,
            Set<String> set,
            Map<String, String> map,
            StringAliasExample alias) {
        validateFields(string, optionalItem, items, set, map, alias);
        this.string = string;
        this.integer = integer;
        this.doubleValue = doubleValue;
        this.optionalItem = optionalItem;
        this.items = Collections.unmodifiableList(items);
        this.set = Collections.unmodifiableSet(set);
        this.map = Collections.unmodifiableMap(map);
        this.alias = alias;
    }

    /** docs for string field */
    @JsonProperty("string")
    public String getString() {
        return this.string;
    }

    /** docs for integer field */
    @JsonProperty("integer")
    public int getInteger() {
        return this.integer;
    }

    /** docs for doubleValue field */
    @JsonProperty("doubleValue")
    public double getDoubleValue() {
        return this.doubleValue;
    }

    /** docs for optionalItem field */
    @JsonProperty("optionalItem")
    public Optional<String> getOptionalItem() {
        return this.optionalItem;
    }

    /** docs for items field */
    @JsonProperty("items")
    public List<String> getItems() {
        return this.items;
    }

    /** docs for set field */
    @JsonProperty("set")
    public Set<String> getSet() {
        return this.set;
    }

    /** @deprecated deprecation documentation. */
    @JsonProperty("map")
    @Deprecated
    public Map<String, String> getMap() {
        return this.map;
    }

    /**
     * docs for alias field
     *
     * @deprecated This field is deprecated.
     */
    @JsonProperty("alias")
    @Deprecated
    public StringAliasExample getAlias() {
        return this.alias;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof ManyFieldExample && equalTo((ManyFieldExample) other));
    }

    private boolean equalTo(ManyFieldExample other) {
        return this.string.equals(other.string)
                && this.integer == other.integer
                && this.doubleValue == other.doubleValue
                && this.optionalItem.equals(other.optionalItem)
                && this.items.equals(other.items)
                && this.set.equals(other.set)
                && this.map.equals(other.map)
                && this.alias.equals(other.alias);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result =
                    Objects.hash(
                            this.string,
                            this.integer,
                            this.doubleValue,
                            this.optionalItem,
                            this.items,
                            this.set,
                            this.map,
                            this.alias);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ManyFieldExample{string: "
                + string
                + ", integer: "
                + integer
                + ", doubleValue: "
                + doubleValue
                + ", optionalItem: "
                + optionalItem
                + ", items: "
                + items
                + ", set: "
                + set
                + ", map: "
                + map
                + ", alias: "
                + alias
                + '}';
    }

    private static void validateFields(
            String string,
            Optional<String> optionalItem,
            List<String> items,
            Set<String> set,
            Map<String, String> map,
            StringAliasExample alias) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, string, "string");
        missingFields = addFieldIfMissing(missingFields, optionalItem, "optionalItem");
        missingFields = addFieldIfMissing(missingFields, items, "items");
        missingFields = addFieldIfMissing(missingFields, set, "set");
        missingFields = addFieldIfMissing(missingFields, map, "map");
        missingFields = addFieldIfMissing(missingFields, alias, "alias");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set",
                    SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(
            List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(6);
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
        private String string;

        private int integer;

        private double doubleValue;

        private Optional<String> optionalItem = Optional.empty();

        private List<String> items = new ArrayList<>();

        private Set<String> set = new LinkedHashSet<>();

        private Map<String, String> map = new LinkedHashMap<>();

        private StringAliasExample alias;

        private boolean _integerInitialized = false;

        private boolean _doubleValueInitialized = false;

        private Builder() {}

        public Builder from(ManyFieldExample other) {
            string(other.getString());
            integer(other.getInteger());
            doubleValue(other.getDoubleValue());
            optionalItem(other.getOptionalItem());
            items(other.getItems());
            set(other.getSet());
            map(other.getMap());
            alias(other.getAlias());
            return this;
        }

        /** docs for string field */
        @JsonSetter("string")
        public Builder string(String string) {
            this.string = Preconditions.checkNotNull(string, "string cannot be null");
            return this;
        }

        /** docs for integer field */
        @JsonSetter("integer")
        public Builder integer(int integer) {
            this.integer = integer;
            this._integerInitialized = true;
            return this;
        }

        /** docs for doubleValue field */
        @JsonSetter("doubleValue")
        public Builder doubleValue(double doubleValue) {
            this.doubleValue = doubleValue;
            this._doubleValueInitialized = true;
            return this;
        }

        /** docs for optionalItem field */
        @JsonSetter(value = "optionalItem", nulls = Nulls.SKIP)
        public Builder optionalItem(Optional<String> optionalItem) {
            this.optionalItem =
                    Preconditions.checkNotNull(optionalItem, "optionalItem cannot be null");
            return this;
        }

        /** docs for optionalItem field */
        public Builder optionalItem(String optionalItem) {
            this.optionalItem =
                    Optional.of(
                            Preconditions.checkNotNull(
                                    optionalItem, "optionalItem cannot be null"));
            return this;
        }

        /** docs for items field */
        @JsonSetter(value = "items", nulls = Nulls.SKIP)
        public Builder items(Iterable<String> items) {
            this.items.clear();
            ConjureCollections.addAll(
                    this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        /** docs for items field */
        public Builder addAllItems(Iterable<String> items) {
            ConjureCollections.addAll(
                    this.items, Preconditions.checkNotNull(items, "items cannot be null"));
            return this;
        }

        /** docs for items field */
        public Builder items(String items) {
            this.items.add(items);
            return this;
        }

        /** docs for set field */
        @JsonSetter(value = "set", nulls = Nulls.SKIP)
        public Builder set(Iterable<String> set) {
            this.set.clear();
            ConjureCollections.addAll(
                    this.set, Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        /** docs for set field */
        public Builder addAllSet(Iterable<String> set) {
            ConjureCollections.addAll(
                    this.set, Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        /** docs for set field */
        public Builder set(String set) {
            this.set.add(set);
            return this;
        }

        /** @deprecated deprecation documentation. */
        @Deprecated
        @JsonSetter(value = "map", nulls = Nulls.SKIP)
        public Builder map(Map<String, String> map) {
            this.map.clear();
            this.map.putAll(Preconditions.checkNotNull(map, "map cannot be null"));
            return this;
        }

        /** @deprecated deprecation documentation. */
        @Deprecated
        public Builder putAllMap(Map<String, String> map) {
            this.map.putAll(Preconditions.checkNotNull(map, "map cannot be null"));
            return this;
        }

        /** @deprecated deprecation documentation. */
        @Deprecated
        public Builder map(String key, String value) {
            this.map.put(key, value);
            return this;
        }

        /**
         * docs for alias field
         *
         * @deprecated This field is deprecated.
         */
        @Deprecated
        @JsonSetter("alias")
        public Builder alias(StringAliasExample alias) {
            this.alias = Preconditions.checkNotNull(alias, "alias cannot be null");
            return this;
        }

        private void validatePrimitiveFieldsHaveBeenInitialized() {
            List<String> missingFields = null;
            missingFields = addFieldIfMissing(missingFields, _integerInitialized, "integer");
            missingFields =
                    addFieldIfMissing(missingFields, _doubleValueInitialized, "doubleValue");
            if (missingFields != null) {
                throw new SafeIllegalArgumentException(
                        "Some required fields have not been set",
                        SafeArg.of("missingFields", missingFields));
            }
        }

        private static List<String> addFieldIfMissing(
                List<String> prev, boolean initialized, String fieldName) {
            List<String> missingFields = prev;
            if (!initialized) {
                if (missingFields == null) {
                    missingFields = new ArrayList<>(2);
                }
                missingFields.add(fieldName);
            }
            return missingFields;
        }

        public ManyFieldExample build() {
            validatePrimitiveFieldsHaveBeenInitialized();
            return new ManyFieldExample(
                    string, integer, doubleValue, optionalItem, items, set, map, alias);
        }
    }
}
