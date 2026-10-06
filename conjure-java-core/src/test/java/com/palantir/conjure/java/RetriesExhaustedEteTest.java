/*
 * (c) Copyright 2026 Palantir Technologies Inc. All rights reserved.
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

import static com.palantir.conjure.java.EteTestServer.clientConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.codahale.metrics.Meter;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.api.errors.UnknownRemoteException;
import com.palantir.conjure.java.client.config.ClientConfiguration;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.dialogue.clients.DialogueClients;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.SafeLoggable;
import com.palantir.tritium.metrics.registry.DefaultTaggedMetricRegistry;
import com.palantir.tritium.metrics.registry.TaggedMetricRegistry;
import dialogue.com.palantir.product.EmptyPathServiceBlocking;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntPredicate;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import undertow.com.palantir.product.EmptyPathServiceEndpoints;

/** Each arrow in A -> B -> C -> D uses a Dialogue client and a separate Undertow server. */
@Execution(ExecutionMode.SAME_THREAD)
@Timeout(30)
final class RetriesExhaustedEteTest {
    private static final HttpString RETRIES_EXHAUSTED_HEADER = new HttpString("Dialogue-Retries-Exhausted");
    private static final String DIAGNOSTIC_METRIC_PREFIX = "dialogue.client.request.retry.diagnostic.";
    private static final ObservedResponse SUCCESS = new ObservedResponse(200, Optional.empty());
    private static final ObservedResponse INTERNAL_ERROR = new ObservedResponse(500, Optional.empty());
    private static final ObservedResponse EXHAUSTED_INTERNAL_ERROR = new ObservedResponse(500, Optional.of("true"));
    private static final ObservedResponse BAD_REQUEST = new ObservedResponse(400, Optional.empty());
    private static final ObservedResponse EXHAUSTED_CONFLICT = new ObservedResponse(409, Optional.of("true"));
    private static final ObservedResponse UNAVAILABLE = new ObservedResponse(503, Optional.empty());
    private static final ObservedResponse EXHAUSTED_UNAVAILABLE = new ObservedResponse(503, Optional.of("true"));

    private final List<Undertow> servers = new ArrayList<>();

    @AfterEach
    void stopServers() {
        for (int index = servers.size() - 1; index >= 0; index--) {
            servers.get(index).stop();
        }
    }

    @ParameterizedTest(name = "C succeeds on request {0} within B's retry budget")
    @ValueSource(ints = {1, 3})
    void succeedsWithinDownstreamRetryBudget(int successAt) {
        TestServer serverC = server("C", attempt -> succeedOnOrAfter(attempt, successAt));
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(2);

        assertThat(aToB.call()).isTrue();

        serverB.assertResponses(SUCCESS);
        serverC.assertResponses(successAt - 1, INTERNAL_ERROR, SUCCESS);
        aToB.assertDiagnostics();
        bToC.assertDiagnostics();
    }

    @ParameterizedTest(name = "C succeeds on request {0} after {1} diagnostic retries by A")
    @CsvSource({"4, 1", "12, 3"})
    void succeedsAfterDownstreamExhaustion(int successAt, int diagnosticRetries) {
        TestServer serverC = server("C", attempt -> succeedOnOrAfter(attempt, successAt));
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(3);

        assertThat(aToB.call()).isTrue();

        serverB.assertResponses(diagnosticRetries, EXHAUSTED_INTERNAL_ERROR, SUCCESS);
        serverC.assertResponses(successAt - 1, INTERNAL_ERROR, SUCCESS);
        aToB.assertDiagnostics(new DiagnosticMetrics("success", diagnosticRetries, 1));
        bToC.assertDiagnostics();
    }

    @Test
    void stopsBeforeRecoveryWhenRetryBudgetIsTooSmall() {
        TestServer serverC = server("C", attempt -> succeedOnOrAfter(attempt, 12));
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(2);

        aToB.assertFailure(500, true);

        // Three attempts by A, each allowing three attempts by B, cannot reach C's twelfth request.
        serverB.assertResponses(3, EXHAUSTED_INTERNAL_ERROR);
        serverC.assertResponses(9, INTERNAL_ERROR);
        aToB.assertDiagnostics(new DiagnosticMetrics("failure", 2, 1));
        bToC.assertDiagnostics();
    }

