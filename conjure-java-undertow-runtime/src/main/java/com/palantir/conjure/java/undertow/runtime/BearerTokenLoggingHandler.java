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

import com.palantir.logsafe.Preconditions;
import com.palantir.tokens.auth.UnverifiedJsonWebToken;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Attempts to extract a {@link UnverifiedJsonWebToken JSON Web Token} from the {@link HttpServerExchange
 * request's} {@link Headers#AUTHORIZATION authorization header}, and populates the SLF4J {@link MDC} with
 * user id, session id, and token id extracted from the JWT. This handler
 * is best-effort and does not throw an exception in case any of these steps fail.
 */
final class BearerTokenLoggingHandler implements HttpHandler {
    private static final Logger log = LoggerFactory.getLogger(BearerTokenLoggingHandler.class);

    private static final String USER_ID_KEY = "userId";
    private static final String SESSION_ID_KEY = "sessionId";
    private static final String TOKEN_ID_KEY = "tokenId";

    private final HttpHandler delegate;

    BearerTokenLoggingHandler(HttpHandler delegate) {
        this.delegate = Preconditions.checkNotNull(delegate, "Delegate handler is required");
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        initializeMdc(exchange);
        try {
            delegate.handleRequest(exchange);
        } finally {
            clearMdc();
        }
    }

    private void initializeMdc(HttpServerExchange exchange) {
        // The Jersey filter implementation fails to clean up MDC state after each request.
        // In environments using both Jersey and generated Undertow handlers, the MDC may
        // contain stale values.
        clearMdc();
        String rawAuthHeader = exchange.getRequestHeaders().getFirst(Headers.AUTHORIZATION);
        if (rawAuthHeader != null) {
            Optional<UnverifiedJsonWebToken> parsedJwt = UnverifiedJsonWebToken.tryParse(rawAuthHeader);
            if (parsedJwt.isPresent()) {
                UnverifiedJsonWebToken jwt = parsedJwt.get();
                MDC.put(USER_ID_KEY, jwt.getUnverifiedUserId());
                jwt.getUnverifiedSessionId().ifPresent(BearerTokenLoggingHandler::setMdcSessionId);
                jwt.getUnverifiedTokenId().ifPresent(BearerTokenLoggingHandler::setMdcTokenId);
            }
        } else {
            log.debug("No AuthHeader present on request.");
        }
    }

    private static void setMdcSessionId(String sessionId) {
        MDC.put(SESSION_ID_KEY, sessionId);
    }

    private static void setMdcTokenId(String tokenId) {
        MDC.put(TOKEN_ID_KEY, tokenId);
    }

    private static void clearMdc() {
        MDC.remove(USER_ID_KEY);
        MDC.remove(SESSION_ID_KEY);
        MDC.remove(TOKEN_ID_KEY);
    }
}
