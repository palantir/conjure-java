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
import org.immutables.value.Value;

@Value.Immutable
public interface Options {

    /**
     * Instructs the {@link JerseyServiceGenerator} to generate binary response endpoints with the
     * {@code javax.ws.rs.core.Response} type.
     */
    @Value.Default
    default boolean jerseyBinaryAsResponse() {
        return false;
    }

    /**
     * Instructs the {@link JerseyServiceGenerator} to add {@link jakarta.validation.constraints.NotNull} annotations to all auth parameters, as well
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
     * Instructs the code generator, when generating JAX-RS classes to generate them with the <code>jakarta.</code>
     * prefix instead of the legacy <code>jaxrs.</code> prefix. At the moment for backwards compatibility
     * this is default false, with the intention that when enough adoption has occurred that this will become
     * default true in the future.
     */
    @Value.Default
    default boolean jakartaPackages() {
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
        return nonNullCollections();
    }

    /**
     * Generates compile-time safe builders to ensure all attributes are set, except those of type list, set, map, or
     * optional. If {@link Options#useStrictStagedBuilders} is true, this option is ignored.
     */
    @Value.Default
    default boolean useStagedBuilders() {
        return false;
    }

    /**
     * Generates compile-time safe builders to ensure all attributes are set.
     */
    @Value.Default
    default boolean useStrictStagedBuilders() {
        return false;
    }

    /**
     * Generated objects which exclude fields with empty optional values.
     */
    @Value.Default
    default boolean excludeEmptyOptionals() {
        return false;
    }

    /**
     * Generated objects exclude fields with empty collection (list, set, and map) values.
     */
    @Value.Default
    default boolean excludeEmptyCollections() {
        return false;
    }

    /**
     * Instructs the object generator to generate union visitors that expose the values of unknowns in addition to their
     * types.
     */
    @Value.Default
    default boolean unionsWithUnknownValues() {
        return false;
    }

    /**
     * Instructs the {@link com.palantir.conjure.java.types.ErrorGenerator} to add {@code Contract} annotations
     * to check methods for stronger static analysis.
     */
    @Value.Default
    default boolean jetbrainsContractAnnotations() {
        return false;
    }

    /** When set, external type imports are generated as their fallback types. */
    @Value.Default
    default boolean externalFallbackTypes() {
        return false;
    }

    /**
     * Java 24's loom support removes the need for async interfaces. When set to true, only Dialogue interfaces for
     * blocking clients will be generated. The Dialogue async interfaces will not be generated.
     */
    @Value.Default
    default boolean excludeDialogueAsyncInterfaces() {
        return false;
    }

    /**
     * If set to true, static factory methods ('of') will be excluded from generated objects with one or more fields.
     * Note that for objects without any fields, static factory methods will still be generated.
     */
    @Value.Default
    default boolean preferObjectBuilders() {
        return false;
    }

    /**
     * Enabling this option will enable associating errors with endpoints, which currently causes parameters of these
     * errors to be serialized as JSON. This is a wire break and can cause errors for clients that parse error
     * parameters. Do not enable this.
     * <p>
     * This flag has been temporarily added to help us iterate on a safer rollout strategy for endpoint-associated
     * errors.
     * <p>
     * This flag will eventually be removed, and you should not use it without letting the Conjure maintainers know.
     */
    @Value.Default
    default boolean dangerousDoNotUseEnableEndpointAssociatedErrors() {
        return false;
    }

    /**
     * Enabling this flag will lead to the generation of Dialogue client interfaces that are able to send a header to
     * servers specifying the Conjure error parameter serialization format they expect, and create custom exceptions for
     * each error defined in the service's package.
     */
    @Value.Default
    @Beta
    default boolean generateErrorParameterFormatRespectingDialogueInterfaces() {
        return false;
    }

    /**
     * Makes immutable copies of collections for union and alias types, respecting the --nonNullCollections flag.
     */
    @Value.Default
    default boolean defensiveCollections() {
        return false;
    }

    /**
     * Uses sealed classes for union type generation.
     * <p>
     * Warning: Disabling this flag after publishing union objects with this flag enabled is an ABI break (additional
     * methods which are generated for sealed unions would be removed).
     */
    @Value.Default
    default boolean sealedUnions() {
        return false;
    }

    /**
     * If {@link #sealedUnions} is enabled, this enables visitor generation for back-compatibility.
     * <p>
     * Warning: Disabling this flag after publishing union objects with this flag enabled is an ABI break (all visitor
     * related methods for sealed union types would be removed).
     */
    @Value.Default
    default boolean sealedUnionVisitors() {
        return true;
    }

    /**
     * If enabled, {@code toString} methods render {@code @DoNotLog}-annotated fields as "REDACTED" instead of including
     * the actual value. A separate {@code dangerousToString} method (itself annotated {@code @DoNotLog}) is generated.
     */
    @Value.Default
    default boolean redactToStringDoNotLog() {
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