    @Test
    void propagatesExhaustionThroughIntermediateServiceWithRetriesDisabled() {
        TestServer serverD = server("D", _attempt -> {
            throw new ServiceException(ErrorType.INTERNAL);
        });
        TestClient cToD = serverD.client(2);
        TestServer serverC = server("C", _attempt -> cToD.call());
        TestClient bToC = serverC.client(0);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(0);

        // Neither A nor B can add an exhaustion marker by retrying: both must receive it from downstream.
        aToB.assertFailure(500, true);

        serverB.assertResponses(EXHAUSTED_INTERNAL_ERROR);
        serverC.assertResponses(EXHAUSTED_INTERNAL_ERROR);
        serverD.assertResponses(3, INTERNAL_ERROR);
        aToB.assertDiagnostics();
        bToC.assertDiagnostics();
        cToD.assertDiagnostics();
    }

    @Test
    void exhaustsRetriesAtEveryHopInThreeHopChain() {
        TestServer serverD = server("D", _attempt -> {
            throw new ServiceException(ErrorType.INTERNAL);
        });
        TestClient cToD = serverD.client(1);
        TestServer serverC = server("C", _attempt -> cToD.call());
        TestClient bToC = serverC.client(1);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(1);

        aToB.assertFailure(500, true);

        serverB.assertResponses(2, EXHAUSTED_INTERNAL_ERROR);
        serverC.assertResponses(4, EXHAUSTED_INTERNAL_ERROR);
        serverD.assertResponses(8, INTERNAL_ERROR);
        aToB.assertDiagnostics(new DiagnosticMetrics("failure", 1, 1));
        bToC.assertDiagnostics(new DiagnosticMetrics("failure", 2, 2));
        cToD.assertDiagnostics();
    }

    @Test
    void succeedsOnTwelfthRequestInThreeHopChain() {
        TestServer serverD = server("D", attempt -> succeedOnOrAfter(attempt, 12));
        TestClient cToD = serverD.client(2);
        TestServer serverC = server("C", _attempt -> cToD.call());
        TestClient bToC = serverC.client(1);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(1);

        assertThat(aToB.call()).isTrue();

        serverB.assertResponses(EXHAUSTED_INTERNAL_ERROR, SUCCESS);
        serverC.assertResponses(3, EXHAUSTED_INTERNAL_ERROR, SUCCESS);
        serverD.assertResponses(11, INTERNAL_ERROR, SUCCESS);
        aToB.assertDiagnostics(new DiagnosticMetrics("success", 1, 1));
        // B's first call to C fails, while its second call recovers after one diagnostic retry.
        bToC.assertDiagnostics(new DiagnosticMetrics("failure", 1, 1), new DiagnosticMetrics("success", 1, 1));
        cToD.assertDiagnostics();
    }

