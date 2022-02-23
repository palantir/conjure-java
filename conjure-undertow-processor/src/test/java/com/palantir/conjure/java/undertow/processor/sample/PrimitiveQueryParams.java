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

package com.palantir.conjure.java.undertow.processor.sample;

import com.google.common.collect.Iterables;
import com.palantir.conjure.java.undertow.annotations.CollectionParamDecoder;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import java.util.Collection;

public interface PrimitiveQueryParams {

    @Handle(method = HttpMethod.GET, path = "/ping")
    void handlePrimitiveQueryParams(
            @Handle.QueryParam(value = "count", decoder = IntParamDecoder.class) int count,
            @Handle.QueryParam(value = "test", decoder = BooleanParamDecoder.class) boolean test,
            @Handle.QueryParam(value = "testboxed", decoder = BooleanParamDecoder.class) Boolean testBoxed);

    enum IntParamDecoder implements CollectionParamDecoder<Integer> {
        INSTANCE;

        @Override
        public Integer decode(Collection<String> value) {
            return Integer.valueOf(Iterables.getOnlyElement(value));
        }
    }

    enum BooleanParamDecoder implements CollectionParamDecoder<Boolean> {
        INSTANCE;

        @Override
        public Boolean decode(Collection<String> value) {
            return Boolean.valueOf(Iterables.getOnlyElement(value));
        }
    }
}
