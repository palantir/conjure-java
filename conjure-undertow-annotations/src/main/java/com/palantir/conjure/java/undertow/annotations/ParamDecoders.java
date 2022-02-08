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

import com.palantir.conjure.java.undertow.lib.PlainSerDe;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/** Default parameter decoders, mirroring and delegating to the deserializers provided by {@link PlainSerDe}. */
public final class ParamDecoders {

    // TODO(fwindheuser): Add methods for all deserializers in 'PlainSerDe'.

    public static ParamDecoder<String> stringParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeString);
    }

    public static ParamDecoder<Optional<String>> optionalStringParamDecoder(PlainSerDe serde) {
        return DelegatingParamDecoder.of(serde::deserializeOptionalString);
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
        return DelegatingParamDecoder.of(serde::deserializeOptionalBoolean);
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

    private static final class DelegatingParamDecoder<T> implements ParamDecoder<T> {

        private final Function<String, T> factory;

        private DelegatingParamDecoder(Function<String, T> factory) {
            this.factory = factory;
        }

        static <T> DelegatingParamDecoder<T> of(Function<String, T> factory) {
            return new DelegatingParamDecoder<>(factory);
        }

        @Override
        public T decode(String value) {
            return factory.apply(value);
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
