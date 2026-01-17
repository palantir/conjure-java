/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import com.palantir.conjure.java.undertow.lib.RequestContext;
import io.undertow.Undertow;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class BytesReadTrackerTest {
    private static final int PORT = 12345;
    private static final String HOST = "localhost";

    private CompletableFuture<Long> bytesRead = new CompletableFuture<>();
    private Undertow server;
    private ConjureContexts contexts;

    @BeforeEach
    public void before() {
        bytesRead = new CompletableFuture<>();
        contexts = new ConjureContexts(DefaultRequestArgHandler.INSTANCE);
        server = Undertow.builder()
                .addHttpListener(PORT, HOST)
                .setHandler(exchange -> {
                    RequestContext context = contexts.createContext(exchange, null);
                    exchange.getRequestReceiver().receiveFullBytes((_e, _b) -> {
                        bytesRead.complete(context.bytesRead());
                    });
                })
                .build();
        server.start();
    }

    @AfterEach
    public void after() {
        if (server != null) {
            server.stop();
        }
    }

    @Test
    public void testCountsBytesRead_none() throws Exception {
        sendRequest(0);
        assertThat(bytesRead.join()).isZero();
    }

    @Test
    public void testCountsBytesRead_some() throws Exception {
        int some = 1000;
        sendRequest(some);
        assertThat(bytesRead.join()).isEqualTo(some);
    }

    @Test
    public void testCountsBytesRead_lots() throws Exception {
        int lots = 1024 * 1024;
        sendRequest(lots);
        assertThat(bytesRead.join()).isEqualTo(lots);
    }

    private void sendRequest(int bodySize) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)
                URI.create("http://" + HOST + ":" + PORT).toURL().openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        if (bodySize > 0) {
            byte[] data = new byte[bodySize];
            try (OutputStream os = connection.getOutputStream()) {
                os.write(data);
            }
        }

        connection.getResponseCode(); // trigger request
        connection.disconnect();
    }
}
