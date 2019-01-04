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
import io.undertow.util.Headers;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class NoCachingResponseHandlerTest {

    private static Undertow server;

    private static final OkHttpClient client = new OkHttpClient.Builder().build();

    @BeforeClass
    public static void beforeClass() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new NoCachingResponseHandler(exchange -> {
                    if (exchange.getQueryParameters().containsKey("cache")) {
                        exchange.getResponseHeaders().put(Headers.CACHE_CONTROL, "custom override");
                    }
                }))
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
    public void testDefaultCacheControl() throws IOException {
        try (Response response = client.newCall(new Request.Builder()
                .get().url("http://localhost:12345").build()).execute()) {
            assertThat(response.header(HttpHeaders.CACHE_CONTROL)).isEqualTo("no-cache, no-store, must-revalidate");
        }
    }

    @Test
    public void testCacheControlOverride() throws IOException {
        try (Response response = client.newCall(new Request.Builder()
                .get().url("http://localhost:12345?cache").build()).execute()) {
            assertThat(response.header(HttpHeaders.CACHE_CONTROL)).isEqualTo("custom override");
        }
    }
}
