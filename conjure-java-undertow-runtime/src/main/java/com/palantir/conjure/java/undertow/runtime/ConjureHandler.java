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
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Conjure routing mechanism which can be registered as an Undertow {@link HttpHandler}.
 * This handler takes care of exception handling, tracing, and web-security headers
 * among {@link ConjureHandler#WRAPPERS other features}.
 */
public final class ConjureHandler implements HttpHandler {

    private static final ImmutableList<BiFunction<Endpoint, HttpHandler, HttpHandler>> WRAPPERS =
            ImmutableList.<BiFunction<Endpoint, HttpHandler, HttpHandler>>of(
            // Allow the server to configure UndertowOptions.DECODE_URL = false to allow slashes in parameters.
            // Servers which do not configure DECODE_URL will still work properly except for encoded slash values.
            // When DECODE_URL has not been disabled, the following handler will no-op
            (endpoint, handler) -> new URLDecodingHandler(handler, "UTF-8"),
            // no-cache and web-security handlers add listeners for the response to be committed,
            // they can be executed on the IO thread.
            (endpoint, handler) -> Methods.GET.equals(endpoint.method())
                    // Only applies to GET methods
                    ? new NoCachingResponseHandler(handler) : handler,
            (endpoint, handler) -> new WebSecurityHandler(handler),
            // It is vitally important to never run blocking operations on the initial IO thread otherwise
            // the server will not process new requests. all handlers executed after BlockingHandler
            // use the larger task pool which is allowed to block. Any operation which sets thread
            // state (e.g. SLF4J MDC or Tracer) must execute on the blocking thread otherwise state
            // will not propagate to the wrapped service.
            (endpoint, handler) -> new BlockingHandler(handler),
            // Logging context and trace handler must execute prior to the exception
            // to provide user and trace information on exceptions.
            (endpoint, handler) -> new LoggingContextHandler(handler),
            (endpoint, handler) -> new TracedOperationHandler(
                    handler, endpoint.method() + " " + endpoint.template()),
            (endpoint, handler) -> new ConjureExceptionHandler(handler)
    ).reverse();

    private final RoutingHandler routingHandler;

    private ConjureHandler(HttpHandler fallback, List<Endpoint> endpoints) {
        this.routingHandler = Handlers.routing().setFallbackHandler(fallback);
        endpoints.forEach(this::register);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        routingHandler.handleRequest(exchange);
    }

    private void register(Endpoint endpoint) {
        HttpHandler current = endpoint.handler();
        for (BiFunction<Endpoint, HttpHandler, HttpHandler> wrapper : WRAPPERS) {
            current = wrapper.apply(endpoint, current);
        }
        routingHandler.add(endpoint.method(), endpoint.template(), current);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

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
            return new ConjureHandler(fallback, endpoints);
        }

        private void checkOverlappingPaths() {
            List<String> duplicates = endpoints.stream()
                    .collect(Collectors.groupingBy(
                            endpoint -> String.format(
                                    "%s: %s",
                                    endpoint.method(),
                                    endpoint.template().replaceAll("\\{.*\\}", "{}"))))
                    .entrySet()
                    .stream().filter(groups -> groups.getValue().size() > 1)
                    .map(entry -> {
                        String services = entry.getValue().stream()
                                .map(endpoint -> String.format("%s.%s", endpoint.serviceName(), endpoint.name()))
                                .collect(Collectors.joining(", "));
                        return String.format("%s: %s", entry.getKey(), services);
                    }).collect(Collectors.toList());
            if (!duplicates.isEmpty()) {
                throw new SafeIllegalArgumentException(
                        "The same route is declared by multiple UndertowServices",
                        SafeArg.of("duplicates", duplicates));
            }
        }
    }
}
