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
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.types.ObjectGenerator;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.conjure.verifier.TestService;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import com.palantir.remoting3.jaxrs.JaxRsClient;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SpecVerifierTest {

    private static com.palantir.conjure.verifier.TestCasesYml testCases;
    private final com.palantir.conjure.verifier.TestService testService;
    private final com.palantir.conjure.verifier.EndpointName endpointName;
    private com.palantir.conjure.verifier.ServiceTestStructure serviceTestStructure;

    @BeforeClass
    public static void beforeClass() throws IOException {
        ObjectMapper objectMapper = ObjectMappers.newServerObjectMapper();
        ConjureDefinition definition = objectMapper
                .readValue(new File("src/test/resources/verifier-spec.json"), ConjureDefinition.class);
        File outputDir = new File("src/verifierSpec/java");

        if (Boolean.FALSE) {
            new ObjectGenerator().emit(definition, outputDir);
            new JerseyServiceGenerator().emit(definition, outputDir);
        }
    }

    @Parameterized.Parameters(name = "{0} is valid Conjure YML: {1}")
    public static Collection<Object[]> data() throws IOException {
        testCases = new ObjectMapper(new YAMLFactory())
                .registerModule(new Jdk8Module())
                .readValue(new File("src/test/resources/testcases.yml"),
                        com.palantir.conjure.verifier.TestCasesYml.class);
        List<Object[]> objects = new ArrayList<>();
        testCases.get().forEach((endpointName1, serviceTestStructures) -> {
            serviceTestStructures.forEach(serviceTestStructure -> {
                objects.add(new Object[]{endpointName1, serviceTestStructure});
            });
        });
        return objects;
    }

    public SpecVerifierTest(
            com.palantir.conjure.verifier.EndpointName endpointName,
            com.palantir.conjure.verifier.ServiceTestStructure serviceTestStructure) {
        this.endpointName = endpointName;
        this.serviceTestStructure = serviceTestStructure;
        this.testService = JaxRsClient.create(TestService.class, EteTestServer.clientUserAgent(),
                EteTestServer.clientConfiguration());
    }

    @Test
    public void runTestCase() {
        System.out.println(this.endpointName);
    }

}
