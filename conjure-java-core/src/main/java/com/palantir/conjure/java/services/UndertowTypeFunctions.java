/*
 * (c) Copyright 2020 Palantir Technologies Inc. All rights reserved.
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

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ListenableFuture;
import com.palantir.conjure.java.Options;
import com.palantir.conjure.java.types.TypeMapper;
import com.palantir.conjure.spec.EndpointDefinition;
import com.palantir.logsafe.SafeArg;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

final class UndertowTypeFunctions {

    /**
     * Asynchronous-processing capable endpoints are generated if either of the following are true.
     *
     * <ul>
     *   <li>The {@link Options#undertowListenableFutures()} is set
     *   <li>Experimental: Both {@link Options#experimentalUndertowAsyncMarkers()} is set and
     *       {@link EndpointDefinition#getMarkers()} contains an imported annotation with name
     *       <pre>Async</pre>
     *       .
     * </ul>
     */
    static boolean isAsync(EndpointDefinition endpoint, Options options) {
        return options.undertowListenableFutures()
                || (options.experimentalUndertowAsyncMarkers()
                        && endpoint.getMarkers().stream()
                                .anyMatch(marker -> marker.accept(IsUndertowAsyncMarkerVisitor.INSTANCE)));
    }

    static ParameterizedTypeName getAsyncReturnType(EndpointDefinition endpoint, TypeMapper mapper, Options flags) {
        Preconditions.checkArgument(
                isAsync(endpoint, flags), "Endpoint must be async", SafeArg.of("endpoint", endpoint));
        return ParameterizedTypeName.get(
                ClassName.get(ListenableFuture.class),
                endpoint.getReturns()
                        .map(mapper::getClassName)
                        .orElseGet(() -> ClassName.get(Void.class))
                        .box());
    }

    private UndertowTypeFunctions() {}
}
