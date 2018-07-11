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
import com.google.common.collect.Multimap;
import com.palantir.conjure.verification.EndpointName;
import com.palantir.conjure.verification.SingleHeaderService;
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
public class SingleHeaderServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SingleHeaderServiceTest.class);
    private static final UserAgent userAgent = UserAgent.of(UserAgent.Agent.of("test", "develop"));

    @ClassRule
    public static final ServerRule server = new ServerRule();

    private static final ObjectMapper objectMapper = ObjectMappers.newClientObjectMapper();
    private static SingleHeaderService testService;

    private static Method[] methods;

    @BeforeClass
    public static void before() {
        testService = JaxRsClient.create(SingleHeaderService.class, userAgent, server.getClientConfiguration());
        methods = testService.getClass().getMethods();
    }

    @Parameterized.Parameters(name = "{0}({2})")
    public static Collection<Object[]> data() throws IOException {
        TestCases testCases = new ObjectMapper(new JsonFactory())
                .registerModule(new Jdk8Module())
                .readValue(new File("build/test-cases/test-cases.json"), TestCases.class);

        List<Object[]> objects = new ArrayList<>();
        testCases.getClient().getSingleHeaderService().forEach((endpointName, singleHeaderTestCases) -> {
            int size = singleHeaderTestCases.size();
            IntStream.range(0, size)
                    .forEach(i -> objects.add(new Object[] {endpointName, i, singleHeaderTestCases.get(i)}));

        });

        return objects;
    }

    @Parameterized.Parameter(0)
    public EndpointName endpointName;

    @Parameterized.Parameter(1)
    public int index;

    @Parameterized.Parameter(2)
    public String jsonString;

    @Test
    public void runTestCase() throws Exception {
        System.out.println(String.format("Invoking %s(%s)", endpointName, jsonString));

        Multimap<EndpointName, String> ignores = HashMultimap.create();
        // server limitation
        ignores.put(EndpointName.of("headerDouble"), "10");
        ignores.put(EndpointName.of("headerDouble"), "10.0");
        ignores.put(EndpointName.of("headerOptionalString"), "null");

        boolean isRemoved = ignores.remove(endpointName, jsonString);
        Assume.assumeFalse(isRemoved);

        for (Method method : methods)  {
            String name = method.getName();
            if (endpointName.get().equals(name)) {
                try {
                    Type type = method.getGenericParameterTypes()[0];
                    Class<?> cls = ClassUtils.getClass(type.getTypeName());
                    method.invoke(
                            testService, objectMapper.readValue(jsonString, cls), index);
                    log.info("Successfully post headers to endpoint {} and index {}", endpointName, index);
                } catch (RemoteException e) {
                    log.error("Caught exception with params: {}", e.getError().parameters());
                    throw e;
                }
            }
        }
    }
}
