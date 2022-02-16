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

import com.palantir.conjure.java.undertow.annotations.DeserializerFactory;
import com.palantir.conjure.java.undertow.annotations.Handle;
import com.palantir.conjure.java.undertow.annotations.HttpMethod;
import com.palantir.conjure.java.undertow.lib.Deserializer;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;

public interface PrimitiveBodyParam {

    @Handle(method = HttpMethod.POST, path = "/post")
    void handlePrimitiveBody(@Handle.Body(IntParamDeserializerFactory.class) int count);

    enum IntParamDeserializerFactory implements DeserializerFactory<Integer> {
        INSTANCE;

        @SuppressWarnings("unchecked")
        @Override
        public <T extends Integer> Deserializer<T> deserializer(
                TypeMarker<T> _type, UndertowRuntime _runtime, Endpoint _endpoint) {
            return _exchange -> (T) (Integer) 1;
        }
    }
}
