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

package com.palantir.conjure.java.undertow.lib.internal;

import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

/**
 * Provides utility methods to parse auth types.
 */
public final class Auth {

    public static AuthHeader header(HttpServerExchange exchange) {
        HeaderValues authorization = exchange.getRequestHeaders().get(Headers.AUTHORIZATION);
        // Do not use Iterables.getOnlyElement because it includes values in the exception message.
        // We do not want credential material logged to disk, even if it's marked unsafe.
        Preconditions.checkArgument(authorization != null && authorization.size() == 1,
                "One Authorization header value is required");
        return AuthHeader.valueOf(authorization.get(0));
    }

    public static BearerToken cookie(HttpServerExchange exchange, String cookieName) {
        return StringDeserializers.deserializeBearerToken(exchange.getRequestCookies().get(cookieName).getValue());
    }

    private Auth() {}
}
