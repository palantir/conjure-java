/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.services;

import com.palantir.conjure.java.types.TypeMapper;

/** Wrapper around various {@link TypeMapper type mappers} used by conjure-undertow generation. */
final class UndertowTypeMappers {

    private final TypeMapper request;
    private final TypeMapper handlerRequest;
    private final TypeMapper params;
    private final TypeMapper response;

    UndertowTypeMappers(TypeMapper request, TypeMapper handlerRequest, TypeMapper params, TypeMapper response) {
        this.request = request;
        this.handlerRequest = handlerRequest;
        this.params = params;
        this.response = response;
    }

    TypeMapper request() {
        return request;
    }

    // Handlers deserialize to immutable representations of the request body type.
    TypeMapper handlerRequest() {
        return handlerRequest;
    }

    TypeMapper params() {
        return params;
    }

    TypeMapper response() {
        return response;
    }
}
