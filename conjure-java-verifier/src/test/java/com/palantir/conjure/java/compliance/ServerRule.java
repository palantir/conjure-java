/*
 * (c) Copyright 2018 Palantir Technologies Inc. All rights reserved.
 */

package com.palantir.conjure.java.compliance;

import com.google.common.collect.ImmutableList;
import com.palantir.remoting.api.config.ssl.SslConfiguration;
import com.palantir.remoting3.clients.ClientConfiguration;
import com.palantir.remoting3.clients.ClientConfigurations;
import com.palantir.remoting3.config.ssl.SslSocketFactories;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.junit.rules.ExternalResource;

public final class ServerRule extends ExternalResource {
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
                        .redirectOutput(Redirect.INHERIT);
        // TODO(dsanduleac): set these as defaults
        processBuilder.environment().put("RUST_LOG", "conjure_verification_server=info,conjure_verification_http=info");
        process = processBuilder.start();
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
