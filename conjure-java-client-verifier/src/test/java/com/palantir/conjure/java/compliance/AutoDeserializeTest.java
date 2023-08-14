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

import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeConfirmService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeService;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.AutoDeserializeServiceBlocking;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.EndpointName;
import com.palantir.conjure.java.com.palantir.conjure.verification.server.PositiveAndNegativeTestCases;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AutoDeserializeTest {

    @RegisterExtension
    public static final VerificationServerExtension server = new VerificationServerExtension();

    private static final Logger log = LoggerFactory.getLogger(AutoDeserializeTest.class);
    private static final AutoDeserializeService jerseyTestService = VerificationClients.autoDeserializeService(server);
    private static final AutoDeserializeServiceBlocking dialogueTestService =
            VerificationClients.dialogueAutoDeserializeService(server);
    private static final AutoDeserializeConfirmService confirmService = VerificationClients.confirmService(server);

    @Retention(RetentionPolicy.RUNTIME)
    @ParameterizedTest(name = "{0}({3}) -> should succeed {2}")
    @MethodSource("testCases")
    public @interface AutoDeserializeTestCases {}

    static Stream<Arguments> testCases() {
        return Cases.TEST_CASES.getAutoDeserialize().entrySet().stream().flatMap(testCase -> {
            EndpointName endpointName = testCase.getKey();
            PositiveAndNegativeTestCases positiveAndNegativeTestCases = testCase.getValue();
            int positiveSize = positiveAndNegativeTestCases.getPositive().size();
            int negativeSize = positiveAndNegativeTestCases.getNegative().size();

            return Stream.concat(
                    IntStream.range(0, positiveSize)
                            .mapToObj(i1 -> Arguments.of(
                                    endpointName,
                                    i1,
                                    true,
                                    positiveAndNegativeTestCases.getPositive().get(i1))),
                    IntStream.range(0, negativeSize)
                            .mapToObj(i -> Arguments.of(
                                    endpointName,
                                    positiveSize + i,
                                    false,
                                    positiveAndNegativeTestCases.getNegative().get(i))));
        });
    }

    @AutoDeserializeTestCases
    public void runConjureJavaRuntimeTestCase(
            EndpointName endpointName, int index, boolean shouldSucceed, String jsonString) throws Exception {
        runTestCase(endpointName, index, shouldSucceed, jsonString, jerseyTestService);
    }

    @AutoDeserializeTestCases
    public void runConjureDialogueTestCase(
            EndpointName endpointName, int index, boolean shouldSucceed, String jsonString) throws Exception {
        runTestCase(endpointName, index, shouldSucceed, jsonString, dialogueTestService);
    }

    private void runTestCase(
            EndpointName endpointName, int index, boolean shouldSucceed, String jsonString, Object service)
            throws Exception {
        Assumptions.assumeFalse(Cases.shouldIgnore(endpointName, jsonString));

        Method method = service.getClass().getMethod(endpointName.get(), int.class);
        // Need to set accessible true work around dialogues anonymous class impl
        method.setAccessible(true);
        System.out.printf(
                "Test case %s: Invoking %s(%s), expected %s%n",
                index, endpointName, jsonString, shouldSucceed ? "success" : "failure");

        if (shouldSucceed) {
            expectSuccess(endpointName, index, service, method);
        } else {
            expectFailure(index, service, method);
        }
    }

    private void expectSuccess(EndpointName endpointName, int index, Object service, Method method) throws Exception {
        try {
            Object resultFromServer = method.invoke(service, index);
            log.info("Received result for endpoint {} and index {}: {}", endpointName, index, resultFromServer);
            confirmService.confirm(endpointName, index, resultFromServer);
        } catch (RemoteException e) {
            log.error("Caught exception with params: {}", e.getError().parameters(), e);
            throw e;
        }
    }

    private void expectFailure(int index, Object service, Method method) {
        Assertions.assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            Object result = method.invoke(service, index);
            log.error("Result should have caused an exception but deserialized to: {}", result);
        });
    }
}
