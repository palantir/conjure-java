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

package com.palantir.conjure.java.undertow.demo;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import com.palantir.conjure.java.undertow.lib.Registrable;
import com.palantir.conjure.java.undertow.runtime.ConjureHandler;
import com.palantir.conjure.java.undertow.runtime.Serializer;
import com.palantir.conjure.java.undertow.runtime.Serializers;
import com.palantir.logsafe.exceptions.SafeRuntimeException;
import com.palantir.tracing.Tracers;
import com.palantir.tracing.api.TraceHttpHeaders;
import io.undertow.Undertow;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

// TODO(rfink): Run these tests against the code produced by the conjure generator
@RunWith(MockitoJUnitRunner.class)
public final class MyServiceUndertowHandlerTest {

    private static final OkHttpClient client = new OkHttpClient.Builder().build();
    private final Serializer serializer = Serializers.json();

    private Undertow server;

    @Before
    public void before() {
        Registrable registrable = new MyServiceUndertowHandler(
                new MyServiceImpl(),
                serializer
        );
        ConjureHandler handler = new ConjureHandler();
        registrable.register(handler);
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(handler)
                .build();
        server.start();
    }

    @After
    public void after() {
        server.stop();
    }

    @Test
    public void incrementTime_sanity() throws IOException {
        OffsetDateTime now = OffsetDateTime.now();
        int numIncHours = 2;

        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/inc/" + now + "/" + numIncHours)
                .build();

        OffsetDateTime result = serializer.deserialize(
                execute(request).byteStream(), new TypeToken<OffsetDateTime>() {});
        assertThat(result).isEqualTo(now.plusHours(numIncHours));
    }

    @Test
    public void isSunday_sanity() throws IOException {
        OffsetDateTime wednesday = OffsetDateTime.parse("2018-08-15T13:00:00.000+00:00");
        OffsetDateTime sunday = OffsetDateTime.parse("2018-08-19T13:00:00.000+00:00");

        Request request = new Request.Builder()
                .post(createBody(ImmutableList.of(wednesday, sunday)))
                .url("http://localhost:12345/issunday")
                .build();

        Map<OffsetDateTime, Boolean> result = serializer.deserialize(
                execute(request).byteStream(), new TypeToken<Map<OffsetDateTime, Boolean>>() {});
        assertThat(result)
                .containsEntry(wednesday, false)
                .containsEntry(sunday, true);
    }

    @Test
    public void handlesServiceExceptions() throws IOException {
        // Setup a request that throws a ServiceException (see MyServiceImpl)
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/inc/" + OffsetDateTime.now() + "/-1")  // ServiceException(INVALID_ARG)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(400);
        }
    }

    @Test
    public void handlesSerializationErrors() throws IOException {
        // Setup a request that throws a ServiceException (see MyServiceImpl)
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/inc/" + OffsetDateTime.now()
                        + "_BOGUS/2")  // ServiceException(INVALID_ARG)
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(400);
        }
    }

    @Test
    public void handlesNonServiceExceptions() throws IOException {
        // Setup a request that throws a RuntimeException (see MyServiceImpl)
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/inc/" + OffsetDateTime.now()
                        + "/0")  // RuntimeException
                .build();

        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(500);
        }
    }

    @Test
    public void handlesTraceIdPropagation() throws IOException {
        String traceId = Tracers.randomId();
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/traceId")
                .header(TraceHttpHeaders.TRACE_ID, traceId)
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(serializer.deserialize(
                    response.body().byteStream(), new TypeToken<String>() {})).isEqualTo(traceId);
        }
    }

    @Test
    public void handlesNotFound() throws IOException {
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/unmatched")
                .build();
        try (Response response = client.newCall(request).execute()) {
            // Do we expect a service exception here?
            assertThat(response.code()).isEqualTo(404);
        }
    }

    @Test
    public void handlesOptionalReturnValues() throws IOException {
        // Optional.of("foo") --> body with "foo" and response code 200.
        Request request = new Request.Builder()
                .get()
                .url("http://localhost:12345/maybeString/true")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(200);
            assertThat(serializer.deserialize(
                    response.body().byteStream(), new TypeToken<String>() {})).isEqualTo("foo");
        }

        // Optional.absent() --> empty body and response code 204.
        request = new Request.Builder()
                .get()
                .url("http://localhost:12345/maybeString/false")
                .build();
        try (Response response = client.newCall(request).execute()) {
            assertThat(response.code()).isEqualTo(204);
            assertThat(response.body().contentLength()).isEqualTo(0);
        }
    }

    private RequestBody createBody(Object value) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return MediaType.parse("application/json");
            }

            @Override
            public void writeTo(BufferedSink sink) {
                try {
                    serializer.serialize(value, sink.outputStream());
                } catch (IOException e) {
                    throw new SafeRuntimeException(e);
                }
            }
        };
    }

    private static ResponseBody execute(Request request) {
        try {
            Response response = client.newCall(request).execute();
            assertThat(response.code()).isEqualTo(200);
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
