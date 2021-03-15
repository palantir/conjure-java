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

package com.palantir.conjure.java.undertow.lib;

import com.google.common.annotations.Beta;
import io.undertow.server.HttpServerExchange;

/**
 * Factory for {@link RequestContext} objects when the {@code server-request-context} tag is used.
 * This is an internal interface that should only be used by generated code, it may change without
 * warning, and guarantees are only made for generated code.
 */
@Beta
public interface Contexts {

    /** Returns a new {@link RequestContext} which describes the incoming request. */
    RequestContext createContext(HttpServerExchange exchange, Endpoint endpoint);
}
