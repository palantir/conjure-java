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

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.serialization.ObjectMappers;
import com.palantir.conjure.java.undertow.lib.Endpoint;
import com.palantir.conjure.java.undertow.lib.ReturnValueWriter;
import com.palantir.conjure.java.undertow.lib.UndertowRuntime;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import java.io.IOException;
import java.time.Duration;
import java.util.OptionalInt;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class AsyncRequestProcessingTest {

    private static final ObjectMapper clientMapper = ObjectMappers.newClientObjectMapper();

    private Undertow server;
    private ListeningScheduledExecutorService scheduler;

    @Before
    public void before() {
        this.scheduler = MoreExecutors.listeningDecorator(Executors.newSingleThreadScheduledExecutor());
        UndertowRuntime runtime = ConjureUndertowRuntime.builder()
                // Use a 1 second timeout so we can wait for the timeout to be hit without tests running too long.
                .asyncTimeout(Duration.ofSeconds(1))
                .build();
        ReturnValueWriter<String> writer = (returnValue, exchange) -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(returnValue);
        };
        HttpHandler httpHandler = exchange -> {
            OptionalInt maybeDelay = runtime.plainSerDe()
                    .deserializeOptionalInteger(exchange.getQueryParameters().get("delay"));
            final ListenableFuture<String> future;
            if (maybeDelay.isPresent()) {
                int delay = maybeDelay.getAsInt();
                future = Futures.transform(
                        scheduler.schedule(() -> { }, delay, TimeUnit.MILLISECONDS),
                        ignored -> "Completed after " + delay + "ms",
                        MoreExecutors.directExecutor());
            } else {
                future = Futures.immediateFuture("Completed immediately");
            }
            runtime.async().register(future, writer, exchange);
        };
        HttpHandler handler = ConjureHandler.builder()
                .endpoints(new Endpoint() {
                    @Override
                    public HttpString method() {
                        return Methods.GET;
                    }

                    @Override
                    public String template() {
                        return "/test";
                    }

                    @Override
                    public HttpHandler handler() {
                        return httpHandler;
                    }

                    @Override
                    public String serviceName() {
                        return "TestService";
                    }

                    @Override
                    public String name() {
                        return "test";
                    }
                }).build();
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(handler)
                .build();
        server.start();
    }

    @After
    public void after() throws InterruptedException {
        server.stop();
        scheduler.shutdownNow();
        assertThat(scheduler.awaitTermination(1, TimeUnit.MINUTES))
                .describedAs("Executor failed to shut down")
                .isTrue();
    }

    @Test
    public void testNoDelay() throws IOException {
        try (Response response = execute(OptionalInt.empty())) {
            assertThat(response).matches(Response::isSuccessful);
            assertThat(response.body().string()).isEqualTo("Completed immediately");
        }
    }

    @Test
    public void testTinyDelay() throws IOException {
        try (Response response = execute(OptionalInt.of(0))) {
            assertThat(response).matches(Response::isSuccessful);
            assertThat(response.body().string()).isEqualTo("Completed after 0ms");
        }
    }

    @Test
    public void testSmallDelay() throws IOException {
        try (Response response = execute(OptionalInt.of(100))) {
            assertThat(response).matches(Response::isSuccessful);
            assertThat(response.body().string()).isEqualTo("Completed after 100ms");
        }
    }

    @Test
    public void testTimeout() throws IOException {
        try (Response response = execute(OptionalInt.of(2000))) {
            assertThat(response).matches(resp -> resp.code() == 500);
            SerializableError error = clientMapper.readValue(response.body().byteStream(), SerializableError.class);
            assertThat(error.errorCode()).isEqualTo("INTERNAL");
        }
    }

    @Test
    public void testExceptionThrownInHandlerMethod() {
        // TODO(ckozak): Exceptions thrown in the service method should be handled the same way as blocking services.
    }

    private static Response execute(OptionalInt delay) {
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/test" + (delay.isPresent() ? "?delay=" + delay.getAsInt() : ""))
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
