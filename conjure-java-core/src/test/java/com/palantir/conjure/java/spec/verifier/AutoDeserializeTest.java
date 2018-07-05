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

package com.palantir.conjure.java.spec.verifier;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.palantir.conjure.java.EteTestServer;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.verifier.AutoDeserializeService;
import com.palantir.conjure.verifier.EndpointName;
import com.palantir.conjure.verifier.TestCasesYml;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import com.palantir.remoting3.jaxrs.JaxRsClient;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class AutoDeserializeTest {

    @ClassRule
    public static final DropwizardAppRule<Configuration> RULE = new DropwizardAppRule<>(EteTestServer.class);
    private static final SpecVerifier specVerifier = new SpecVerifier();
    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();
    private final AutoDeserializeService testService = JaxRsClient.create(
            AutoDeserializeService.class,
            EteTestServer.clientUserAgent(),
            EteTestServer.clientConfiguration());

    @Parameterized.Parameters(name = "{0} (should succeed {2}): {1}")
    public static Collection<Object[]> data() throws IOException {
        TestCasesYml testCases = new ObjectMapper(new YAMLFactory())
                .registerModule(new Jdk8Module())
                .readValue(new File("src/test/resources/testcases.yml"), TestCasesYml.class);

        List<Object[]> objects = new ArrayList<>();

        testCases.getAutoDeserialize().forEach((endpointName, positiveAndNegativeTestCases) -> {
            int positiveSize = positiveAndNegativeTestCases.getPositive().size();
            int negativeSize = positiveAndNegativeTestCases.getNegative().size();
            IntStream.range(0, positiveSize )
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
            } catch(Exception e) {
                // TODO restrict type to the specific deserialization error
                specVerifier.notifyResponseDeserializedFailed();
            }

        }
    }

    @BeforeClass
    public static void regenerateCode() throws IOException {
        ConjureDefinition definition = objectMapper
                .readValue(new File("src/test/resources/verifier-spec.json"), ConjureDefinition.class);
        File outputDir = new File("src/verifierSpec/java");

        if (Boolean.FALSE) {
            new ObjectGenerator().emit(definition, outputDir);
            new JerseyServiceGenerator().emit(definition, outputDir);
        }
    }
}
