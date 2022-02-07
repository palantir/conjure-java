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
import java.util.List;
import java.util.Optional;

public interface DefaultDecoders {

    @Handle(method = HttpMethod.GET, path = "/queryParam")
    String queryParam(
            @Handle.QueryParam(value = "q") String qParam,
            @Handle.QueryParam(value = "r") Boolean rParam,
            @Handle.QueryParam(value = "s") List<Integer> sParams,
            @Handle.QueryParam(value = "t") Optional<String> tParams,
            @Handle.QueryParam(value = "u", decoder = StringCollectionDecoder.class) String uParam);

    enum StringCollectionDecoder implements CollectionParamDecoder<String> {
        INSTANCE;

        @Override
        public String decode(Collection<String> value) {
            return Iterables.getOnlyElement(value);
        }
    }
}
