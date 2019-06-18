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
import com.google.common.collect.Lists;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.logsafe.Preconditions;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.tracing.undertow.TracedOperationHandler;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.server.handlers.URLDecodingHandler;
import io.undertow.util.Methods;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Conjure routing mechanism which can be registered as an Undertow {@link HttpHandler}.
 * This handler takes care of exception handling, tracing, and web-security headers.
 */
public final class ConjureHandler implements HttpHandler {

    private final EndpointHandlerWrapper stackedEndpointHandlerWrapper;
    private final RoutingHandler routingHandler;

    private ConjureHandler(
            HttpHandler fallback,
            List<Endpoint> endpoints,
            EndpointHandlerWrapper stackedEndpointHandlerWrapper) {
        this.routingHandler = Handlers.routing().setFallbackHandler(fallback);
        this.stackedEndpointHandlerWrapper = stackedEndpointHandlerWrapper;
        endpoints.forEach(this::register);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        routingHandler.handleRequest(exchange);
    }

    private void register(Endpoint endpoint) {
        routingHandler.add(
                endpoint.method(),
                endpoint.template(),
                Endpoints.map(endpoint, stackedEndpointHandlerWrapper).handler());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private static final ImmutableList<EndpointHandlerWrapper> WRAPPERS_BEFORE_BLOCKING =
                ImmutableList.of(
                        // Allow the server to configure UndertowOptions.DECODE_URL = false to allow slashes in
                        // parameters. Servers which do not configure DECODE_URL will still work properly except for
                        // encoded slash values. When DECODE_URL has not been disabled, the following handler will no-op
                        endpoint -> Optional.of(new URLDecodingHandler(endpoint.handler(), "UTF-8")),
                        // no-cache and web-security handlers add listeners for the response to be committed,
                        // they can be executed on the IO thread.
                        endpoint -> Methods.GET.equals(endpoint.method())
                                // Only applies to GET methods
                                ? Optional.of(new NoCachingResponseHandler(endpoint.handler()))
                                : Optional.empty(),
                        endpoint -> Optional.of(new WebSecurityHandler(endpoint.handler()))
                );
        // It is vitally important to never run blocking operations on the initial IO thread otherwise
        // the server will not process new requests. all handlers executed after BlockingHandler
        // use the larger task pool which is allowed to block. Any operation which sets thread
        // state (e.g. SLF4J MDC or Tracer) must execute on the blocking thread otherwise state
        // will not propagate to the wrapped service.
        private static final ImmutableList<EndpointHandlerWrapper> WRAPPERS_AFTER_BLOCKING =
                ImmutableList.of(
                        endpoint -> Optional.of(new BlockingHandler(endpoint.handler())),
                        // Logging context and trace handler must execute prior to the exception
                        // to provide user and trace information on exceptions.
                        endpoint -> Optional.of(new LoggingContextHandler(endpoint.handler())),
                        endpoint -> Optional.of(new TracedOperationHandler(
                                        endpoint.handler(), endpoint.method() + " " + endpoint.template())),
                        endpoint -> Optional.of(new ConjureExceptionHandler(endpoint.handler())));

        private ImmutableList<EndpointHandlerWrapper> wrappersJustBeforeBlocking =
                ImmutableList.of();

        /**
         * This MUST only be used for non-blocking operations that are meant to be run on the io-thread.
         * For blocking operations, please wrap the UndertowService themselves using {@link UndertowServices#map}.
         * If you call this multiple time, the last wrapper will be applied last, meaning it will wrap the previously
         * added {@link EndpointHandlerWrapper}s.
         */
        public Builder addEndpointHandlerWrapperBeforeBlocking(EndpointHandlerWrapper wrapper) {
            wrappersJustBeforeBlocking = ImmutableList.<EndpointHandlerWrapper>builder()
                    .addAll(wrappersJustBeforeBlocking)
                    .add(wrapper)
                    .build();
            return this;
        }

        private final List<Endpoint> endpoints = Lists.newArrayList();
        private HttpHandler fallback = ResponseCodeHandler.HANDLE_404;

        private Builder() { }

        @CanIgnoreReturnValue
        public Builder endpoints(Endpoint value) {
            endpoints.add(Preconditions.checkNotNull(value, "Value is required"));
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
         * The fallback {@link HttpHandler handler} is invoked when no {@link Endpoint} matches a request.
         * By default a 404 response status will be served.
         */
        @CanIgnoreReturnValue
        public Builder fallback(HttpHandler value) {
            fallback = Preconditions.checkNotNull(value, "Value is required");
            return this;
        }

        public HttpHandler build() {
            checkOverlappingPaths();
            EndpointHandlerWrapper current = endpoint -> Optional.empty();
            current = stackEndpointHandlerWrapper(current, WRAPPERS_AFTER_BLOCKING.reverse());
            current = stackEndpointHandlerWrapper(current, wrappersJustBeforeBlocking);
            current = stackEndpointHandlerWrapper(current, WRAPPERS_BEFORE_BLOCKING.reverse());
            return new ConjureHandler(fallback, endpoints, current);
        }

        private EndpointHandlerWrapper stackEndpointHandlerWrapper(
                EndpointHandlerWrapper initial,
                Collection<EndpointHandlerWrapper> wrappers) {
            return wrappers.stream().reduce(
                    initial,
                    (wrapper1, wrapper2) ->
                            endpoint -> {
                                Endpoint nEndpoint = Endpoints.map(endpoint, wrapper1);
                                return Optional.of(wrapper2.wrap(nEndpoint).orElseGet(nEndpoint::handler));
                            });
        }

        private void checkOverlappingPaths() {
            Set<String> duplicates = endpoints.stream()
                    .collect(Collectors.groupingBy(
                            endpoint -> String.format(
                                    "%s: %s",
                                    endpoint.method(),
                                    endpoint.template().replaceAll("\\{.*?\\}", "{param}"))))
                    .entrySet()
                    .stream().filter(groups -> groups.getValue().size() > 1)
                    .map(entry -> {
                        String services = entry.getValue().stream()
                                .map(endpoint -> String.format("%s.%s", endpoint.serviceName(), endpoint.name()))
                                .collect(Collectors.joining(", "));
                        return String.format("%s: %s", entry.getKey(), services);
                    }).collect(Collectors.toSet());
            if (!duplicates.isEmpty()) {
                throw new SafeIllegalArgumentException(
                        "The same route is declared by multiple UndertowServices",
                        SafeArg.of("duplicates", duplicates));
            }
        }
    }
}
