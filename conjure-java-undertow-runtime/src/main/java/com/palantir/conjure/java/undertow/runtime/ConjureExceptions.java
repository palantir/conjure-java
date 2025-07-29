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

import com.google.common.util.concurrent.RateLimiter;
import com.palantir.conjure.java.api.errors.CheckedServiceException;
import com.palantir.conjure.java.api.errors.ConjureErrorParameterFormat;
import com.palantir.conjure.java.api.errors.ConjureErrorParameterFormats;
import com.palantir.conjure.java.api.errors.ConjureErrorParameterFormats.ConjureErrorParameterFormatRequestDecodingAdapter;
import com.palantir.conjure.java.api.errors.EndpointServiceException;
import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.QosReasons;
import com.palantir.conjure.java.api.errors.QosReasons.QosResponseEncodingAdapter;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.conjure.java.undertow.lib.ExceptionHandler;
import com.palantir.conjure.java.undertow.lib.Serializer;
import com.palantir.conjure.java.undertow.lib.TypeMarker;
import com.palantir.deadlines.DeadlineExpiredException;
import com.palantir.deadlines.DeadlineExpiredReasons;
import com.palantir.logsafe.SafeArg;
import com.palantir.logsafe.logger.SafeLogger;
import com.palantir.logsafe.logger.SafeLoggerFactory;
import io.undertow.io.UndertowOutputStream;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import java.io.IOException;
import java.io.OutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Consumer;
import org.xnio.IoUtils;

/**
 * Maps caught {@link Throwable} instances into HTTP responses. The result is written into the
 * {@link HttpServerExchange exchange's} response, and an appropriate HTTP status code is set.
 */
public enum ConjureExceptions implements ExceptionHandler {
    INSTANCE;

    private static final SafeLogger log = SafeLoggerFactory.get(ConjureExceptions.class);
    // Exceptions should always be serialized using JSON
    private static final Serializer<ConjureError> serializer =
            new ConjureBodySerDe(Collections.singletonList(Encodings.json())).serializer(new TypeMarker<>() {});

    // Log at most once every second
    private static final RateLimiter qosLoggingRateLimiter = RateLimiter.create(1);

    private enum ConjureErrorParamsDecoder implements ConjureErrorParameterFormatRequestDecodingAdapter<HeaderMap> {
        INSTANCE;

        @Override
        public String getFirstHeader(HeaderMap response, String headerName) {
            return response.getFirst(headerName);
        }
    }

    @SuppressWarnings("CyclomaticComplexity")
    @Override
    public void handle(HttpServerExchange exchange, Throwable throwable) {
        setFailure(exchange, throwable);
        if (throwable instanceof EndpointServiceException endpointServiceException) {
            endpointServiceException(exchange, endpointServiceException);
        } else if (throwable instanceof CheckedServiceException checkedServiceException) {
            // This is kept around for backward compatibility. Support for this can be removed in a future version of
            // Conjure.
            checkedServiceException(exchange, checkedServiceException);
        } else if (throwable instanceof ServiceException serviceException) {
            Optional<ConjureErrorParameterFormat> maybeErrorParamFormatHeader = ConjureErrorParameterFormats.parseFromRequest(exchange.getRequestHeaders(), ConjureErrorParamsDecoder.INSTANCE);
            if ( maybeErrorParamFormatHeader.isPresent()
                    && maybeErrorParamFormatHeader.get().equals(ConjureErrorParameterFormat.JSON_FORMAT)) {
                serviceExceptionWithJsonParameterValues(exchange, serviceException);
            } else {
                serviceException(exchange, serviceException);
            }
        } else if (throwable instanceof QosException qosException) {
            qosException(exchange, qosException);
        } else if (throwable instanceof RemoteException remoteException) {
            remoteException(exchange, remoteException);
        } else if (throwable instanceof IllegalArgumentException) {
            illegalArgumentException(exchange, throwable);
        } else if (throwable instanceof FrameworkException frameworkException) {
            frameworkException(exchange, frameworkException);
        } else if (throwable instanceof DeadlineExpiredException deadlineExpiredException) {
            deadlineExpiredException(exchange, deadlineExpiredException);
        } else if (throwable instanceof Error error) {
            error(exchange, error);
        } else if (throwable instanceof IOException && !exchange.getConnection().isOpen()) {
            log.info(
                    "I/O exception from a closed connection. The request may have been aborted by the client",
                    throwable);
        } else {
            ServiceException exception = new ServiceException(ErrorType.INTERNAL, throwable);
            log(exception, throwable);
            writeResponse(
                    exchange,
                    Optional.of(ConjureError.fromServiceException(exception)),
                    exception.getErrorType().httpErrorCode());
        }
    }

