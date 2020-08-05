package com.palantir.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.UUID;
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

    private int memoizedHashCode;

    private PrimitiveOptionalsExample(
            OptionalDouble num,
            Optional<Boolean> bool,
            OptionalInt integer,
            Optional<SafeLong> safelong,
            Optional<ResourceIdentifier> rid,
            Optional<BearerToken> bearertoken,
            Optional<UUID> uuid) {
        validateFields(num, bool, integer, safelong, rid, bearertoken, uuid);
        this.num = num;
        this.bool = bool;
        this.integer = integer;
        this.safelong = safelong;
        this.rid = rid;
        this.bearertoken = bearertoken;
        this.uuid = uuid;
    }

    @JsonProperty("num")
    public OptionalDouble getNum() {
        return this.num;
    }

    @JsonProperty("bool")
    public Optional<Boolean> getBool() {
        return this.bool;
    }

    @JsonProperty("integer")
    public OptionalInt getInteger() {
        return this.integer;
    }

    @JsonProperty("safelong")
    public Optional<SafeLong> getSafelong() {
        return this.safelong;
    }

    @JsonProperty("rid")
    public Optional<ResourceIdentifier> getRid() {
        return this.rid;
    }

    @JsonProperty("bearertoken")
    public Optional<BearerToken> getBearertoken() {
        return this.bearertoken;
    }

    @JsonProperty("uuid")
    public Optional<UUID> getUuid() {
        return this.uuid;
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
                && this.uuid.equals(other.uuid);
    }

    @Override
    public int hashCode() {
        int result = memoizedHashCode;
        if (result == 0) {
            result = Objects.hash(
                    this.num, this.bool, this.integer, this.safelong, this.rid, this.bearertoken, this.uuid);
            memoizedHashCode = result;
        }
        return result;
    }

    @Override
    public String toString() {
        return "PrimitiveOptionalsExample{num: " + num + ", bool: " + bool + ", integer: " + integer + ", safelong: "
                + safelong + ", rid: " + rid + ", bearertoken: " + bearertoken + ", uuid: " + uuid + '}';
    }

    private static void validateFields(
            OptionalDouble num,
            Optional<Boolean> bool,
            OptionalInt integer,
            Optional<SafeLong> safelong,
            Optional<ResourceIdentifier> rid,
            Optional<BearerToken> bearertoken,
            Optional<UUID> uuid) {
        List<String> missingFields = null;
        missingFields = addFieldIfMissing(missingFields, num, "num");
        missingFields = addFieldIfMissing(missingFields, bool, "bool");
        missingFields = addFieldIfMissing(missingFields, integer, "integer");
        missingFields = addFieldIfMissing(missingFields, safelong, "safelong");
        missingFields = addFieldIfMissing(missingFields, rid, "rid");
        missingFields = addFieldIfMissing(missingFields, bearertoken, "bearertoken");
        missingFields = addFieldIfMissing(missingFields, uuid, "uuid");
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
    public static final class Builder {
        private OptionalDouble num = OptionalDouble.empty();

        private Optional<Boolean> bool = Optional.empty();

        private OptionalInt integer = OptionalInt.empty();

        private Optional<SafeLong> safelong = Optional.empty();

        private Optional<ResourceIdentifier> rid = Optional.empty();

        private Optional<BearerToken> bearertoken = Optional.empty();

        private Optional<UUID> uuid = Optional.empty();

        private Builder() {}

        public Builder from(PrimitiveOptionalsExample other) {
            num(other.getNum());
            bool(other.getBool());
            integer(other.getInteger());
            safelong(other.getSafelong());
            rid(other.getRid());
            bearertoken(other.getBearertoken());
            uuid(other.getUuid());
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

        public PrimitiveOptionalsExample build() {
            return new PrimitiveOptionalsExample(num, bool, integer, safelong, rid, bearertoken, uuid);
        }
    }
}
