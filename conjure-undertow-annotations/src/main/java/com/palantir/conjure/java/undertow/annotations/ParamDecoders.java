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

import com.palantir.conjure.java.lib.SafeLong;
import com.palantir.conjure.java.undertow.lib.PlainSerDe;
import com.palantir.ri.ResourceIdentifier;
import com.palantir.tokens.auth.BearerToken;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

/** Default parameter decoders, mirroring and delegating to the deserializers provided by {@link PlainSerDe}. */
public final class ParamDecoders {

    public static ParamDecoder<String> stringParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeString);
    }

    public static ParamDecoder<Optional<String>> optionalStringParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalString, Optional.empty());
    }

    public static CollectionParamDecoder<String> stringCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeString);
    }

    public static CollectionParamDecoder<Optional<String>> optionalStringCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalString);
    }

    public static CollectionParamDecoder<List<String>> stringListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeStringList);
    }

    public static CollectionParamDecoder<Set<String>> stringSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeStringSet);
    }

    public static ParamDecoder<Boolean> booleanParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeBoolean);
    }

    public static ParamDecoder<Optional<Boolean>> optionalBooleanParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalBoolean, Optional.empty());
    }

    public static CollectionParamDecoder<Boolean> booleanCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeBoolean);
    }

    public static CollectionParamDecoder<Optional<Boolean>> optionalBooleanCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalBoolean);
    }

    public static CollectionParamDecoder<List<Boolean>> booleanListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeBooleanList);
    }

    public static CollectionParamDecoder<Set<Boolean>> booleanSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeBooleanSet);
    }

    public static ParamDecoder<BearerToken> bearerTokenParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeBearerToken);
    }

    public static ParamDecoder<Optional<BearerToken>> optionalBearerTokenParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalBearerToken, Optional.empty());
    }

    public static CollectionParamDecoder<BearerToken> bearerTokenCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeBearerToken);
    }

    public static CollectionParamDecoder<Optional<BearerToken>> optionalBearerTokenCollectionParamDecoder(
            PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalBearerToken);
    }

    public static CollectionParamDecoder<List<BearerToken>> bearerTokenListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeBearerTokenList);
    }

    public static CollectionParamDecoder<Set<BearerToken>> bearerTokenSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeBearerTokenSet);
    }

    public static ParamDecoder<OffsetDateTime> dateTimeParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeDateTime);
    }

    public static ParamDecoder<Optional<OffsetDateTime>> optionalDateTimeParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalDateTime, Optional.empty());
    }

    public static CollectionParamDecoder<OffsetDateTime> dateTimeCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeDateTime);
    }

    public static CollectionParamDecoder<Optional<OffsetDateTime>> optionalDateTimeCollectionParamDecoder(
            PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalDateTime);
    }

    public static CollectionParamDecoder<List<OffsetDateTime>> dateTimeListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeDateTimeList);
    }

    public static CollectionParamDecoder<Set<OffsetDateTime>> dateTimeSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeDateTimeSet);
    }

    public static ParamDecoder<Double> doubleParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeDouble);
    }

    public static ParamDecoder<OptionalDouble> optionalDoubleParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalDouble, OptionalDouble.empty());
    }

    public static CollectionParamDecoder<Double> doubleCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeDouble);
    }

    public static CollectionParamDecoder<OptionalDouble> optionalDoubleCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalDouble);
    }

    public static CollectionParamDecoder<List<Double>> doubleListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeDoubleList);
    }

    public static CollectionParamDecoder<Set<Double>> doubleSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeDoubleSet);
    }

    public static ParamDecoder<Integer> integerParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeInteger);
    }

    public static ParamDecoder<OptionalInt> optionalIntegerParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalInteger, OptionalInt.empty());
    }

    public static CollectionParamDecoder<Integer> integerCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeInteger);
    }

    public static CollectionParamDecoder<OptionalInt> optionalIntegerCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalInteger);
    }

    public static CollectionParamDecoder<List<Integer>> integerListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeIntegerList);
    }

    public static CollectionParamDecoder<Set<Integer>> integerSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeIntegerSet);
    }

    public static ParamDecoder<ResourceIdentifier> ridParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeRid);
    }

    public static ParamDecoder<Optional<ResourceIdentifier>> optionalRidParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalRid, Optional.empty());
    }

    public static CollectionParamDecoder<ResourceIdentifier> ridCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeRid);
    }

    public static CollectionParamDecoder<Optional<ResourceIdentifier>> optionalRidCollectionParamDecoder(
            PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalRid);
    }

    public static CollectionParamDecoder<List<ResourceIdentifier>> ridListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeRidList);
    }

    public static CollectionParamDecoder<Set<ResourceIdentifier>> ridSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeRidSet);
    }

    public static ParamDecoder<SafeLong> safeLongParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeSafeLong);
    }

    public static ParamDecoder<Optional<SafeLong>> optionalSafeLongParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalSafeLong, Optional.empty());
    }

    public static CollectionParamDecoder<SafeLong> safeLongCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeSafeLong);
    }

    public static CollectionParamDecoder<Optional<SafeLong>> optionalSafeLongCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalSafeLong);
    }

    public static CollectionParamDecoder<List<SafeLong>> safeLongListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeSafeLongList);
    }

    public static CollectionParamDecoder<Set<SafeLong>> safeLongSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeSafeLongSet);
    }

    public static ParamDecoder<UUID> uuidParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeUuid);
    }

    public static ParamDecoder<Optional<UUID>> optionalUuidParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalUuid, Optional.empty());
    }

    public static CollectionParamDecoder<UUID> uuidCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeUuid);
    }

    public static CollectionParamDecoder<Optional<UUID>> optionalUuidCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeOptionalUuid);
    }

    public static CollectionParamDecoder<List<UUID>> uuidListCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeUuidList);
    }

    public static CollectionParamDecoder<Set<UUID>> uuidSetCollectionParamDecoder(PlainSerDe serde) {
        return DelegatingCollectionParamDecoder.of(serde::deserializeUuidSet);
    }

    private static final class DelegatingParamDecoder<T> implements ParamDecoder<T> {

        private final Function<String, T> factory;
        private final Optional<T> noValuePresent;

        private DelegatingParamDecoder(Function<String, T> factory, Optional<T> noValuePresent) {
            this.factory = factory;
            this.noValuePresent = noValuePresent;
        }

        static <T> DelegatingParamDecoder<T> of(Function<String, T> factory) {
            return new DelegatingParamDecoder<>(factory, Optional.empty());
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

    private static final class DelegatingCollectionParamDecoder<T> implements CollectionParamDecoder<T> {

        private final Function<Collection<String>, T> factory;

        private DelegatingCollectionParamDecoder(Function<Collection<String>, T> factory) {
            this.factory = factory;
        }

        static <T> DelegatingCollectionParamDecoder<T> of(Function<Collection<String>, T> factory) {
            return new DelegatingCollectionParamDecoder<>(factory);
        }

        @Override
        public T decode(Collection<String> value) {
            return factory.apply(value);
        }
    }

    private ParamDecoders() {}
}
