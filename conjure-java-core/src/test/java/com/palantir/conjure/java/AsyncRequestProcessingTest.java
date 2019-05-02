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
import com.google.common.net.HttpHeaders;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.defs.Conjure;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.services.UndertowServiceGenerator;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.ConjureUndertowRuntime;
import com.palantir.conjure.spec.ConjureDefinition;
import com.palantir.product.AsyncRequestProcessingTestServiceEndpoints;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class AsyncRequestProcessingTest extends TestBase {

    @ClassRule
    public static final TemporaryFolder folder = new TemporaryFolder();

    private static final ObjectMapper CLIENT_MAPPER = ObjectMappers.newClientObjectMapper();

    private static final int PORT = 12347;

    private ListeningScheduledExecutorService executor;
    private Undertow server;

    @BeforeClass
    public static void beforeClass() throws IOException {
        ConjureDefinition def = Conjure.parse(ImmutableList.of(
                new File("src/test/resources/async-request-processing-test.yml")));
        List<Path> files = new UndertowServiceGenerator(ImmutableSet.of(
                FeatureFlags.UndertowListenableFutures, FeatureFlags.UndertowServicePrefix))
                .emit(def, folder.getRoot());
        validateGeneratorOutput(files, Paths.get("src/integrationInput/java/com/palantir/product"));
    }

    @Before
    public void before() {
        executor = MoreExecutors.listeningDecorator(Executors.newSingleThreadScheduledExecutor());
        UndertowRuntime context = ConjureUndertowRuntime.builder()
                // Use a 1 second timeout so we can wait for the timeout to be hit without tests running too long.
                .asyncTimeout(Duration.ofSeconds(1))
                .build();

        server = Undertow.builder()
                .setServerOption(UndertowOptions.DECODE_URL, false)
                .addHttpListener(PORT, "0.0.0.0")
                .setHandler(ConjureHandler.builder()
                        .addAllEndpoints(ImmutableList.of(AsyncRequestProcessingTestServiceEndpoints.of(
                                new AsyncRequestProcessingTestResource(executor)))
                                .stream()
                                .flatMap(service -> service.endpoints(context).stream())
                                .collect(ImmutableList.toImmutableList()))
                        .build())
                .build();
        server.start();
    }

    @After
    public void after() throws InterruptedException {
        executor.shutdownNow();
        server.stop();
        assertThat(executor.awaitTermination(10, TimeUnit.SECONDS)).isTrue();
    }

    @Test
    public void testNoDelay() throws IOException {
        try (Response response = requestToDelayEndpoint(OptionalInt.empty())) {
            assertThat(response).matches(Response::isSuccessful);
            assertThat(response.body().string()).isEqualTo("\"Completed immediately\"");
        }
    }

    @Test
    public void testTinyDelay() throws IOException {
        try (Response response = requestToDelayEndpoint(OptionalInt.of(0))) {
            assertThat(response).matches(Response::isSuccessful);
            assertThat(response.body().string()).isEqualTo("\"Completed after 0ms\"");
        }
    }

    @Test
    public void testSmallDelay() throws IOException {
        try (Response response = requestToDelayEndpoint(OptionalInt.of(100))) {
            assertThat(response).matches(Response::isSuccessful);
            assertThat(response.body().string()).isEqualTo("\"Completed after 100ms\"");
        }
    }

    @Test
    public void testTimeout() throws IOException {
        try (Response response = requestToDelayEndpoint(OptionalInt.of(2000))) {
            assertThat(response).matches(resp -> resp.code() == 500);
            SerializableError error = CLIENT_MAPPER.readValue(response.body().byteStream(), SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("INTERNAL");
        }
    }

    @Test
    public void testExceptionThrownInHandlerMethod() throws IOException {
        try (Response response = client().newCall(new Request.Builder()
                .get()
                .url("http://localhost:" + PORT + "/async/throws")
                .build()).execute()) {
            assertThat(response).matches(resp -> resp.code() == ErrorType.CONFLICT.httpErrorCode());
            SerializableError error = CLIENT_MAPPER.readValue(response.body().byteStream(), SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("CONFLICT");
        }
    }

    @Test
    public void testFailedFuture() throws IOException {
        try (Response response = client().newCall(new Request.Builder()
                .get()
                .url("http://localhost:" + PORT + "/async/failed-future")
                .build()).execute()) {
            assertThat(response).matches(resp -> resp.code() == ErrorType.CONFLICT.httpErrorCode());
            SerializableError error = CLIENT_MAPPER.readValue(response.body().byteStream(), SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("CONFLICT");
        }
    }

    @Test
    public void testFailedFutureAsyncDelay() throws IOException {
        try (Response response = client().newCall(new Request.Builder()
                .get()
                .url("http://localhost:" + PORT + "/async/failed-future?delayMillis=100")
                .build()).execute()) {
            assertThat(response).matches(resp -> resp.code() == ErrorType.CONFLICT.httpErrorCode());
            SerializableError error = CLIENT_MAPPER.readValue(response.body().byteStream(), SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("CONFLICT");
        }
    }

    @Test
    public void testAsyncOptionalBinaryNotPresent() throws IOException {
        try (Response response = client().newCall(new Request.Builder()
                .get()
                .url("http://localhost:" + PORT + "/async/binary")
                .build()).execute()) {
            assertThat(response).matches(resp -> resp.code() == 204);
            assertThat(response.header(HttpHeaders.CONTENT_TYPE)).isNull();
        }
    }

    @Test
    public void testAsyncOptionalBinaryPresent() throws IOException {
        try (Response response = client().newCall(new Request.Builder()
                .get()
                .url("http://localhost:" + PORT + "/async/binary?stringValue=Hello")
                .build()).execute()) {
            assertThat(response).matches(resp -> resp.code() == 200);
            assertThat(response.header(HttpHeaders.CONTENT_TYPE)).startsWith("application/octet-stream");
            assertThat(new String(response.body().bytes(), StandardCharsets.UTF_8)).isEqualTo("Hello");
        }
    }

    private static Response requestToDelayEndpoint(OptionalInt delay) {
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:" + PORT + "/async/delay"
                        + (delay.isPresent() ? "?delayMillis=" + delay.getAsInt() : ""))
                .build();
        try {
            return client().newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .cache(null)
                .build();
    }
}
