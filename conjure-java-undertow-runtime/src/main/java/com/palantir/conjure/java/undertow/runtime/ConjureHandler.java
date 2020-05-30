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

package com.palantir.conjure.java.undertow.runtime;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.logsafe.exceptions.SafeIllegalStateException;
import com.palantir.tracing.undertow.TracedOperationHandler;
import com.palantir.tracing.undertow.TracedRequestHandler;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.URLDecodingHandler;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Conjure routing mechanism which can be registered as an Undertow {@link HttpHandler}. This handler takes care of
 * exception handling, tracing, and web-security headers.
 */
public final class ConjureHandler implements HttpHandler {

    private final RoutingHandler routingHandler;

    private ConjureHandler(HttpHandler fallback, List<Endpoint> endpoints) {
        this.routingHandler = Handlers.routing()
                .setFallbackHandler(fallback)
                // The method may be valid for another handlers, the
                // fallback handler will be used instead of 405 status.
                .setInvalidMethodHandler(null);
        endpoints.forEach(this::register);
        registerOptionsEndpoints(routingHandler, endpoints);
    }

    private static void registerOptionsEndpoints(RoutingHandler routingHandler, List<Endpoint> endpoints) {
        endpoints.stream()
                .collect(ImmutableSetMultimap.toImmutableSetMultimap(
                        endpoint -> normalizeTemplate(endpoint.template()), Endpoint::method))
                .asMap()
                .forEach((normalizedPath, methods) -> routingHandler.add(
                        Methods.OPTIONS,
                        normalizedPath,
                        new WebSecurityHandler(new OptionsHandler(ImmutableSet.copyOf(methods)))));
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        routingHandler.handleRequest(exchange);
    }

    private void register(Endpoint endpoint) {
        routingHandler.add(endpoint.method(), endpoint.template(), endpoint.handler());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static final ImmutableSet<HttpString> ALLOWED_METHODS =
                ImmutableSet.of(Methods.GET, Methods.PUT, Methods.POST, Methods.DELETE);

        private final List<EndpointHandlerWrapper> wrappersJustBeforeBlocking = new ArrayList<>();

        private final List<Endpoint> endpoints = new ArrayList<>();
        private HttpHandler fallback = ResponseCodeHandler.HANDLE_404;

        private Builder() {}

        @CanIgnoreReturnValue
        public Builder endpoints(Endpoint value) {
            Preconditions.checkNotNull(value, "Value is required");
            if (!ALLOWED_METHODS.contains(value.method())) {
                throw new SafeIllegalStateException(
                        "Endpoint method is not recognized",
                        SafeArg.of("method", value.method()),
                        SafeArg.of("template", value.template()),
                        SafeArg.of("service", value.serviceName()),
                        SafeArg.of("name", value.name()));
            }
            endpoints.add(value);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder addAllEndpoints(Iterable<Endpoint> values) {
            Preconditions.checkNotNull(values, "Values is required");
            for (Endpoint endpoint : values) {
                endpoints(endpoint);
            }
            return this;
        }

        /**
         * The fallback {@link HttpHandler handler} is invoked when no {@link Endpoint} matches a request. By default a
         * 404 response status will be served.
         */
        @CanIgnoreReturnValue
        public Builder fallback(HttpHandler value) {
            fallback = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        /**
         * This MUST only be used for non-blocking operations that are meant to be run on the io-thread. For blocking
         * operations, please wrap the UndertowService themselves If you call this multiple time, the last wrapper will
         * be applied last, meaning it will be wrapped by the previously added {@link EndpointHandlerWrapper}s.
         */
        @CanIgnoreReturnValue
        public Builder addWrapperBeforeBlocking(EndpointHandlerWrapper wrapper) {
            wrappersJustBeforeBlocking.add(wrapper);
            return this;
        }

        public HttpHandler build() {
            checkOverlappingPaths();

            ImmutableList<EndpointHandlerWrapper> wrappers = ImmutableList.<EndpointHandlerWrapper>builder()
                    .add(
                            // Begin the server span as early as possible to capture the most of the request.
                            endpoint -> Optional.of(new TracedRequestHandler(endpoint.handler())),
                            // Allow the server to configure UndertowOptions.DECODE_URL = false to allow slashes in
                            // parameters. Servers which do not configure DECODE_URL will still work properly except
                            // for encoded slash values. When DECODE_URL has not been disabled, the following handler
                            // will no-op
                            endpoint -> Optional.of(new URLDecodingHandler(endpoint.handler(), "UTF-8")),
                            // no-cache and web-security handlers add listeners for the response to be committed,
                            // they can be executed on the IO thread.
                            endpoint -> Methods.GET.equals(endpoint.method())
                                    // Only applies to GET methods
                                    ? Optional.of(new NoCachingResponseHandler(endpoint.handler()))
                                    : Optional.empty(),
                            endpoint -> Optional.of(new WebSecurityHandler(endpoint.handler())),
                            endpoint -> endpoint.deprecated()
                                    .map(_reason -> new DeprecationReportingResponseHandler(endpoint.handler())))
                    // Apply custom non-blocking handlers just before the BlockingHandler
                    .addAll(wrappersJustBeforeBlocking)
                    // It is vitally important to never run blocking operations on the initial IO thread otherwise
                    // the server will not process new requests. all handlers executed after BlockingHandler
                    // use the larger task pool which is allowed to block. Any operation which sets thread
                    // state (e.g. SLF4J MDC or Tracer) must execute on the blocking thread otherwise state
                    // will not propagate to the wrapped service.
                    .add(
                            endpoint -> Optional.of(new BlockingHandler(endpoint.handler())),
                            // Logging context and trace handler must execute prior to the exception
                            // to provide user and trace information on exceptions.
                            endpoint -> Optional.of(new LoggingContextHandler(endpoint.handler())),
                            endpoint -> Optional.of(new TracedOperationHandler(
                                    endpoint.handler(), endpoint.method() + " " + endpoint.template())),
                            endpoint -> Optional.of(new ConjureExceptionHandler(endpoint.handler())))
                    .build()
                    .reverse();

            return new ConjureHandler(
                    fallback,
                    endpoints.stream()
                            .map(endpoint -> wrap(endpoint, wrappers))
                            .collect(ImmutableList.toImmutableList()));
        }

        private Endpoint wrap(Endpoint input, List<EndpointHandlerWrapper> wrappers) {
            Endpoint current = input;
            for (EndpointHandlerWrapper wrapper : wrappers) {
                current = Endpoints.map(current, wrapper);
            }
            return current;
        }

        private void checkOverlappingPaths() {
            Set<String> duplicates = endpoints.stream()
                    .collect(Collectors.groupingBy(endpoint ->
                            String.format("%s: %s", endpoint.method(), normalizeTemplate(endpoint.template()))))
                    .entrySet()
                    .stream()
                    .filter(groups -> groups.getValue().size() > 1)
                    .map(entry -> {
                        String services = entry.getValue().stream()
                                .map(endpoint -> String.format("%s.%s", endpoint.serviceName(), endpoint.name()))
                                .collect(Collectors.joining(", "));
                        return String.format("%s: %s", entry.getKey(), services);
                    })
                    .collect(Collectors.toSet());
            if (!duplicates.isEmpty()) {
                throw new SafeIllegalArgumentException(
                        "The same route is declared by multiple UndertowServices",
                        SafeArg.of("duplicates", duplicates));
            }
        }
    }

    private static String normalizeTemplate(String template) {
        return template.replaceAll("\\{.*?\\}", "{param}");
    }
}
