/*
 * (c) Copyright 2023 Palantir Technologies Inc. All rights reserved.
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

import com.github.stefanbirkner.systemlambda.SystemLambda;
import io.undertow.Undertow;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConjureExceptionDisconnectTest {

    private static Undertow server;
    private static CountDownLatch latch = new CountDownLatch(1);

    @BeforeAll
    public static void before() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new ConjureExceptionHandler(
                        _e -> {
                            Thread.sleep(Duration.ofMillis(50).toMillis());
                            throw new IOException();
                        },
                        (exchange, throwable) -> {
                            ConjureExceptions.INSTANCE.handle(exchange, throwable);
                            latch.countDown();
                        }))
                .build();
        server.start();
    }

    @AfterAll
    public static void after() {
        server.stop();
    }

    @Test
    @SuppressWarnings("FutureReturnValueIgnored")
    public void testClientDisconnect() throws Exception {
        String output = SystemLambda.tapSystemErrAndOut(() -> {
            URL url = new URL("http://localhost:12345");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10);
            try {
                connection.getResponseCode();
            } catch (SocketTimeoutException _e) {
                connection.disconnect();
            }
        });
        assertThat(latch.await(100, TimeUnit.MILLISECONDS)).isTrue();
        assertThat(output).contains("INFO", "aborted by the client");
    }
}
