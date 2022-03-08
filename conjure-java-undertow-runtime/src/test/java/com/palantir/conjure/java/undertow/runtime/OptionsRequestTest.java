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
import com.google.errorprone.annotations.MustBeClosed;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import io.undertow.Undertow;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.UUID;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class OptionsRequestTest {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .followRedirects(false)
            .retryOnConnectionFailure(false)
            .build();

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
    public void test_getAndPost() {
        try (Response response = execute("/first")) {
            assertThat(response.code()).isEqualTo(204);
            assertThat(response.header(HttpHeaders.ALLOW)).isEqualTo("OPTIONS, GET, HEAD, POST");
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
                    .isEqualTo("Authorization");
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                    .isEqualTo("OPTIONS, GET, HEAD, POST");
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_MAX_AGE)).isEqualTo("600");
        }
    }

    @Test
    public void test_webSecurityHeaders() {
        try (Response response = execute("/first")) {
            assertThat(response.code()).isEqualTo(204);
            assertThat(response.header(HttpHeaders.CONTENT_SECURITY_POLICY)).isNotNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)).isEqualTo("origin.com");
            assertThat(response.header(HttpHeaders.VARY)).isEqualTo("Origin");
        }
    }

    @Test
    public void test_parameterized() {
        try (Response response = execute("/second/paramValue/and/secondParam")) {
            assertThat(response.code()).isEqualTo(204);
            assertThat(response.header(HttpHeaders.ALLOW)).isEqualTo("OPTIONS, PUT");
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
                    .isEqualTo("Authorization");
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                    .isEqualTo("OPTIONS, PUT");
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_MAX_AGE)).isEqualTo("600");
        }
    }

    @Test
    public void test_customOptionsEndpoint() {
        try (Response response = execute("GET", "/third")) {
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.header(HttpHeaders.ALLOW)).isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_MAX_AGE)).isNull();
        }
        try (Response response = execute("OPTIONS", "/third")) {
            assertThat(response.code()).isEqualTo(418);
            assertThat(response.header(HttpHeaders.ALLOW)).isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_MAX_AGE)).isNull();
        }
    }

    @Test
    public void test_customOptionsEndpointWithNoOtherMethods() {
        try (Response response = execute("OPTIONS", "/fourth")) {
            assertThat(response.code()).isEqualTo(418);
            assertThat(response.header(HttpHeaders.ALLOW)).isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS))
                    .isNull();
            assertThat(response.header(HttpHeaders.ACCESS_CONTROL_MAX_AGE)).isNull();
        }
    }

    @MustBeClosed
    private Response execute(String path) {
        return execute("OPTIONS", path);
    }

    @MustBeClosed
    private Response execute(String method, String path) {
        Request request = new Request.Builder()
                .method(method, null)
                .url("http://localhost:" + port + path)
                .header(HttpHeaders.ORIGIN, "origin.com")
                .build();
        try {
            return client.newCall(request).execute();
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
