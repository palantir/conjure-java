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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeConfirmService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeServiceBlocking;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.EndpointName;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDeserializeTest {

    @RegisterExtension
    public static final VerificationServerRule server = new VerificationServerRule();

    private static final Logger log = LoggerFactory.getLogger(AutoDeserializeTest.class);
    private static final AutoDeserializeService jerseyTestService = VerificationClients.autoDeserializeService(server);
    private static final AutoDeserializeServiceBlocking dialogueTestService =
            VerificationClients.dialogueAutoDeserializeService(server);
    private static final AutoDeserializeConfirmService confirmService = VerificationClients.confirmService(server);

    private static Collection<Arguments> data() {
        List<Arguments> objects = new ArrayList<>();
        Cases.TEST_CASES.getAutoDeserialize().forEach((endpointName, positiveAndNegativeTestCases) -> {
            int positiveSize = positiveAndNegativeTestCases.getPositive().size();
            int negativeSize = positiveAndNegativeTestCases.getNegative().size();

            IntStream.range(0, positiveSize)
                    .forEach(i -> objects.add(Arguments.of(
                            endpointName,
                            i,
                            true,
                            positiveAndNegativeTestCases.getPositive().get(i))));

            IntStream.range(0, negativeSize)
                    .forEach(i -> objects.add(Arguments.of(
                            endpointName,
                            positiveSize + i,
                            false,
                            positiveAndNegativeTestCases.getNegative().get(i))));
        });
        return objects;
    }

    @ParameterizedTest(name = "{0}({3}) -> should succeed {2}")
    @MethodSource("data")
    public void runConjureJavaRuntimeTestCase(
            EndpointName endpointName, int index, boolean shouldSucceed, String jsonString) throws Exception {
        runTestCase(jerseyTestService, endpointName, index, shouldSucceed, jsonString);
    }

    @ParameterizedTest(name = "{0}({3}) -> should succeed {2}")
    @MethodSource("data")
    public void runConjureDialogueTestCase(
            EndpointName endpointName, int index, boolean shouldSucceed, String jsonString) throws Exception {
        runTestCase(dialogueTestService, endpointName, index, shouldSucceed, jsonString);
    }

    private void runTestCase(
            Object service, EndpointName endpointName, int index, boolean shouldSucceed, String jsonString)
            throws Exception {
        assumeFalse(Cases.shouldIgnore(endpointName, jsonString));

        Method method = service.getClass().getMethod(endpointName.get(), int.class);
        // Need to set accessible true work around dialogues anonymous class impl
        method.setAccessible(true);
        System.out.printf(
                "Test case %s: Invoking %s(%s), expected %s%n",
                index, endpointName, jsonString, shouldSucceed ? "success" : "failure");

        if (shouldSucceed) {
            expectSuccess(service, method, endpointName, index);
        } else {
            expectFailure(service, method, endpointName, index);
        }
    }

    private void expectSuccess(Object service, Method method, EndpointName endpointName, int index) throws Exception {
        try {
            Object resultFromServer = method.invoke(service, index);
            log.info("Received result for endpoint {} and index {}: {}", endpointName, index, resultFromServer);
            confirmService.confirm(endpointName, index, resultFromServer);
        } catch (RemoteException e) {
            log.error("Caught exception with params: {}", e.getError().parameters(), e);
            throw e;
        }
    }

    private void expectFailure(Object service, Method method, EndpointName endpointName, int index) {
        assertThatExceptionOfType(Exception.class)
                .as("Endpoint %s should have caused exception", endpointName)
                .isThrownBy(() -> {
                    Object result = method.invoke(service, index);
                    log.error("Result should have caused an exception but deserialized to: {}", result);
                });
    }
}
