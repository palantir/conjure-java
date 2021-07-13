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

package com.palantir.conjure.java.verification.server;

import com.google.common.io.Resources;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.EndpointName;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.PositiveAndNegativeTestCases;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.VerificationClientRequest;
import com.palantir.conjure.java.com.palantir.conjure.verification.client.VerificationClientService;
import com.palantir.conjure.java.verification.server.undertest.JerseyServerUnderTestApplication;
import com.palantir.conjure.java.verification.server.undertest.UndertowServerUnderTestExtension;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Assume;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(DropwizardExtensionsSupport.class)
@Execution(ExecutionMode.SAME_THREAD)
public final class AutoDeserializeTest {
    private static final Logger log = LoggerFactory.getLogger(AutoDeserializeTest.class);

    public static final DropwizardAppExtension<?> jerseyServerUnderTestExtension = new DropwizardAppExtension<>(
            JerseyServerUnderTestApplication.class,
            Resources.getResource("config.yml").getPath());

    @RegisterExtension
    public static final UndertowServerUnderTestExtension undertowServerUnderTest =
            new UndertowServerUnderTestExtension();

    @RegisterExtension
    public static final VerificationClientExtension VERIFICATION_CLIENT_EXTENSION = new VerificationClientExtension();

    private static final VerificationClientService verificationService =
            VerificationClients.verificationClientService(VERIFICATION_CLIENT_EXTENSION);

    @Retention(RetentionPolicy.RUNTIME)
    @ParameterizedTest(name = "{0}({3}) -> should succeed {2}")
    @MethodSource("getTestCases")
    public @interface AutoDeserializeTestCases {}

    public static Stream<Arguments> getTestCases() {
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
    public void runJerseyTestCase(EndpointName endpointName, int index, boolean shouldSucceed, String jsonString) {
        runTestCase(endpointName, index, shouldSucceed, jsonString, jerseyServerUnderTestExtension.getLocalPort());
    }

    @AutoDeserializeTestCases
    public void runUndertowTestCase(EndpointName endpointName, int index, boolean shouldSucceed, String jsonString) {
        runTestCase(endpointName, index, shouldSucceed, jsonString, undertowServerUnderTest.getLocalPort());
    }

    public void runTestCase(EndpointName endpointName, int index, boolean shouldSucceed, String jsonString, int port) {
        Assume.assumeFalse(Cases.shouldIgnore(endpointName, jsonString));

        if (shouldSucceed) {
            expectSuccess(endpointName, index, port);
        } else {
            expectFailure(endpointName, index, port);
        }
    }

    private void expectSuccess(EndpointName endpointName, int index, int port) {
        try {
            verificationService.runTestCase(VerificationClientRequest.builder()
                    .endpointName(endpointName)
                    .testCase(index)
                    .baseUrl(String.format("http://localhost:%d/test/api", port))
                    .build());
        } catch (RemoteException e) {
            log.error("Caught exception with params: {}", e.getError().parameters(), e);
            throw e;
        }
    }

    private void expectFailure(EndpointName endpointName, int index, int port) {
        Assertions.assertThatExceptionOfType(Exception.class).isThrownBy(() -> {
            verificationService.runTestCase(VerificationClientRequest.builder()
                    .endpointName(endpointName)
                    .testCase(index)
                    .baseUrl(String.format("http://localhost:%d/test/api", port))
                    .build());
            log.error("Result should have caused an exception");
        });
    }
}
