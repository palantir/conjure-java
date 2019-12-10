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

import com.codahale.metrics.Meter;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.logsafe.SafeArg;
import com.palantir.tritium.metrics.registry.SharedTaggedMetricRegistries;
import io.undertow.io.UndertowOutputStream;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;

/**
 * Maps caught {@link Throwable} instances into HTTP responses. The result is written into the
 * {@link HttpServerExchange exchange's} response, and an appropriate HTTP status code is set.
 */
final class ConjureExceptions {

    private static final Logger log = LoggerFactory.getLogger(ConjureExceptions.class);

    private static final Meter internalErrorMeter =
            ConjureServerMetrics.of(SharedTaggedMetricRegistries.getSingleton()).internalerrorAll();

    private ConjureExceptions() { }

    static Serializer<SerializableError> serializer() {
        // Exceptions should always be serialized using JSON
        return new ConjureBodySerDe(Collections.singletonList(Encodings.json()))
                .serializer(new TypeMarker<SerializableError>() {});
    }

    static void handle(HttpServerExchange exchange, Serializer<SerializableError> serializer, Throwable throwable) {
        if (throwable instanceof ServiceException) {
            serviceException(exchange, serializer, (ServiceException) throwable);
        } else if (throwable instanceof QosException) {
            qosException(exchange, serializer, (QosException) throwable);
        } else if (throwable instanceof RemoteException) {
            remoteException(exchange, serializer, (RemoteException) throwable);
        } else if (throwable instanceof IllegalArgumentException) {
            illegalArgumentException(exchange, serializer, throwable);
        } else if (throwable instanceof FrameworkException) {
            frameworkException(exchange, serializer, (FrameworkException) throwable);
        } else if (throwable instanceof Error) {
            error(exchange, serializer, (Error) throwable);
        } else {
            ServiceException exception = new ServiceException(ErrorType.INTERNAL, throwable);
            log(exception);
            internalErrorMeter.mark();
            writeResponse(
                    exchange,
                    Optional.of(SerializableError.forException(exception)),
                    exception.getErrorType().httpErrorCode(),
                    serializer);
        }
    }

    private static void serviceException(
            HttpServerExchange exchange, Serializer<SerializableError> serializer, ServiceException exception) {
        log(exception);
        if (exception.getErrorType().httpErrorCode() == 500) {
            internalErrorMeter.mark();
        }
        writeResponse(
                exchange,
                Optional.of(SerializableError.forException(exception)),
                exception.getErrorType().httpErrorCode(),
                serializer);
    }

    private static void qosException(
            HttpServerExchange exchange, Serializer<SerializableError> serializer, QosException exception) {
        exception.accept(QOS_EXCEPTION_HEADERS).accept(exchange);
        log.debug("Possible quality-of-service intervention", exception);
        writeResponse(
                exchange,
                Optional.empty(),
                exception.accept(QOS_EXCEPTION_STATUS_CODE),
                serializer);
    }

    // RemoteExceptions are thrown by Conjure clients to indicate a remote/service-side problem.
    // We forward these exceptions, but change the ErrorType to INTERNAL unless it was a 403, i.e., the problem is now
    // considered internal to *this* service rather than the originating service. This means in particular
    // that Conjure errors are defined only local to a given service and these error types don't
    // propagate through other services.
    private static void remoteException(
            HttpServerExchange exchange, Serializer<SerializableError> serializer, RemoteException exception) {

        ErrorType errorType = mapRemoteExceptionErrorType(exception);
        writeResponse(exchange,
                Optional.of(SerializableError.builder()
                        .errorName(errorType.name())
                        .errorCode(errorType.code().toString())
                        .errorInstanceId(exception.getError().errorInstanceId())
                        .build()),
                errorType.httpErrorCode(),
                serializer);
    }

    private static ErrorType mapRemoteExceptionErrorType(RemoteException exception) {
        if (exception.getStatus() == 401) {
            log.info("Encountered a remote unauthorized exception."
                            + " Mapping to a default unauthorized exception before propagating",
                    SafeArg.of("errorInstanceId", exception.getError().errorInstanceId()),
                    SafeArg.of("errorName", exception.getError().errorName()),
                    SafeArg.of("statusCode", exception.getStatus()),
                    exception);

            return ErrorType.UNAUTHORIZED;
        } else if (exception.getStatus() == 403) {
            log.info("Encountered a remote permission denied exception."
                            + " Mapping to a default permission denied exception before propagating",
                    SafeArg.of("errorInstanceId", exception.getError().errorInstanceId()),
                    SafeArg.of("errorName", exception.getError().errorName()),
                    SafeArg.of("statusCode", exception.getStatus()),
                    exception);

            return ErrorType.PERMISSION_DENIED;
        } else {
            // log at WARN instead of ERROR because this indicates an issue in a remote server
            log.warn("Encountered a remote exception. Mapping to an internal error before propagating",
                    SafeArg.of("errorInstanceId", exception.getError().errorInstanceId()),
                    SafeArg.of("errorName", exception.getError().errorName()),
                    SafeArg.of("statusCode", exception.getStatus()),
                    exception);

            return ErrorType.INTERNAL;
        }
    }