    private static void checkedServiceException(HttpServerExchange exchange, CheckedServiceException exception) {
        log(exception);
        writeResponse(
                exchange,
                Optional.of(ConjureError.fromCheckedServiceException(exception)),
                exception.getErrorType().httpErrorCode());
    }

    private static void endpointServiceException(HttpServerExchange exchange, EndpointServiceException exception) {
        log(exception);
        writeResponse(
                exchange,
                Optional.of(ConjureError.fromEndpointServiceException(exception)),
                exception.getErrorType().httpErrorCode());
    }

    private static void serviceException(HttpServerExchange exchange, ServiceException exception) {
        Optional<ConjureErrorParameterFormat> maybeErrorParamFormatHeader =
                ConjureErrorParameterFormats.parseFromRequest(
                        exchange.getRequestHeaders(), ConjureErrorParamsDecoder.INSTANCE);

        boolean isHeaderPresent = maybeErrorParamFormatHeader.isPresent();
        boolean isJsonErrorParameterValueFormat = isHeaderPresent
                && maybeErrorParamFormatHeader
                        .get()
                        .toString()
                        .equalsIgnoreCase(ConjureErrorParameterFormat.JSON_FORMAT.toString());

        // Log unrecognized format if present but not JSON
        if (isHeaderPresent && !isJsonErrorParameterValueFormat) {
            log.info(
                    "Unrecognized Conjure error parameter format header",
                    SafeArg.of("headerValue", maybeErrorParamFormatHeader.get()));
        }

        // Use the appropriate serialization based on format
        logWithSerializationFormat(exception, isJsonErrorParameterValueFormat);
        ConjureError conjureError = isJsonErrorParameterValueFormat
                ? ConjureError.fromServiceExceptionWithJsonSerializedParameterValues(exception)
                : ConjureError.fromServiceException(exception);

        writeResponse(
                exchange, Optional.of(conjureError), exception.getErrorType().httpErrorCode());
    }

    private enum ConjureErrorParamsDecoder implements ConjureErrorParameterFormatRequestDecodingAdapter<HeaderMap> {
        INSTANCE;

        @Override
        public String getFirstHeader(HeaderMap requestHeaderMap, String headerName) {
            return requestHeaderMap.getFirst(headerName);
        }
    }

    private static void serviceExceptionWithJsonParameterValues(HttpServerExchange exchange, ServiceException exception) {
        log(exception);
        writeResponse(
                exchange,
                Optional.of(ConjureError.fromServiceExceptionWithJsonParameters(exception)),
                exception.getErrorType().httpErrorCode());
    }

    private static void qosException(HttpServerExchange exchange, QosException qosException) {
        qosException.accept(QOS_EXCEPTION_HEADERS).accept(exchange);
        QosReasons.encodeToResponse(qosException.getReason(), exchange, UndertowQosResponseEncodingAdapter.INSTANCE);

        if (log.isDebugEnabled()) {
            log.debug("Quality-of-Service error handling request", qosException);
        } else if (qosExceptionHasAdditionalMetadata(qosException) || qosLoggingRateLimiter.tryAcquire()) {
            log.info("Quality-of-Service error handling request", qosException);
        }

        writeResponse(exchange, Optional.empty(), qosException.accept(QOS_EXCEPTION_STATUS_CODE));
    }

    /** Returns true if {@link QosException} provides additional metadata and should be logged at {@code info}. */
    private static boolean qosExceptionHasAdditionalMetadata(QosException qosException) {
        try {
            if (qosException.getCause() != null) {
                return true;
            }
            Throwable[] suppressed = qosException.getSuppressed();
            return suppressed != null && suppressed.length > 1;
        } catch (Throwable t) {
            // Fallback in case of unexpected bytecode manipulation (getCause or getSuppressed throws due to
            // poor instrumentation/mocks)
            return true;
        }
    }

