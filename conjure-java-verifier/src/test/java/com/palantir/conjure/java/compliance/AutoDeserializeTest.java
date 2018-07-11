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

package com.palantir.conjure.java.compliance;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.collect.HashMultimap;
import com.palantir.conjure.verification.AutoDeserializeConfirmService;
import com.palantir.conjure.verification.AutoDeserializeService;
import com.palantir.conjure.verification.EndpointName;
import com.palantir.conjure.verification.TestCases;
import com.palantir.remoting.api.errors.RemoteException;
import com.palantir.remoting3.clients.UserAgent;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import com.palantir.remoting3.jaxrs.JaxRsClient;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public class AutoDeserializeTest {
    private static final Logger log = LoggerFactory.getLogger(AutoDeserializeTest.class);
    private static final UserAgent userAgent = UserAgent.of(UserAgent.Agent.of("test", "develop"));

    @ClassRule
    public static final ServerRule server = new ServerRule();

    private static final SpecVerifier specVerifier = new SpecVerifier();
    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();
    private static AutoDeserializeService testService;
    private static AutoDeserializeConfirmService confirmService;

    @BeforeClass
    public static void before() throws Exception {
        testService = JaxRsClient.create(AutoDeserializeService.class, userAgent, server.getClientConfiguration());
        confirmService =
                JaxRsClient.create(AutoDeserializeConfirmService.class, userAgent, server.getClientConfiguration());
    }

    @Parameterized.Parameters(name = "{0}({3}) -> should succeed {2}")
    public static Collection<Object[]> data() throws IOException {
        TestCases testCases = new ObjectMapper(new JsonFactory())
                .registerModule(new Jdk8Module())
                .readValue(new File("build/test-cases/test-cases.json"), TestCases.class);

        List<Object[]> objects = new ArrayList<>();

        testCases.getClient().getAutoDeserialize().forEach((endpointName, positiveAndNegativeTestCases) -> {
            int positiveSize = positiveAndNegativeTestCases.getPositive().size();
            int negativeSize = positiveAndNegativeTestCases.getNegative().size();
            IntStream.range(0, positiveSize)
                    .forEach(i -> objects.add(new Object[]{endpointName, i, true, positiveAndNegativeTestCases.getPositive().get(i)}));

            IntStream.range(0, negativeSize)
                    .forEach(i -> objects.add(new Object[]{endpointName, positiveSize + i, false, positiveAndNegativeTestCases.getNegative().get(i)}));
        });

        return objects;
    }

    @Parameterized.Parameter(0)
    public EndpointName endpointName;

    @Parameterized.Parameter(1)
    public int index;

    @Parameterized.Parameter(2)
    public boolean shouldSucceed;

    @Parameterized.Parameter(3)
    public String jsonString;

    @Test
    public void runTestCase() throws Exception {
        System.out.println(String.format("Invoking %s(%s), expected %s",
                endpointName,
                jsonString,
                shouldSucceed ? "success" : "failure"));
        Method method = testService.getClass().getMethod(endpointName.get(), int.class);

        HashMultimap<EndpointName, String> ignores = HashMultimap.create();
        ignores.put(EndpointName.of("receiveBooleanExample"), "{\"value\":0}"); // jackson is casting 0 -> false and 1 -> true (.disable(MapperFeature.ALLOW_COERCION_OF_SCALARS);) in 2.9 will save us
        ignores.put(EndpointName.of("receiveBooleanExample"), "{\"value\":\"true\"}"); // jackson is casting 0 -> false and 1 -> true

        // jackson magical casting
        ignores.put(EndpointName.of("receiveStringExample"), "{\"value\":8}");
        ignores.put(EndpointName.of("receiveDateTimeExample"), "{\"value\":\"1523040070\"}"); // jackson is coercing this to an actual datetime object
        ignores.put(EndpointName.of("receiveDateTimeExample"), "{\"value\": 1523040070}");// jackson is coercing this to an actual datetime object
        ignores.put(EndpointName.of("receiveDoubleExample"), "{\"value\":\"1.23\"}"); // jackson is turning this into 1.23L
        ignores.put(EndpointName.of("receiveIntegerExample"), "{\"value\":1.23}"); // jackson is coercing this to '1'

        ignores.put(EndpointName.of("receiveDoubleExample"), "{\"value\":13}"); // bug with the verification-server
        ignores.put(EndpointName.of("receiveMapExample"), "{}"); // bug with the verification-server


        boolean testIsDisabled = ignores.containsEntry(endpointName, jsonString);
        Assume.assumeFalse(testIsDisabled);


        if (shouldSucceed) {
            try {
                Object resultFromServer = method.invoke(testService, index);
                log.info("Received result for endpoint {} and index {}: {}", endpointName, index, resultFromServer);
                confirmService.confirm(endpointName.get(), index, resultFromServer);
            } catch (RemoteException e) {
                log.error("Caught exception with params: {}", e.getError().parameters());
                throw e;
            }
        } else {
            Assertions.assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
                Object result = method.invoke(testService, index);
                log.error("Result should have caused an exception but deserialized to: {}", result);
            });
        }
    }
}
