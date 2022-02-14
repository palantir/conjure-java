/*
 * (c) Copyright 2021 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.undertow.example;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.net.HttpHeaders;
import com.palantir.tokens.auth.AuthHeader;
import com.palantir.tokens.auth.BearerToken;
import io.undertow.Undertow;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ExampleServiceTest {

    @Test
    void testSimpleRequest() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/simple").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(204);
        } finally {
            server.stop();
        }
    }

    @Test
    void testPing() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/ping").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasContent("\"pong\"");
        } finally {
            server.stop();
        }
    }

    @Test
    void testAsync() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/pingAsync").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasContent("\"pong\"");
        } finally {
            server.stop();
        }
    }

    @Test
    void testAsyncVoid() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/voidAsync").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(204);
        } finally {
            server.stop();
        }
    }

    @Test
    void testReturnPrimitive() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/returnPrimitive").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasContent("1");
        } finally {
            server.stop();
        }
    }

    @Test
    void testBinary() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/binary").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/octet-stream");
            assertThat(connection.getInputStream()).hasBinaryContent("binary".getBytes(StandardCharsets.UTF_8));
        } finally {
            server.stop();
        }
    }

    @Test
    void testNamedBinary() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/namedBinary").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/octet-stream");
            assertThat(connection.getInputStream()).hasBinaryContent("binary".getBytes(StandardCharsets.UTF_8));
        } finally {
            server.stop();
        }
    }

    @Test
    void testOptionalBinary() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/optionalBinary").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/octet-stream");
            assertThat(connection.getInputStream()).hasBinaryContent("binary".getBytes(StandardCharsets.UTF_8));
        } finally {
            server.stop();
        }
    }

    @Test
    void testOptionalNamedBinary() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/optionalNamedBinary").openConnection();
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/octet-stream");
            assertThat(connection.getInputStream()).hasBinaryContent("binary".getBytes(StandardCharsets.UTF_8));
        } finally {
            server.stop();
        }
    }

    @Test
    void testPostRequest() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/post").openConnection();
            connection.setDoOutput(true);
            byte[] contents = ("\"" + UUID.randomUUID() + "\"").getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.getOutputStream().write(contents);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(contents);
        } finally {
            server.stop();
        }
    }

    @Test
    void testQueryParam() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection = (HttpURLConnection)
                    new URL("http://localhost:" + port + "/queryParam?q=parameterValue").openConnection();
            byte[] expected = "\"parameterValue\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(expected);
        } finally {
            server.stop();
        }
    }

    @Test
    void testPathParam() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/path/parameterValue").openConnection();
            byte[] expected = "\"parameterValue\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(expected);
        } finally {
            server.stop();
        }
    }

    @Test
    void testHeaderParam() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/headerParam").openConnection();
            connection.setRequestProperty("Foo", "parameterValue");
            byte[] expected = "\"parameterValue\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(expected);
        } finally {
            server.stop();
        }
    }

    @Test
    void testAuthHeader() throws IOException {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            AuthHeader authHeader =
                    AuthHeader.of(BearerToken.valueOf(UUID.randomUUID().toString()));
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/authHeader").openConnection();
            connection.setRequestProperty(HttpHeaders.AUTHORIZATION, authHeader.toString());
            byte[] expected = ("\"" + authHeader.getBearerToken().toString() + "\"").getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(expected);
        } finally {
            server.stop();
        }
    }

    @Test
    void testCookieParam() throws Exception {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/cookie").openConnection();
            connection.setRequestProperty(HttpHeaders.COOKIE, "MY_COOKIE=cookie-value");
            byte[] expected = "\"cookie-value\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(expected);
        } finally {
            server.stop();
        }
    }

    @Test
    void testOptionalCookieParam() throws Exception {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);

            // Cookie value is present
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/optionalCookie").openConnection();
            connection.setRequestProperty(HttpHeaders.COOKIE, "MY_COOKIE=cookie-value");
            byte[] expected = "\"cookie-value\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            try (InputStream responseStream = connection.getInputStream()) {
                assertThat(responseStream).hasBinaryContent(expected);
            }

            // Cookie value is empty
            connection = (HttpURLConnection) new URL("http://localhost:" + port + "/optionalCookie").openConnection();
            byte[] emptyResponse = "\"empty\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            try (InputStream responseStream = connection.getInputStream()) {
                assertThat(responseStream).hasBinaryContent(emptyResponse);
            }
        } finally {
            server.stop();
        }
    }

    @Test
    void testOptionalComplexCookieParam() throws Exception {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);

            // Cookie value is present
            HttpURLConnection connection = (HttpURLConnection)
                    new URL("http://localhost:" + port + "/optionalBigIntegerCookie").openConnection();
            connection.setRequestProperty(HttpHeaders.COOKIE, "BIG_INTEGER=1");
            byte[] expected = "\"1\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            try (InputStream responseStream = connection.getInputStream()) {
                assertThat(responseStream).hasBinaryContent(expected);
            }

            // Cookie value is empty
            connection = (HttpURLConnection)
                    new URL("http://localhost:" + port + "/optionalBigIntegerCookie").openConnection();
            byte[] emptyResponse = "\"empty\"".getBytes(StandardCharsets.UTF_8);
            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            try (InputStream responseStream = connection.getInputStream()) {
                assertThat(responseStream).hasBinaryContent(emptyResponse);
            }
        } finally {
            server.stop();
        }
    }

    @Test
    void testAuthCookieParam() throws Exception {
        Undertow server = TestHelper.started(ExampleServiceEndpoints.of(new ExampleResource()));
        try {
            int port = TestHelper.getPort(server);
            HttpURLConnection connection =
                    (HttpURLConnection) new URL("http://localhost:" + port + "/authCookie").openConnection();
            connection.setRequestProperty(HttpHeaders.COOKIE, "AUTH_TOKEN=my-token");
            byte[] expected = "\"my-token\"".getBytes(StandardCharsets.UTF_8);

            assertThat(connection.getResponseCode()).isEqualTo(200);
            assertThat(connection.getContentType()).startsWith("application/json");
            assertThat(connection.getInputStream()).hasBinaryContent(expected);
        } finally {
            server.stop();
        }
    }
}
