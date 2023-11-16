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

import com.google.common.collect.Iterables;
import com.google.common.net.HttpHeaders;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class OptionsRequestTest {

    private Undertow server;
    private int port;

    @BeforeEach
    public void before() {
        server = Undertow.builder()
                .addHttpListener(0, "localhost")
                .setHandler(ConjureHandler.builder()
                        .services(EndpointService.of(endpoint(Methods.GET, "/first")))
                        .services(EndpointService.of(endpoint(Methods.POST, "/first")))
                        .services(EndpointService.of(endpoint(Methods.PUT, "/second/{p1}/and/{p2}")))
                        .services(EndpointService.of(endpoint(Methods.GET, "/third", 200)))
                        .services(EndpointService.of(endpoint(Methods.OPTIONS, "/third", 418 /* I'm a teapot */)))
                        .services(EndpointService.of(endpoint(Methods.OPTIONS, "/fourth", 418 /* I'm a teapot */)))
                        .build())
                .build();
        server.start();
        port = ((InetSocketAddress)
                        Iterables.getOnlyElement(server.getListenerInfo()).getAddress())
                .getPort();
    }

    @AfterEach
    public void after() {
        server.stop();
    }

    @Test
    public void test_getAndPost() throws IOException {
        HttpURLConnection connection = execute("/first");
        assertThat(connection.getResponseCode()).isEqualTo(204);
        assertThat(connection.getHeaderField(HttpHeaders.ALLOW)).isEqualTo("OPTIONS, GET, HEAD, POST");
    }

    @Test
    public void test_webSecurityHeaders() throws IOException {
        HttpURLConnection connection = execute("/first");
        assertThat(connection.getResponseCode()).isEqualTo(204);
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_SECURITY_POLICY))
                .isNotNull();
    }

    @Test
    public void test_parameterized() throws IOException {
        HttpURLConnection connection = execute("/second/paramValue/and/secondParam");
        assertThat(connection.getResponseCode()).isEqualTo(204);
        assertThat(connection.getHeaderField(HttpHeaders.ALLOW)).isEqualTo("OPTIONS, PUT");
    }

    @Test
    public void test_customOptionsEndpoint() throws IOException {
        HttpURLConnection connection = execute("GET", "/third");
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getHeaderField(HttpHeaders.ALLOW)).isNull();

        HttpURLConnection otherConnection = execute("OPTIONS", "/third");
        assertThat(otherConnection.getResponseCode()).isEqualTo(418);
        assertThat(otherConnection.getHeaderField(HttpHeaders.ALLOW)).isNull();
    }

    @Test
    public void test_customOptionsEndpointWithNoOtherMethods() throws IOException {
        HttpURLConnection connection = execute("OPTIONS", "/fourth");
        assertThat(connection.getResponseCode()).isEqualTo(418);
        assertThat(connection.getHeaderField(HttpHeaders.ALLOW)).isNull();
    }

    private HttpURLConnection execute(String path) {
        return execute("OPTIONS", path);
    }

    private HttpURLConnection execute(String method, String path) {
        try {
            URL url = new URL("http://localhost:" + port + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Endpoint endpoint(HttpString method, String template) {
        return endpoint(method, template, 500);
    }

    private static Endpoint endpoint(HttpString method, String template, int code) {
        return Endpoint.builder()
                .handler(new ResponseCodeHandler(code))
                .method(method)
                .template(template)
                .serviceName(UUID.randomUUID().toString())
                .name(UUID.randomUUID().toString())
                .build();
    }
}
