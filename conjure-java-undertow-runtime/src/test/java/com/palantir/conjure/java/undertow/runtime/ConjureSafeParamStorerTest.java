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

import com.google.common.collect.ImmutableMap;
import com.palantir.logsafe.testing.Assertions;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConjureSafeParamStorerTest {

    private static Undertow server;

    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private static HttpServerExchange cExchange;

    private static ConjureSafeParamStorer paramStorer = ConjureSafeParamStorer.INSTANCE;

    @BeforeClass
    public static void beforeClass() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(exchange -> {
                    paramStorer.putSafeParam(exchange, "key1", "value1");
                    paramStorer.putSafeParam(exchange, "key2", "value2");
                    paramStorer.putSafeParam(exchange, "key1", "newValue2");
                    cExchange = exchange;
                })
                .build();
        server.start();
    }

    @AfterClass
    public static void afterClass() {
        server.stop();
    }

    @Test
    public void testParametersAreAttached() throws IOException {
        try (Response response = client.newCall(new Request.Builder()
                .get().url("http://localhost:12345").build()).execute()) {
            response.close();
            Assertions.assertThat(paramStorer.getSafeParams(cExchange))
                    .isEqualTo(ImmutableMap.of(
                            "key1", "value1",
                            "key2", "newValue2"));
        }
    }

}
