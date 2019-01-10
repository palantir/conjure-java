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
import com.palantir.tokens.auth.UnverifiedJsonWebToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.MDC;

/**
 * Provides utility methods to parse auth types.
 */
public final class Auth {

    private static final String USER_ID_KEY = "userId";
    private static final String SESSION_ID_KEY = "sessionId";
    private static final String TOKEN_ID_KEY = "tokenId";
    private static Consumer<String> sessionIdSetter = sessionId -> MDC.put(SESSION_ID_KEY, sessionId);
    private static Consumer<String> tokenIdSetter = tokenId -> MDC.put(TOKEN_ID_KEY, tokenId);

    /**
     * Parses an {@link AuthHeader} from the provided {@link HttpServerExchange} and applies
     * {@link UnverifiedJsonWebToken} information to the {@link MDC thread state} if present.
     */
    public static AuthHeader header(HttpServerExchange exchange) {
        HeaderValues authorization = exchange.getRequestHeaders().get(Headers.AUTHORIZATION);
        // Do not use Iterables.getOnlyElement because it includes values in the exception message.
        // We do not want credential material logged to disk, even if it's marked unsafe.
        Preconditions.checkArgument(authorization != null && authorization.size() == 1,
                "One Authorization header value is required");
        return setState(AuthHeader.valueOf(authorization.get(0)));
    }

    /**
     * Parses a {@link BearerToken} from the provided {@link HttpServerExchange} and applies
     * {@link UnverifiedJsonWebToken} information to the {@link MDC thread state} if present.
     */
    public static BearerToken cookie(HttpServerExchange exchange, String cookieName) {
        return setState(StringDeserializers.deserializeBearerToken(
                exchange.getRequestCookies().get(cookieName).getValue()));
    }

    /**
     * Attempts to extract a {@link UnverifiedJsonWebToken JSON Web Token} from the
     * {@link BearerToken} value, and populates the SLF4J {@link MDC} with
     * user id, session id, and token id extracted from the JWT. This is
     * best-effort and does not throw an exception in case any of these steps fail.
     */
    private static BearerToken setState(BearerToken token) {
        Optional<UnverifiedJsonWebToken> parsedJwt = UnverifiedJsonWebToken.tryParse(token.getToken());
        if (parsedJwt.isPresent()) {
            UnverifiedJsonWebToken jwt = parsedJwt.get();
            MDC.put(USER_ID_KEY, jwt.getUnverifiedUserId());
            jwt.getUnverifiedSessionId().ifPresent(sessionIdSetter);
            jwt.getUnverifiedTokenId().ifPresent(tokenIdSetter);
        }
        return token;
    }

    private static AuthHeader setState(AuthHeader authHeader) {
        setState(authHeader.getBearerToken());
        return authHeader;
    }

    private Auth() {}
}
