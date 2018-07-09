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

package com.palantir.conjure.java.verifier;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.ImmutableList;
import com.palantir.conjure.compliance.AutoDeserializeService;
import com.palantir.conjure.compliance.EndpointName;
import com.palantir.conjure.compliance.TestCases;
import com.palantir.remoting.api.config.ssl.SslConfiguration;
import com.palantir.remoting3.clients.ClientConfigurations;
import com.palantir.remoting3.clients.UserAgent;
import com.palantir.remoting3.config.ssl.SslSocketFactories;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import com.palantir.remoting3.jaxrs.JaxRsClient;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AutoDeserializeTest {
    private static final SslConfiguration TRUST_STORE_CONFIGURATION = new SslConfiguration.Builder()
            .trustStorePath(Paths.get("../conjure-java-core/var/security/truststore.jks"))
            .build();
    private static final SSLSocketFactory SSL_SOCKET_FACTORY =
            SslSocketFactories.createSslSocketFactory(TRUST_STORE_CONFIGURATION);
    private static final X509TrustManager TRUST_MANAGER =
            SslSocketFactories.createX509TrustManager(TRUST_STORE_CONFIGURATION);

    private static final SpecVerifier specVerifier = new SpecVerifier();
    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();
    private static final AutoDeserializeService testService = JaxRsClient.create(
            AutoDeserializeService.class,
            UserAgent.of(UserAgent.Agent.of("test", "develop")),
            ClientConfigurations.of(
                ImmutableList.of("http://localhost:8000/"),
                SSL_SOCKET_FACTORY,
                TRUST_MANAGER));

    @Parameterized.Parameters(name = "{0} (should succeed {2}): {1}")
    public static Collection<Object[]> data() throws IOException {
        TestCases testCases = new ObjectMapper(new JsonFactory())
                .registerModule(new Jdk8Module())
                .readValue(new File("src/test/resources/testcases.yml"), TestCases.class);

        List<Object[]> objects = new ArrayList<>();

        testCases.getClient().getAutoDeserialize().forEach((endpointName, positiveAndNegativeTestCases) -> {
            int positiveSize = positiveAndNegativeTestCases.getPositive().size();
            int negativeSize = positiveAndNegativeTestCases.getNegative().size();
            IntStream.range(0, positiveSize)
                    .forEach(i -> objects.add(new Object[]{endpointName, i, true}));

            IntStream.range(positiveSize, positiveSize + negativeSize)
                    .forEach(i -> objects.add(new Object[]{endpointName, i, false}));
        });

        return objects;
    }

    @Parameterized.Parameter(0)
    public EndpointName endpointName;

    @Parameterized.Parameter(1)
    public int index;

    @Parameterized.Parameter(2)
    public boolean shouldSucceed;

    @Test
    public void runTestCase() throws Exception {
        System.out.println(this.endpointName + " " + shouldSucceed);
        Method method = testService.getClass().getMethod(endpointName.get(), int.class);

        if (shouldSucceed) {
            Object resultFromServer = method.invoke(testService, index);
            specVerifier.verifyResponseJsonIsOk(objectMapper.writeValueAsString(resultFromServer));
        } else {
            try {
                method.invoke(testService, index);
                failBecauseExceptionWasNotThrown(Exception.class);
            } catch (Exception e) {
                // TODO(forozco): restrict type to the specific deserialization error
                specVerifier.notifyResponseDeserializedFailed();
            }
        }
    }
}
