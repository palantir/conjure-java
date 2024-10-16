/*
 * (c) Copyright 2024 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.tokens.auth.UnverifiedJsonWebToken;
import io.undertow.server.HttpServerExchange;
import java.util.Optional;

/**
 * Configuration point for servers add a callback to be invoked when an auth token
 * has been parsed, but before the remainder of the request handling proceeds.
 *
 * See also {@link ConjureUndertowRuntime.Builder#jsonWebTokenHandler(JsonWebTokenHandler)}.
 */
public interface JsonWebTokenHandler {
    void handle(HttpServerExchange exchange, Optional<UnverifiedJsonWebToken> token);
}
