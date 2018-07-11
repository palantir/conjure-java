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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.palantir.conjure.verification.ClientTestCases;
import com.palantir.conjure.verification.EndpointName;
import com.palantir.remoting.api.errors.RemoteException;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Assume;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public class SingleParamServiceTests {

    @ClassRule
    public static final VerificationServerRule server = new VerificationServerRule();

    private static final Logger log = LoggerFactory.getLogger(SingleParamServiceTests.class);
    private static final Multimap<EndpointName, String> ignoredTests = ignoredTests();
    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();
    private static ImmutableMap<String, Object> servicesMaps = ImmutableMap.of(
            "singlePathParamService", VerificationClients.singlePathParamService(server),
            "singleHeaderService", VerificationClients.singleHeaderService(server),
            "singleQueryParamService", VerificationClients.singleQueryParamService(server));

    @Parameterized.Parameter(0)
    public String serviceName;

    @Parameterized.Parameter(1)
    public EndpointName endpointName;

    @Parameterized.Parameter(2)
    public int index;

    @Parameterized.Parameter(3)
    public String jsonString;

    @Parameterized.Parameters(name = "{0}/{1}({3})")
    public static Collection<Object[]> data() throws IOException {
        ClientTestCases testCases = server.getTestCases().getClient();

        List<Object[]> objects = new ArrayList<>();
        testCases.getSingleHeaderService().forEach((endpointName, singleHeaderTestCases) -> {
            int size = singleHeaderTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(
                            new Object[] {"singleHeaderService", endpointName, i, singleHeaderTestCases.get(i)}));

        });

        testCases.getSinglePathParamService().forEach((endpointName, singleHeaderTestCases) -> {
            int size = singleHeaderTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(
                            new Object[] {"singlePathParamService", endpointName, i, singleHeaderTestCases.get(i)}));

        });

        testCases.getSingleQueryParamService().forEach((endpointName, singleQueryTestCases) -> {
            int size = singleQueryTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(
                            new Object[] {"singleQueryParamService", endpointName, i, singleQueryTestCases.get(i)}));

        });

        return objects;
    }

    @Test
    public void runTestCase() throws Exception {
        boolean testIsDisabled = ignoredTests.remove(endpointName, jsonString);
        Assume.assumeFalse(testIsDisabled);

        System.out.println(String.format("Invoking %s %s(%s)", serviceName, endpointName, jsonString));

        Object service = servicesMaps.get(serviceName);
        for (Method method : servicesMaps.get(serviceName).getClass().getMethods()) {
            String name = method.getName();
            if (endpointName.get().equals(name)) {
                try {
                    // HACKHACK, index parameter order is different for different services.
                    if ("singleHeaderService".equals(serviceName)) {
                        Type type = method.getGenericParameterTypes()[0];
                        Class<?> cls = ClassUtils.getClass(type.getTypeName());
                        method.invoke(
                                service, objectMapper.readValue(jsonString, cls), index);
                    } else {
                        Type type = method.getGenericParameterTypes()[1];
                        Class<?> cls = ClassUtils.getClass(type.getTypeName());
                        method.invoke(
                                service, index, objectMapper.readValue(jsonString, cls));
                    }

                    log.info("Successfully post param to endpoint {} and index {}", endpointName, index);
                } catch (RemoteException e) {
                    log.error("Caught exception with params: {}", e.getError().parameters());
                    throw e;
                }
            }
        }
    }

    private static Multimap<EndpointName, String> ignoredTests() {
        Multimap<EndpointName, String> ignores = HashMultimap.create();

        // server limitation
        ignores.put(EndpointName.of("headerDouble"), "10");
        ignores.put(EndpointName.of("headerDouble"), "10.0");
        ignores.put(EndpointName.of("headerOptionalString"), "null");

        ignores.put(EndpointName.of("pathParamDouble"), "10");
        ignores.put(EndpointName.of("pathParamDouble"), "10.0");
        ignores.put(EndpointName.of("pathParamString"), "\"\"");
        ignores.put(EndpointName.of("pathParamAliasString"), "\"\"");

        ignores.put(EndpointName.of("queryParamDouble"), "10");
        ignores.put(EndpointName.of("queryParamDouble"), "10.0");

        return ignores;
    }
}