    // RemoteExceptions are thrown by Conjure clients to indicate a remote/service-side problem.
    // We forward these exceptions, but change the ErrorType to INTERNAL unless it was a 401 or 403, i.e.,
    // the problem is now considered internal to *this* service rather than the originating service.
    // This means in particular that Conjure errors are defined only local to a given service and these
    // error types don't propagate through other services.
    private static void remoteException(HttpServerExchange exchange, RemoteException remoteException) {
        if (remoteException.getStatus() == 401 || remoteException.getStatus() == 403) {
            log.info(
                    "Encountered a remote exception",
                    SafeArg.of("errorInstanceId", remoteException.getError().errorInstanceId()),
                    SafeArg.of("errorName", remoteException.getError().errorName()),
                    SafeArg.of("statusCode", remoteException.getStatus()),
                    remoteException);

            writeResponse(
                    exchange,
                    Optional.of(ConjureError.fromRemoteException(remoteException)),
                    remoteException.getStatus());
        } else {
            // log at WARN instead of ERROR because this indicates an issue in a remote server
            log.warn(
                    "Encountered a remote exception. Mapping to an internal error before propagating",
                    SafeArg.of("errorInstanceId", remoteException.getError().errorInstanceId()),
                    SafeArg.of("errorName", remoteException.getError().errorName()),
                    SafeArg.of("statusCode", remoteException.getStatus()),
                    remoteException);

            ServiceException exception = new ServiceException(ErrorType.INTERNAL, remoteException);
            writeResponse(
                    exchange,
                    Optional.of(ConjureError.fromServiceException(exception)),
                    exception.getErrorType().httpErrorCode());
        }
    }

    private static void illegalArgumentException(HttpServerExchange exchange, Throwable throwable) {
        ServiceException exception = new ServiceException(ErrorType.INVALID_ARGUMENT, throwable);
        log(exception, throwable);
        writeResponse(
                exchange,
                Optional.of(ConjureError.fromServiceException(exception)),
                exception.getErrorType().httpErrorCode());
    }

    private static void frameworkException(HttpServerExchange exchange, FrameworkException frameworkException) {
        int statusCode = frameworkException.getStatusCode();
        ServiceException exception = new ServiceException(frameworkException.getErrorType(), frameworkException);
        log(exception, frameworkException);
        writeResponse(exchange, Optional.of(ConjureError.fromServiceException(exception)), statusCode);
    }

    private static void deadlineExpiredException(HttpServerExchange exchange, DeadlineExpiredException exception) {
        if (exception instanceof DeadlineExpiredException.Internal) {
            log.error("internal deadline exceeded", exception);
        } else {
            log.error("deadline exceeded", exception);
        }
        // writeResponse is called by UndertowDeadlineReasonResponseEncodingAdapter#setStatus
        DeadlineExpiredReasons.encodeToResponse(
                exception, exchange, UndertowDeadlineReasonResponseEncodingAdapter.INSTANCE);
    }

    private static void error(HttpServerExchange exchange, Error error) {
        // log errors in order to associate the log line with the correct traceId but
        // avoid doing work beyond setting a 500 response code, no response body is sent.
        log.error("Error handling request", error);
        // The writeResponse method terminates responses if data has already been sent to clients
        // do not interpret partial data as a full response.
        writeResponse(exchange, Optional.empty(), ErrorType.INTERNAL.httpErrorCode());
    }

