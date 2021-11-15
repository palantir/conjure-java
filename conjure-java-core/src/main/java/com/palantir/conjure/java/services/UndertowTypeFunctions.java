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
import com.palantir.humanreadabletypes.HumanReadableDuration;
import com.palantir.logsafe.SafeArg;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class UndertowTypeFunctions {

    private static final Pattern ASYNC_TAG = Pattern.compile("server-async(\\{timeout=(.*?)})?");

    /**
     * Asynchronous-processing capable endpoints are generated if any of the following are true.
     *
     * <ul>
     *   <li>The {@link EndpointDefinition} is {@link EndpointDefinition#getTags() tagged} with {@code server-async}
     *   <li>The {@link EndpointDefinition} is {@link EndpointDefinition#getTags() tagged} with
     *       {@code server-async{timeout=10 minutes}} using a timeout value based on a {@link HumanReadableDuration}.
     *   <li>The {@link Options#undertowListenableFutures()} is set
     *   <li>Experimental: Both {@link Options#experimentalUndertowAsyncMarkers()} is set and
     *       {@link EndpointDefinition#getMarkers()} contains an imported annotation with name
     *       <pre>Async</pre>.
     * </ul>
     */
    static boolean isAsync(EndpointDefinition endpoint, Options options) {
        return async(endpoint, options).isPresent();
    }

    /**
     * Asynchronous-processing capable endpoints are generated if any of the following are true:
     *
     * <ul>
     *   <li>The {@link EndpointDefinition} is {@link EndpointDefinition#getTags() tagged} with {@code server-async}
     *   <li>The {@link EndpointDefinition} is {@link EndpointDefinition#getTags() tagged} with
     *       {@code server-async{timeout=10 minutes}} using a timeout value based on a {@link HumanReadableDuration}.
     *   <li>The {@link Options#undertowListenableFutures()} is set
     *   <li>Experimental: Both {@link Options#experimentalUndertowAsyncMarkers()} is set and
     *       {@link EndpointDefinition#getMarkers()} contains an imported annotation with name
     *       <pre>Async</pre>.
     * </ul>
     *
     * This method returns a {@link AsyncRequestProcessingMetadata} when async processing is enabled, which provides
     * a timeout duration used to limit the maximum asynchronous processing time.
     */
    static Optional<AsyncRequestProcessingMetadata> async(EndpointDefinition endpoint, Options options) {
        Optional<AsyncRequestProcessingMetadata> result = endpoint.getTags().stream()
                .map(ASYNC_TAG::matcher)
                .filter(Matcher::matches)
                .map(matcher -> {
                    String timeout = matcher.groupCount() == 2 ? matcher.group(2) : null;
                    if (timeout != null) {
                        HumanReadableDuration duration = HumanReadableDuration.valueOf(timeout);
                        return new AsyncRequestProcessingMetadata(duration);
                    }
                    return new AsyncRequestProcessingMetadata();
                })
                .findFirst();
        if (result.isPresent()) {
            return result;
        }
        return options.undertowListenableFutures()
                        || (options.experimentalUndertowAsyncMarkers()
                                && endpoint.getMarkers().stream()
                                        .anyMatch(marker -> marker.accept(IsUndertowAsyncMarkerVisitor.INSTANCE)))
                ? Optional.of(new AsyncRequestProcessingMetadata())
                : Optional.empty();
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

    static final class AsyncRequestProcessingMetadata {
        private final Optional<HumanReadableDuration> timeout;

        AsyncRequestProcessingMetadata() {
            timeout = Optional.empty();
        }

        AsyncRequestProcessingMetadata(HumanReadableDuration timeout) {
            this.timeout = Optional.of(timeout);
            Preconditions.checkState(timeout.toNanoseconds() > 0L, "Timeout must be positive");
        }

        Optional<HumanReadableDuration> timeout() {
            return timeout;
        }

        @Override
        public String toString() {
            return "AsyncRequestProcessingMetadata{timeout=" + timeout + '}';
        }
    }
}
