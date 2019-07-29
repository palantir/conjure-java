/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
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

package com.palantir.conjure.java.verification.server;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.api.config.ssl.SslConfiguration;
import com.palantir.conjure.java.client.config.ClientConfiguration;
import com.palantir.conjure.java.client.config.ClientConfigurations;
import com.palantir.conjure.java.config.ssl.SslSocketFactories;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spins up the 'verification-client' executable which will bind to port 8000, and tears it down at the end of the test.
 */
public final class VerificationClientExtension implements BeforeAllCallback, AfterAllCallback {

    private static final Logger log = LoggerFactory.getLogger(VerificationClientExtension.class);
    private static final SslConfiguration TRUST_STORE_CONFIGURATION = new SslConfiguration.Builder()
            .trustStorePath(Paths.get("../conjure-java-core/var/security/truststore.jks"))
            .build();
    private static final int PORT = 16297;
    private static final ClientConfiguration clientConfiguration = ClientConfigurations.of(
            ImmutableList.of("http://localhost:" + PORT + "/"),
            SslSocketFactories.createSslSocketFactory(TRUST_STORE_CONFIGURATION),
            SslSocketFactories.createX509TrustManager(TRUST_STORE_CONFIGURATION));

    private Process process;

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "build/verification/verifier",
                "build/test-cases/test-cases.json",
                "build/test-cases/verification-api.json")
                .redirectErrorStream(true)
                .redirectOutput(Redirect.PIPE);

        processBuilder.environment().put("PORT", String.valueOf(PORT));
        // TODO(dsanduleac): set these as defaults
        processBuilder.environment().put("RUST_LOG", "conjure_verification_client=info,conjure_verification_http=info");

        process = processBuilder.start();

        log.info("Waiting for server to start up");
        blockUntilServerStarted(process.getInputStream());
    }

    private static void blockUntilServerStarted(InputStream inputStream) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,
                    StandardCharsets.UTF_8))) {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    // TODO(dsanduleac): super hacky
                    // should have logic to derive port from the server's output in a structured way
                    if (line.contains("Listening on")) {
                        latch.countDown();
                    }
                    System.out.println(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.setDaemon(true);
        thread.start();

        latch.await(10, TimeUnit.SECONDS);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        process.destroyForcibly();
        try {
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