    private static void writeResponse(HttpServerExchange exchange, Optional<ConjureError> maybeBody, int statusCode) {
        // Do not attempt to write the failure if data has already been written
        if (!isResponseStarted(exchange)) {
            exchange.setStatusCode(statusCode);
            if (maybeBody.isPresent()) {
                try {
                    serializer.serialize(maybeBody.get(), exchange);
                } catch (IOException | RuntimeException e) {
                    log.info("Failed to write error response", e);
                }
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
        if (outputStream instanceof UndertowOutputStream undertowOutputStream) {
            undertowOutputStream.resetBuffer();
        }
        return false;
    }

    private static void logWithSerializationFormat(
            ServiceException serviceException, boolean isUsingJsonSerializationForParameters) {
        if (serviceException.getErrorType().httpErrorCode() / 100 == 4 /* client error */) {
            log.info(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", serviceException.getErrorInstanceId()),
                    SafeArg.of("errorName", serviceException.getErrorType().name()),
                    SafeArg.of("isUsingJsonSerializationForParameters", isUsingJsonSerializationForParameters),
                    serviceException);
        } else {
            log.error(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", serviceException.getErrorInstanceId()),
                    SafeArg.of("errorName", serviceException.getErrorType().name()),
                    SafeArg.of("isUsingJsonSerializationForParameters", isUsingJsonSerializationForParameters),
                    serviceException);
        }
    }

    private static void log(ServiceException serviceException, Throwable exceptionForLogging) {
        if (serviceException.getErrorType().httpErrorCode() / 100 == 4 /* client error */) {
            log.info(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", serviceException.getErrorInstanceId()),
                    SafeArg.of("errorName", serviceException.getErrorType().name()),
                    exceptionForLogging);
        } else {
            log.error(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", serviceException.getErrorInstanceId()),
                    SafeArg.of("errorName", serviceException.getErrorType().name()),
                    exceptionForLogging);
        }
    }

    private static void log(CheckedServiceException checkedServiceException) {
        if (checkedServiceException.getErrorType().httpErrorCode() / 100 == 4 /* client error */) {
            log.info(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", checkedServiceException.getErrorInstanceId()),
                    SafeArg.of(
                            "errorName", checkedServiceException.getErrorType().name()),
                    checkedServiceException);
        } else {
            log.error(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", checkedServiceException.getErrorInstanceId()),
                    SafeArg.of(
                            "errorName", checkedServiceException.getErrorType().name()),
                    checkedServiceException);
        }
    }

    private static void log(EndpointServiceException endpointServiceException) {
        if (endpointServiceException.getErrorType().httpErrorCode() / 100 == 4 /* client error */) {
            log.info(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", endpointServiceException.getErrorInstanceId()),
                    SafeArg.of(
                            "errorName", endpointServiceException.getErrorType().name()),
                    endpointServiceException);
        } else {
            log.error(
                    "Error handling request",
                    SafeArg.of("errorInstanceId", endpointServiceException.getErrorInstanceId()),
                    SafeArg.of(
                            "errorName", endpointServiceException.getErrorType().name()),
                    endpointServiceException);
        }
    }

    private static void setFailure(HttpServerExchange exchange, Throwable failure) {
        // Optimistically set the value, and revert in the unlikely case it has already been set.
        Throwable previous = exchange.putAttachment(Attachments.FAILURE, failure);
        if (previous != null) {
            exchange.putAttachment(Attachments.FAILURE, previous);
            log.info(
                    "Failure has already been set to a {} and will not be updated to the following",
                    SafeArg.of("existingFailureType", previous.getClass()),
                    failure);
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

    private static final QosException.Visitor<Consumer<HttpServerExchange>> QOS_EXCEPTION_HEADERS =
            new QosException.Visitor<Consumer<HttpServerExchange>>() {
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
                    return _exchange -> {};
                }
            };

    private enum UndertowQosResponseEncodingAdapter implements QosResponseEncodingAdapter<HttpServerExchange> {
        INSTANCE;

        @Override
        public void setHeader(HttpServerExchange exchange, String headerName, String headerValue) {
            exchange.getResponseHeaders().put(HttpString.tryFromString(headerName), headerValue);
        }
    }

    private enum UndertowDeadlineReasonResponseEncodingAdapter
            implements DeadlineExpiredReasons.ResponseEncodingAdapter<HttpServerExchange> {
        INSTANCE;

        @Override
        public void setHeader(HttpServerExchange exchange, String headerName, String headerValue) {
            exchange.getResponseHeaders().put(HttpString.tryFromString(headerName), headerValue);
        }

        @Override
        public void setStatus(HttpServerExchange exchange, int status) {
            writeResponse(exchange, Optional.empty(), status);
        }
    }
}
