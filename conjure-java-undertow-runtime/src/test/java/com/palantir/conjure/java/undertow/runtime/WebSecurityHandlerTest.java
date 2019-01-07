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

import static org.assertj.core.api.Java6Assertions.assertThat;

import com.google.common.net.HttpHeaders;
import io.undertow.Undertow;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class WebSecurityHandlerTest {

    private static Undertow server;

    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    @BeforeClass
    public static void beforeClass() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new WebSecurityHandler(exchange -> { }))
                .build();
        server.start();
    }

    @AfterClass
    public static void afterClass() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testWebSecurityHeaders() throws IOException {
        try (Response response = client.newCall(new Request.Builder()
                .get().url("http://localhost:12345").build()).execute()) {
            validateCommonResponseHeaders(response);
            assertThat(response.header("X-Content-Security-Policy")).isNull();
        }
    }

    @Test
    public void testInternetExplorer10() throws IOException {
        testFallbackSecurityPolicyHeader("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; Trident/6.0)");
    }

    @Test
    public void testInternetExplorer11() throws IOException {
        testFallbackSecurityPolicyHeader("Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko");
    }

    private void testFallbackSecurityPolicyHeader(String userAgent) throws IOException {
        try (Response response = client.newCall(new Request.Builder().get().url("http://localhost:12345")
                .header(HttpHeaders.USER_AGENT, userAgent).build()).execute()) {
            validateCommonResponseHeaders(response);
            assertThat(response.header("X-Content-Security-Policy"))
                    .isEqualTo("default-src 'self'; img-src 'self' data:; "
                            + "style-src 'self' 'unsafe-inline'; frame-ancestors 'self';");
        }
    }

    private void validateCommonResponseHeaders(Response response) {
        assertThat(response.header(HttpHeaders.CONTENT_SECURITY_POLICY))
                .isEqualTo("default-src 'self'; img-src 'self' data:; "
                        + "style-src 'self' 'unsafe-inline'; frame-ancestors 'self';");
        assertThat(response.header(HttpHeaders.REFERRER_POLICY)).isEqualTo("strict-origin-when-cross-origin");
        assertThat(response.header(HttpHeaders.X_CONTENT_TYPE_OPTIONS)).isEqualTo("nosniff");
        assertThat(response.header(HttpHeaders.X_FRAME_OPTIONS)).isEqualTo("sameorigin");
        assertThat(response.header(HttpHeaders.X_XSS_PROTECTION)).isEqualTo("1; mode=block");
    }
}
