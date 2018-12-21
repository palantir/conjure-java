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
