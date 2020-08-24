/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java;

import com.google.common.annotations.Beta;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import org.immutables.value.Value;

@Value.Immutable
public interface Options {

    /**
     * Instructs the {@link JerseyServiceGenerator} to generate binary response endpoints with the {@link Response}
     * type.
     */
    @Value.Default
    default boolean jerseyBinaryAsResponse() {
        return false;
    }

    /**
     * Instructs the {@link JerseyServiceGenerator} to add {@link NotNull} annotations to all auth parameters, as well
     * as all non-optional body params on service endpoints.
     */
    @Value.Default
    default boolean requireNotNullAuthAndBodyParams() {
        return false;
    }

    /** Undertow generated service interfaces are generated with an "Undertow" prefix. */
    @Value.Default
    default boolean undertowServicePrefix() {
        return false;
    }

    /** Use the conjure immutable "Bytes" class over ByteBuffer. */
    @Value.Default
    default boolean useImmutableBytes() {
        return false;
    }

    /**
     * Instructs the {@link com.palantir.conjure.java.services.UndertowServiceGenerator} to generate service endpoints
     * returning {@link com.google.common.util.concurrent.ListenableFuture} to allow asynchronous request processing.
     */
    @Value.Default
    default boolean undertowListenableFutures() {
        return false;
    }

    /**
     * Allows synchronous and {@link com.google.common.util.concurrent.ListenableFuture} based asynchronous request
     * handling to be mixed in a single module using {@link com.palantir.conjure.spec.EndpointDefinition#getMarkers()}.
     * This feature is experimental and subject to change.
     */
    @Beta
    @Value.Default
    default boolean experimentalUndertowAsyncMarkers() {
        return false;
    }

    /**
     * Instructs the {@link ObjectGenerator} to not generate objects that fail to deserialize if unknown fields are
     * encountered.
     */
    @Value.Default
    default boolean strictObjects() {
        return false;
    }

    /**
     * Instructs the {@link ObjectGenerator} to generate objects that fail to deserialize collections with null values.
     */
    @Value.Default
    default boolean nonNullCollections() {
        return false;
    }

    /**
     * Instructs the service generator to use top-level collections that fail to deserialize collections with null
     * values.
     */
    @Value.Default
    default boolean nonNullTopLevelCollectionValues() {
        return false;
    }

    Optional<String> packagePrefix();

    Optional<String> apiVersion();

    class Builder extends ImmutableOptions.Builder {}

    static Builder builder() {
        return new Builder();
    }

    static Options empty() {
        return builder().build();
    }
}
