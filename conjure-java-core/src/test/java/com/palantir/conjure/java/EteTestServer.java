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

package com.palantir.conjure.java;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.api.config.service.UserAgent;
import com.palantir.conjure.java.api.config.ssl.SslConfiguration;
import com.palantir.conjure.java.client.config.ClientConfiguration;
import com.palantir.conjure.java.client.config.ClientConfigurations;
import com.palantir.conjure.java.config.ssl.SslSocketFactories;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import java.nio.file.Paths;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public final class EteTestServer extends Application<Configuration> {
    @Override
    public void run(Configuration configuration, Environment environment) {
        environment.getApplicationContext().setContextPath("/test-example/*");
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(new EteResource());
        environment.jersey().register(new EmptyPathResource());
    }

    private static final SslConfiguration TRUST_STORE_CONFIGURATION =
            new SslConfiguration.Builder().trustStorePath(Paths.get("var/security/truststore.jks")).build();
    private static final SSLSocketFactory SSL_SOCKET_FACTORY =
            SslSocketFactories.createSslSocketFactory(TRUST_STORE_CONFIGURATION);
    private static final X509TrustManager TRUST_MANAGER =
            SslSocketFactories.createX509TrustManager(TRUST_STORE_CONFIGURATION);

    public static ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().from(
                ClientConfigurations.of(
                        ImmutableList.of("http://localhost:8080/test-example/api"),
                        SSL_SOCKET_FACTORY,
                        TRUST_MANAGER))
                // Disable retries to avoid spinning unnecessarily on negative tests
                .maxNumRetries(0)
                .build();
    }

    public static UserAgent clientUserAgent() {
        return UserAgent.of(UserAgent.Agent.of("test", "develop"));
    }
}
