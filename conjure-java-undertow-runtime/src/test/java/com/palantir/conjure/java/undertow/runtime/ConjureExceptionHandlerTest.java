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

package com.palantir.conjure.java.undertow.runtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.google.common.collect.ImmutableList;
import com.palantir.conjure.java.api.errors.CheckedRemoteException;
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.logsafe.Arg;
import com.palantir.logsafe.SafeArg;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ConjureExceptionHandlerTest {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .followRedirects(false) // we want to explicitly test the 'Location' header
            .build();

    private RuntimeException exception = null;
    private Exception checkedException;
    private Undertow server;

    private static class TestCheckedServiceException extends CheckedServiceException {
        protected TestCheckedServiceException(ErrorType errorType, Arg<?>... parameters) {
            super(errorType, parameters);
        }
    }

    private static class TestCheckedRemoteException extends CheckedRemoteException {
        protected TestCheckedRemoteException(SerializableError error, int status) {
            super(error, status);
        }
    }

    private class Delegate implements HttpHandler {
        @Override
        public void handleRequest(HttpServerExchange exchange) throws Exception {
            if (exception != null) {
                throw exception;
            } else {
                throw checkedException;
            }
        }
    }

    @BeforeEach
    public void before() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(
                        new BlockingHandler(new ConjureExceptionHandler(new Delegate(), ConjureExceptions.INSTANCE)))
                .build();
        server.start();
    }

    @AfterEach
    public void after() {
        server.stop();
    }

    @Test
    public void handlesServiceException() throws IOException {
        exception = new ServiceException(ErrorType.CONFLICT, SafeArg.of("foo", "bar"));
        Response response = execute();
        assertThat(response.body().string())
                .contains("{\"errorCode\":\"CONFLICT\"")
                .contains("\"parameters\":{\"foo\":\"bar\"}}");
        assertThat(response.code()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
    }

    @Test
    public void handlesCheckedServiceException() throws IOException {
        exception = null;
        checkedException = new TestCheckedServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("bar", "baz"));
        Response response = execute();
        assertThat(response.body().string())
                .contains("{\"errorCode\":\"INVALID_ARGUMENT\"")
                .contains("\"parameters\":{\"bar\":\"baz\"}}");
        assertThat(response.code()).isEqualTo(ErrorType.INVALID_ARGUMENT.httpErrorCode());
    }

    @Test
    public void handlesRemoteException() throws IOException {
        SerializableError remoteError =
                SerializableError.forException(new ServiceException(ErrorType.CONFLICT, SafeArg.of("foo", "bar")));
        exception = new RemoteException(remoteError, ErrorType.CONFLICT.httpErrorCode());
        Response response = execute();

        // Propagates errorInstanceId and changes error code and name to INTERNAL
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.INTERNAL.code().toString())
                .errorName(ErrorType.INTERNAL.name())
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        assertThat(response.body().string()).isEqualTo(stream.toString());
        // remote exceptions should result in 500 status
        assertThat(response.code()).isEqualTo(ErrorType.INTERNAL.httpErrorCode());
    }

    @Test
    public void handlesCheckedRemoteException() throws IOException {
        SerializableError remoteError = SerializableError.forException(
                new ServiceException(ErrorType.INVALID_ARGUMENT, SafeArg.of("bar", "baz")));
        exception = null;
        checkedException = new TestCheckedRemoteException(remoteError, ErrorType.INVALID_ARGUMENT.httpErrorCode());
        Response response = execute();

        // Propagates errorInstanceId and changes error code and name to INTERNAL
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.INTERNAL.code().toString())
                .errorName(ErrorType.INTERNAL.name())
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        assertThat(response.body().string()).isEqualTo(stream.toString());
        // remote exceptions should result in 500 status
        assertThat(response.code()).isEqualTo(ErrorType.INTERNAL.httpErrorCode());
    }

    @Test
    public void handles401RemoteException() throws IOException {
        SerializableError remoteError =
                SerializableError.forException(new ServiceException(ErrorType.UNAUTHORIZED, SafeArg.of("foo", "bar")));
        exception = new RemoteException(remoteError, ErrorType.UNAUTHORIZED.httpErrorCode());
        Response response = execute();

        // Propagates errorInstanceId and does not change error code and name
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.UNAUTHORIZED.code().toString())
                .errorName(ErrorType.UNAUTHORIZED.name())
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        assertThat(response.body().string()).isEqualTo(stream.toString());
        assertThat(response.code()).isEqualTo(ErrorType.UNAUTHORIZED.httpErrorCode());
    }

    @Test
    public void handles403RemoteException() throws IOException {
        SerializableError remoteError = SerializableError.forException(
                new ServiceException(ErrorType.PERMISSION_DENIED, SafeArg.of("foo", "bar")));
        exception = new RemoteException(remoteError, ErrorType.PERMISSION_DENIED.httpErrorCode());
        Response response = execute();

        // Propagates errorInstanceId and does not change error code and name
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.PERMISSION_DENIED.code().toString())
                .errorName(ErrorType.PERMISSION_DENIED.name())
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        assertThat(response.body().string()).isEqualTo(stream.toString());
        assertThat(response.code()).isEqualTo(ErrorType.PERMISSION_DENIED.httpErrorCode());
    }

    @Test
    public void handlesQosExceptionThrottleWithoutDuration() throws IOException {
        exception = QosException.throttle();
        Response response = execute();

        assertThat(response.code()).isEqualTo(429);
        assertThat(response.body().string()).isEmpty();
        assertThat(response.headers().toMultimap()).doesNotContainKey("Retry-After");
        assertThat(response.headers().toMultimap()).containsOnlyKeys("connection", "content-length", "date");
    }

    @Test
    public void handlesQosExceptionThrottleWithDuration() throws IOException {
        exception = QosException.throttle(Duration.ofMinutes(2));
        Response response = execute();

        assertThat(response.code()).isEqualTo(429);
        assertThat(response.headers().toMultimap()).containsEntry("Retry-After", ImmutableList.of("120"));
        assertThat(response.body().string()).isEmpty();
    }

    @Test
    public void handlesQosExceptionRetryOther() throws IOException {
        exception = QosException.retryOther(new URL("http://foo"));
        Response response = execute();

        assertThat(response.code()).isEqualTo(308);
        assertThat(response.headers().toMultimap()).containsEntry("Location", ImmutableList.of("http://foo"));
        assertThat(response.body().string()).isEmpty();
    }

    @Test
    public void handlesQosExceptionUnavailable() throws IOException {
        exception = QosException.unavailable();
        Response response = execute();

        assertThat(response.code()).isEqualTo(503);
        assertThat(response.body().string()).isEmpty();
    }

    @Test
    public void handlesIllegalArgumentException() throws IOException {
        exception = new IllegalArgumentException("Foo");
        Response response = execute();
        assertThat(response.body().string()).contains("{\"errorCode\":\"INVALID_ARGUMENT\"");
        assertThat(response.code()).isEqualTo(ErrorType.INVALID_ARGUMENT.httpErrorCode());
    }

    @Test
    public void handlesRuntimeException() throws IOException {
        exception = new RuntimeException("Foo");
        Response response = execute();
        assertThat(response.body().string()).contains("{\"errorCode\":\"INTERNAL\"");
        assertThat(response.code()).isEqualTo(ErrorType.INTERNAL.httpErrorCode());
    }

    @Test
    public void handlesErrorWithoutSendingResponseBody() throws IOException {
        server.stop();
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new BlockingHandler(new ConjureExceptionHandler(
                        _exchange -> {
                            throw new Error();
                        },
                        ConjureExceptions.INSTANCE)))
                .build();
        server.start();

        Response response = execute();
        assertThat(response.body().string()).isEmpty();
        assertThat(response.code()).isEqualTo(500);
    }

    @Test
    public void handlesErrorWithoutRethrowing() {
        HttpHandler handler = new ConjureExceptionHandler(
                _exchange -> {
                    throw new Error();
                },
                ConjureExceptions.INSTANCE);
        assertThatCode(() -> handler.handleRequest(HttpServerExchanges.createStub()))
                .doesNotThrowAnyException();
    }

    private static Response execute() {
        Request request =
                new Request.Builder().get().url("http://localhost:12345").build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
