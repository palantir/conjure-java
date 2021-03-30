/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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
import com.google.common.collect.ImmutableListMultimap;
import com.palantir.conjure.java.undertow.lib.Contexts;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.RequestContext;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;

enum ConjureContexts implements Contexts {
    INSTANCE;

    @Override
    public RequestContext createContext(HttpServerExchange exchange, Endpoint _endpoint) {
        return new ConjureServerRequestContext(exchange);
    }

    private static final class ConjureServerRequestContext implements RequestContext {

        private final HttpServerExchange exchange;

        private ImmutableListMultimap<String, String> cachedQueryParams;

        ConjureServerRequestContext(HttpServerExchange exchange) {
            this.exchange = exchange;
        }

        @Override
        public List<String> header(String headerName) {
            HeaderValues header = exchange.getRequestHeaders().get(headerName);
            return header == null ? ImmutableList.of() : Collections.unmodifiableList(header);
        }

        @Override
        public Optional<String> firstHeader(String headerName) {
            return Optional.ofNullable(exchange.getRequestHeaders().getFirst(headerName));
        }

        @Override
        public ImmutableListMultimap<String, String> queryParameters() {
            ImmutableListMultimap<String, String> cachedQueryParamsSnapshot = cachedQueryParams;
            if (cachedQueryParamsSnapshot == null) {
                cachedQueryParamsSnapshot = buildQueryParameters();
                cachedQueryParams = cachedQueryParamsSnapshot;
            }
            return cachedQueryParamsSnapshot;
        }

        private ImmutableListMultimap<String, String> buildQueryParameters() {
            Map<String, Deque<String>> rawQueryParameters = exchange.getQueryParameters();
            if (rawQueryParameters.isEmpty()) {
                return ImmutableListMultimap.of();
            }
            ImmutableListMultimap.Builder<String, String> builder = ImmutableListMultimap.builder();
            exchange.getQueryParameters().forEach(builder::putAll);
            return builder.build();
        }

        @Override
        public String toString() {
            return "ConjureServerRequestContext{exchange=" + exchange + '}';
        }
    }
}
