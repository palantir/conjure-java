/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.compliance;

import com.google.common.collect.ImmutableList;
import com.palantir.remoting.api.config.ssl.SslConfiguration;
import com.palantir.remoting3.clients.ClientConfiguration;
import com.palantir.remoting3.clients.ClientConfigurations;
import com.palantir.remoting3.config.ssl.SslSocketFactories;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ServerRule extends ExternalResource {
    private static final Logger log = LoggerFactory.getLogger(ServerRule.class);

    private static final SslConfiguration TRUST_STORE_CONFIGURATION = new SslConfiguration.Builder()
            .trustStorePath(Paths.get("../conjure-java-core/var/security/truststore.jks"))
            .build();
    private static final SSLSocketFactory SSL_SOCKET_FACTORY =
            SslSocketFactories.createSslSocketFactory(TRUST_STORE_CONFIGURATION);
    private static final X509TrustManager TRUST_MANAGER =
            SslSocketFactories.createX509TrustManager(TRUST_STORE_CONFIGURATION);
    private static final ClientConfiguration clientConfiguration =
            ClientConfigurations.of(ImmutableList.of("http://localhost:8000/"), SSL_SOCKET_FACTORY, TRUST_MANAGER);
    private Process process;

    public ClientConfiguration getClientConfiguration() {
        return clientConfiguration;
    }

    @Override
    public void before() throws Exception {
        ProcessBuilder processBuilder =
                new ProcessBuilder("build/verification-server/server", "build/test-cases/test-cases.json")
                        .redirectErrorStream(true)
                        .redirectOutput(Redirect.PIPE);
        // TODO(dsanduleac): set these as defaults
        processBuilder.environment().put("RUST_LOG", "conjure_verification_server=info,conjure_verification_http=info");

        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),
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
        process = processBuilder.start();
        thread.start();
        log.info("Waiting for server to start up");
        latch.await(10, TimeUnit.SECONDS);
    }

    @Override
    protected void after() {
        process.destroyForcibly();
        try {
            process.waitFor(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