    @Test
    void propagatesExhaustionWhenBWrapsTheFailureInANonRetryableError() {
        TestServer serverC = server("C", _attempt -> {
            throw new ServiceException(ErrorType.INTERNAL);
        });
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", _attempt -> {
            try {
                return bToC.call();
            } catch (RemoteException exception) {
                throw new ServiceException(ErrorType.CONFLICT, exception);
            }
        });
        TestClient aToB = serverB.client(2);

        aToB.assertFailure(409, true);

        serverB.assertResponses(EXHAUSTED_CONFLICT);
        serverC.assertResponses(3, INTERNAL_ERROR);
        // Receiving the marker alone does not count as a diagnostic retry, and a 409 must not be retried.
        aToB.assertDiagnostics();
        bToC.assertDiagnostics();
    }

    @Test
    void stopsOnAnUnmarkedNonRetryableErrorAfterADiagnosticRetry() {
        TestServer serverC = server("C", _attempt -> {
            throw new ServiceException(ErrorType.INTERNAL);
        });
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", attempt -> {
            if (attempt == 1) {
                return bToC.call();
            }
            throw new ServiceException(ErrorType.INVALID_ARGUMENT);
        });
        TestClient aToB = serverB.client(3);

        aToB.assertFailure(400, false);

        serverB.assertResponses(EXHAUSTED_INTERNAL_ERROR, BAD_REQUEST);
        serverC.assertResponses(3, INTERNAL_ERROR);
        aToB.assertDiagnostics(new DiagnosticMetrics("failure", 1, 1));
        bToC.assertDiagnostics();
    }

    @Test
    void continuesCountingDiagnosticRetriesAfterAnUnmarkedRetryableError() {
        TestServer serverC = server("C", _attempt -> {
            throw new ServiceException(ErrorType.INTERNAL);
        });
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", attempt -> {
            if (attempt == 1) {
                return bToC.call();
            }
            return succeedOnOrAfter(attempt, 3);
        });
        TestClient aToB = serverB.client(3);

        assertThat(aToB.call()).isTrue();

        serverB.assertResponses(EXHAUSTED_INTERNAL_ERROR, INTERNAL_ERROR, SUCCESS);
        serverC.assertResponses(3, INTERNAL_ERROR);
        aToB.assertDiagnostics(new DiagnosticMetrics("success", 2, 1));
        bToC.assertDiagnostics();
    }

    @Test
    void diagnosticStateDoesNotLeakToTheNextCallOnTheSameClient() {
        TestServer serverC = server("C", attempt -> succeedOnOrAfter(attempt, 4));
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", attempt -> {
            if (attempt <= 2) {
                return bToC.call();
            }
            return succeedOnOrAfter(attempt, 5);
        });
        TestClient aToB = serverB.client(2);

        assertThat(aToB.call()).isTrue();
        aToB.assertDiagnostics(new DiagnosticMetrics("success", 1, 1));
        serverB.assertResponses(EXHAUSTED_INTERNAL_ERROR, SUCCESS);

        // This call retries twice, but neither failure comes from an exhausted downstream client.
        assertThat(aToB.call()).isTrue();

        serverB.assertResponses(EXHAUSTED_INTERNAL_ERROR, SUCCESS, INTERNAL_ERROR, INTERNAL_ERROR, SUCCESS);
        serverC.assertResponses(3, INTERNAL_ERROR, SUCCESS);
        aToB.assertDiagnostics(new DiagnosticMetrics("success", 1, 1));
        bToC.assertDiagnostics();
    }

    @ParameterizedTest(name = "A retries a local B failure {0} times")
    @ValueSource(ints = {0, 2})
    void localFailuresDoNotProduceDiagnosticRetries(int retries) {
        TestServer serverB = server("B", _attempt -> {
            throw new ServiceException(ErrorType.INTERNAL);
        });
        TestClient aToB = serverB.client(retries);

        // When retries are enabled, A adds its own marker after exhausting them. B never sent a marker.
        aToB.assertFailure(500, retries > 0);

        serverB.assertResponses(retries + 1, INTERNAL_ERROR);
        aToB.assertDiagnostics();
    }

    @Test
    void propagatesExhaustionForQosUnavailable() {
        TestServer serverC = server("C", _attempt -> {
            throw QosException.unavailable();
        });
        TestClient bToC = serverC.client(2);
        TestServer serverB = server("B", _attempt -> bToC.call());
        TestClient aToB = serverB.client(1);

        assertThatThrownBy(aToB::call)
                .isInstanceOfSatisfying(
                        QosException.Unavailable.class, failure -> assertExhaustionMarker(failure, true));

        serverB.assertResponses(2, EXHAUSTED_UNAVAILABLE);
        serverC.assertResponses(6, UNAVAILABLE);
        aToB.assertDiagnostics(new DiagnosticMetrics("failure", 1, 1));
        bToC.assertDiagnostics();
    }

    @ParameterizedTest(name = "plain-text 500: B retries = {0}, A retries = {1}")
    @CsvSource({"0, 0", "2, 0", "2, 1"})
    void propagatesExhaustionFromUnknownRemoteException(int downstreamRetries, int outerRetries) {
        AtomicInteger downstreamAttempts = new AtomicInteger();
        TestClient bToC = rawServerClient(
                exchange -> {
                    downstreamAttempts.incrementAndGet();
                    exchange.setStatusCode(500);
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Downstream failed");
                },
                downstreamRetries);
        List<UnknownRemoteException> downstreamFailures = new CopyOnWriteArrayList<>();
        TestServer serverB = server("B", _attempt -> {
            try {
                return bToC.call();
            } catch (UnknownRemoteException failure) {
                downstreamFailures.add(failure);
                throw failure;
            }
        });
        TestClient aToB = serverB.client(outerRetries);
        boolean retriesExhausted = downstreamRetries > 0;

        aToB.assertFailure(500, retriesExhausted);

        // Assert what B receives before Conjure converts it to a normal INTERNAL error for A.
        assertThat(downstreamFailures).hasSize(outerRetries + 1).allSatisfy(failure -> {
            assertThat(failure.getStatus()).isEqualTo(500);
            assertThat(failure.getBody()).isEqualTo("Downstream failed");
            assertExhaustionMarker(failure, retriesExhausted);
        });
        assertThat(downstreamAttempts).hasValue((downstreamRetries + 1) * (outerRetries + 1));
        serverB.assertResponses(outerRetries + 1, retriesExhausted ? EXHAUSTED_INTERNAL_ERROR : INTERNAL_ERROR);
        if (outerRetries == 0) {
            aToB.assertDiagnostics();
        } else {
            aToB.assertDiagnostics(new DiagnosticMetrics("failure", outerRetries, 1));
        }
        bToC.assertDiagnostics();
    }

    @ParameterizedTest(name = "connection closed: B retries = {0}, A retries = {1}")
    @CsvSource({"0, 0", "2, 0", "2, 1"})
    void propagatesExhaustionFromIoException(int downstreamRetries, int outerRetries) {
        AtomicInteger downstreamAttempts = new AtomicInteger();
        TestClient bToC = rawServerClient(
                exchange -> {
                    downstreamAttempts.incrementAndGet();
                    // Fail at the transport layer, before any HTTP response or exhaustion header can be sent.
                    exchange.getConnection().close();
                },
                downstreamRetries);
        List<RuntimeException> downstreamFailures = new CopyOnWriteArrayList<>();
        TestServer serverB = server("B", _attempt -> {
            try {
                return bToC.call();
            } catch (RuntimeException failure) {
                downstreamFailures.add(failure);
                throw failure;
            }
        });
        TestClient aToB = serverB.client(outerRetries);
        boolean retriesExhausted = downstreamRetries > 0;

        aToB.assertFailure(500, retriesExhausted);

        assertThat(downstreamFailures).hasSize(outerRetries + 1).allSatisfy(failure -> {
            // Blocking Dialogue clients wrap checked IOExceptions; the marker lives on the cause.
            assertThat(failure).hasCauseInstanceOf(IOException.class);
            assertExhaustionMarker(failure.getCause(), retriesExhausted);
        });
        assertThat(downstreamAttempts).hasValue((downstreamRetries + 1) * (outerRetries + 1));
        serverB.assertResponses(outerRetries + 1, retriesExhausted ? EXHAUSTED_INTERNAL_ERROR : INTERNAL_ERROR);
        if (outerRetries == 0) {
            aToB.assertDiagnostics();
        } else {
            aToB.assertDiagnostics(new DiagnosticMetrics("failure", outerRetries, 1));
        }
        bToC.assertDiagnostics();
    }

    private TestServer server(String name, IntPredicate response) {
        TestServer server = new TestServer(name, response);
        servers.add(server.server);
        return server;
    }

    private TestClient rawServerClient(HttpHandler handler, int retries) {
        Undertow server = startServer(handler);
        servers.add(server);
        return new TestClient(port(server), retries);
    }

    private static Undertow startServer(HttpHandler handler) {
        Undertow server = Undertow.builder()
                .setIoThreads(1)
                .setWorkerThreads(4)
                .addHttpListener(0, "localhost")
                .setHandler(Handlers.path().addPrefixPath("/test-example/api", handler))
                .build();
        server.start();
        return server;
    }

    private static int port(Undertow server) {
        return ((InetSocketAddress)
                        Iterables.getOnlyElement(server.getListenerInfo()).getAddress())
                .getPort();
    }

    private static boolean succeedOnOrAfter(int attempt, int successAt) {
        if (attempt < successAt) {
            throw new ServiceException(ErrorType.INTERNAL);
        }
        return true;
    }

    private static void assertExhaustionMarker(Throwable failure, boolean expected) {
        // Blocking clients may wrap the remote exception to capture the calling thread's stack trace.
        boolean hasMarker = Throwables.getCausalChain(failure).stream()
                .flatMap(cause -> Arrays.stream(cause.getSuppressed()))
                .filter(SafeLoggable.class::isInstance)
                .map(SafeLoggable.class::cast)
                .flatMap(diagnostic -> diagnostic.getArgs().stream())
                .anyMatch(SafeArg.of(RETRIES_EXHAUSTED_HEADER.toString(), "true")::equals);
        assertThat(hasMarker).as("Exhaustion marker on the client exception").isEqualTo(expected);
    }

    private record ObservedResponse(int status, Optional<String> retriesExhaustedHeader) {}

    private record DiagnosticMetrics(String result, long retries, long requests) {}

    private static final class TestServer {
        private final String name;
        private final AtomicInteger attempts = new AtomicInteger();
        private final List<ObservedResponse> responses = new CopyOnWriteArrayList<>();
        private final Undertow server;
        private final int port;

        TestServer(String name, IntPredicate response) {
            this.name = name;
            HttpHandler handler = ConjureHandler.builder()
                    .services(EmptyPathServiceEndpoints.of(() -> response.test(attempts.incrementAndGet())))
                    .build();
            HttpHandler recordingHandler = exchange -> {
                // Capture the actual outgoing status/header before the client can receive the response.
                exchange.addResponseCommitListener(committed -> responses.add(new ObservedResponse(
                        committed.getStatusCode(),
                        Optional.ofNullable(committed.getResponseHeaders().getFirst(RETRIES_EXHAUSTED_HEADER)))));
                handler.handleRequest(exchange);
            };
            server = startServer(recordingHandler);
            port = port(server);
        }

        TestClient client(int retries) {
            return new TestClient(port, retries);
        }

        void assertResponses(ObservedResponse... expected) {
            assertThat(attempts.get()).as("Requests reaching %s", name).isEqualTo(expected.length);
            assertThat(responses).as("Responses sent by %s", name).containsExactly(expected);
        }

        void assertResponses(int repetitions, ObservedResponse repeated, ObservedResponse... remaining) {
            List<ObservedResponse> expected = new ArrayList<>(Collections.nCopies(repetitions, repeated));
            expected.addAll(Arrays.asList(remaining));
            assertResponses(expected.toArray(ObservedResponse[]::new));
        }
    }

    private static final class TestClient {
        private final TaggedMetricRegistry registry = new DefaultTaggedMetricRegistry();
        private final EmptyPathServiceBlocking delegate;

        TestClient(int port, int retries) {
            delegate = DialogueClients.create(
                    EmptyPathServiceBlocking.class,
                    ClientConfiguration.builder()
                            .from(clientConfiguration(port))
                            .maxNumRetries(retries)
                            .backoffSlotSize(Duration.ZERO)
                            .taggedMetricRegistry(registry)
                            .build());
        }

        boolean call() {
            return delegate.emptyPath();
        }

        void assertFailure(int status, boolean retriesExhausted) {
            assertThatThrownBy(this::call).isInstanceOfSatisfying(RemoteException.class, failure -> {
                assertThat(failure.getStatus()).isEqualTo(status);
                assertExhaustionMarker(failure, retriesExhausted);
            });
        }

        void assertDiagnostics(DiagnosticMetrics... expected) {
            List<Tuple> actualMetrics = registry.getMetrics().entrySet().stream()
                    .filter(entry -> entry.getKey().safeName().startsWith(DIAGNOSTIC_METRIC_PREFIX))
                    .map(entry -> tuple(
                            entry.getKey().safeName(),
                            entry.getKey().safeTags().get("result"),
                            ((Meter) entry.getValue()).getCount()))
                    .toList();
            List<Tuple> expectedMetrics = new ArrayList<>();
            for (DiagnosticMetrics metrics : expected) {
                expectedMetrics.add(tuple(DIAGNOSTIC_METRIC_PREFIX + "retries", metrics.result(), metrics.retries()));
                expectedMetrics.add(tuple(DIAGNOSTIC_METRIC_PREFIX + "requests", metrics.result(), metrics.requests()));
            }
            assertThat(actualMetrics).containsExactlyInAnyOrderElementsOf(expectedMetrics);
        }
    }
}
