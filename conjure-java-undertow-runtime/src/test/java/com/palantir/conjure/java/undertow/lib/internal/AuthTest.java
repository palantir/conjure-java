/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.undertow.lib.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.logsafe.exceptions.SafeIllegalArgumentException;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.junit.Test;


public final class AuthTest {

    @Test
    public void testParseAuthHeader() {
        AuthHeader expected = AuthHeader.of(BearerToken.valueOf("token"));
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, expected.toString());
        assertThat(Auth.header(exchange)).isEqualTo(expected);
    }

    @Test
    public void testAuthHeaderNotPresent() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        assertThatThrownBy(() -> Auth.header(exchange))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessage("One Authorization header value is required");
    }

    @Test
    public void testAuthHeaderMultipleValues() {
        HttpServerExchange exchange = HttpServerExchanges.createStub();
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, "Bearer foo");
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, "Bearer bar");
        assertThatThrownBy(() -> Auth.header(exchange))
                .isInstanceOf(SafeIllegalArgumentException.class)
                .hasMessage("One Authorization header value is required");
    }
}
