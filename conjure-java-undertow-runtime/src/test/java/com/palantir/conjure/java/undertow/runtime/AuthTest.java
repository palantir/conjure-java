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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import org.junit.jupiter.api.Test;

public final class AuthTest {

    private static final UndertowRuntime CONTEXT = ConjureUndertowRuntime.builder().build();

    @Test
    public void testParseAuthHeader() {
        AuthHeader expected = AuthHeader.of(BearerToken.valueOf("token"));
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, expected.toString());
        assertThat(CONTEXT.auth().header(exchange)).isEqualTo(expected);
    }

    @Test
    public void testAuthHeaderNotPresent() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        assertThatThrownBy(() -> CONTEXT.auth().header(exchange))
                .isInstanceOf(ServiceException.class)
                .hasMessage("ServiceException: UNAUTHORIZED (Conjure:MissingCredential)");
    }

    @Test
    public void testAuthHeaderEmptyValue() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, "");
        assertThatThrownBy(() -> CONTEXT.auth().header(exchange))
                .isInstanceOf(ServiceException.class)
                .hasMessage("ServiceException: UNAUTHORIZED (Conjure:MalformedCredential)");
    }

    @Test
    public void testAuthHeaderMultipleValues() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, "Bearer foo");
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, "Bearer bar");
        assertThatThrownBy(() -> CONTEXT.auth().header(exchange))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("ServiceException: UNAUTHORIZED (Conjure:MalformedCredential)");
    }

    //delete
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
        assertThatThrownBy(() -> CONTEXT.auth().cookie(exchange, "any"))
                .isInstanceOf(ServiceException.class)
                .hasMessage("ServiceException: UNAUTHORIZED (Conjure:MissingCredential)");
    }

    @Test
    public void testAuthCookieEmptyValue() {
        String cookieName = "Auth-Token";
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestCookies().put(cookieName, new CookieImpl(cookieName, ""));
        assertThatThrownBy(() -> CONTEXT.auth().cookie(exchange, cookieName))
                .isInstanceOf(ServiceException.class)
                .hasMessage("ServiceException: UNAUTHORIZED (Conjure:MalformedCredential)");
    }
}
