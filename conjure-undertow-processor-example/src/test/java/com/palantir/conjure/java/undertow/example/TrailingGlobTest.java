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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.serialization.ObjectMappers;
import io.undertow.Undertow;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TrailingGlobTest {

    private static final ObjectMapper MAPPER = ObjectMappers.newClientJsonMapper();

    private static Undertow server;
    private static int port;

    @BeforeAll
    public static void beforeAll() {
        server = TestHelper.started(TrailingGlobResourceEndpoints.of(new TrailingGlobResource()));
        port = TestHelper.getPort(server);
    }

    @AfterAll
    public static void afterAll() {
        if (server != null) {
            server.stop();
            server = null;
            port = -1;
        }
    }

    @Test
    void testSimpleString() throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL("http://localhost:" + port + "/ping/foo").openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getContentType()).startsWith("application/json");
        try (InputStream body = connection.getInputStream()) {
            List<String> result = MAPPER.readValue(body, new TypeReference<>() {});
            assertThat(result).containsExactly("foo");
        }
    }

    @Test
    void testMultipleSegments() throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL("http://localhost:" + port + "/ping/foo/bar").openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getContentType()).startsWith("application/json");
        try (InputStream body = connection.getInputStream()) {
            List<String> result = MAPPER.readValue(body, new TypeReference<>() {});
            assertThat(result).containsExactly("foo", "bar");
        }
    }

    @Test
    void testEmpty() throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL("http://localhost:" + port + "/ping/").openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getContentType()).startsWith("application/json");
        try (InputStream body = connection.getInputStream()) {
            List<String> result = MAPPER.readValue(body, new TypeReference<>() {});
            // Expect a list with one empty-string element
            assertThat(result).containsExactly("");
        }
    }

    @Test
    void testMultipleEmpty() throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) new URL("http://localhost:" + port + "/ping//").openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getContentType()).startsWith("application/json");
        try (InputStream body = connection.getInputStream()) {
            List<String> result = MAPPER.readValue(body, new TypeReference<>() {});
            // Expect a list with one empty-string element
            assertThat(result).containsExactly("", "");
        }
    }

    @Test
    void testEncodedSlash() throws IOException {
        HttpURLConnection connection = (HttpURLConnection)
                new URL("http://localhost:" + port + "/ping/foo%2Fbar/baz%252Fbang").openConnection();
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getContentType()).startsWith("application/json");
        try (InputStream body = connection.getInputStream()) {
            List<String> result = MAPPER.readValue(body, new TypeReference<>() {});
            // raw '%2F' is decoded to a slash within the first path segment
            // raw '%25' is decoded to '%' in the second. It's important that nothing is double-decoded
            assertThat(result).containsExactly("foo/bar", "baz%2Fbang");
        }
    }
}
