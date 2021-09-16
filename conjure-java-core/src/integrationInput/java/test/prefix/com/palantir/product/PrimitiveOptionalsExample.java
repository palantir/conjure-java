package test.prefix.com.palantir.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;

@JsonDeserialize(builder = PrimitiveOptionalsExample.Builder.class)
@Generated("com.palantir.conjure.java.types.BeanGenerator")
public final class PrimitiveOptionalsExample {
    private final OptionalDouble num;

    private final Optional<Boolean> bool;

    private final OptionalInt integer;

    private final Optional<SafeLong> safelong;

    private final Optional<ResourceIdentifier> rid;

    private final Optional<BearerToken> bearertoken;

    private final Optional<UUID> uuid;

    private final Optional<Map<String, String>> map;

    private final Optional<List<String>> list;

    private final Optional<Set<String>> set;

    private final Optional<StringAliasOne> aliasOne;

    private final StringAliasTwo aliasTwo;

    private final Optional<ListAlias> aliasList;

    private final Optional<MapAliasExample> aliasMap;

    private final OptionalAlias aliasOptional;

    private final OptionalMapAliasExample aliasOptionalMap;

    private final OptionalListAliasExample aliasOptionalList;

    private final OptionalSetAliasExample aliasOptionalSet;

    private int memoizedHashCode;

    private PrimitiveOptionalsExample(
            OptionalDouble num,
            Optional<Boolean> bool,
            OptionalInt integer,
            Optional<SafeLong> safelong,
            Optional<ResourceIdentifier> rid,
            Optional<BearerToken> bearertoken,
            Optional<UUID> uuid,
            Optional<Map<String, String>> map,
            Optional<List<String>> list,
            Optional<Set<String>> set,
            Optional<StringAliasOne> aliasOne,
            StringAliasTwo aliasTwo,
            Optional<ListAlias> aliasList,
            Optional<MapAliasExample> aliasMap,
            OptionalAlias aliasOptional,
            OptionalMapAliasExample aliasOptionalMap,
            OptionalListAliasExample aliasOptionalList,
            OptionalSetAliasExample aliasOptionalSet) {
        validateFields(
                num,
                bool,
                integer,
                safelong,
                rid,
                bearertoken,
                uuid,
                map,
                list,
                set,
                aliasOne,
                aliasTwo,
                aliasList,
                aliasMap,
                aliasOptional,
                aliasOptionalMap,
                aliasOptionalList,
                aliasOptionalSet);
        this.num = num;
        this.bool = bool;
        this.integer = integer;
        this.safelong = safelong;
        this.rid = rid;
        this.bearertoken = bearertoken;
        this.uuid = uuid;
        this.map = map;
        this.list = list;
        this.set = set;
        this.aliasOne = aliasOne;
        this.aliasTwo = aliasTwo;
        this.aliasList = aliasList;
        this.aliasMap = aliasMap;
        this.aliasOptional = aliasOptional;
        this.aliasOptionalMap = aliasOptionalMap;
        this.aliasOptionalList = aliasOptionalList;
        this.aliasOptionalSet = aliasOptionalSet;
    }

    @JsonProperty("num")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public OptionalDouble getNum() {
        return this.num;
    }

    @JsonProperty("bool")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Boolean> getBool() {
        return this.bool;
    }

    @JsonProperty("integer")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public OptionalInt getInteger() {
        return this.integer;
    }

    @JsonProperty("safelong")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<SafeLong> getSafelong() {
        return this.safelong;
    }

    @JsonProperty("rid")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<ResourceIdentifier> getRid() {
        return this.rid;
    }

    @JsonProperty("bearertoken")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<BearerToken> getBearertoken() {
        return this.bearertoken;
    }

    @JsonProperty("uuid")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<UUID> getUuid() {
        return this.uuid;
    }

    @JsonProperty("map")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Map<String, String>> getMap() {
        return this.map;
    }

    @JsonProperty("list")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<List<String>> getList() {
        return this.list;
    }

