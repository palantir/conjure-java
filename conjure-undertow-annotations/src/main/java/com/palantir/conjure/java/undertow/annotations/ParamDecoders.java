/*
 * (c) Copyright 2022 Palantir Technologies Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.palantir.conjure.java.undertow.annotations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.PlainSerDe;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/** Default parameter decoders, mirroring and delegating to the deserializers provided by {@link PlainSerDe}. */
public final class ParamDecoders {

    public static ParamDecoder<String> stringParamDecoder(PlainSerDe serde) {
        return serde::deserializeString;
    }

    public static ParamDecoder<Optional<String>> optionalStringParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalString;
    }

    public static CollectionParamDecoder<String> stringCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeString;
    }

    public static CollectionParamDecoder<Optional<String>> optionalStringCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalString;
    }

    public static CollectionParamDecoder<List<String>> stringListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeStringList;
    }

    public static CollectionParamDecoder<Set<String>> stringSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeStringSet;
    }

    public static ParamDecoder<Boolean> booleanParamDecoder(PlainSerDe serde) {
        return serde::deserializeBoolean;
    }

    public static ParamDecoder<Optional<Boolean>> optionalBooleanParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalBoolean;
    }

    public static CollectionParamDecoder<Boolean> booleanCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeBoolean;
    }

    public static CollectionParamDecoder<Optional<Boolean>> optionalBooleanCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalBoolean;
    }

    public static CollectionParamDecoder<List<Boolean>> booleanListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeBooleanList;
    }

    public static CollectionParamDecoder<Set<Boolean>> booleanSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeBooleanSet;
    }

    public static ParamDecoder<BearerToken> bearerTokenParamDecoder(PlainSerDe serde) {
        return serde::deserializeBearerToken;
    }

    public static ParamDecoder<Optional<BearerToken>> optionalBearerTokenParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalBearerToken;
    }

    public static CollectionParamDecoder<BearerToken> bearerTokenCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeBearerToken;
    }

    public static CollectionParamDecoder<Optional<BearerToken>> optionalBearerTokenCollectionParamDecoder(
            PlainSerDe serde) {
        return serde::deserializeOptionalBearerToken;
    }

    public static CollectionParamDecoder<List<BearerToken>> bearerTokenListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeBearerTokenList;
    }

    public static CollectionParamDecoder<Set<BearerToken>> bearerTokenSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeBearerTokenSet;
    }

    public static ParamDecoder<OffsetDateTime> dateTimeParamDecoder(PlainSerDe serde) {
        return serde::deserializeDateTime;
    }

    public static ParamDecoder<Optional<OffsetDateTime>> optionalDateTimeParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalDateTime;
    }

    public static CollectionParamDecoder<OffsetDateTime> dateTimeCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeDateTime;
    }

    public static CollectionParamDecoder<Optional<OffsetDateTime>> optionalDateTimeCollectionParamDecoder(
            PlainSerDe serde) {
        return serde::deserializeOptionalDateTime;
    }

    public static CollectionParamDecoder<List<OffsetDateTime>> dateTimeListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeDateTimeList;
    }

    public static CollectionParamDecoder<Set<OffsetDateTime>> dateTimeSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeDateTimeSet;
    }

    public static ParamDecoder<Double> doubleParamDecoder(PlainSerDe serde) {
        return serde::deserializeDouble;
    }

    public static ParamDecoder<OptionalDouble> optionalDoubleParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalDouble, OptionalDouble.empty());
    }

    public static CollectionParamDecoder<Double> doubleCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeDouble;
    }

    public static CollectionParamDecoder<OptionalDouble> optionalDoubleCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalDouble;
    }

    public static CollectionParamDecoder<List<Double>> doubleListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeDoubleList;
    }

    public static CollectionParamDecoder<Set<Double>> doubleSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeDoubleSet;
    }

    public static ParamDecoder<Integer> integerParamDecoder(PlainSerDe serde) {
        return serde::deserializeInteger;
    }

    public static ParamDecoder<OptionalInt> optionalIntegerParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalInteger, OptionalInt.empty());
    }

    public static CollectionParamDecoder<Integer> integerCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeInteger;
    }

    public static CollectionParamDecoder<OptionalInt> optionalIntegerCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalInteger;
    }

    public static CollectionParamDecoder<List<Integer>> integerListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeIntegerList;
    }

    public static CollectionParamDecoder<Set<Integer>> integerSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeIntegerSet;
    }

    public static ParamDecoder<ResourceIdentifier> ridParamDecoder(PlainSerDe serde) {
        return serde::deserializeRid;
    }

    public static ParamDecoder<Optional<ResourceIdentifier>> optionalRidParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalRid;
    }

    public static CollectionParamDecoder<ResourceIdentifier> ridCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeRid;
    }

    public static CollectionParamDecoder<Optional<ResourceIdentifier>> optionalRidCollectionParamDecoder(
            PlainSerDe serde) {
        return serde::deserializeOptionalRid;
    }

    public static CollectionParamDecoder<List<ResourceIdentifier>> ridListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeRidList;
    }

    public static CollectionParamDecoder<Set<ResourceIdentifier>> ridSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeRidSet;
    }

    public static ParamDecoder<SafeLong> safeLongParamDecoder(PlainSerDe serde) {
        return serde::deserializeSafeLong;
    }

    public static ParamDecoder<Optional<SafeLong>> optionalSafeLongParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalSafeLong;
    }

    public static CollectionParamDecoder<SafeLong> safeLongCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeSafeLong;
    }

    public static CollectionParamDecoder<Optional<SafeLong>> optionalSafeLongCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalSafeLong;
    }

    public static CollectionParamDecoder<List<SafeLong>> safeLongListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeSafeLongList;
    }

    public static CollectionParamDecoder<Set<SafeLong>> safeLongSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeSafeLongSet;
    }

    public static ParamDecoder<Long> longParamDecoder(PlainSerDe serde) {
        return complexParamDecoder(serde, Long::parseLong);
    }

    public static ParamDecoder<OptionalLong> optionalLongParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(
                value -> OptionalLong.of(serde.deserializeComplex(value, Long::parseLong)), OptionalLong.empty());
    }

    public static CollectionParamDecoder<Long> longCollectionParamDecoder(PlainSerDe serde) {
        return value -> serde.deserializeComplex(value, Long::parseLong);
    }

    public static CollectionParamDecoder<OptionalLong> optionalLongCollectionParamDecoder(PlainSerDe serde) {
        return value -> {
            if (value == null || Iterables.isEmpty(value)) {
                return OptionalLong.empty();
            }
            return OptionalLong.of(serde.deserializeComplex(value, Long::parseLong));
        };
    }

    public static CollectionParamDecoder<List<Long>> longListCollectionParamDecoder(PlainSerDe serde) {
        return complexListCollectionParamDecoder(serde, Long::parseLong);
    }

    public static CollectionParamDecoder<Set<Long>> longSetCollectionParamDecoder(PlainSerDe serde) {
        return complexSetCollectionParamDecoder(serde, Long::parseLong);
    }

    public static ParamDecoder<UUID> uuidParamDecoder(PlainSerDe serde) {
        return serde::deserializeUuid;
    }

    public static ParamDecoder<Optional<UUID>> optionalUuidParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalUuid;
    }

    public static CollectionParamDecoder<UUID> uuidCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeUuid;
    }

    public static CollectionParamDecoder<Optional<UUID>> optionalUuidCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeOptionalUuid;
    }

    public static CollectionParamDecoder<List<UUID>> uuidListCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeUuidList;
    }

    public static CollectionParamDecoder<Set<UUID>> uuidSetCollectionParamDecoder(PlainSerDe serde) {
        return serde::deserializeUuidSet;
    }

    public static <T> ParamDecoder<T> complexParamDecoder(PlainSerDe serde, Function<String, T> factory) {
        return value -> serde.deserializeComplex(value, factory);
    }

    public static <T> ParamDecoder<Optional<T>> optionalComplexParamDecoder(
            PlainSerDe serde, Function<String, T> factory) {
        return DelegatingParamDecoder.of(
                value -> serde.deserializeOptionalComplex(ImmutableList.of(value), factory), Optional.empty());
    }

    public static <T> CollectionParamDecoder<T> complexCollectionParamDecoder(
            PlainSerDe serde, Function<String, T> factory) {
        return value -> serde.deserializeComplex(value, factory);
    }

    public static <T> CollectionParamDecoder<Optional<T>> optionalComplexCollectionParamDecoder(
            PlainSerDe serde, Function<String, T> factory) {
        return value -> serde.deserializeOptionalComplex(value, factory);
    }

    public static <T> CollectionParamDecoder<List<T>> complexListCollectionParamDecoder(
            PlainSerDe serde, Function<String, T> factory) {
        return value -> serde.deserializeComplexList(value, factory);
    }

    public static <T> CollectionParamDecoder<Set<T>> complexSetCollectionParamDecoder(
            PlainSerDe serde, Function<String, T> factory) {
        return value -> serde.deserializeComplexSet(value, factory);
    }

    private static final class DelegatingParamDecoder<T> implements ParamDecoder<T> {

        private final Function<String, T> factory;
        private final Optional<T> noValuePresent;

        private DelegatingParamDecoder(Function<String, T> factory, Optional<T> noValuePresent) {
            this.factory = factory;
            this.noValuePresent = noValuePresent;
        }

        static <T> DelegatingParamDecoder<T> of(Function<String, T> factory, T noValuePresent) {
            return new DelegatingParamDecoder<>(factory, Optional.of(noValuePresent));
        }

        @Override
        public T decode(String value) {
            return factory.apply(value);
        }

        @Override
        public Optional<T> noValuePresent() {
            return noValuePresent;
        }
    }

    private ParamDecoders() {}
}
