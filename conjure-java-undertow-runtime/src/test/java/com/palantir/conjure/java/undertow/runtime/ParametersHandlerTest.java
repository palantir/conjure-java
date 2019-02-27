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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.palantir.conjure.java.undertow.lib.Parameters;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParametersHandlerTest {

    private static Undertow server;

    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private static HttpServerExchange cExchange;

    @BeforeClass
    public static void beforeClass() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new ParametersHandler(exchange -> {
                    Parameters.putUnsafePathParam(exchange, "unsafePathName", "unsafePathValue1");
                    Parameters.putUnsafePathParam(exchange, "unsafePathName", "unsafePathValue2");
                    Parameters.putSafePathParam(exchange, "safePathName", "safePathValue");
                    Parameters.putUnsafeQueryParam(exchange, "unsafeQueryName", "unsafeQueryValue");
                    Parameters.putSafeQueryParam(exchange, "safeQueryName", "safeQueryValue");
                    cExchange = exchange;
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
    public void testWebSecurityHeaders() throws IOException {
        try (Response response = client.newCall(new Request.Builder()
                .get().url("http://localhost:12345").build()).execute()) {
            assertThatMultimapEquals(
                    cExchange.getAttachment(Parameters.UNSAFE_PATH_PARAMS_ATTACH_KEY),
                    ImmutableMap.of("unsafePathName", ImmutableList.of("unsafePathValue1", "unsafePathValue2")));
            assertThatMultimapEquals(
                    cExchange.getAttachment(Parameters.SAFE_PATH_PARAMS_ATTACH_KEY),
                    ImmutableMap.of("safePathName", ImmutableList.of("safePathValue")));
            assertThatMultimapEquals(
                    cExchange.getAttachment(Parameters.UNSAFE_QUERY_PARAMS_ATTACH_KEY),
                    ImmutableMap.of("unsafeQueryName", ImmutableList.of("unsafeQueryValue")));
            assertThatMultimapEquals(
                    cExchange.getAttachment(Parameters.SAFE_QUERY_PARAMS_ATTACH_KEY),
                    ImmutableMap.of("safeQueryName", ImmutableList.of("safeQueryValue")));
        }
    }

    private void assertThatMultimapEquals(
            Multimap<String, String> actual,
            ImmutableMap<String, ImmutableList<String>> expected) {
        assertThat(actual.asMap().entrySet().stream().collect(
                ImmutableMap.toImmutableMap(e -> e.getKey(), e -> ImmutableList.copyOf(e.getValue()))))
                .isEqualTo(expected);
    }
}