    @JsonProperty("set")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<Set<String>> getSet() {
        return this.set;
    }

    @JsonProperty("aliasOne")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<StringAliasOne> getAliasOne() {
        return this.aliasOne;
    }

    @JsonProperty("aliasTwo")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public StringAliasTwo getAliasTwo() {
        return this.aliasTwo;
    }

    @JsonProperty("aliasList")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<ListAlias> getAliasList() {
        return this.aliasList;
    }

    @JsonProperty("aliasMap")
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public Optional<MapAliasExample> getAliasMap() {
        return this.aliasMap;
    }

    @JsonProperty("aliasOptional")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public OptionalAlias getAliasOptional() {
        return this.aliasOptional;
    }

    @JsonProperty("aliasOptionalMap")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public OptionalMapAliasExample getAliasOptionalMap() {
        return this.aliasOptionalMap;
    }

    @JsonProperty("aliasOptionalList")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public OptionalListAliasExample getAliasOptionalList() {
        return this.aliasOptionalList;
    }

    @JsonProperty("aliasOptionalSet")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public OptionalSetAliasExample getAliasOptionalSet() {
        return this.aliasOptionalSet;
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || (other instanceof PrimitiveOptionalsExample && equalTo((PrimitiveOptionalsExample) other));
    }

    private boolean equalTo(PrimitiveOptionalsExample other) {
        return this.num.equals(other.num)
                && this.bool.equals(other.bool)
                && this.integer.equals(other.integer)
                && this.safelong.equals(other.safelong)
                && this.rid.equals(other.rid)
                && this.bearertoken.equals(other.bearertoken)
                && this.uuid.equals(other.uuid)
                && this.map.equals(other.map)
                && this.list.equals(other.list)
                && this.set.equals(other.set)
                && this.aliasOne.equals(other.aliasOne)
                && this.aliasTwo.equals(other.aliasTwo)
                && this.aliasList.equals(other.aliasList)
                && this.aliasMap.equals(other.aliasMap)
                && this.aliasOptional.equals(other.aliasOptional)
                && this.aliasOptionalMap.equals(other.aliasOptionalMap)
                && this.aliasOptionalList.equals(other.aliasOptionalList)
                && this.aliasOptionalSet.equals(other.aliasOptionalSet);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(
                    this.num,
                    this.bool,
                    this.integer,
                    this.safelong,
                    this.rid,
                    this.bearertoken,
                    this.uuid,
                    this.map,
                    this.list,
                    this.set,
                    this.aliasOne,
                    this.aliasTwo,
                    this.aliasList,
                    this.aliasMap,
                    this.aliasOptional,
                    this.aliasOptionalMap,
                    this.aliasOptionalList,
                    this.aliasOptionalSet);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimitiveOptionalsExample{num: " + num + ", bool: " + bool + ", integer: " + integer + ", safelong: "
                + safelong + ", rid: " + rid + ", bearertoken: " + bearertoken + ", uuid: " + uuid + ", map: " + map
                + ", list: " + list + ", set: " + set + ", aliasOne: " + aliasOne + ", aliasTwo: " + aliasTwo
                + ", aliasList: " + aliasList + ", aliasMap: " + aliasMap + ", aliasOptional: " + aliasOptional
                + ", aliasOptionalMap: " + aliasOptionalMap + ", aliasOptionalList: " + aliasOptionalList
                + ", aliasOptionalSet: " + aliasOptionalSet + '}';
    }

    private static void validateFields(
            OptionalDouble num,
            Optional<Boolean> bool,
            OptionalInt integer,
            Optional<SafeLong> safelong,
            Optional<ResourceIdentifier> rid,
            Optional<BearerToken> bearertoken,
            Optional<UUID> uuid,
            Optional<Map<String, String>> map,
            Optional<List<String>> list,
            Optional<Set<String>> set,
            Optional<StringAliasOne> aliasOne,
            StringAliasTwo aliasTwo,
            Optional<ListAlias> aliasList,
            Optional<MapAliasExample> aliasMap,
            OptionalAlias aliasOptional,
            OptionalMapAliasExample aliasOptionalMap,
            OptionalListAliasExample aliasOptionalList,
            OptionalSetAliasExample aliasOptionalSet) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, num, "num");
        missingFields = addFieldIfMissing(missingFields, bool, "bool");
        missingFields = addFieldIfMissing(missingFields, integer, "integer");
        missingFields = addFieldIfMissing(missingFields, safelong, "safelong");
        missingFields = addFieldIfMissing(missingFields, rid, "rid");
        missingFields = addFieldIfMissing(missingFields, bearertoken, "bearertoken");
        missingFields = addFieldIfMissing(missingFields, uuid, "uuid");
        missingFields = addFieldIfMissing(missingFields, map, "map");
        missingFields = addFieldIfMissing(missingFields, list, "list");
        missingFields = addFieldIfMissing(missingFields, set, "set");
        missingFields = addFieldIfMissing(missingFields, aliasOne, "aliasOne");
        missingFields = addFieldIfMissing(missingFields, aliasTwo, "aliasTwo");
        missingFields = addFieldIfMissing(missingFields, aliasList, "aliasList");
        missingFields = addFieldIfMissing(missingFields, aliasMap, "aliasMap");
        missingFields = addFieldIfMissing(missingFields, aliasOptional, "aliasOptional");
        missingFields = addFieldIfMissing(missingFields, aliasOptionalMap, "aliasOptionalMap");
        missingFields = addFieldIfMissing(missingFields, aliasOptionalList, "aliasOptionalList");
        missingFields = addFieldIfMissing(missingFields, aliasOptionalSet, "aliasOptionalSet");
        if (missingFields != null) {
            throw new ServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("missingFields", missingFields));
        }
    }

    private static List<String> addFieldIfMissing(List<String> prev, Object fieldValue, String fieldName) {
        List<String> missingFields = prev;
        if (fieldValue == null) {
            if (missingFields == null) {
                missingFields = new ArrayList<>(18);
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
        private OptionalDouble num = OptionalDouble.empty();

        private Optional<Boolean> bool = Optional.empty();

        private OptionalInt integer = OptionalInt.empty();

        private Optional<SafeLong> safelong = Optional.empty();

        private Optional<ResourceIdentifier> rid = Optional.empty();

        private Optional<BearerToken> bearertoken = Optional.empty();

        private Optional<UUID> uuid = Optional.empty();

        private Optional<Map<String, String>> map = Optional.empty();

        private Optional<List<String>> list = Optional.empty();

        private Optional<Set<String>> set = Optional.empty();

        private Optional<StringAliasOne> aliasOne = Optional.empty();

        private StringAliasTwo aliasTwo;

        private Optional<ListAlias> aliasList = Optional.empty();

        private Optional<MapAliasExample> aliasMap = Optional.empty();

        private OptionalAlias aliasOptional;

        private OptionalMapAliasExample aliasOptionalMap;

        private OptionalListAliasExample aliasOptionalList;

        private OptionalSetAliasExample aliasOptionalSet;

        private Builder() {}

        public Builder from(PrimitiveOptionalsExample other) {
            num(other.getNum());
            bool(other.getBool());
            integer(other.getInteger());
            safelong(other.getSafelong());
            rid(other.getRid());
            bearertoken(other.getBearertoken());
            uuid(other.getUuid());
            map(other.getMap());
            list(other.getList());
            set(other.getSet());
            aliasOne(other.getAliasOne());
            aliasTwo(other.getAliasTwo());
            aliasList(other.getAliasList());
            aliasMap(other.getAliasMap());
            aliasOptional(other.getAliasOptional());
            aliasOptionalMap(other.getAliasOptionalMap());
            aliasOptionalList(other.getAliasOptionalList());
            aliasOptionalSet(other.getAliasOptionalSet());
            return this;
        }

        @JsonSetter(value = "num", nulls = Nulls.SKIP)
        public Builder num(@Nonnull OptionalDouble num) {
            this.num = Preconditions.checkNotNull(num, "num cannot be null");
            return this;
        }

        public Builder num(double num) {
            this.num = OptionalDouble.of(num);
            return this;
        }

        @JsonSetter(value = "bool", nulls = Nulls.SKIP)
        public Builder bool(@Nonnull Optional<Boolean> bool) {
            this.bool = Preconditions.checkNotNull(bool, "bool cannot be null");
            return this;
        }

        public Builder bool(boolean bool) {
            this.bool = Optional.of(bool);
            return this;
        }

        @JsonSetter(value = "integer", nulls = Nulls.SKIP)
        public Builder integer(@Nonnull OptionalInt integer) {
            this.integer = Preconditions.checkNotNull(integer, "integer cannot be null");
            return this;
        }

        public Builder integer(int integer) {
            this.integer = OptionalInt.of(integer);
            return this;
        }

        @JsonSetter(value = "safelong", nulls = Nulls.SKIP)
        public Builder safelong(@Nonnull Optional<SafeLong> safelong) {
            this.safelong = Preconditions.checkNotNull(safelong, "safelong cannot be null");
            return this;
        }

        public Builder safelong(@Nonnull SafeLong safelong) {
            this.safelong = Optional.of(Preconditions.checkNotNull(safelong, "safelong cannot be null"));
            return this;
        }

        @JsonSetter(value = "rid", nulls = Nulls.SKIP)
        public Builder rid(@Nonnull Optional<ResourceIdentifier> rid) {
            this.rid = Preconditions.checkNotNull(rid, "rid cannot be null");
            return this;
        }

        public Builder rid(@Nonnull ResourceIdentifier rid) {
            this.rid = Optional.of(Preconditions.checkNotNull(rid, "rid cannot be null"));
            return this;
        }

        @JsonSetter(value = "bearertoken", nulls = Nulls.SKIP)
        public Builder bearertoken(@Nonnull Optional<BearerToken> bearertoken) {
            this.bearertoken = Preconditions.checkNotNull(bearertoken, "bearertoken cannot be null");
            return this;
        }

        public Builder bearertoken(@Nonnull BearerToken bearertoken) {
            this.bearertoken = Optional.of(Preconditions.checkNotNull(bearertoken, "bearertoken cannot be null"));
            return this;
        }

        @JsonSetter(value = "uuid", nulls = Nulls.SKIP)
        public Builder uuid(@Nonnull Optional<UUID> uuid) {
            this.uuid = Preconditions.checkNotNull(uuid, "uuid cannot be null");
            return this;
        }

        public Builder uuid(@Nonnull UUID uuid) {
            this.uuid = Optional.of(Preconditions.checkNotNull(uuid, "uuid cannot be null"));
            return this;
        }

        @JsonSetter(value = "map", nulls = Nulls.SKIP)
        public Builder map(@Nonnull Optional<? extends Map<String, String>> map) {
            this.map = Preconditions.checkNotNull(map, "map cannot be null").map(Function.identity());
            return this;
        }

        public Builder map(@Nonnull Map<String, String> map) {
            this.map = Optional.of(Preconditions.checkNotNull(map, "map cannot be null"));
            return this;
        }

        @JsonSetter(value = "list", nulls = Nulls.SKIP)
        public Builder list(@Nonnull Optional<? extends List<String>> list) {
            this.list = Preconditions.checkNotNull(list, "list cannot be null").map(Function.identity());
            return this;
        }

        public Builder list(@Nonnull List<String> list) {
            this.list = Optional.of(Preconditions.checkNotNull(list, "list cannot be null"));
            return this;
        }

        @JsonSetter(value = "set", nulls = Nulls.SKIP)
        public Builder set(@Nonnull Optional<? extends Set<String>> set) {
            this.set = Preconditions.checkNotNull(set, "set cannot be null").map(Function.identity());
            return this;
        }

        public Builder set(@Nonnull Set<String> set) {
            this.set = Optional.of(Preconditions.checkNotNull(set, "set cannot be null"));
            return this;
        }

        @JsonSetter(value = "aliasOne", nulls = Nulls.SKIP)
        public Builder aliasOne(@Nonnull Optional<StringAliasOne> aliasOne) {
            this.aliasOne = Preconditions.checkNotNull(aliasOne, "aliasOne cannot be null");
            return this;
        }

        public Builder aliasOne(@Nonnull StringAliasOne aliasOne) {
            this.aliasOne = Optional.of(Preconditions.checkNotNull(aliasOne, "aliasOne cannot be null"));
            return this;
        }

        @JsonSetter(value = "aliasTwo", nulls = Nulls.AS_EMPTY)
        public Builder aliasTwo(@Nonnull StringAliasTwo aliasTwo) {
            this.aliasTwo = Preconditions.checkNotNull(aliasTwo, "aliasTwo cannot be null");
            return this;
        }

        @JsonSetter(value = "aliasList", nulls = Nulls.SKIP)
        public Builder aliasList(@Nonnull Optional<ListAlias> aliasList) {
            this.aliasList = Preconditions.checkNotNull(aliasList, "aliasList cannot be null");
            return this;
        }

        public Builder aliasList(@Nonnull ListAlias aliasList) {
            this.aliasList = Optional.of(Preconditions.checkNotNull(aliasList, "aliasList cannot be null"));
            return this;
        }

        @JsonSetter(value = "aliasMap", nulls = Nulls.SKIP)
        public Builder aliasMap(@Nonnull Optional<MapAliasExample> aliasMap) {
            this.aliasMap = Preconditions.checkNotNull(aliasMap, "aliasMap cannot be null");
            return this;
        }

        public Builder aliasMap(@Nonnull MapAliasExample aliasMap) {
            this.aliasMap = Optional.of(Preconditions.checkNotNull(aliasMap, "aliasMap cannot be null"));
            return this;
        }

        @JsonSetter(value = "aliasOptional", nulls = Nulls.AS_EMPTY)
        public Builder aliasOptional(@Nonnull OptionalAlias aliasOptional) {
            this.aliasOptional = Preconditions.checkNotNull(aliasOptional, "aliasOptional cannot be null");
            return this;
        }

        @JsonSetter(value = "aliasOptionalMap", nulls = Nulls.AS_EMPTY)
        public Builder aliasOptionalMap(@Nonnull OptionalMapAliasExample aliasOptionalMap) {
            this.aliasOptionalMap = Preconditions.checkNotNull(aliasOptionalMap, "aliasOptionalMap cannot be null");
            return this;
        }

        @JsonSetter(value = "aliasOptionalList", nulls = Nulls.AS_EMPTY)
        public Builder aliasOptionalList(@Nonnull OptionalListAliasExample aliasOptionalList) {
            this.aliasOptionalList = Preconditions.checkNotNull(aliasOptionalList, "aliasOptionalList cannot be null");
            return this;
        }

        @JsonSetter(value = "aliasOptionalSet", nulls = Nulls.AS_EMPTY)
        public Builder aliasOptionalSet(@Nonnull OptionalSetAliasExample aliasOptionalSet) {
            this.aliasOptionalSet = Preconditions.checkNotNull(aliasOptionalSet, "aliasOptionalSet cannot be null");
            return this;
        }

        public PrimitiveOptionalsExample build() {
            return new PrimitiveOptionalsExample(
                    num,
                    bool,
                    integer,
                    safelong,
                    rid,
                    bearertoken,
                    uuid,
                    map,
                    list,
                    set,
                    aliasOne,
                    aliasTwo,
                    aliasList,
                    aliasMap,
                    aliasOptional,
                    aliasOptionalMap,
                    aliasOptionalList,
                    aliasOptionalSet);
        }
    }
}
