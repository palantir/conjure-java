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

import com.palantir.logsafe.Arg;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link RequestArgHandler} implementation which simply {@link Logger#debug(String, Object)} logs
 * provided values immediately upon {@link #arg(HttpServerExchange, Arg)}. Server frameworks should provide
 * a better implementation which allows data to be associated with requests.
 */
enum DefaultRequestArgHandler implements RequestArgHandler {
    INSTANCE;

    private static final Logger log = LoggerFactory.getLogger(DefaultRequestArgHandler.class);

    @Override
    public void arg(HttpServerExchange _exchange, Arg<?> value) {
        log.debug("Request parameter {}", value);
    }

    @Override
    public String toString() {
        return "DefaultRequestLogParameterHandler{}";
    }
}
