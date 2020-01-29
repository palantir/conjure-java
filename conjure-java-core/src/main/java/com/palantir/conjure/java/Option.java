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
import java.util.function.Predicate;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import org.derive4j.Data;

@Data
public interface Option {
    Predicate<Option> IsJerseyBinaryAsResponse =
            Options.cases().jerseyBinaryAsResponse(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsRequireNotNullAuthAndBodyParams =
            Options.cases().requireNotNullAuthAndBodyParams(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsUndertowServicePrefix =
            Options.cases().undertowServicePrefix(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsUseImmutableBytes =
            Options.cases().useImmutableBytes(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsUndertowListenableFutures =
            Options.cases().undertowListenableFutures(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsExperimentalUndertowAsyncMarkers =
            Options.cases().experimentalUndertowAsyncMarkers(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsStrictObjects =
            Options.cases().strictObjects(() -> true).otherwise(() -> false)::apply;
    Predicate<Option> IsNonNullCollections =
            Options.cases().nonNullCollections(() -> true).otherwise(() -> false)::apply;

    interface Cases<T> {
        /**
         * Instructs the {@link JerseyServiceGenerator} to generate binary response endpoints with the {@link Response}
         * type.
         */
        T jerseyBinaryAsResponse();

        /**
         * Instructs the {@link JerseyServiceGenerator} to add {@link NotNull} annotations to all auth parameters, as
         * well as all non-optional body params on service endpoints.
         */
        T requireNotNullAuthAndBodyParams();

        /** Undertow generated service interfaces are generated with an "Undertow" prefix. */
        T undertowServicePrefix();

        /** Use the conjure immutable "Bytes" class over ByteBuffer. */
        T useImmutableBytes();

        /**
         * Instructs the {@link com.palantir.conjure.java.services.UndertowServiceGenerator} to generate service
         * endpoints returning {@link com.google.common.util.concurrent.ListenableFuture} to allow asynchronous request
         * processing.
         */
        T undertowListenableFutures();

        /**
         * Allows synchronous and {@link com.google.common.util.concurrent.ListenableFuture} based asynchronous request
         * handling to be mixed in a single module using
         * {@link com.palantir.conjure.spec.EndpointDefinition#getMarkers()}. This feature is experimental and
         * subject to change.
         */
        @Beta
        T experimentalUndertowAsyncMarkers();

        /**
         * Instructs the {@link ObjectGenerator} to not generate objects that fail to deserialize if unknown fields are
         * encountered.
         */
        T strictObjects();

        /**
         * Instructs the {@link ObjectGenerator} to generate objects that fail to deserialize collections with null
         * values.
         */
        T nonNullCollections();
    }

    <T> T matches(Cases<T> cases);
}
