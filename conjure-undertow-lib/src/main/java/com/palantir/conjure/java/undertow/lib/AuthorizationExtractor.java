/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import com.palantir.tokens.auth.UnverifiedJsonWebToken;
import io.undertow.server.HttpServerExchange;
import java.util.Optional;

/** Provides auth functionality for generated code. */
public interface AuthorizationExtractor {

    /**
     * Parses an {@link AuthHeader} from the provided {@link HttpServerExchange}.
     * Implementations are responsible for calling {@link #setRequestToken(HttpServerExchange, Optional)} before
     * returning the result.
     */
    AuthHeader header(HttpServerExchange exchange);

    /**
     * Parses a {@link BearerToken} from the provided {@link HttpServerExchange}.
     * Implementations are responsible for calling {@link #setRequestToken(HttpServerExchange, Optional)} before
     * returning the result.
     */
    BearerToken cookie(HttpServerExchange exchange, String cookieName);

    /**
     * Set the {@link HttpServerExchange request} {@link UnverifiedJsonWebToken} for observability information.
     * Implementations may choose to set logging context data and track the token value for request logging.
     * This data is never used for auth and is not guaranteed to be verified.
     */
    default void setRequestToken(HttpServerExchange exchange, Optional<UnverifiedJsonWebToken> token) {
        // no-op default implementation for cross-version compatibility.
    }
}