    private static void illegalArgumentException(
            HttpServerExchange exchange, Serializer<SerializableError> serializer, Throwable throwable) {
        ServiceException exception = new ServiceException(ErrorType.INVALID_ARGUMENT, throwable);
        log(exception);
        writeResponse(
                exchange,
                Optional.of(SerializableError.forException(exception)),
                exception.getErrorType().httpErrorCode(),
                serializer);
    }

    private static void frameworkException(
            HttpServerExchange exchange,
            Serializer<SerializableError> serializer,
            FrameworkException frameworkException) {
        int statusCode = frameworkException.getStatusCode();
        ErrorType errorType = statusCode / 100 == 4 ? ErrorType.INVALID_ARGUMENT : ErrorType.INTERNAL;
        ServiceException exception = new ServiceException(errorType, frameworkException);
        log(exception);
        writeResponse(exchange, Optional.of(SerializableError.forException(exception)), statusCode, serializer);
    }

    private static void error(HttpServerExchange exchange, Serializer<SerializableError> serializer, Error error) {
        // log errors in order to associate the log line with the correct traceId but
        // avoid doing work beyond setting a 500 response code, no response body is sent.
        log.error("Error handling request", error);
        internalErrorMeter.mark();
        // The writeResponse method terminates responses if data has already been sent to clients
        // do not interpret partial data as a full response.
        writeResponse(exchange, Optional.empty(), ErrorType.INTERNAL.httpErrorCode(), serializer);
    }

    private static void writeResponse(
            HttpServerExchange exchange,
            Optional<SerializableError> maybeBody,
            int statusCode,
            Serializer<SerializableError> serializer) {
        // Do not attempt to write the failure if data has already been written
        if (!isResponseStarted(exchange)) {
            exchange.setStatusCode(statusCode);
            try {
                if (maybeBody.isPresent()) {
                    serializer.serialize(maybeBody.get(), exchange);
                }
            } catch (IOException | RuntimeException e) {
                log.info("Failed to write error response", e);
            }
        } else {
            // This prevents the server from sending the final null chunk, alerting
            // clients that the response was terminated prior to receiving full contents.
            // Note that in the case of http/2 this does not close a connection, which
            // would break other active requests, only resets the stream.
            log.warn("Closing the connection to alert the client of an error");
            IoUtils.safeClose(exchange.getConnection());
        }
    }

    private static boolean isResponseStarted(HttpServerExchange exchange) {
        if (exchange.isResponseStarted()) {
            return true;
        }
        // The blocking exchange output stream may have un-committed data buffered.
        // In this case we can clear the buffer allowing us to send a serializable error.
        OutputStream outputStream = exchange.getOutputStream();
        if (outputStream instanceof UndertowOutputStream) {
            ((UndertowOutputStream) outputStream).resetBuffer();
        }
        return false;
    }

    private static void log(ServiceException exception) {
        if (exception.getErrorType().httpErrorCode() / 100 == 4 /* client error */) {
            log.info("Error handling request",
                    SafeArg.of("errorInstanceId", exception.getErrorInstanceId()),
                    SafeArg.of("errorName", exception.getErrorType().name()),
                    exception);
        } else {
            log.error("Error handling request",
                    SafeArg.of("errorInstanceId", exception.getErrorInstanceId()),
                    SafeArg.of("errorName", exception.getErrorType().name()),
                    exception);
        }
    }

    private static final QosException.Visitor<Integer> QOS_EXCEPTION_STATUS_CODE = new QosException.Visitor<Integer>() {
        @Override
        public Integer visit(QosException.Throttle _exception) {
            return 429;
        }

        @Override
        public Integer visit(QosException.RetryOther _exception) {
            return 308;
        }

        @Override
        public Integer visit(QosException.Unavailable _exception) {
            return 503;
        }
    };

    private static final QosException.Visitor<Consumer<HttpServerExchange>>
            QOS_EXCEPTION_HEADERS = new QosException.Visitor<Consumer<HttpServerExchange>>() {
                @Override
                public Consumer<HttpServerExchange> visit(QosException.Throttle exception) {
                    return exchange -> exception.getRetryAfter().ifPresent(duration -> {
                        exchange.getResponseHeaders()
                                .put(Headers.RETRY_AFTER, Long.toString(duration.get(ChronoUnit.SECONDS)));
                    });
                }

                @Override
                public Consumer<HttpServerExchange> visit(QosException.RetryOther exception) {
                    return exchange -> exchange.getResponseHeaders()
                            .put(Headers.LOCATION, exception.getRedirectTo().toString());
                }

                @Override
                public Consumer<HttpServerExchange> visit(QosException.Unavailable _exception) {
                    return exchange -> {

                    };
                }
            };
}
