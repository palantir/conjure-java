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

/**
 * Configuration point for servers to associate arguments provided to
 * {@link com.palantir.conjure.java.undertow.lib.RequestContext#requestArg(Arg)}
 * with the request for request logging and other observability needs.
 */
public interface RequestArgHandler {

    void arg(HttpServerExchange exchange, Arg<?> value);
}
