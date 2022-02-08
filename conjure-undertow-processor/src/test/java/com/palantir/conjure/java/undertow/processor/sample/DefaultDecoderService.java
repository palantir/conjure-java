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
import com.palantir.conjure.java.undertow.annotations.ParamDecoder;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DefaultDecoderService {

    @Handle(method = HttpMethod.GET, path = "/queryParam")
    String queryParam(
            @Handle.QueryParam(value = "stringParam") String stringParam,
            @Handle.QueryParam(value = "booleanParam") Boolean booleanParam,
            @Handle.QueryParam(value = "stringSetParam") Set<String> stringSetParam,
            @Handle.QueryParam(value = "stringListParam") List<String> stringListParam,
            @Handle.QueryParam(value = "optionalStringParam") Optional<String> optionalStringParam,
            @Handle.QueryParam(value = "decoderParam", decoder = StringCollectionDecoder.class) String decoderParam);

    @Handle(method = HttpMethod.GET, path = "/headers")
    String headers(
            @Handle.Header(value = "stringParam") String stringParam,
            @Handle.Header(value = "booleanParam") Boolean booleanParam,
            @Handle.Header(value = "stringSetParam") Set<String> stringSetParam,
            @Handle.Header(value = "stringListParam") List<String> stringListParam,
            @Handle.Header(value = "optionalStringParam") Optional<String> optionalStringParam,
            @Handle.Header(value = "decoderParam", decoder = StringCollectionDecoder.class) String decoderParam);

    @Handle(method = HttpMethod.GET, path = "/pathParam/{stringParam}/{booleanParam}/{decoderParam}")
    String pathParam(
            @Handle.PathParam String stringParam,
            @Handle.PathParam Boolean booleanParam,
            @Handle.PathParam(decoder = StringDecoder.class) String decoderParam);

    enum StringCollectionDecoder implements CollectionParamDecoder<String> {
        INSTANCE;

        @Override
        public String decode(Collection<String> value) {
            return Iterables.getOnlyElement(value);
        }
    }

    enum StringDecoder implements ParamDecoder<String> {
        INSTANCE;

        @Override
        public String decode(String value) {
            return value;
        }
    }
}
