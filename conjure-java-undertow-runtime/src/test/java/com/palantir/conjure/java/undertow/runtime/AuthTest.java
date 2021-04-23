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

import static com.palantir.conjure.java.api.testing.Assertions.assertThatServiceExceptionThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.net.HttpHeaders;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import org.junit.jupiter.api.Test;

public final class AuthTest {

    private static final UndertowRuntime CONTEXT =
            ConjureUndertowRuntime.builder().build();

    @Test
    public void testParseAuthHeader() {
        AuthHeader expected = AuthHeader.of(BearerToken.valueOf("token"));
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.addRequestHeader(HttpHeaders.AUTHORIZATION, expected.toString());
        assertThat(CONTEXT.auth().header(exchange)).isEqualTo(expected);
    }

    @Test
    public void testAuthHeaderNotPresent() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        assertThatServiceExceptionThrownBy(() -> CONTEXT.auth().header(exchange))
                .hasType(ErrorType.create(ErrorType.Code.UNAUTHORIZED, "Conjure:MissingCredentials"));
    }

    @Test
    public void testAuthHeaderEmptyValue() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.addRequestHeader(HttpHeaders.AUTHORIZATION, "");
        assertThatServiceExceptionThrownBy(() -> CONTEXT.auth().header(exchange))
                .hasType(ErrorType.create(ErrorType.Code.UNAUTHORIZED, "Conjure:MalformedCredentials"));
    }

    @Test
    public void testAuthHeaderMultipleValues() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.addRequestHeader(HttpHeaders.AUTHORIZATION, "Bearer foo");
        exchange.addRequestHeader(HttpHeaders.AUTHORIZATION, "Bearer bar");
        assertThatServiceExceptionThrownBy(() -> CONTEXT.auth().header(exchange))
                .hasType(ErrorType.create(ErrorType.Code.UNAUTHORIZED, "Conjure:MalformedCredentials"));
    }

    @Test
    public void testParseAuthCookie() {
        BearerToken expected = BearerToken.valueOf("token");
        String cookieName = "Auth-Token";
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestCookies().put(cookieName, new CookieImpl(cookieName, "token"));
        assertThat(CONTEXT.auth().cookie(exchange, cookieName)).isEqualTo(expected);
    }

    @Test
    public void testAuthCookieNotPresent() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        assertThatServiceExceptionThrownBy(() -> CONTEXT.auth().cookie(exchange, "any"))
                .hasType(ErrorType.create(ErrorType.Code.UNAUTHORIZED, "Conjure:MissingCredentials"));
    }

    @Test
    public void testAuthCookieEmptyValue() {
        String cookieName = "Auth-Token";
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestCookies().put(cookieName, new CookieImpl(cookieName, ""));
        assertThatServiceExceptionThrownBy(() -> CONTEXT.auth().cookie(exchange, cookieName))
                .hasType(ErrorType.create(ErrorType.Code.UNAUTHORIZED, "Conjure:MalformedCredentials"));
    }
}
