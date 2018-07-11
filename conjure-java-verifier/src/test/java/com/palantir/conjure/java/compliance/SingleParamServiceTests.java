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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.palantir.conjure.verification.EndpointName;
import com.palantir.conjure.verification.SingleHeaderService;
import com.palantir.conjure.verification.SinglePathParamService;
import com.palantir.conjure.verification.SingleQueryParamService;
import com.palantir.conjure.verification.TestCases;
import com.palantir.remoting.api.errors.RemoteException;
import com.palantir.remoting3.clients.UserAgent;
import com.palantir.remoting3.ext.jackson.ObjectMappers;
import com.palantir.remoting3.jaxrs.JaxRsClient;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.ClassUtils;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public class SingleParamServiceTests {
    private static final Logger log = LoggerFactory.getLogger(SingleParamServiceTests.class);
    private static final UserAgent userAgent = UserAgent.of(UserAgent.Agent.of("test", "develop"));

    @ClassRule
    public static final ServerRule server = new ServerRule();

    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();
    private static ImmutableMap<String, Object> servicesMaps;

    @BeforeClass
    public static void before() {
        SinglePathParamService singlePathParamService = JaxRsClient.create(
                SinglePathParamService.class, userAgent, server.getClientConfiguration());
        SingleHeaderService singleHeaderService = JaxRsClient.create(
                SingleHeaderService.class, userAgent, server.getClientConfiguration());
        SingleQueryParamService singleQueryParamService = JaxRsClient.create(SingleQueryParamService.class, userAgent,
                server.getClientConfiguration());
        servicesMaps = ImmutableMap.of(
                "singlePathParamService", singlePathParamService,
                "singleHeaderService", singleHeaderService,
                "singleQueryParamService", singleQueryParamService);
    }

    @Parameterized.Parameters(name = "{0}/{1}({3})")
    public static Collection<Object[]> data() throws IOException {
        TestCases testCases = new ObjectMapper(new JsonFactory())
                .registerModule(new Jdk8Module())
                .readValue(new File("build/test-cases/test-cases.json"), TestCases.class);

        List<Object[]> objects = new ArrayList<>();
        testCases.getClient().getSingleHeaderService().forEach((endpointName, singleHeaderTestCases) -> {
            int size = singleHeaderTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(
                            new Object[] {"singleHeaderService", endpointName, i, singleHeaderTestCases.get(i)}));

        });

        testCases.getClient().getSinglePathParamService().forEach((endpointName, singleHeaderTestCases) -> {
            int size = singleHeaderTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(
                            new Object[] {"singlePathParamService", endpointName, i, singleHeaderTestCases.get(i)}));

        });

        testCases.getClient().getSingleQueryParamService().forEach((endpointName, singleQueryTestCases) -> {
            int size = singleQueryTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(
                            new Object[] {"singleQueryParamService", endpointName, i, singleQueryTestCases.get(i)}));

        });

        return objects;
    }


    @Parameterized.Parameter(0)
    public String serviceName;

    @Parameterized.Parameter(1)
    public EndpointName endpointName;

    @Parameterized.Parameter(2)
    public int index;

    @Parameterized.Parameter(3)
    public String jsonString;

    @Test
    public void runTestCase() throws Exception {
        System.out.println(String.format("Invoking %s %s(%s)", serviceName, endpointName, jsonString));

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

        boolean isRemoved = ignores.remove(endpointName, jsonString);
        Assume.assumeFalse(isRemoved);

        Object service = servicesMaps.get(serviceName);
        for (Method method : servicesMaps.get(serviceName).getClass().getMethods()) {
            String name = method.getName();
            if (endpointName.get().equals(name)) {
                try {
                    // HACKHACK, index parameter order is different for different services.
                    Type type = method.getGenericParameterTypes()[0];
                    if (type.getTypeName() != "int") {
                        Class<?> cls = ClassUtils.getClass(type.getTypeName());
                        method.invoke(
                                service, objectMapper.readValue(jsonString, cls), index);
                    } else {
                        type = method.getGenericParameterTypes()[1];
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
}
