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

import static org.assertj.core.api.Assertions.assertThat;

import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

public class BearerTokenLoggingTest {

    private static final String SESSION_TOKEN = "eyJhbGciOiJFUzI1NiJ9."
            + "eyJleHAiOjE0NTk1NTIzNDksInNpZCI6IlA4WmoxRDVJVGUyNlR0Z"
            + "UsrWXVEWXc9PSIsInN1YiI6Inc1UDJXUU1CUTA2cHlYSXdTbEIvL0E9PSJ9"
            + ".XwPO_EEDVj6BBLScuf70_CH4jyI1ECmgVSoXLHpGlK-yIqm8MyUyFyNQTu8jh9kYheW-zBl64gmTnatkjjDH1A";

    private static final String API_TOKEN = "eyJhbGciOiJFUzI1NiJ9."
            + "eyJzdWIiOiJ3NVAyV1FNQlEwNnB5WEl3U2xCLy9BPT0iLCJqdGkiOiJwRm0wb1ZDSlQrQ0dWZFhmMmJLMy9RPT0ifQ."
            + "hBUerwGsc4FFPIujHJ-7ncGe3-zZQcdPOuRZ8B84nzPNYjlyPmB8VLizsvR23CK3KQUEAlQ2AN_9a5p5_WgPAQ";

    private static final String PROXY_TOKEN = "eyJhbGciOiJFUzI1NiJ9."
            + "eyJ0eXAiOiJwIiwic3ViIjoidzVQMldRTUJRMDZweVhJd1NsQi8vQT09IiwianRpIjoicEZtMG9WQ0pUK0NHVmRYZj"
            + "JiSzMvUT09In0.5s1oFL0XMMooE1eW1-CkCVwctT-RXcbTR-TPCy7JUOjb_39UnZYUfNlsn4aHi5M2C5hAiQUxmG"
            + "-NvjlrKNHPZw";

    private static final String INVALID_BEARER_TOKEN = "IncorrectBearerToken";

    private static final String INVALID_PAYLOAD_TOKEN = "eyJhbGciOiJFUzI1NiJ9."
            + "eyJzdWIiOiJrazlVMHB0ZVJ3K1FYYk55ZkZkcklBPT0iLCJqdGkiOiJ2MEtCNWdVTFJkT3dFWWh4Z1o3bERnPT0iCg."
            + "hBUerwGsc4FFPIujHJ-7ncGe3-zZQcdPOuRZ8B84nzPNYjlyPmB8VLizsvR23CK3KQUEAlQ2AN_9a5p5_WgPAQ";

    private static final String USER_ID = "c393f659-0301-434e-a9c9-72304a507ffc";
    private static final String SESSION_ID = "3fc663d4-3e48-4ded-ba4e-d78af98b8363";
    private static final String TOKEN_ID = "a459b4a1-5089-4fe0-8655-d5dfd9b2b7fd";

    private static final UndertowRuntime CONTEXT =
            ConjureUndertowRuntime.builder().build();

    private AtomicReference<Runnable> delegateRunnable = new AtomicReference<>();
    private HttpHandler delegate = request -> delegateRunnable.get().run();
    private HttpServerExchange exchange = HttpServerExchanges.createStub();

    private HttpHandler handler;

    @BeforeEach
    public void before() {
        MDC.clear();
        delegateRunnable.set(null);
        exchange = HttpServerExchanges.createStub();
        exchange.setRequestMethod(Methods.GET);
        handler = new LoggingContextHandler(httpServerExchange -> {
            CONTEXT.auth().header(httpServerExchange);
            delegate.handleRequest(httpServerExchange);
        });
    }

    @AfterEach
    public void after() {
        MDC.clear();
    }

    @Test
    public void testMdcIsCleared() throws Exception {
        MDC.put("userId", "foo");
        MDC.put("sessionId", "foo");
        MDC.put("tokenId", "foo");
        exchange.getRequestHeaders().add(Headers.AUTHORIZATION, "hello");
        delegateRunnable.set(this::assertMdcUnset);
        handler.handleRequest(exchange);
        assertMdcUnset();
    }

    @Test
    public void testSessionToken() throws Exception {
        runTest(SESSION_TOKEN, USER_ID, SESSION_ID, null);
    }

    @Test
    public void testCookieAuth() throws Exception {
        handler = new LoggingContextHandler(httpServerExchange -> {
            CONTEXT.auth().cookie(httpServerExchange, "PALANTIR_TOKEN");
            assertThat(MDC.get("userId")).isEqualTo(USER_ID);
            assertThat(MDC.get("sessionId")).isEqualTo(SESSION_ID);
            assertThat(MDC.get("tokenId")).isNull();
        });
        exchange.getRequestCookies().put("PALANTIR_TOKEN", new CookieImpl("PALANTIR_TOKEN", SESSION_TOKEN));
        handler.handleRequest(exchange);
        assertMdcUnset();
    }

    @Test
    public void testApiToken() throws Exception {
        runTest(API_TOKEN, USER_ID, null, TOKEN_ID);
    }

    @Test
    public void testProxyToken() throws Exception {
        runTest(PROXY_TOKEN, USER_ID, null, TOKEN_ID);
    }

    @Test
    public void testInvalidToken() throws Exception {
        runTest(INVALID_BEARER_TOKEN, null, null, null);
    }

    @Test
    public void testPayloadToken() throws Exception {
        runTest(INVALID_PAYLOAD_TOKEN, null, null, null);
    }

    private void runTest(
            String authHeader, @Nullable String userId, @Nullable String sessionId, @Nullable String tokenId)
            throws Exception {
        exchange.getRequestHeaders().put(Headers.AUTHORIZATION, authHeader);
        AtomicBoolean invoked = new AtomicBoolean();
        delegateRunnable.set(() -> {
            assertThat(MDC.get("userId")).isEqualTo(userId);
            assertThat(MDC.get("sessionId")).isEqualTo(sessionId);
            assertThat(MDC.get("tokenId")).isEqualTo(tokenId);
            invoked.set(true);
        });
        handler.handleRequest(exchange);
        assertMdcUnset();
        // All tokens provide a user ID. Cases which do not include a user ID should result in an empty value.
        if (userId == null) {
            assertThat(exchange.getAttachment(Attachments.UNVERIFIED_JWT)).isEmpty();
        } else {
            assertThat(exchange.getAttachment(Attachments.UNVERIFIED_JWT)).isPresent();
        }
        assertThat(invoked).isTrue();
    }

    private void assertMdcUnset() {
        assertThat(MDC.get("userId")).isNull();
        assertThat(MDC.get("sessionId")).isNull();
        assertThat(MDC.get("tokenId")).isNull();
    }
}
