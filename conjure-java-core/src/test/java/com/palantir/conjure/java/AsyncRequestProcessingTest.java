/*
 * (c) Copyright 2019 Palantir Technologies Inc. All rights reserved.
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

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.api.config.service.UserAgent;
import com.palantir.conjure.java.api.config.ssl.SslConfiguration;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.client.config.ClientConfigurations;
import com.palantir.conjure.java.client.jaxrs.JaxRsClient;
import com.palantir.conjure.java.config.ssl.SslSocketFactories;
import com.palantir.conjure.java.okhttp.HostMetricsRegistry;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.services.JerseyServiceGenerator;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.ConjureUndertowRuntime;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.product.AsyncRequestProcessingTestService;
import com.palantir.product.AsyncRequestProcessingTestServiceEndpoints;
import com.palantir.tracing.Tracer;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public final class AsyncRequestProcessingTest extends TestBase {

    @TempDir
    public static File folder;

    private static final ObjectMapper CLIENT_MAPPER = ObjectMappers.newClientObjectMapper();
    private static final SslConfiguration TRUST_STORE_CONFIGURATION = new SslConfiguration.Builder()
            .trustStorePath(Paths.get("var/security/truststore.jks"))
            .build();

    private static final int PORT = 12347;

    private ListeningScheduledExecutorService executor;
    private Undertow server;
    private AsyncRequestProcessingTestService client;

    @BeforeAll
    public static void beforeClass() throws IOException {
        ConjureDefinition def =
                Conjure.parse(ImmutableList.of(new File("src/test/resources/async-request-processing-test.yml")));
        List<Path> files = new GenerationCoordinator(
                        MoreExecutors.directExecutor(),
                        ImmutableSet.of(
                                new UndertowServiceGenerator(Options.builder()
                                        .undertowServicePrefix(true)
                                        .undertowListenableFutures(true)
                                        .build()),
                                new JerseyServiceGenerator(Options.empty())))
                .emit(def, folder);
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }

    @BeforeEach
    public void before() {
        executor = MoreExecutors.listeningDecorator(Executors.newSingleThreadScheduledExecutor());
        server = Undertow.builder()
                .setServerOption(UndertowOptions.DECODE_URL, false)
                .addHttpListener(PORT, "0.0.0.0")
                .setHandler(ConjureHandler.builder()
                        .runtime(ConjureUndertowRuntime.builder()
                                // Use a 1 second timeout so we can wait for the timeout to be hit without tests running
                                // too long.
                                .asyncTimeout(Duration.ofSeconds(1))
                                .build())
                        .services(AsyncRequestProcessingTestServiceEndpoints.of(
                                new AsyncRequestProcessingTestResource(executor)))
                        .build())
                .build();
        server.start();
        client = JaxRsClient.create(
                AsyncRequestProcessingTestService.class,
                UserAgent.of(UserAgent.Agent.of("test", "develop")),
                new HostMetricsRegistry(),
                ClientConfigurations.of(
                        ImmutableList.of("http://localhost:" + PORT + "/"),
                        SslSocketFactories.createSslSocketFactory(TRUST_STORE_CONFIGURATION),
                        SslSocketFactories.createX509TrustManager(TRUST_STORE_CONFIGURATION)));
    }

    @AfterEach
    public void after() throws InterruptedException {
        executor.shutdownNow();
        server.stop();
        assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();
    }

    @Test
    public void testNoDelay() throws IOException {
        HttpURLConnection connection = requestToDelayEndpoint(OptionalInt.empty());
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(getResponseBody(connection)).isEqualTo("\"Completed immediately\"");
    }

    @Test
    public void testTinyDelay() throws IOException {
        HttpURLConnection connection = requestToDelayEndpoint(OptionalInt.of(0));
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(getResponseBody(connection)).isEqualTo("\"Completed after 0ms\"");
    }

    @Test
    public void testSmallDelay() throws IOException {
        HttpURLConnection connection = requestToDelayEndpoint(OptionalInt.of(100));
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(getResponseBody(connection)).isEqualTo("\"Completed after 100ms\"");
    }

    @Test
    public void testTimeout() throws IOException {
        HttpURLConnection connection = requestToDelayEndpoint(OptionalInt.of(2000));
        assertThat(connection.getResponseCode()).isEqualTo(500);
        SerializableError error = CLIENT_MAPPER.readValue(getErrorBody(connection), SerializableError.class);
        assertThat(error.errorCode()).isEqualTo("TIMEOUT");
        assertThat(error.errorName()).isEqualTo("Conjure:AsyncRequestProcessingTimeout");
        assertThat(error.parameters()).containsEntry("timeoutSeconds", "1");
    }

    @Test
    public void testFiveSecondTimeoutIsNotReached() throws IOException {
        HttpURLConnection connection = requestToDelayEndpointWithFiveSecondTimeout(OptionalInt.of(2000));
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(getResponseBody(connection)).isEqualTo("\"Completed after 2000ms\"");
    }

    @Test
    public void testExceptionThrownInHandlerMethod() throws IOException {
        HttpURLConnection connection = openConnectionToTestServer("/async/throws");
        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        SerializableError error = CLIENT_MAPPER.readValue(getErrorBody(connection), SerializableError.class);
        assertThat(error.errorCode()).isEqualTo("CONFLICT");
    }

    @Test
    public void testFailedFuture() throws IOException {
        HttpURLConnection connection = openConnectionToTestServer("/async/failed-future");
        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        SerializableError error = CLIENT_MAPPER.readValue(getErrorBody(connection), SerializableError.class);
        assertThat(error.errorCode()).isEqualTo("CONFLICT");
    }

    @Test
    public void testFailedFutureAsyncDelay() throws IOException {
        HttpURLConnection connection = openConnectionToTestServer("/async/failed-future?delayMillis=100");
        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        SerializableError error = CLIENT_MAPPER.readValue(getErrorBody(connection), SerializableError.class);
        assertThat(error.errorCode()).isEqualTo("CONFLICT");
    }

    @Test
    public void testAsyncOptionalBinaryNotPresent() throws IOException {
        HttpURLConnection connection = openConnectionToTestServer("/async/binary");
        assertThat(connection.getResponseCode()).isEqualTo(204);
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_TYPE)).isNull();
    }

    @Test
    public void testAsyncOptionalBinaryPresent() throws IOException {
        HttpURLConnection connection = openConnectionToTestServer("/async/binary?stringValue=Hello");
        assertThat(connection.getResponseCode()).isEqualTo(200);
        assertThat(connection.getHeaderField(HttpHeaders.CONTENT_TYPE)).startsWith("application/octet-stream");
        assertThat(getResponseBody(connection)).isEqualTo("Hello");
    }

    @Test
    public void testAsyncOperationHasTraceId_immediateResult() {
        Tracer.fastStartSpan("test");
        try {
            assertThat(client.futureTraceId(OptionalInt.empty())).asString().isEqualTo(Tracer.getTraceId());
        } finally {
            Tracer.fastCompleteSpan();
        }
    }

    @Test
    public void testAsyncOperationHasTraceId_delayedResult() {
        Tracer.fastStartSpan("test");
        try {
            assertThat(client.futureTraceId(OptionalInt.of(100))).asString().isEqualTo(Tracer.getTraceId());
        } finally {
            Tracer.fastCompleteSpan();
        }
    }

    private static HttpURLConnection requestToDelayEndpoint(OptionalInt delay) {
        return openConnectionToTestServer(
                "/async/delay" + (delay.isPresent() ? "?delayMillis=" + delay.getAsInt() : ""));
    }

    private static HttpURLConnection requestToDelayEndpointWithFiveSecondTimeout(OptionalInt delay) {
        return openConnectionToTestServer(
                "/async/delay-5s-timeout" + (delay.isPresent() ? "?delayMillis=" + delay.getAsInt() : ""));
    }

    private static String getResponseBody(HttpURLConnection connection) {
        try (InputStream response = connection.getInputStream()) {
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getErrorBody(HttpURLConnection connection) {
        try (InputStream response = connection.getErrorStream()) {
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpURLConnection openConnectionToTestServer(String path) {
        try {
            URL url = new URL("http://0.0.0.0:" + PORT + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
