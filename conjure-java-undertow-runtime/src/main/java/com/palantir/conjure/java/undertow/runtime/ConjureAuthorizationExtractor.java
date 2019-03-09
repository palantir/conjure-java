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

package com.palantir.conjure.java.undertow.runtime;

import com.palantir.conjure.java.undertow.lib.AuthorizationExtractor;
import com.palantir.conjure.java.undertow.lib.PlainSerDe;
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
 * Implementation of {@link AuthorizationExtractor} which sets thread state based on a parsed
 * {@link UnverifiedJsonWebToken}. This behavior requires invocations to be wrapped with the
 * {@link LoggingContextHandler} to avoid leaking {@link MDC} state to other operations.
 *
 * Package private internal API.
 */
final class ConjureAuthorizationExtractor implements AuthorizationExtractor {

    private final PlainSerDe plainSerDe;

    ConjureAuthorizationExtractor(PlainSerDe plainSerDe) {
        this.plainSerDe = plainSerDe;
    }

    /**
     * Parses an {@link AuthHeader} from the provided {@link HttpServerExchange} and applies
     * {@link UnverifiedJsonWebToken} information to the {@link MDC thread state} if present.
     */
    @Override
    public AuthHeader header(HttpServerExchange exchange) {
        HeaderValues authorization = exchange.getRequestHeaders().get(Headers.AUTHORIZATION);
        // Do not use Iterables.getOnlyElement because it includes values in the exception message.
        // We do not want credential material logged to disk, even if it's marked unsafe.
        Preconditions.checkArgument(authorization != null && authorization.size() == 1,
                "One Authorization header value is required");
        return setState(exchange, AuthHeader.valueOf(authorization.get(0)));
    }

    /**
     * Parses a {@link BearerToken} from the provided {@link HttpServerExchange} and applies
     * {@link UnverifiedJsonWebToken} information to the {@link MDC thread state} if present.
     */
    @Override
    public BearerToken cookie(HttpServerExchange exchange, String cookieName) {
        return setState(exchange,
                plainSerDe.deserializeBearerToken(exchange.getRequestCookies().get(cookieName).getValue()));
    }

    private static final String USER_ID_KEY = "userId";
    private static final String SESSION_ID_KEY = "sessionId";
    private static final String TOKEN_ID_KEY = "tokenId";
    private static Consumer<String> sessionIdSetter = sessionId -> MDC.put(SESSION_ID_KEY, sessionId);
    private static Consumer<String> tokenIdSetter = tokenId -> MDC.put(TOKEN_ID_KEY, tokenId);

    /**
     * Attempts to extract a {@link UnverifiedJsonWebToken JSON Web Token} from the
     * {@link BearerToken} value, and populates the SLF4J {@link MDC} with
     * user id, session id, and token id extracted from the JWT. This is
     * best-effort and does not throw an exception in case any of these steps fail.
     */
    private static BearerToken setState(HttpServerExchange exchange, BearerToken token) {
        Optional<UnverifiedJsonWebToken> parsedJwt = UnverifiedJsonWebToken.tryParse(token.getToken());
        exchange.putAttachment(Attachments.UNVERIFIED_JWT, parsedJwt);
        if (parsedJwt.isPresent()) {
            UnverifiedJsonWebToken jwt = parsedJwt.get();
            MDC.put(USER_ID_KEY, jwt.getUnverifiedUserId());
            jwt.getUnverifiedSessionId().ifPresent(sessionIdSetter);
            jwt.getUnverifiedTokenId().ifPresent(tokenIdSetter);
        }
        return token;
    }

    private static AuthHeader setState(HttpServerExchange exchange, AuthHeader authHeader) {
        setState(exchange, authHeader.getBearerToken());
        return authHeader;
    }
}
