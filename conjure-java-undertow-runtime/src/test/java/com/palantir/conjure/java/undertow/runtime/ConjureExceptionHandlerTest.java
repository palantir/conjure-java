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
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.ErrorType.Code;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.QosReason;
import com.palantir.conjure.java.api.errors.QosReason.DueTo;
import com.palantir.conjure.java.api.errors.QosReason.RetryHint;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.HttpServerExchanges;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.logsafe.Safe;
import com.palantir.logsafe.SafeArg;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ConjureExceptionHandlerTest {

    private Exception exception;
    private Undertow server;

    @BeforeEach
    public void before() {
        server = Undertow.builder()
                .addHttpListener(12345, "localhost")
                .setHandler(new BlockingHandler(new ConjureExceptionHandler(
                        _exchange -> {
                            throw exception;
                        },
                        ConjureExceptions.INSTANCE)))
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
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        assertThat(getErrorBody(connection))
                .contains("{\"errorCode\":\"CONFLICT\"")
                .contains("\"parameters\":{\"foo\":\"bar\"}}");
    }

    @Test
    public void checkServiceExceptionArgumentsSerializedWithToString() throws IOException {
        record Argument(
                String field1, Optional<Integer> maybeField2, List<String> collectionField, String nullString) {}
        exception = new ServiceException(
                ErrorType.CONFLICT,
                SafeArg.of("arg", new Argument("foo", Optional.of(42), List.of("bar", "baz"), null)));
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        String expectedSerializedArg =
                "\"Argument[field1=foo, maybeField2=Optional[42], collectionField=[bar, baz], nullString=null]\"";
        assertThat(getErrorBody(connection))
                .contains("{\"errorCode\":\"CONFLICT\"")
                .contains("\"parameters\":{\"arg\":" + expectedSerializedArg + "}}");
    }

    @Test
    public void handlesCheckedServiceException() throws IOException {
        final class DifferentPackage extends CheckedServiceException {
            private DifferentPackage(@Nullable Throwable cause) {
                super(ErrorType.CONFLICT, cause);
            }
        }

        exception = new DifferentPackage(null);
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        assertThat(getErrorBody(connection))
                .contains("{\"errorCode\":\"CONFLICT\"")
                .contains("\"parameters\":{}}");
    }

    @Test
    public void handlesCheckedServiceExceptionWithOptionals() throws IOException {
        record Argument(
                Integer integer,
                Integer nullValue,
                OptionalInt optionalInt,
                OptionalInt emptyOptionalInt,
                OptionalDouble optionalDouble,
                OptionalDouble emptyOptionalDouble,
                Optional<String> optional,
                Optional<String> emptyOptional,
                Optional<List<String>> optOfList,
                List<Optional<String>> listOfOpt) {}

        final class OptionalException extends CheckedServiceException {
            private OptionalException(@Safe Argument argument, @Nullable Throwable cause) {
                super(ErrorType.CONFLICT, cause, SafeArg.of("arg", argument));
            }
        }

        exception = new OptionalException(
                new Argument(
                        1,
                        null,
                        OptionalInt.of(2),
                        OptionalInt.empty(),
                        OptionalDouble.of(3.0),
                        OptionalDouble.empty(),
                        Optional.of("value"),
                        Optional.empty(),
                        Optional.of(List.of("a", "b")),
                        List.of(Optional.of("c"), Optional.empty(), Optional.of("d"))),
                null);

        HttpURLConnection connection = execute();
        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.CONFLICT.httpErrorCode());
        String expectedSerializedArgs = "{\"integer\":1," + "\"nullValue\":null,"
                + "\"optionalInt\":2,"
                + "\"emptyOptionalInt\":null,"
                + "\"optionalDouble\":3.0,"
                + "\"emptyOptionalDouble\":null,"
                + "\"optional\":\"value\","
                + "\"emptyOptional\":null,"
                + "\"optOfList\":[\"a\",\"b\"],"
                + "\"listOfOpt\":[\"c\",null,\"d\"]}";
        assertThat(getErrorBody(connection))
                .contains("{\"errorCode\":\"CONFLICT\"")
                .contains("\"parameters\":{\"arg\":" + expectedSerializedArgs + "}}");
    }

    @Test
    public void handlesRemoteException() throws IOException {
        SerializableError remoteError =
                SerializableError.forException(new ServiceException(ErrorType.CONFLICT, SafeArg.of("foo", "bar")));
        exception = new RemoteException(remoteError, ErrorType.CONFLICT.httpErrorCode());
        HttpURLConnection connection = execute();

        // Propagates errorInstanceId and changes error code and name to INTERNAL
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.INTERNAL.code().toString())
                .errorName(ErrorType.INTERNAL.name())
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        // remote exceptions should result in 500 status
        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.INTERNAL.httpErrorCode());
        assertThat(getErrorBody(connection)).isEqualTo(stream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void handles401RemoteException() throws IOException {
        SerializableError remoteError = SerializableError.forException(
                new ServiceException(ErrorType.create(Code.UNAUTHORIZED, "Test:ErrorName"), SafeArg.of("foo", "bar")));
        exception = new RemoteException(remoteError, ErrorType.UNAUTHORIZED.httpErrorCode());
        HttpURLConnection connection = execute();

        // Propagates errorInstanceId and does not change error code and name
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.UNAUTHORIZED.code().toString())
                .errorName("Test:ErrorName")
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.UNAUTHORIZED.httpErrorCode());
        assertThat(getErrorBody(connection)).isEqualTo(stream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void handles403RemoteException() throws IOException {
        SerializableError remoteError = SerializableError.forException(new ServiceException(
                ErrorType.create(Code.PERMISSION_DENIED, "Test:ErrorName"), SafeArg.of("foo", "bar")));
        exception = new RemoteException(remoteError, ErrorType.PERMISSION_DENIED.httpErrorCode());
        HttpURLConnection connection = execute();

        // Propagates errorInstanceId and does not change error code and name
        // Does not propagate args
        SerializableError expectedPropagatedError = SerializableError.builder()
                .errorCode(ErrorType.PERMISSION_DENIED.code().toString())
                .errorName("Test:ErrorName")
                .errorInstanceId(remoteError.errorInstanceId())
                .build();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Encodings.json().serializer(new TypeMarker<SerializableError>() {}).serialize(expectedPropagatedError, stream);

        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.PERMISSION_DENIED.httpErrorCode());
        assertThat(getErrorBody(connection)).isEqualTo(stream.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void handlesQosExceptionThrottleWithoutDuration() throws IOException {
        exception = QosException.throttle();
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(429);
        assertThat(connection.getErrorStream()).isNull();
        assertThat(connection.getHeaderFields()).doesNotContainKey("Retry-After");
        assertThat(connection.getHeaderFields()).containsKeys("Connection", "Content-Length", "Date");
    }

    @Test
    public void handlesQosExceptionThrottleWithDuration() throws IOException {
        exception = QosException.throttle(Duration.ofMinutes(2));
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(429);
        assertThat(connection.getHeaderFields()).containsEntry("Retry-After", ImmutableList.of("120"));
        assertThat(connection.getErrorStream()).isNull();
    }

    @Test
    public void handlesQosExceptionRetryOther() throws IOException {
        exception = QosException.retryOther(new URL("http://foo"));
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(308);
        assertThat(connection.getHeaderFields()).containsEntry("Location", ImmutableList.of("http://foo"));
        assertThat(connection.getErrorStream()).isNull();
    }

    @Test
    public void handlesQosExceptionUnavailable() throws IOException {
        exception = QosException.unavailable();
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(503);
        assertThat(connection.getErrorStream()).isNull();
    }

    @Test
    public void handlesQosExceptionUnavailableWithMetadata() throws IOException {
        exception = QosException.unavailable(QosReason.builder()
                .reason("reason")
                .retryHint(RetryHint.DO_NOT_RETRY)
                .dueTo(DueTo.CUSTOM)
                .build());
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(503);
        assertThat(connection.getHeaderFields()).containsEntry("Qos-Retry-Hint", ImmutableList.of("do-not-retry"));
        assertThat(connection.getHeaderFields()).containsEntry("Qos-Due-To", ImmutableList.of("custom"));
        assertThat(connection.getErrorStream()).isNull();
    }

    @Test
    public void handlesIllegalArgumentException() throws IOException {
        exception = new IllegalArgumentException("Foo");
        HttpURLConnection connection = execute();

        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.INVALID_ARGUMENT.httpErrorCode());
        assertThat(getErrorBody(connection)).contains("{\"errorCode\":\"INVALID_ARGUMENT\"");
    }

    @Test
    public void handlesRuntimeException() throws IOException {
        exception = new RuntimeException("Foo");
        HttpURLConnection connection = execute();
        assertThat(connection.getResponseCode()).isEqualTo(ErrorType.INTERNAL.httpErrorCode());
        assertThat(getErrorBody(connection)).contains("{\"errorCode\":\"INTERNAL\"");
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

        HttpURLConnection connection = execute();
        assertThat(connection.getResponseCode()).isEqualTo(500);
        assertThat(connection.getErrorStream()).isNull();
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

    private static String getErrorBody(HttpURLConnection connection) {
        try (InputStream response = connection.getErrorStream()) {
            return new String(response.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpURLConnection execute() {
        try {
            URL url = new URL("http://localhost:12345");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
