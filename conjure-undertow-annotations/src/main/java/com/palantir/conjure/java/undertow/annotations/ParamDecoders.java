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

import com.google.common.collect.Iterables;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ParamDecoders {

    public static ParamDecoder<String> stringDecoder() {
        return DelegateDecoder.of(Function.identity());
    }

    public static ParamDecoder<Boolean> booleanDecoder() {
        return DelegateDecoder.of(Boolean::valueOf);
    }

    public static ParamDecoder<Integer> integerDecoder() {
        return DelegateDecoder.of(Integer::valueOf);
    }

    /** Expects only a single value to be present. Delegates to the provided {@link ParamDecoder}. */
    public static final class OnlyElementCollectionParamDecoder<T> implements CollectionParamDecoder<T> {

        private final ParamDecoder<T> paramDecoder;

        public OnlyElementCollectionParamDecoder(ParamDecoder<T> paramDecoder) {
            this.paramDecoder = paramDecoder;
        }

        @Override
        public T decode(Collection<String> value) {
            return paramDecoder.decode(Iterables.getOnlyElement(value));
        }
    }

    /** Decodes all elements of the provided collection. Delegates to the provided {@link ParamDecoder}. */
    public static final class AllElementsCollectionParamDecoder<T> implements CollectionParamDecoder<List<T>> {

        private final ParamDecoder<T> paramDecoder;

        public AllElementsCollectionParamDecoder(ParamDecoder<T> paramDecoder) {
            this.paramDecoder = paramDecoder;
        }

        @Override
        public List<T> decode(Collection<String> value) {
            return value.stream().map(paramDecoder::decode).collect(Collectors.toList());
        }
    }

    /** Returns {@link Optional#empty} if no value is present. Delegates to the provided {@link ParamDecoder}. */
    public static final class OptionalCollectionParamDecoder<T> implements CollectionParamDecoder<Optional<T>> {

        private final ParamDecoder<T> paramDecoder;

        public OptionalCollectionParamDecoder(ParamDecoder<T> paramDecoder) {
            this.paramDecoder = paramDecoder;
        }

        @Override
        public Optional<T> decode(Collection<String> value) {
            if (value.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(paramDecoder.decode(Iterables.getOnlyElement(value)));
        }
    }

    private static final class DelegateDecoder<T> implements ParamDecoder<T> {

        private final Function<String, T> decodeFunc;

        static <T> DelegateDecoder<T> of(Function<String, T> decodeFunc) {
            return new DelegateDecoder<>(decodeFunc);
        }

        private DelegateDecoder(Function<String, T> decodeFunc) {
            this.decodeFunc = decodeFunc;
        }

        @Override
        public T decode(String value) {
            if (value == null) {
                throw new SafeIllegalArgumentException("Value is required");
            }
            return decodeFunc.apply(value);
        }
    }

    private ParamDecoders() {}
}
