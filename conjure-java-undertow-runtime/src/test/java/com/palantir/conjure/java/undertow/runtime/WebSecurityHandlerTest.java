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

import com.google.common.net.HttpHeaders;
import io.undertow.Undertow;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class WebSecurityHandlerTest {

    private static Undertow server;

    @BeforeAll
    public static void beforeClass() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new WebSecurityHandler(_exchange -> {}))
                .build();
        server.start();
    }

    @AfterAll
    public static void afterClass() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testWebSecurityHeaders() {
        HttpURLConnection connection = openConnectionToTestServer();
        assertThat(connection.getHeaderField("X-Content-Security-Policy")).isNull();
    }

    @Test
    public void testInternetExplorer10() {
        testFallbackSecurityPolicyHeader("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
    }

    @Test
    public void testInternetExplorer11() {
        testFallbackSecurityPolicyHeader("Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
    }

    private void testFallbackSecurityPolicyHeader(String userAgent) {
        HttpURLConnection connection = openConnectionToTestServer();
        connection.setRequestProperty(HttpHeaders.USER_AGENT, userAgent);
        validateCommonResponseHeaders(connection);
        assertThat(connection.getHeaderField("X-Content-Security-Policy"))
                .isEqualTo("default-src 'self'; img-src 'self' data:; "
                        + "style-src 'self' 'unsafe-inline'; frame-ancestors 'self';");
    }

    private void validateCommonResponseHeaders(HttpURLConnection connection) {
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_SECURITY_POLICY))
                .isEqualTo("default-src 'self'; img-src 'self' data:; "
                        + "style-src 'self' 'unsafe-inline'; frame-ancestors 'self';");
        assertThat(connection.getHeaderField(HttpHeaders.REFERRER_POLICY)).isEqualTo("strict-origin-when-cross-origin");
        assertThat(connection.getHeaderField(HttpHeaders.X_CONTENT_TYPE_OPTIONS))
                .isEqualTo("nosniff");
        assertThat(connection.getHeaderField(HttpHeaders.X_FRAME_OPTIONS)).isEqualTo("sameorigin");
        assertThat(connection.getHeaderField(HttpHeaders.X_XSS_PROTECTION)).isEqualTo("1; mode=block");
    }

    private static HttpURLConnection openConnectionToTestServer() {
        try {
            URL url = new URL("http://localhost:12345");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
