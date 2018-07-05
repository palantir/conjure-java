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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.verifier.TestService;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import com.palantir.remoting3.jaxrs.JaxRsClient;
import com.palantir.tokens.auth.AuthHeader;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;

public class SpecVerifierTest {

    @ClassRule
    public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<>(EteTestServer.class);

    private final com.palantir.conjure.verifier.TestService testService = JaxRsClient.create(
            TestService.class,
            EteTestServer.clientUserAgent(),
            EteTestServer.clientConfiguration());

    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();

    private static final SpecVerifier specVerifier = new SpecVerifier();

    @BeforeClass
    public static void beforeClass() throws IOException {

        ConjureDefinition definition = objectMapper
                .readValue(new File("src/test/resources/verifier-spec.json"), ConjureDefinition.class);
        File outputDir = new File("src/verifierSpec/java");

        if (Boolean.FALSE) {
            new ObjectGenerator().emit(definition, outputDir);
            new JerseyServiceGenerator().emit(definition, outputDir);
        }
    }

    @Test
    public void getStringAuth_1() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(testService.getStringAuth(AuthHeader.valueOf("abcd123")));
        specVerifier.verifyResponseJsonIsOk(json);
    }

    @Test
    public void echoHeaderParam() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(testService.echoHeaderParam("abcd123"));
        specVerifier.verifyResponseJsonIsOk(json);
    }

    @Test
    public void echoPathParam_1() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(testService.echoPathParam("abcd123"));
        specVerifier.verifyResponseJsonIsOk(json);
    }

    @Test
    public void echoPathParam_2() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(testService.echoPathParam("!@#$%^&*(),./?"));
        specVerifier.verifyResponseJsonIsOk(json);
    }

    @Test
    public void badEchoPath() {
        try {
            testService.echoPathParam("fooasdasd");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
