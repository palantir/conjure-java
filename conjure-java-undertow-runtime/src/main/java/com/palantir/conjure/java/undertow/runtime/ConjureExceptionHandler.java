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

import com.palantir.conjure.java.api.errors.ErrorType;
import com.palantir.conjure.java.api.errors.QosException;
import com.palantir.conjure.java.api.errors.RemoteException;
import com.palantir.conjure.java.api.errors.SerializableError;
import com.palantir.conjure.java.api.errors.ServiceException;
import com.palantir.logsafe.SafeArg;
import io.undertow.io.UndertowOutputStream;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;

/**
 * Delegates to the given {@link HttpHandler}, and catches&forwards all {@link Throwable}s. Any exception thrown in
 * the delegate handler is caught and serialized using the configured {@link SerializerRegistry} into a
 * {@link SerializableError}. The result is written it into the exchange's output stream, and an appropriate HTTP
 * status code is set.
 */
final class ConjureExceptionHandler implements HttpHandler {

    private static final Logger log = LoggerFactory.getLogger(ConjureExceptionHandler.class);
    // Exceptions should always be serialized using JSON
    private static final SerializerRegistry DEFAULT_SERIALIZERS = new SerializerRegistry(Serializers.json());

    private final SerializerRegistry serializers;
    private final HttpHandler delegate;

    ConjureExceptionHandler(SerializerRegistry serializers, HttpHandler delegate) {
        this.serializers = serializers;
        this.delegate = delegate;
    }

    ConjureExceptionHandler(HttpHandler delegate) {
        this(DEFAULT_SERIALIZERS, delegate);
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) {
        try {
            delegate.handleRequest(exchange);
        } catch (Throwable throwable) {
            if (throwable instanceof ServiceException) {
                serviceException(exchange, (ServiceException) throwable);
            } else if (throwable instanceof QosException) {
                qosException(exchange, (QosException) throwable);
            } else if (throwable instanceof RemoteException) {
                remoteException(exchange, (RemoteException) throwable);
            } else if (throwable instanceof IllegalArgumentException) {
                illegalArgumentException(exchange, throwable);
            } else if (throwable instanceof FrameworkException) {
                frameworkException(exchange, (FrameworkException) throwable);
            } else if (throwable instanceof Error) {
                throw (Error) throwable;
            } else {
                ServiceException exception = new ServiceException(ErrorType.INTERNAL, throwable);
                log(exception);
                writeResponse(
                        exchange,
                        Optional.of(SerializableError.forException(exception)),
                        exception.getErrorType().httpErrorCode());
            }
        }
    }

    private void serviceException(HttpServerExchange exchange, ServiceException exception) {
        log(exception);
        writeResponse(
                exchange,
                Optional.of(SerializableError.forException(exception)),
                exception.getErrorType().httpErrorCode());
    }

    private void qosException(HttpServerExchange exchange, QosException exception) {
        exception.accept(QOS_EXCEPTION_HEADERS).accept(exchange);
        log.debug("Possible quality-of-service intervention", exception);
        writeResponse(
                exchange,
                Optional.empty(),
                exception.accept(QOS_EXCEPTION_STATUS_CODE));
    }

    // RemoteExceptions are thrown by Conjure clients to indicate a remote/service-side problem.
    // We forward these exceptions, but change the ErrorType to INTERNAL, i.e., the problem is now
    // considered internal to *this* service rather than the originating service. This means in particular
    // that Conjure errors are defined only local to a given service and these error types don't
    // propagate through other services.
    private void remoteException(HttpServerExchange exchange, RemoteException exception) {
        // log at WARN instead of ERROR because although this indicates an issue in a remote server
        log.warn("Encountered a remote exception. Mapping to an internal error before propagating",
                SafeArg.of("errorInstanceId", exception.getError().errorInstanceId()),
                SafeArg.of("errorName", exception.getError().errorName()),
                SafeArg.of("statusCode", exception.getStatus()),
                exception);

        ErrorType errorType = ErrorType.INTERNAL;
        writeResponse(exchange,
                Optional.of(SerializableError.builder()
                        .errorName(errorType.name())
                        .errorCode(errorType.code().toString())
                        .errorInstanceId(exception.getError().errorInstanceId())
                        .build()),
                errorType.httpErrorCode());
    }

    private void illegalArgumentException(HttpServerExchange exchange, Throwable throwable) {
        ServiceException exception = new ServiceException(ErrorType.INVALID_ARGUMENT, throwable);
        log(exception);
        writeResponse(
                exchange,
                Optional.of(SerializableError.forException(exception)),
                exception.getErrorType().httpErrorCode());
    }

    private void frameworkException(HttpServerExchange exchange, FrameworkException frameworkException) {
        int statusCode = frameworkException.getStatusCode();
        ErrorType errorType = statusCode / 100 == 4 ? ErrorType.INVALID_ARGUMENT : ErrorType.INTERNAL;
        ServiceException exception = new ServiceException(errorType, frameworkException);
        log(exception);
        writeResponse(exchange, Optional.of(SerializableError.forException(exception)), statusCode);
    }

    private void writeResponse(HttpServerExchange exchange, Optional<SerializableError> maybeBody, int statusCode) {
        // Do not attempt to write the failure if data has already been written
        if (!isResponseStarted(exchange)) {
            exchange.setStatusCode(statusCode);
            try {
                if (maybeBody.isPresent()) {
                    serializers.serialize(maybeBody.get(), exchange);
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
                    SafeArg.of("errorInstanceId", exception.getErrorInstanceId()), exception);
        } else {
            log.error("Error handling request",
                    SafeArg.of("errorInstanceId", exception.getErrorInstanceId()), exception);
        }
    }

    private static final QosException.Visitor<Integer> QOS_EXCEPTION_STATUS_CODE = new QosException.Visitor<Integer>() {
        @Override
        public Integer visit(QosException.Throttle exception) {
            return 429;
        }

        @Override
        public Integer visit(QosException.RetryOther exception) {
            return 308;
        }

        @Override
        public Integer visit(QosException.Unavailable exception) {
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
                public Consumer<HttpServerExchange> visit(QosException.Unavailable exception) {
                    return exchange -> {

                    };
                }
            };
}
