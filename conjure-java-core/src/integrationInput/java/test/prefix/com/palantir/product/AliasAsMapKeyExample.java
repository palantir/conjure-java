package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = AliasAsMapKeyExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class AliasAsMapKeyExample {
    private final Map<StringAliasExample, ManyFieldExample> strings;

    private final Map<RidAliasExample, ManyFieldExample> rids;

    private final Map<BearerTokenAliasExample, ManyFieldExample> bearertokens;

    private final Map<IntegerAliasExample, ManyFieldExample> integers;

    private final Map<SafeLongAliasExample, ManyFieldExample> safelongs;

    private final Map<DateTimeAliasExample, ManyFieldExample> datetimes;

    private final Map<UuidAliasExample, ManyFieldExample> uuids;

    private int memoizedHashCode;

    private AliasAsMapKeyExample(
            Map<StringAliasExample, ManyFieldExample> strings,
            Map<RidAliasExample, ManyFieldExample> rids,
            Map<BearerTokenAliasExample, ManyFieldExample> bearertokens,
            Map<IntegerAliasExample, ManyFieldExample> integers,
            Map<SafeLongAliasExample, ManyFieldExample> safelongs,
            Map<DateTimeAliasExample, ManyFieldExample> datetimes,
            Map<UuidAliasExample, ManyFieldExample> uuids) {
        validateFields(strings, rids, bearertokens, integers, safelongs, datetimes, uuids);
        this.strings = Collections.unmodifiableMap(strings);
        this.rids = Collections.unmodifiableMap(rids);
        this.bearertokens = Collections.unmodifiableMap(bearertokens);
        this.integers = Collections.unmodifiableMap(integers);
        this.safelongs = Collections.unmodifiableMap(safelongs);
        this.datetimes = Collections.unmodifiableMap(datetimes);
        this.uuids = Collections.unmodifiableMap(uuids);
    }

    @JsonProperty("strings")
    public Map<StringAliasExample, ManyFieldExample> getStrings() {
        return this.strings;
    }

    @JsonProperty("rids")
    public Map<RidAliasExample, ManyFieldExample> getRids() {
        return this.rids;
    }

    @JsonProperty("bearertokens")
    public Map<BearerTokenAliasExample, ManyFieldExample> getBearertokens() {
        return this.bearertokens;
    }

    @JsonProperty("integers")
    public Map<IntegerAliasExample, ManyFieldExample> getIntegers() {
        return this.integers;
    }

    @JsonProperty("safelongs")
    public Map<SafeLongAliasExample, ManyFieldExample> getSafelongs() {
        return this.safelongs;
    }

    @JsonProperty("datetimes")
    public Map<DateTimeAliasExample, ManyFieldExample> getDatetimes() {
        return this.datetimes;
    }

    @JsonProperty("uuids")
    public Map<UuidAliasExample, ManyFieldExample> getUuids() {
        return this.uuids;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof AliasAsMapKeyExample && equalTo((AliasAsMapKeyExample) other));
    }

    private boolean equalTo(AliasAsMapKeyExample other) {
        return this.strings.equals(other.strings)
                && this.rids.equals(other.rids)
                && this.bearertokens.equals(other.bearertokens)
                && this.integers.equals(other.integers)
                && this.safelongs.equals(other.safelongs)
                && this.datetimes.equals(other.datetimes)
                && this.uuids.equals(other.uuids);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            int hash = 1;
            hash = 31 * hash + this.strings.hashCode();
            hash = 31 * hash + this.rids.hashCode();
            hash = 31 * hash + this.bearertokens.hashCode();
            hash = 31 * hash + this.integers.hashCode();
            hash = 31 * hash + this.safelongs.hashCode();
            hash = 31 * hash + this.datetimes.hashCode();
            hash = 31 * hash + this.uuids.hashCode();
            result = hash;
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "AliasAsMapKeyExample{strings: " + strings + ", rids: " + rids + ", bearertokens: " + bearertokens
                + ", integers: " + integers + ", safelongs: " + safelongs + ", datetimes: " + datetimes + ", uuids: "
                + uuids + '}';
    }

    private static void validateFields(
            Map<StringAliasExample, ManyFieldExample> strings,
            Map<RidAliasExample, ManyFieldExample> rids,
            Map<BearerTokenAliasExample, ManyFieldExample> bearertokens,
            Map<IntegerAliasExample, ManyFieldExample> integers,
            Map<SafeLongAliasExample, ManyFieldExample> safelongs,
            Map<DateTimeAliasExample, ManyFieldExample> datetimes,
            Map<UuidAliasExample, ManyFieldExample> uuids) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, strings, "strings");
        missingFields = addFieldIfMissing(missingFields, rids, "rids");
        missingFields = addFieldIfMissing(missingFields, bearertokens, "bearertokens");
        missingFields = addFieldIfMissing(missingFields, integers, "integers");
        missingFields = addFieldIfMissing(missingFields, safelongs, "safelongs");
        missingFields = addFieldIfMissing(missingFields, datetimes, "datetimes");
        missingFields = addFieldIfMissing(missingFields, uuids, "uuids");
        if (missingFields != null) {
            throw new SafeIllegalArgumentException(
                    "Some required fields have not been set", SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(7);
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

        private Map<StringAliasExample, ManyFieldExample> strings = new LinkedHashMap<>();

        private Map<RidAliasExample, ManyFieldExample> rids = new LinkedHashMap<>();

        private Map<BearerTokenAliasExample, ManyFieldExample> bearertokens = new LinkedHashMap<>();

        private Map<IntegerAliasExample, ManyFieldExample> integers = new LinkedHashMap<>();

        private Map<SafeLongAliasExample, ManyFieldExample> safelongs = new LinkedHashMap<>();

        private Map<DateTimeAliasExample, ManyFieldExample> datetimes = new LinkedHashMap<>();

        private Map<UuidAliasExample, ManyFieldExample> uuids = new LinkedHashMap<>();

        private Builder() {}

        public Builder from(AliasAsMapKeyExample other) {
            checkNotBuilt();
            strings(other.getStrings());
            rids(other.getRids());
            bearertokens(other.getBearertokens());
            integers(other.getIntegers());
            safelongs(other.getSafelongs());
            datetimes(other.getDatetimes());
            uuids(other.getUuids());
            return this;
        }

        @JsonSetter(value = "strings", nulls = Nulls.SKIP)
        public Builder strings(@Nonnull Map<StringAliasExample, ManyFieldExample> strings) {
            checkNotBuilt();
            this.strings.clear();
            this.strings.putAll(Preconditions.checkNotNull(strings, "strings cannot be null"));
            return this;
        }

        public Builder putAllStrings(@Nonnull Map<StringAliasExample, ManyFieldExample> strings) {
            checkNotBuilt();
            this.strings.putAll(Preconditions.checkNotNull(strings, "strings cannot be null"));
            return this;
        }

        public Builder strings(StringAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.strings.put(key, value);
            return this;
        }

        @JsonSetter(value = "rids", nulls = Nulls.SKIP)
        public Builder rids(@Nonnull Map<RidAliasExample, ManyFieldExample> rids) {
            checkNotBuilt();
            this.rids.clear();
            this.rids.putAll(Preconditions.checkNotNull(rids, "rids cannot be null"));
            return this;
        }

        public Builder putAllRids(@Nonnull Map<RidAliasExample, ManyFieldExample> rids) {
            checkNotBuilt();
            this.rids.putAll(Preconditions.checkNotNull(rids, "rids cannot be null"));
            return this;
        }

        public Builder rids(RidAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.rids.put(key, value);
            return this;
        }

        @JsonSetter(value = "bearertokens", nulls = Nulls.SKIP)
        public Builder bearertokens(@Nonnull Map<BearerTokenAliasExample, ManyFieldExample> bearertokens) {
            checkNotBuilt();
            this.bearertokens.clear();
            this.bearertokens.putAll(Preconditions.checkNotNull(bearertokens, "bearertokens cannot be null"));
            return this;
        }

        public Builder putAllBearertokens(@Nonnull Map<BearerTokenAliasExample, ManyFieldExample> bearertokens) {
            checkNotBuilt();
            this.bearertokens.putAll(Preconditions.checkNotNull(bearertokens, "bearertokens cannot be null"));
            return this;
        }

        public Builder bearertokens(BearerTokenAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.bearertokens.put(key, value);
            return this;
        }

        @JsonSetter(value = "integers", nulls = Nulls.SKIP)
        public Builder integers(@Nonnull Map<IntegerAliasExample, ManyFieldExample> integers) {
            checkNotBuilt();
            this.integers.clear();
            this.integers.putAll(Preconditions.checkNotNull(integers, "integers cannot be null"));
            return this;
        }

        public Builder putAllIntegers(@Nonnull Map<IntegerAliasExample, ManyFieldExample> integers) {
            checkNotBuilt();
            this.integers.putAll(Preconditions.checkNotNull(integers, "integers cannot be null"));
            return this;
        }

        public Builder integers(IntegerAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.integers.put(key, value);
            return this;
        }

        @JsonSetter(value = "safelongs", nulls = Nulls.SKIP)
        public Builder safelongs(@Nonnull Map<SafeLongAliasExample, ManyFieldExample> safelongs) {
            checkNotBuilt();
            this.safelongs.clear();
            this.safelongs.putAll(Preconditions.checkNotNull(safelongs, "safelongs cannot be null"));
            return this;
        }

        public Builder putAllSafelongs(@Nonnull Map<SafeLongAliasExample, ManyFieldExample> safelongs) {
            checkNotBuilt();
            this.safelongs.putAll(Preconditions.checkNotNull(safelongs, "safelongs cannot be null"));
            return this;
        }

        public Builder safelongs(SafeLongAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.safelongs.put(key, value);
            return this;
        }

        @JsonSetter(value = "datetimes", nulls = Nulls.SKIP)
        public Builder datetimes(@Nonnull Map<DateTimeAliasExample, ManyFieldExample> datetimes) {
            checkNotBuilt();
            this.datetimes.clear();
            this.datetimes.putAll(Preconditions.checkNotNull(datetimes, "datetimes cannot be null"));
            return this;
        }

        public Builder putAllDatetimes(@Nonnull Map<DateTimeAliasExample, ManyFieldExample> datetimes) {
            checkNotBuilt();
            this.datetimes.putAll(Preconditions.checkNotNull(datetimes, "datetimes cannot be null"));
            return this;
        }

        public Builder datetimes(DateTimeAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.datetimes.put(key, value);
            return this;
        }

        @JsonSetter(value = "uuids", nulls = Nulls.SKIP)
        public Builder uuids(@Nonnull Map<UuidAliasExample, ManyFieldExample> uuids) {
            checkNotBuilt();
            this.uuids.clear();
            this.uuids.putAll(Preconditions.checkNotNull(uuids, "uuids cannot be null"));
            return this;
        }

        public Builder putAllUuids(@Nonnull Map<UuidAliasExample, ManyFieldExample> uuids) {
            checkNotBuilt();
            this.uuids.putAll(Preconditions.checkNotNull(uuids, "uuids cannot be null"));
            return this;
        }

        public Builder uuids(UuidAliasExample key, ManyFieldExample value) {
            checkNotBuilt();
            this.uuids.put(key, value);
            return this;
        }

        public AliasAsMapKeyExample build() {
            checkNotBuilt();
            this._buildInvoked = true;
            return new AliasAsMapKeyExample(strings, rids, bearertokens, integers, safelongs, datetimes, uuids);
        }

        private void checkNotBuilt() {
            Preconditions.checkState(!_buildInvoked, "Build has already been called");
        }
    }
}
