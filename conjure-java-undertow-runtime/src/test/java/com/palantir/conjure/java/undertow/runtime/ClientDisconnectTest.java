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
import com.google.common.util.concurrent.Uninterruptibles;
import io.undertow.Undertow;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClientDisconnectTest {

    private Undertow server;
    private CountDownLatch latch = new CountDownLatch(1);

    @BeforeEach
    public void before() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new ConjureExceptionHandler(
                        _exchange -> Uninterruptibles.sleepUninterruptibly(Duration.ofMillis(50)),
                        (_e, _t) -> latch.countDown()))
                .build();
        server.start();
    }

    @AfterEach
    public void after() {
        server.stop();
    }

    @Test
    public void testClientDisconnect() throws Exception {
        String output = SystemLambda.tapSystemErrAndOut(() -> {
            URL url = new URL("http://localhost:12345");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // necessary to execute the request
            connection.getResponseCode();
            connection.disconnect();
        });
        assertThat(latch.await(1000, TimeUnit.MILLISECONDS)).isTrue();
        assertThat(output).contains("INFO", "aborted by the client");
    }
}
